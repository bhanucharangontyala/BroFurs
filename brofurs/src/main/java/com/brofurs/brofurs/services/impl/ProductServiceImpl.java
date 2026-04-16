package com.brofurs.brofurs.services.impl;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.brofurs.brofurs.dto.ProductDto;
import com.brofurs.brofurs.entity.Category;
import com.brofurs.brofurs.entity.Product;
import com.brofurs.brofurs.entity.ProductImage;
import com.brofurs.brofurs.entity.ProductWoodPrice;
import com.brofurs.brofurs.entity.WoodType;
import com.brofurs.brofurs.exception.ResourceNotFoundException;
import com.brofurs.brofurs.repository.CategoryRepository;
import com.brofurs.brofurs.repository.ProductRepository;
import com.brofurs.brofurs.repository.ProductWoodPriceRepository;
import com.brofurs.brofurs.repository.WoodTypeRepository;
import com.brofurs.brofurs.services.FileStorageService;
import com.brofurs.brofurs.services.ProductService;

/**
 * ProductServiceImpl
 *
 * Core business logic for the furniture product catalogue.
 *
 * Key responsibilities: - CRUD with slug auto-generation - Image upload via
 * FileStorageService (primary + additional images) - Wood type price row upsert
 * (create or update per product+woodType pair) - Dynamic price calculation:
 * override > basePrice + adjustment - Wood price map for JavaScript price
 * calculator on the detail page
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final WoodTypeRepository woodTypeRepository;
	private final ProductWoodPriceRepository woodPriceRepository;
	private final FileStorageService fileStorageService;

	public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
			WoodTypeRepository woodTypeRepository, ProductWoodPriceRepository woodPriceRepository,
			FileStorageService fileStorageService) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
		this.woodTypeRepository = woodTypeRepository;
		this.woodPriceRepository = woodPriceRepository;
		this.fileStorageService = fileStorageService;
	}


	@Override
	@Transactional(readOnly = true)
	public List<Product> findFeatured() {
		return productRepository.findByActiveTrueOrderByCreatedAtDesc().stream().limit(8).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> findAllActive() {
		return productRepository.findAllWithImages();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Product> findById(Long id) {
		return productRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Product> findBySlugWithImages(String slug) {
		return productRepository.findBySlugWithImages(slug);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Product> searchProducts(Long categoryId, String name, Pageable pageable) {
		return productRepository.searchProducts(categoryId, name, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public long countActive() {
		return productRepository.countByActiveTrue();
	}

	// ── Wood Pricing ──────────────────────────────────────────────

	@Override
	@Transactional(readOnly = true)
	public List<ProductWoodPrice> getWoodPrices(Long productId) {
		return woodPriceRepository.findByProductIdAndActiveTrueOrderByWoodTypeNameAsc(productId);
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Long, ProductWoodPrice> getWoodPriceMap(Long productId) {
		Map<Long, ProductWoodPrice> map = new HashMap<>();
		getWoodPrices(productId).forEach(wp -> map.put(wp.getWoodType().getId(), wp));
		return map;
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal calculatePrice(Long productId, Long woodTypeId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", productId));

		if (woodTypeId == null) {
			return product.getBasePrice();
		}

		return woodPriceRepository.findByProductIdAndWoodTypeId(productId, woodTypeId)
				.map(wp -> wp.calculateFinalPrice(product.getBasePrice())).orElse(product.getBasePrice());
	}

	// ── Create ────────────────────────────────────────────────────

	@Override
	public Product save(ProductDto dto) {
		Product product = new Product();
		mapDtoToProduct(dto, product);
		product.setSlug(generateUniqueSlug(dto.getName()));
		Product saved = productRepository.save(product);
		saveImages(dto, saved);
		saveWoodPrices(dto, saved);
		return saved;
	}

	// ── Update ────────────────────────────────────────────────────

	@Override
	public Product update(Long id, ProductDto dto) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", id));
		mapDtoToProduct(dto, product);
		Product saved = productRepository.save(product);
		saveImages(dto, saved);
		saveWoodPrices(dto, saved);
		return saved;
	}

	// ── Delete ────────────────────────────────────────────────────

	@Override
	public void delete(Long id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", id));

		// Delete physical image files from disk
		product.getImages().forEach(img -> fileStorageService.delete(img.getImageName()));

		productRepository.delete(product);
	}

	// ── Private Helpers ───────────────────────────────────────────

	/**
	 * Copy non-image, non-pricing fields from the DTO onto the entity.
	 */
	private void mapDtoToProduct(ProductDto dto, Product product) {
		Category category = categoryRepository.findById(dto.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException("Category", dto.getCategoryId()));

		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setCategory(category);
		product.setBasePrice(dto.getBasePrice());
		product.setCustomSizeAllowed(dto.isCustomSizeAllowed());
		product.setStockStatus(dto.getStockStatus());
		product.setActive(dto.isActive());
	}

	/**
	 * Handle primary image and additional image uploads. Only processes non-empty
	 * files — skips if nothing was uploaded.
	 */
	private void saveImages(ProductDto dto, Product product) {

		// Primary image — replaces the existing primary if a new one is uploaded
		if (dto.getPrimaryImageFile() != null && !dto.getPrimaryImageFile().isEmpty()) {
			// Remove existing primary image from disk and from the list
			product.getImages().stream().filter(ProductImage::isPrimaryImage)
					.forEach(img -> fileStorageService.delete(img.getImageName()));
			product.getImages().removeIf(ProductImage::isPrimaryImage);

			String filename = fileStorageService.store(dto.getPrimaryImageFile());
			ProductImage primaryImage = new ProductImage();
			primaryImage.setImageName(filename);
			primaryImage.setImagePath("/uploads/" + filename);
			primaryImage.setPrimaryImage(true);
			primaryImage.setProduct(product);
			product.getImages().add(primaryImage);
		}

		// Additional gallery images — appended, never replace existing ones
		if (dto.getAdditionalImageFiles() != null) {
			for (MultipartFile file : dto.getAdditionalImageFiles()) {
				if (file != null && !file.isEmpty()) {
					String filename = fileStorageService.store(file);
					ProductImage additionalImage = new ProductImage();
					additionalImage.setImageName(filename);
					additionalImage.setImagePath("/uploads/" + filename);
					additionalImage.setPrimaryImage(false);
					additionalImage.setProduct(product);
					product.getImages().add(additionalImage);
				}
			}
		}
	}

	/**
	 * Upsert wood price rows from the parallel DTO lists. If a ProductWoodPrice
	 * already exists for this product+woodType, update it. Otherwise, create a new
	 * row.
	 */
	private void saveWoodPrices(ProductDto dto, Product product) {
		if (dto.getWoodTypeIds() == null || dto.getWoodTypeIds().isEmpty())
			return;

		for (int i = 0; i < dto.getWoodTypeIds().size(); i++) {
			Long woodTypeId = dto.getWoodTypeIds().get(i);
			if (woodTypeId == null)
				continue;

			WoodType woodType = woodTypeRepository.findById(woodTypeId).orElse(null);
			if (woodType == null)
				continue;

			// Fetch existing row or create a new one
			ProductWoodPrice pwp = woodPriceRepository.findByProductIdAndWoodTypeId(product.getId(), woodTypeId)
					.orElse(new ProductWoodPrice());

			pwp.setProduct(product);
			pwp.setWoodType(woodType);

			// Price adjustment (defaults to zero if not provided)
			BigDecimal adjustment = (dto.getPriceAdjustments() != null && i < dto.getPriceAdjustments().size()
					&& dto.getPriceAdjustments().get(i) != null) ? dto.getPriceAdjustments().get(i) : BigDecimal.ZERO;
			pwp.setPriceAdjustment(adjustment);

			// Final price override (nullable — null means use basePrice + adjustment)
			BigDecimal override = (dto.getFinalPriceOverrides() != null && i < dto.getFinalPriceOverrides().size())
					? dto.getFinalPriceOverrides().get(i)
					: null;
			pwp.setFinalPriceOverride(override);
			pwp.setActive(true);

			woodPriceRepository.save(pwp);
		}
	}

	/**
	 * Generate a URL-safe slug from a product name and append a short hex timestamp
	 * suffix to ensure uniqueness.
	 *
	 * Example: "Royal King Size Bed" → "royal-king-size-bed-1a2b3c"
	 */
	private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9-]");
	private static final Pattern MULTIPLE_HYPHENS = Pattern.compile("-+");

	private String generateUniqueSlug(String name) {
		String normalized = Normalizer.normalize(name, Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		String slug = NON_ALPHANUMERIC.matcher(normalized.toLowerCase(Locale.ENGLISH).trim().replace(" ", "-"))
				.replaceAll("");
		slug = MULTIPLE_HYPHENS.matcher(slug).replaceAll("-");
		return slug + "-" + Long.toHexString(System.currentTimeMillis());
	}
}
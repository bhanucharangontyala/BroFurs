package com.brofurs.brofurs.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.brofurs.brofurs.entity.Category;
import com.brofurs.brofurs.entity.Product;
import com.brofurs.brofurs.entity.ProductWoodPrice;
import com.brofurs.brofurs.entity.Role;
import com.brofurs.brofurs.entity.User;
import com.brofurs.brofurs.entity.WoodType;
import com.brofurs.brofurs.enums.StockStatus;
import com.brofurs.brofurs.repository.CategoryRepository;
import com.brofurs.brofurs.repository.ProductRepository;
import com.brofurs.brofurs.repository.ProductWoodPriceRepository;
import com.brofurs.brofurs.repository.RoleRepository;
import com.brofurs.brofurs.repository.UserRepository;
import com.brofurs.brofurs.repository.WoodTypeRepository;

/**
 * DataInitializer
 *
 * Runs once on every application startup (CommandLineRunner). Seeds the
 * database with default data only when the relevant table is empty, so re-runs
 * are safe and idempotent.
 *
 * Seeded data: 1. Roles → ROLE_ADMIN, ROLE_USER 2. Admin user →
 * admin@furnicraft.com / admin123 3. Categories → Beds, Shelves, Windows,
 * Doors, Cupboards, Others 4. Wood types → Neem, Mango, Teak, Other 5. Products
 * → 3 sample products with wood pricing
 */
@Component
public class DataInitializer implements CommandLineRunner {

	private final RoleRepository roleRepo;
	private final UserRepository userRepo;
	private final CategoryRepository categoryRepo;
	private final WoodTypeRepository woodTypeRepo;
	private final ProductRepository productRepo;
	private final ProductWoodPriceRepository woodPriceRepo;
	private final PasswordEncoder encoder;

	public DataInitializer(RoleRepository roleRepo, UserRepository userRepo, CategoryRepository categoryRepo,
			WoodTypeRepository woodTypeRepo, ProductRepository productRepo, ProductWoodPriceRepository woodPriceRepo,
			PasswordEncoder encoder) {
		this.roleRepo = roleRepo;
		this.userRepo = userRepo;
		this.categoryRepo = categoryRepo;
		this.woodTypeRepo = woodTypeRepo;
		this.productRepo = productRepo;
		this.woodPriceRepo = woodPriceRepo;
		this.encoder = encoder;
	}

	@Override
	public void run(String... args) {
		seedRoles();
		seedAdmin();
		seedCategories();
		seedWoodTypes();
		seedProducts();
	}

	// ──────────────────────────────────────────────
	// 1. Roles
	// ──────────────────────────────────────────────
	private void seedRoles() {
		if (roleRepo.count() == 0) {
			roleRepo.saveAll(List.of(new Role("ROLE_ADMIN"), new Role("ROLE_USER")));
			System.out.println("✅ Roles seeded: ROLE_ADMIN, ROLE_USER");
		}
	}

	// ──────────────────────────────────────────────
	// 2. Default Admin User
	// ──────────────────────────────────────────────
	private void seedAdmin() {
		if (!userRepo.existsByEmail("admin@furnicraft.com")) {
			Role adminRole = roleRepo.findByName("ROLE_ADMIN")
					.orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found — run seedRoles first"));

			User admin = new User();
			admin.setFullName("FurniCraft Admin");
			admin.setEmail("admin@furnicraft.com");
			admin.setPhone("9999999999");
			admin.setPassword(encoder.encode("admin123"));
			admin.setAddress("FurniCraft HQ, Mumbai, India");
			admin.setEnabled(true);
			admin.addRole(adminRole);

			userRepo.save(admin);
			System.out.println("✅ Admin user created: admin@furnicraft.com / admin123");
		}
	}

	// ──────────────────────────────────────────────
	// 3. Categories
	// ──────────────────────────────────────────────
	private void seedCategories() {
		if (categoryRepo.count() == 0) {
			String[][] categories = { { "Beds", "Comfortable and stylish wooden beds for every bedroom" },
					{ "Shelves", "Wall-mounted and freestanding shelving units" },
					{ "Windows", "Wooden window frames, shutters, and panels" },
					{ "Doors", "Interior and exterior solid wooden doors" },
					{ "Cupboards", "Spacious wardrobes, almirahs, and storage units" },
					{ "Others", "Miscellaneous furniture — tables, chairs, and more" } };

			for (String[] c : categories) {
				Category cat = new Category();
				cat.setName(c[0]);
				cat.setDescription(c[1]);
				cat.setActive(true);
				categoryRepo.save(cat);
			}
			System.out.println("✅ Categories seeded: Beds, Shelves, Windows, Doors, Cupboards, Others");
		}
	}

	// ──────────────────────────────────────────────
	// 4. Wood Types
	// ──────────────────────────────────────────────
	private void seedWoodTypes() {
		if (woodTypeRepo.count() == 0) {
			String[][] woodTypes = { { "Neem", "Light, durable, and naturally pest-resistant neem wood" },
					{ "Mango", "Rich-grained, eco-friendly mango wood with warm tones" },
					{ "Teak", "Premium quality teak — highly durable, water-resistant" },
					{ "Other", "Custom or mixed wood types as per requirement" } };

			for (String[] w : woodTypes) {
				WoodType wt = new WoodType();
				wt.setName(w[0]);
				wt.setDescription(w[1]);
				wt.setActive(true);
				woodTypeRepo.save(wt);
			}
			System.out.println("✅ Wood types seeded: Neem, Mango, Teak, Other");
		}
	}

	// ──────────────────────────────────────────────
	// 5. Sample Products
	// ──────────────────────────────────────────────
	private void seedProducts() {
		if (productRepo.count() > 0)
			return;

		List<Category> cats = categoryRepo.findByActiveTrueOrderByNameAsc();
		List<WoodType> woods = woodTypeRepo.findByActiveTrueOrderByNameAsc();

		if (cats.isEmpty() || woods.isEmpty())
			return;

		// Resolve categories by name
		Category beds = findCategory(cats, "Beds");
		Category doors = findCategory(cats, "Doors");
		Category cupboards = findCategory(cats, "Cupboards");

		// Price adjustments indexed to match seedWoodTypes insertion order:
		// Mango → +500, Neem → +0, Other → +1000, Teak → +3000
		// (findByActiveTrueOrderByNameAsc sorts alphabetically)
		BigDecimal[] adjustments = { new BigDecimal("500"), // Mango
				BigDecimal.ZERO, // Neem
				new BigDecimal("1000"), // Other
				new BigDecimal("3000") // Teak
		};

		createProduct("Royal King Size Bed", "royal-king-size-bed",
				"A majestic king-size bed with a hand-carved headboard and sturdy frame. "
						+ "Perfect for master bedrooms. Available in custom dimensions.",
				beds, new BigDecimal("18000"), true, StockStatus.IN_STOCK, woods, adjustments);

		createProduct("Classic Wooden Door", "classic-wooden-door",
				"Solid wood door with a timeless traditional design. "
						+ "Suitable for both interior and exterior use. Made to order.",
				doors, new BigDecimal("12000"), true, StockStatus.MADE_TO_ORDER, woods, adjustments);

		createProduct("3-Door Sliding Wardrobe", "3-door-sliding-wardrobe",
				"Spacious 3-door sliding wardrobe with a central mirror panel "
						+ "and smooth ball-bearing gliding mechanism.",
				cupboards, new BigDecimal("22000"), true, StockStatus.IN_STOCK, woods, adjustments);

		System.out.println("✅ Sample products seeded: Bed, Door, Wardrobe");
	}

	// ──────────────────────────────────────────────
	// Helpers
	// ──────────────────────────────────────────────

	private Category findCategory(List<Category> cats, String name) {
		return cats.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(cats.get(0));
	}

	private void createProduct(String name, String slug, String description, Category category, BigDecimal basePrice,
			boolean customSizeAllowed, StockStatus stockStatus, List<WoodType> woods, BigDecimal[] adjustments) {
		Product product = new Product();
		product.setName(name);
		product.setSlug(slug);
		product.setDescription(description);
		product.setCategory(category);
		product.setBasePrice(basePrice);
		product.setCustomSizeAllowed(customSizeAllowed);
		product.setStockStatus(stockStatus);
		product.setActive(true);
		productRepo.save(product);

		for (int i = 0; i < woods.size() && i < adjustments.length; i++) {
			ProductWoodPrice pwp = new ProductWoodPrice();
			pwp.setProduct(product);
			pwp.setWoodType(woods.get(i));
			pwp.setPriceAdjustment(adjustments[i]);
			pwp.setFinalPriceOverride(null);
			pwp.setActive(true);
			woodPriceRepo.save(pwp);
		}
	}
}

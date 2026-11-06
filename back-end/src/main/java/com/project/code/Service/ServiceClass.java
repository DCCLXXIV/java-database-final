package com.project.code.Service;

import org.springframework.stereotype.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;

@Service
public class ServiceClass {

    // 1. **validateInventory Method**:
    // - Checks if an inventory record exists for a given product and store
    // combination.
    // - Parameters: `Inventory inventory`
    // - Return Type: `boolean` (Returns `false` if inventory exists, otherwise
    // `true`)

    // 2. **validateProduct Method**:
    // - Checks if a product exists by its name.
    // - Parameters: `Product product`
    // - Return Type: `boolean` (Returns `false` if a product with the same name
    // exists, otherwise `true`)

    // 3. **ValidateProductId Method**:
    // - Checks if a product exists by its ID.
    // - Parameters: `long id`
    // - Return Type: `boolean` (Returns `false` if the product does not exist with
    // the given ID, otherwise `true`)

    // 4. **getInventoryId Method**:
    // - Fetches the inventory record for a given product and store combination.
    // - Parameters: `Inventory inventory`
    // - Return Type: `Inventory` (Returns the inventory record for the
    // product-store combination)

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    ServiceClass(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public boolean validateInventory(Inventory inventory) {
        Inventory existingInventory = inventoryRepository.findByProductIdandStoreId(inventory.getProduct().getId(),
                inventory.getStore().getId());

        if (existingInventory != null) {
            return false;
        }

        return true;
    }

    public boolean validateProduct(Product product) {
        Product existingProduct = productRepository.findByName(product.getName());

        if (existingProduct != null) {
            return false;
        }

        return true;
    }

    public boolean validateProductId(Long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return false;
        }

        return true;
    }

    public Inventory getInventoryId(Inventory inventory) {
        return inventoryRepository.findByProductIdandStoreId(inventory.getProduct().getId(),
                inventory.getStore().getId());
    }
}

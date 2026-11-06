package com.project.code.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.code.Model.Customer;
import com.project.code.Model.Inventory;
import com.project.code.Model.OrderDetails;
import com.project.code.Model.OrderItem;
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Product;
import com.project.code.Model.PurchaseProductDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.OrderDetailsRepository;
import com.project.code.Repo.OrderItemRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Repo.StoreRepository;

@Service
public class OrderService {
    // 1. **saveOrder Method**:
    // - Processes a customer's order, including saving the order details and
    // associated items.
    // - Parameters: `PlaceOrderRequestDTO placeOrderRequest` (Request data for
    // placing an order)
    // - Return Type: `void` (This method doesn't return anything, it just processes
    // the order)

    // 2. **Retrieve or Create the Customer**:
    // - Check if the customer exists by their email using `findByEmail`.
    // - If the customer exists, use the existing customer; otherwise, create and
    // save a new customer using `customerRepository.save()`.

    // 3. **Retrieve the Store**:
    // - Fetch the store by ID from `storeRepository`.
    // - If the store doesn't exist, throw an exception. Use
    // `storeRepository.findById()`.

    // 4. **Create OrderDetails**:
    // - Create a new `OrderDetails` object and set customer, store, total price,
    // and the current timestamp.
    // - Set the order date using `java.time.LocalDateTime.now()` and save the order
    // with `orderDetailsRepository.save()`.

    // 5. **Create and Save OrderItems**:
    // - For each product purchased, find the corresponding inventory, update stock
    // levels, and save the changes using `inventoryRepository.save()`.
    // - Create and save `OrderItem` for each product and associate it with the
    // `OrderDetails` using `orderItemRepository.save()`.

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final InventoryRepository inventoryRepository;

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final StoreRepository storeRepository;

    @Autowired
    private final OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private final OrderItemRepository orderItemRepository;

    public OrderService(
            ProductRepository productRepository,
            InventoryRepository inventoryRepository,
            CustomerRepository customerRepository,
            StoreRepository storeRepository,
            OrderDetailsRepository orderDetailsRepository,
            OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
        this.orderDetailsRepository = orderDetailsRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {
        Customer customer = customerRepository.findByEmail(placeOrderRequest.getCustomerEmail());

        if (customer == null) {
            customer = new Customer();

            customer.setName(placeOrderRequest.getCustomerName());
            customer.setEmail(placeOrderRequest.getCustomerEmail());
            customer.setPhone(placeOrderRequest.getCustomerPhone());

            customer = customerRepository.save(customer);
        }

        Store store = storeRepository.findById(placeOrderRequest.getStoreId()).orElseThrow();

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomer(customer);
        orderDetails.setStore(store);
        orderDetails.setTotalPrice(placeOrderRequest.getTotalPrice());
        orderDetails.setDate(java.time.LocalDateTime.now());

        orderDetailsRepository.save(orderDetails);

        for (PurchaseProductDTO product : placeOrderRequest.getPurchaseProduct()) {

            Inventory inventory = inventoryRepository.findByProductIdandStoreId(
                    product.getId(),
                    placeOrderRequest.getStoreId());

            inventory.setStockLevel(inventory.getStockLevel() - product.getQuantity());
            inventoryRepository.save(inventory);

            Product productEntity = productRepository.findById(product.getId()).orElseThrow();

            OrderItem orderItem = new OrderItem(
                    orderDetails,
                    productEntity,
                    product.getQuantity(),
                    product.getPrice());

            orderItemRepository.save(orderItem);
        }
    }
}

package com.project.code.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.code.Model.Customer;
import com.project.code.Model.Review;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.ReviewRepository;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    // 1. Set Up the Controller Class:
    // - Annotate the class with `@RestController` to designate it as a REST
    // controller for handling HTTP requests.
    // - Map the class to the `/reviews` URL using `@RequestMapping("/reviews")`.

    // 2. Autowired Dependencies:
    // - Inject the following dependencies via `@Autowired`:
    // - `ReviewRepository` for accessing review data.
    // - `CustomerRepository` for retrieving customer details associated with
    // reviews.

    // 3. Define the `getReviews` Method:
    // - Annotate with `@GetMapping("/{storeId}/{productId}")` to fetch reviews for
    // a specific product in a store by `storeId` and `productId`.
    // - Accept `storeId` and `productId` via `@PathVariable`.
    // - Fetch reviews using `findByStoreIdAndProductId()` method from
    // `ReviewRepository`.
    // - Filter reviews to include only `comment`, `rating`, and the `customerName`
    // associated with the review.
    // - Use `findById(review.getCustomerId())` from `CustomerRepository` to get
    // customer name.
    // - Return filtered reviews in a `Map<String, Object>` with key `reviews`.

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(
            @PathVariable Long storeId,
            @PathVariable Long productId) {

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> reviewsList = new ArrayList<>();

        List<Review> reviews = reviewRepository.findByStoreIdAndProductId(storeId, productId);

        for (Review review : reviews) {
            Map<String, Object> reviewData = new HashMap<>();

            Customer customer = customerRepository.findById(review.getCustomerId()).orElse(null);

            reviewData.put("comment", review.getComment());
            reviewData.put("rating", review.getRating());
            reviewData.put("customerName", customer != null ? customer.getName() : "Unknown");

            reviewsList.add(reviewData);
        }

        response.put("reviews", reviewsList);

        return response;
    }
}

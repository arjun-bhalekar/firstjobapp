package com.sb.app.firstjobapp.review;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/companies/{companyId}")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<Review>> getAllReviews(@PathVariable Long companyId) {
        return ResponseEntity.ok(reviewService.getAllReviews(companyId));
    }

    @PostMapping("/reviews")
    public ResponseEntity<String> addReview(@PathVariable Long companyId, @RequestBody Review review) {

        boolean isReviewSaved = reviewService.addReview(companyId, review);
        if (isReviewSaved)
            return new ResponseEntity<>("Review added successfully", HttpStatus.OK);
        else
            return new ResponseEntity<>("Review not saved", HttpStatus.NOT_FOUND);

    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long companyId,
                                            @PathVariable Long reviewId) {
        Review review = reviewService.getReview(companyId, reviewId);
        if (Objects.nonNull(review))
            return ResponseEntity.ok(review);
        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long companyId,
                                               @PathVariable Long reviewId,
                                               @RequestBody Review review) {
        if (reviewService.updateReview(companyId, reviewId, review))
            return new ResponseEntity<>("Review updated successfully",
                    HttpStatus.OK);
        else
            return new ResponseEntity<>("Review not updated",
                    HttpStatus.OK);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long companyId,
                                               @PathVariable Long reviewId) {
        if (reviewService.deleteReview(companyId, reviewId))
            return new ResponseEntity<>("Review deleted successfully",
                    HttpStatus.OK);
        else
            return new ResponseEntity<>("Review not deleted",
                    HttpStatus.OK);
    }

}



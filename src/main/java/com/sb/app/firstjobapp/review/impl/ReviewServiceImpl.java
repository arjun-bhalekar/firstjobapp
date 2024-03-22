package com.sb.app.firstjobapp.review.impl;

import com.sb.app.firstjobapp.company.Company;
import com.sb.app.firstjobapp.company.CompanyService;
import com.sb.app.firstjobapp.review.Review;
import com.sb.app.firstjobapp.review.ReviewRepository;
import com.sb.app.firstjobapp.review.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;
    private CompanyService companyService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, CompanyService companyService) {
        this.reviewRepository = reviewRepository;
        this.companyService = companyService;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean addReview(Long companyId, Review review) {

        Company company = companyService.getCompanyById(companyId);
        if (Objects.nonNull(company)) {
            review.setCompany(company);
            reviewRepository.save(review);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Review getReview(Long companyId, Long reviewId) {
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);
        return reviews.stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean updateReview(Long companyId, Long reviewId, Review updatedReview) {
        Company company = companyService.getCompanyById(companyId);
        if (Objects.nonNull(company)) {
            updatedReview.setCompany(company);
            updatedReview.setId(reviewId);
            reviewRepository.save(updatedReview);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReview(Long companyId, Long reviewId) {

        if (companyService.getCompanyById(companyId) != null
                && reviewRepository.existsById(reviewId)) {

            Review review = reviewRepository.findById(reviewId).orElse(null);
            if(review.getCompany().getId().equals(companyId)) {
//            Company company = review.getCompany();
//            company.getReviews().remove(review);
//            companyService.updateCompany(companyId, company);
                reviewRepository.deleteById(reviewId);
                return true;
            }
        }
        return false;
    }
}

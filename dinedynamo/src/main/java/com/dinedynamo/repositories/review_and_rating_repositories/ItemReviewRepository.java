package com.dinedynamo.repositories.review_and_rating_repositories;
import com.dinedynamo.collections.review_collections.ItemReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ItemReviewRepository extends MongoRepository<ItemReview, String> {
    List<ItemReview> findByItemId(String itemId);
}

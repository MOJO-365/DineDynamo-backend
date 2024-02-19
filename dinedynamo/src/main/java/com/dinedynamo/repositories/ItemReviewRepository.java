package com.dinedynamo.repositories;
import com.dinedynamo.collections.ItemReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ItemReviewRepository extends MongoRepository<ItemReview, String> {
    List<ItemReview> findByItemId(String itemId);
}

package com.dinedynamo.collections.order_collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "take-away-orders")
public class TakeAway {

        @Id
        private String takeAwayId;
        private String restaurantId;
        private String tableId;
        private String dateTime;
        private boolean prepared;
        private List<OrderList> orderLists;


}
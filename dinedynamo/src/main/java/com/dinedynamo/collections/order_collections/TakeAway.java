package com.dinedynamo.collections.order_collections;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "takeAway_orders")
public class TakeAway {

        @Id
        private String takeAwayId;
        private String restaurantId;
        private LocalDateTime dateTime;
        private String customerEmail;
        private String  customerName;
        private String  customerPhone;
        private boolean pickedUp;
        private List<OrderList> orderList;


}

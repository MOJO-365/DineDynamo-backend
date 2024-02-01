package com.dinedynamo.collections;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

@Document(collection = "takeaway")

public class TakeAway {
    @Id
    private String  takeAwayId;
    private String  restaurantId;
    private String customerEmail;
    private String  customerName;
    private String  customerPhone;
    private String  dateTime;
    private JSONArray OrderList;


}

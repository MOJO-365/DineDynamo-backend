package com.dinedynamo.collections.restaurant_collections;


import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BugQuery
{

    //String senderEmail;

    @Builder.Default
    String bugQueryTitle = " ";

    String bugDescription;

    String restaurantId;



}

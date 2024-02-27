package com.dinedynamo.trial;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("trial")
public class TrialCollection
{
    @Id
    String id;
    LocalDateTime localDateTime;
    LocalTime localTime;


}

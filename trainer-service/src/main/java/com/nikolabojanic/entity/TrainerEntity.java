package com.nikolabojanic.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(name = "firstName_lastName", def = "{'firstName': 1, 'lastName': 1}")
@Document(collection = "trainer")
public class TrainerEntity {
    @Id
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private List<YearEntity> years;
}

package ru.prudnikova.models.work;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Offer {
    private Integer id;
    private String title;
    private String state;
    private String move_building_id;
    private String move_newbuilding_secondary_id;
}

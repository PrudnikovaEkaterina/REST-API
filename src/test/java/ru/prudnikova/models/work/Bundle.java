package ru.prudnikova.models.work;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bundle {
    private Integer id;
    private String title;
    private String state;
    private ArrayList<Offer> offers;
}

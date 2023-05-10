package ru.prudnikova.models.work;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    private ArrayList<Bundle> data;
}

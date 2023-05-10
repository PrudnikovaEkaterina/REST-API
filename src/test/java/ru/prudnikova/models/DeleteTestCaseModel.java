package ru.prudnikova.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteTestCaseModel {
    private Integer id;
    private Integer projectId;
    private String name;
    private boolean deleted;
    private boolean automated;
    private boolean external;
    private ArrayList<Object> tags;
    private ArrayList<Object> links;
}

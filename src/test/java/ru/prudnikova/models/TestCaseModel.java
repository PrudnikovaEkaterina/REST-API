package ru.prudnikova.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCaseModel {
    private Integer id;
    private String name;
    private boolean automated;
    private boolean external;
    private long createdDate;
    private String statusName;
    private String statusColor;
}

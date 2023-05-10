package ru.prudnikova.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScenarioModel {
    public ArrayList<StepModel> steps;

    public static ScenarioModel createStepsTestCase() {
        ArrayList<StepModel> arrayList = StepModel.createListTestCase();
        ScenarioModel body=new ScenarioModel();
        body.setSteps(arrayList);
        return body;
    }
}

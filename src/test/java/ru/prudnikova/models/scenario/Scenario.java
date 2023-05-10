package ru.prudnikova.models.scenario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scenario {
    public ArrayList<Step> steps;

    public static Scenario createScenarioSteps() {
        ArrayList<Step> arrayList = Step.createListSteps();
        Scenario scenario=new Scenario();
        scenario.setSteps(arrayList);
        return scenario;
    }
}

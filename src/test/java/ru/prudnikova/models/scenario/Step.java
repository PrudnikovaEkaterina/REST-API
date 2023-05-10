package ru.prudnikova.models.scenario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Step {
    public String name;
    public ArrayList<Step> steps;
    public boolean leaf;
    public int stepsCount;
    public boolean hasContent;



    public static  ArrayList<Step> createListSteps(){
        Faker faker = new Faker();
        Step step = new Step();
        step.setName("Шаг "+faker.business().creditCardNumber());
        Step step1 = new Step();
        step1.setName("Шаг "+faker.business().creditCardNumber());
        ArrayList<Step> arrayList = new ArrayList<>();
        arrayList.add(step);
        arrayList.add(step1);
        return arrayList;
    }

}


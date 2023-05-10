package ru.prudnikova.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StepModel {
    private String name;
    private String spacing;

    public StepModel(String name, String spacing) {
        this.name = name;
        this.spacing = spacing;
    }


    public static  ArrayList<StepModel> createListTestCase(){
        Faker faker = new Faker();
        StepModel step = new StepModel("Шаг "+faker.business().creditCardNumber(), null);
        StepModel step1 = new StepModel("Шаг "+faker.business().creditCardNumber(), null);
        ArrayList<StepModel> arrayList = new ArrayList<>();
        arrayList.add(step);
        arrayList.add(step1);
        return arrayList;
    }

}


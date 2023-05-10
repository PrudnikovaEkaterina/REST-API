package ru.prudnikova.generators;

import com.github.javafaker.Faker;
import ru.prudnikova.models.TestCaseModel;

public class DescriptionGenerator {
    public static String descriptionGenerate() {
        String description;
        Faker faker = new Faker();
      return description=faker.dog().name();
    }
}

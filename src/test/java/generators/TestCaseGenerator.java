package generators;

import com.github.javafaker.Faker;
import ru.prudnikova.models.TestCaseModel;

public class TestCaseGenerator {

    public static TestCaseModel generationTestCaseName() {
        Faker faker = new Faker();
        TestCaseModel testCaseModel = new TestCaseModel();
        testCaseModel.setName(faker.funnyName().name());
        return testCaseModel;
    }
}

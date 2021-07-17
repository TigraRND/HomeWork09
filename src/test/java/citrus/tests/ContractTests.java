package citrus.tests;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ContractTests extends TestNGCitrusTestRunner {

    @Autowired
    private HttpClient restClient;
    private TestContext context;

    @BeforeClass
    public void startUp(){
        this.context = citrus.createTestContext();
    }

    @Test(testName = "Получение информации о пользователе")
    @CitrusTest
    public void getSingleUser(){
        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .get("users/2")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response(HttpStatus.valueOf(200))
                .messageType(MessageType.JSON)
                .payload("{\n" +
                        "   \"data\":{\n" +
                        "      \"id\":2,\n" +
                        "      \"email\":\"janet.weaver@reqres.in\",\n" +
                        "      \"first_name\":\"Janet\",\n" +
                        "      \"last_name\":\"Weaver\",\n" +
                        "      \"avatar\":\"https://reqres.in/img/faces/2-image.jpg\"\n" +
                        "   },\n" +
                        "   \"support\":{\n" +
                        "      \"url\":\"https://reqres.in/#support-heading\",\n" +
                        "      \"text\":\"To keep ReqRes free, contributions towards server costs are appreciated!\"\n" +
                        "   }\n" +
                        "}")
        );
    }

    @Test(testName = "Пользователь не найден")
    @CitrusTest
    public void getSingleUserNotFound(){
        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .get("users/23")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response(HttpStatus.valueOf(404))
                .messageType(MessageType.JSON)
        );
    }

    @Test(testName = "Создание пользователя")
    @CitrusTest
    public void postCreateUser(){
        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .post("users")
                .payload("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"leader\"\n" +
                        "}")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response(HttpStatus.valueOf(201))
                .messageType(MessageType.JSON)
        );
    }

    @Test(testName = "Удаление пользователя")
    @CitrusTest
    public void deleteUser(){
        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .delete("users/2")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response(HttpStatus.valueOf(204))
                .messageType(MessageType.JSON)
        );
    }

    @Test(testName = "Успешная регистрация")
    @CitrusTest
    public void postRegisterSuccessful(){
        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .post("register")
                .payload("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response(HttpStatus.valueOf(200))
                .messageType(MessageType.JSON)
        );
    }

    @Test(testName = "Неудачная авторизация")
    @CitrusTest
    public void postLoginUnsuccessful(){
        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .send()
                .post("login")
                .payload("{\n" +
                        "    \"email\": \"peter@klaven\"\n" +
                        "}")
        );

        http(httpActionBuilder -> httpActionBuilder
                .client(restClient)
                .receive()
                .response(HttpStatus.valueOf(400))
                .messageType(MessageType.JSON)
                .payload("{\n" +
                        "    \"error\": \"Missing password\"\n" +
                        "}")
        );
    }
}
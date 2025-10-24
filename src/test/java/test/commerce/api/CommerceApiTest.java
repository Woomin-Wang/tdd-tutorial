package test.commerce.api;

import commerce.CommerceApiApp;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
        classes = {CommerceApiApp.class, TestFixtureConfiguration.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

public @interface CommerceApiTest {

}

package test.commerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.ThrowingConsumer;

import java.util.Base64;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JWTAssertion {
    public static ThrowingConsumer<String> conformToJWTFormat() {
        return s -> {
            String[] parts = s.split("\\.");
            assertThat(parts).hasSize(3);
            assertThat(parts[0]).matches(JWTAssertion::isBase64UrlEncodedJson);
            assertThat(parts[1]).matches(JWTAssertion::isBase64UrlEncodedJson);
            assertThat(parts[2]).matches(JWTAssertion::isBase64UrlEncoded);
        };
    }

    private static boolean isBase64UrlEncodedJson(String s) {
        try {
            new ObjectMapper().readTree(Base64.getUrlDecoder().decode(s));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isBase64UrlEncoded(String s) {
        try {
            Base64.getUrlDecoder().decode(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

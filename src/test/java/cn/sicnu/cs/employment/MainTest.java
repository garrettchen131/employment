package cn.sicnu.cs.employment;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.IntStream;

public class MainTest {
    public static void main(String[] args) {


        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        System.out.println(key);
        System.out.println(key.getFormat());
        System.out.println(key.getAlgorithm());
        System.out.println(key.getEncoded());
        System.out.println(key.getEncoded().length);
        System.out.println(Arrays.toString(key.getEncoded()));
        System.out.println(new String(Base64.getEncoder().encode(key.getEncoded())));

    }
}

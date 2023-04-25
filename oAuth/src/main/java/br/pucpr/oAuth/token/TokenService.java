package br.pucpr.oAuth.token;

import br.pucpr.oAuth.Token_Resource;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Service
public class TokenService {

    private List<String> code_chalenge = new ArrayList<>();
    private List<String> code_verifier = new ArrayList<>();
    private List<String> state_verifier = new ArrayList<>();

    private String acess_token = "hey";

    public static Properties getProp() throws IOException {
        Properties props = new Properties();
        FileInputStream file = new FileInputStream(
                "D:\\PUC\\JV\\oauth2-authorization-code-flow\\oAuth\\src\\main\\resources\\token.properties");
        props.load(file);
        return props;

    }
    public Token_Response RequestToken(Token_Request credentials) throws IOException {

        var api = new RestTemplate();
        var uri = new DefaultUriBuilderFactory().builder()
                .scheme("https").host("github.com")
                .path("login/oauth/access_token")
                .build();
        Properties prop = getProp();
        String secret = prop.getProperty("token.client.secret");
        String clientid = prop.getProperty("token.client.id");
        var header = new HttpHeaders();
        header.add("Content-Type", "application/json");
        header.add("Accept", "application/json");

        var request_token = new Token_DTO(clientid, secret, credentials.getCode(), "http://localhost:8080/auth");
        var request = new HttpEntity<>(request_token , header);

        var response = api.exchange(
                uri,
                HttpMethod.POST,
                request,
                Token_Response.class

        );
        return response.getStatusCode().is2xxSuccessful() ? response.getBody(): null;
    }

    public static String randomString(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * alphabet.length());
            output.append(alphabet.charAt(randomIndex));
        }
        return output.toString();
    }
    public String getemails(String token) throws IOException {
        URL url = new URL("https://api.github.com/user/emails");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");

        httpConn.setRequestProperty("Accept", "application/vnd.github+json");
        httpConn.setRequestProperty("Authorization", "Bearer "+token);
        httpConn.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        System.out.println(response);
        return response;

    }

    public String authrequest(Token_Request request){
        var code = request.code;
        code_verifier.add(code);
        state_verifier.add(request.state);
        String sha256hex = DigestUtils.sha256Hex(code);
        code_chalenge.add(sha256hex);
        System.out.println(sha256hex);
        return  acess_token;
    }

    public String authcode(AuthorizationToken token){
        if( code_chalenge.contains(token.getChallengeString())){
            return  "AcessToken";
        }
        return null;
    }
}

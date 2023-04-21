package br.pucpr.oAuth.token;

import br.pucpr.oAuth.Token_Resource;
import com.fasterxml.jackson.annotation.JsonAlias;
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
import java.util.Scanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Service
public class TokenService {

    public static Properties getProp() throws IOException {
		Properties props = new Properties();
		FileInputStream file = new FileInputStream(
				"./properties/token.properties");
		props.load(file);
		return props;

	}
    public Token_Response RequestToken(Token_Request credentials) {

        var api = new RestTemplate();
        var uri = new DefaultUriBuilderFactory().builder()
                .scheme("https").host("github.com")
                .path("login/oauth/access_token")
                .build();
        Properties prop = getProp();
        secret = prop.getProperty("token.client.secret");
		clientid = prop.getProperty("token.client.id");
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
}

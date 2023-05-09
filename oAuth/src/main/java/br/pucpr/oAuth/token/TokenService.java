package br.pucpr.oAuth.token;

import org.apache.commons.codec.digest.DigestUtils;
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
import java.util.*;

import java.io.FileInputStream;

@Service
public class TokenService {


    Hashtable<String  , String> codeState =  new Hashtable<>();

    private List<String> code_chalenge = new ArrayList<>();
    private List<String> code_verifier = new ArrayList<>();
    private List<String> state_verifier = new ArrayList<>();

    private String acessToken = "hey";

    public static Properties getProp() throws IOException {
        Properties props = new Properties();
        FileInputStream file = new FileInputStream(
                "D:\\PUC\\JV\\oauth2-authorization-code-flow\\oAuth\\src\\main\\resources\\token.properties");
        props.load(file);
        return props;

    }
    public TokenResponse RequestToken(TokenRequest credentials) throws IOException {

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

        var request_token = new TokenDTO(clientid, secret, credentials.getChallengeString(), "http://localhost:8080/auth");
        var request = new HttpEntity<>(request_token , header);

        var response = api.exchange(
                uri,
                HttpMethod.POST,
                request,
                TokenResponse.class

        );
        return response.getStatusCode().is2xxSuccessful() ? response.getBody(): null;
    }

    public static TokenResponseDTO randomString(int length) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * alphabet.length());
            output.append(alphabet.charAt(randomIndex));
        }
        String sha256hex = DigestUtils.sha256Hex(output.toString());
        TokenResponseDTO response = new TokenResponseDTO(output.toString() , sha256hex);
        return response;
    }
    public String getemails(String token) throws IOException {
        URL url = new URL("https://api.github.com/user/emails");
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");

        httpConn.setRequestProperty("Accept", "application/vnd.github+json");
        httpConn.setRequestProperty("Authorization", "Bearer " + token);
        httpConn.setRequestProperty("X-GitHub-Api-Version", "2022-11-28");

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        System.out.println(response);
        return response;

    }

    public String authrequest(TokenRequest request){
        var CS = request.challengeString;
        var state = request.state;

        code_chalenge.add(CS);


        return  acessToken;
    }

    public String authcode(AuthorizationToken token){

        var code = token.getCode();

        String codeChallenge = DigestUtils.sha256Hex(code);


        if(code_chalenge.contains(codeChallenge)){
            return  "AcessToken";
        }
        return null;
    }
}

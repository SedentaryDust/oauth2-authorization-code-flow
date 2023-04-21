package br.pucpr.oAuth;


import br.pucpr.oAuth.token.TokenService;
import br.pucpr.oAuth.token.Token_Request;
import br.pucpr.oAuth.token.Token_Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class Token_Resource {
    private TokenService service;
    private String acess_token;
    public Token_Resource(TokenService service){
        this.service = service;
    }
    @GetMapping("/")
    public void PathVar(@RequestParam("code") String code , @RequestParam("state") String state) throws IOException {
        var request =  new Token_Request(code , state);
        System.out.println("PEGUEI AS VARAIVEIS " + request.getCode() +  "  espacinho   " + request.getState() );

        var response = service.RequestToken(request);
        acess_token = response.getAccess_token();
        System.out.println(response.getAccess_token());
    }

    @GetMapping("/emails")
    public void ShowsEmais() throws IOException {

        String response = service.getemails(acess_token);
        System.out.println(response);
    }
}

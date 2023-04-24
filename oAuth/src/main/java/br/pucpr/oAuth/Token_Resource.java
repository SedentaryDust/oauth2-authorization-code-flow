package br.pucpr.oAuth;


import br.pucpr.oAuth.token.TokenService;
import br.pucpr.oAuth.token.Token_Request;
import br.pucpr.oAuth.token.Token_Response;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/token/api")
public class Token_Resource {
    private TokenService service;
    private String acess_token;
    public Token_Resource(TokenService service){
        this.service = service;
    }
    @PostMapping("/")
    public String PathVar(@Valid @RequestBody Token_Request request) {
        return service.authcode(request);

    }

    @GetMapping("/emails")
    public void ShowsEmais() throws IOException {

        String response = service.getemails(acess_token);
        System.out.println(response);
    }
}

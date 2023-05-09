package br.pucpr.oAuth;


import br.pucpr.oAuth.token.AuthorizationToken;
import br.pucpr.oAuth.token.TokenResponseDTO;
import br.pucpr.oAuth.token.TokenService;
import br.pucpr.oAuth.token.TokenRequest;
import jakarta.validation.Valid;
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
    public String PathVar(@Valid @RequestBody TokenRequest request) {
        return service.authrequest(request);

    }

    @GetMapping("/generate_code")
    public TokenResponseDTO CodeGenerator(){
        return service.randomString(10);
    }
    @PostMapping("/getAuthToken")
    public String AuthVar(@Valid @RequestBody AuthorizationToken request) {
        var response = service.authcode(request);
        return response != null ? response : "codigo invalido";
    }
}

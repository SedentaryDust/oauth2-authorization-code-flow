package br.pucpr.oAuth.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Token_Response {
        String access_token;
        String token_type;
        String scope;
}

package br.pucpr.oAuth.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Data
public class TokenRequest {
    String code;
    String state;
}

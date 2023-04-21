package br.pucpr.oAuth.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Data
public class Token_DTO {
    String client_id;
    String client_secret;
    String code;
    String redirect;




}

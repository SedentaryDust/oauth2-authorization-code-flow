package br.pucpr.oAuth.token;

import lombok.Data;

@Data
public class AuthorizationToken {

    String authcode;

    String challengeString;
}

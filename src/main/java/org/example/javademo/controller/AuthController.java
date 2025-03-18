package org.example.javademo.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.javademo.Constant.Constant;
import org.example.javademo.config.RestConfig;
import org.example.javademo.response.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${keycloak.auth-server-url}")
    private String keycloakBaseUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.redirect-uri}")
    private String redirectUri;

    @Value("${app.frontend-url:http://localhost:3000/dashboard}") // URL frontend, có thể cấu hình
    private String frontendUrl;

    private final RestConfig restConfig;

    private static final String TOKEN_ENDPOINT = "/protocol/openid-connect/token";

    private final String tokenUrl;

    // Constructor để khởi tạo tokenUrl
    public AuthController(RestConfig restConfig,
                          @Value("${keycloak.auth-server-url}") String keycloakBaseUrl,
                          @Value("${keycloak.realm}") String realm,
                          @Value("${keycloak.resource}") String clientId,
                          @Value("${keycloak.client-secret}") String clientSecret,
                          @Value("${keycloak.redirect-uri}") String redirectUri,
                          @Value("${app.frontend-url:http://localhost:3000/ai}") String frontendUrl) {
        this.restConfig = restConfig;
        this.keycloakBaseUrl = keycloakBaseUrl;
        this.realm = realm;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.frontendUrl = frontendUrl;
        this.tokenUrl = keycloakBaseUrl + "/realms/" + realm + TOKEN_ENDPOINT;
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login() {
        String authUrl = keycloakBaseUrl + "/realms/" + realm + "/protocol/openid-connect/auth" +
                "?response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri;
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(authUrl)).build();
    }

    @GetMapping("/callback")
    public ResponseEntity<Void> callback(@RequestParam String code, HttpServletResponse httpResponse) {
        HttpEntity<MultiValueMap<String, String>> request = buildTokenRequest(code);

        try {
            ResponseEntity<Map> response = restConfig.restTemplate().postForEntity(tokenUrl, request, Map.class);
            Map<String, Object> body = response.getBody();

            if (body == null || !response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
            }

            String accessToken = (String) body.get(Constant.JWT_ACCESS_TOKEN);
            String refreshToken = (String) body.get(Constant.JWT_REFRESH_TOKEN);

            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 3600); // Ví dụ: 7 ngày
            httpResponse.addCookie(refreshCookie);

            System.out.println("aceess token :" + accessToken);

            // Redirect về frontend
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl)).build();
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/me")
    public AuthResponse getUserInfo(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return new AuthResponse(
                jwt.getClaimAsString(Constant.JWT_USERNAME),
                jwt.getClaimAsString(Constant.JWT_EMAIL),
                jwt.getClaim(Constant.JWT_REALM_ACCESS) != null ? jwt.getClaim(Constant.JWT_REALM_ACCESS) : Map.of(),
                jwt.getClaim(Constant.JWT_RESOURCE_ACCESS) != null ? jwt.getClaim(Constant.JWT_RESOURCE_ACCESS) : Map.of()
        );
    }

    private HttpEntity<MultiValueMap<String, String>> buildTokenRequest(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add(Constant.JWT_GRANT_TYPE, Constant.JWT_AUTHORIZATION_CODE);
        form.add(Constant.JWT_CODE, code);
        form.add(Constant.JWT_CLIENT_ID, clientId);
        form.add(Constant.JWT_CLIENT_SECRET, clientSecret);
        form.add(Constant.JWT_REDIRECT, redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return new HttpEntity<>(form, headers);
    }
}
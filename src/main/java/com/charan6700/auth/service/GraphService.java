package com.charan6700.auth.service;

import com.charan6700.auth.dto.GraphUserDataResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class GraphService {

    @Value("${microsoft.graph.client-id}")
    private String clientId;

    @Value("${microsoft.graph.client-secret}")
    private String clientSecret;

    @Value("${microsoft.graph.tenant-id}")
    private String tenantId;

    @Value("${microsoft.graph.scope}")
    private String scope;

    private final WebClient webClient = WebClient.builder().build();

    // Get Microsoft Graph Access Token
    public String getAccessToken() {
        String tokenUrl = UriComponentsBuilder
                .fromHttpUrl("https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token")
                .toUriString();

        String requestBody = "grant_type=" + encode("client_credentials") +
                "&client_id=" + encode(clientId) +
                "&client_secret=" + encode(clientSecret) +
                "&scope=" + encode(scope);

        Map<String, String> response = webClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return response != null ? response.get("access_token") : null;
    }

    // Fetch All Users from Microsoft Graph API
    public GraphUserDataResponse getAllUsers(String token) {
        String accessToken = getAccessToken();
        if (accessToken == null) {
            throw new RuntimeException("Failed to obtain access token");
        }

        System.out.println("Access token in graph: " + accessToken);

        String url = "https://graph.microsoft.com/v1.0/users";

        return webClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(GraphUserDataResponse.class)
                .block();
    }

    public String sendEmail(String recipientEmail, String subject, String body) {
        String accessToken = getAccessToken(); // Get a valid token

        System.out.println("Using Token: " + accessToken); // Debugging

        // Construct JSON email payload
        String emailJson = """
        {
          "message": {
            "subject": "%s",
            "body": {
              "contentType": "HTML",
              "content": "%s"
            },
            "toRecipients": [
              {
                "emailAddress": {
                  "address": "%s"
                }
              }
            ]
          }
        }
        """.formatted(subject, body, recipientEmail);

        return webClient.post()
                .uri("https://graph.microsoft.com/v1.0/users/test-user@saicharankadaveruoutlook.onmicrosoft.com/sendMail")
                .headers(headers -> {
                    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
                    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .bodyValue(emailJson) // ✅ Send JSON body
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Graph API Response: " + response))
                .block();
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}


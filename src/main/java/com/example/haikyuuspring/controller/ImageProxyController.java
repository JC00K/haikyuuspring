package com.example.haikyuuspring.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/proxy")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")  // ← FIXED
public class ImageProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final List<String> ALLOWED_DOMAINS = Arrays.asList(
            "static.wikia.nocookie.net",
            "vignette.wikia.nocookie.net",
            "picsum.photos",
            "via.placeholder.com",
            "i.imgur.com"
    );

    @GetMapping("/image")
    public ResponseEntity<byte[]> proxyImage(@RequestParam String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();

            if (ALLOWED_DOMAINS.stream().noneMatch(host::contains)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            byte[] imageBytes = restTemplate.getForObject(url, byte[].class);

            MediaType contentType = determineContentType(url);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(contentType);
            headers.setCacheControl("max-age=86400");

            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private MediaType determineContentType(String url) {
        String lowerUrl = url.toLowerCase();
        if (lowerUrl.endsWith(".png")) return MediaType.IMAGE_PNG;
        if (lowerUrl.endsWith(".jpg") || lowerUrl.endsWith(".jpeg")) return MediaType.IMAGE_JPEG;
        if (lowerUrl.endsWith(".gif")) return MediaType.IMAGE_GIF;
        if (lowerUrl.endsWith(".webp")) return MediaType.parseMediaType("image/webp");
        return MediaType.IMAGE_JPEG;
    }
}
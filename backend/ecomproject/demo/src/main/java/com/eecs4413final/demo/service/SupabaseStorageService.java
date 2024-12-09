package com.eecs4413final.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class SupabaseStorageService {

    private final WebClient webClient;

    @Value("${SUPABASE_URL}")
    private String supabaseUrl;

    @Value("${SUPABASE_API_KEY}")
    private String supabaseApiKey;

    @Autowired
    public SupabaseStorageService(@Value("${SUPABASE_URL}") String supabaseUrl,
                                  @Value("${SUPABASE_API_KEY}") String supabaseApiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(supabaseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + supabaseApiKey)
                .defaultHeader("apikey", supabaseApiKey)
                .build();
    }

    /**
     * Uploads an image to the 'product-images' bucket in Supabase Storage.
     *
     * @param file     The image file to upload.
     * @param fileName The desired name of the file in storage.
     * @return The public URL of the uploaded image.
     * @throws IOException If an I/O error occurs.
     */
    public Mono<String> uploadImage(MultipartFile file, String fileName) throws IOException {
        System.out.println("Uploading image: " + fileName);
        // Convert MultipartFile to ByteArrayResource
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return fileName;
            }
        };

        return webClient.post()
                .uri("/storage/v1/object/product-images/" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .bodyValue(resource)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> System.out.println("Image uploaded successfully: " + response))
                .doOnError(error -> System.err.println("Error uploading image: " + error.getMessage()))
                .map(response -> supabaseUrl + "/storage/v1/object/public/product-images/" + fileName);
    }

    /**
     * Deletes an image from the 'product-images' bucket in Supabase Storage.
     *
     * @param fileName The name of the file to delete.
     * @return A Mono signaling completion.
     */
    public Mono<Void> deleteImage(String fileName) {
        System.out.println("Deleting image: " + fileName);
        return webClient.delete()
                .uri("/storage/v1/object/product-images/" + fileName)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(aVoid -> System.out.println("Image deleted successfully: " + fileName))
                .doOnError(error -> System.err.println("Error deleting image: " + error.getMessage()));
    }
}
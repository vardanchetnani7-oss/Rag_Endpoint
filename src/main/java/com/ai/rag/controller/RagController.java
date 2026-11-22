package com.ai.rag.controller;


import com.ai.rag.service.PdfIngestService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RagController {

    private final PdfIngestService ingestService;
    private final ChatClient chatClient;

    public RagController(PdfIngestService ingestService, ChatClient chatClient) {
        this.ingestService = ingestService;
        this.chatClient = chatClient;
    }

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) throws IOException {
        int chunks = ingestService.ingest(new ByteArrayResource(file.getBytes()));
        return Map.of("status", "ingested", "chunks", chunks);
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String question) {
        return chatClient.prompt().user(question).call().content();
    }
}

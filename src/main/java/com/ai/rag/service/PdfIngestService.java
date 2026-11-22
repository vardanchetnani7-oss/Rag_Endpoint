package com.ai.rag.service;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfIngestService {

    private final VectorStore vectorStore;

    public PdfIngestService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public int ingest(Resource pdf) {
        var reader = new PagePdfDocumentReader(pdf,
                PdfDocumentReaderConfig.builder()
                        .withPagesPerDocument(1)
                        .build());

        List<Document> chunks = TokenTextSplitter.builder().build().apply(reader.get());
        vectorStore.add(chunks);
        return chunks.size();
    }
}
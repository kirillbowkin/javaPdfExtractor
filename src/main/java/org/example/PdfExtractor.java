package org.example;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;


public class PdfExtractor {
    private final PDDocument pdfDocument;

    /**
     * Constructor
     *
     * @param pdfFile
     * @throws PdfLoadException
     */
    public PdfExtractor(File pdfFile) throws PdfLoadException {
        try {
            this.pdfDocument = PDDocument.load(pdfFile);
        } catch (IOException e) {
            throw new PdfLoadException("Failed to load pdf file", e.getCause());
        }
    }

    public List<String> getHighlightedWords() {
        return Collections.emptyList();
    }
}

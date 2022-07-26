package org.example;

import org.junit.jupiter.api.BeforeEach;

import java.io.File;

class PdfExtractorTest {

    private PdfExtractor pdfExtractor;

    @BeforeEach
    void setUp() throws PdfLoadException {
        // This pdf contains highlighted text
        File pdfFile = new File("src/test/resources/Willows.pdf");
        this.pdfExtractor = new PdfExtractor(pdfFile);
    }

}
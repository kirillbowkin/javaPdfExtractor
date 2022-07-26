package org.example;

import org.junit.jupiter.api.BeforeAll;

import java.io.File;

class PdfExtractorTest {

    @BeforeAll
    void setUp() throws PdfLoadException {
        // This pdf contains highlighted text
        File pdfFile = new File("src/test/resources/Willows.pdf");
        PdfExtractor pdfExtractor = new PdfExtractor(pdfFile);
    }

}
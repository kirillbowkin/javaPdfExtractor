package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

class PdfExtractorTest {

    private PdfExtractor pdfExtractor;

    @BeforeEach
    void setUp() throws PdfLoadException {
        // This pdf contains highlighted text
        File pdfFile = new File("src/test/resources/Willows.pdf");
        this.pdfExtractor = new PdfExtractor(pdfFile);
    }

    @Test
    void shouldPassIfThereAreHighlightedWords() {
        List<String> highlightedWords = this.pdfExtractor.getHighlightedWords();
        assertNotNull(highlightedWords);
        assertTrue(highlightedWords.size() > 0);
    }

}
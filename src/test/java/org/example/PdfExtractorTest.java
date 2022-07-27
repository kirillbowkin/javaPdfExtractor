package org.example;

import org.example.exceptions.PdfLoadException;
import org.example.exceptions.WordsExtractionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PdfExtractorTest {

    private PdfExtractor pdfExtractor;

    @BeforeEach
    void setUp() throws PdfLoadException {
        // This pdf contains highlighted text
        File pdfFile = new File("src/test/resources/Willows.pdf");
        this.pdfExtractor = new PdfExtractor(pdfFile);
    }

    @Test
    void shouldPassIfThereAreHighlightedWords() throws WordsExtractionException {
        List<String> highlightedWords = this.pdfExtractor.getHighlightedWords();
        assertNotNull(highlightedWords);
        assertTrue(highlightedWords.size() > 0);
    }

    @Test
    void shouldPassIfReturnsAllHighlightedWords() throws WordsExtractionException {
        List<String> highlightedWords = this.pdfExtractor.getHighlightedWords();
        assertEquals(highlightedWords.size(), 6);
        assertEquals(highlightedWords, Arrays.asList("flood", "sand", "masses of shore", "kingdom", "Donaueschingen", "Danube"));
    }

}
package org.example;

import org.example.exceptions.GetSquiggledWordsException;
import org.example.exceptions.GetUnderlinedWordsException;
import org.example.exceptions.PdfLoadException;
import org.example.exceptions.GetHighlightedWordsException;
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
        // This pdf contains highlighted, underlined text
        File pdfFile = new File("src/test/resources/Willows.pdf");
        this.pdfExtractor = new PdfExtractor(pdfFile);
    }

    @Test
    void shouldPassIfThereAreHighlightedWords() throws GetHighlightedWordsException {
        List<String> highlightedWords = this.pdfExtractor.getHighlightedWords();
        assertNotNull(highlightedWords);
        assertTrue(highlightedWords.size() > 0);
    }

    @Test
    void shouldPassIfReturnsAllHighlightedWords() throws GetHighlightedWordsException {
        List<String> highlightedWords = this.pdfExtractor.getHighlightedWords();
        assertEquals(highlightedWords.size(), 6);
        assertEquals(highlightedWords, Arrays.asList("flood", "sand", "masses of shore", "kingdom", "Donaueschingen", "Danube"));
    }

    @Test
    void shouldPassIfThereAreUnderlinedWords() throws GetUnderlinedWordsException {
        List<String> underlinedWords = this.pdfExtractor.getUnderlinedWords();
        assertNotNull(underlinedWords);
        assertTrue(underlinedWords.size() > 0);
    }

    @Test
    void shouldPassIfReturnsAllUnderlinedWords() throws GetUnderlinedWordsException {
        List<String> highlightedWords = this.pdfExtractor.getUnderlinedWords();
        assertEquals(highlightedWords.size(), 2);
        assertEquals(highlightedWords, Arrays.asList("These willows never attain to the dignity  of  trees", "green  swells  like  the  sea"));
    }

    @Test
    void shouldPassIfThereAreSquiggledWords() throws GetSquiggledWordsException {
        List<String> squiggledWords = this.pdfExtractor.getSquiggledWords();
        assertNotNull(squiggledWords);
        assertTrue(squiggledWords.size() > 0);
    }

    @Test
    void shouldPassIfReturnsAllSquiggledWords() throws GetSquiggledWordsException {
        List<String> squiggledWords = this.pdfExtractor.getSquiggledWords();
        assertEquals(squiggledWords.size(), 2);
        assertEquals(squiggledWords, Arrays.asList("After leaving Vienna and long before you come to Budapest the Danube", "Sumpfe"));
    }

}
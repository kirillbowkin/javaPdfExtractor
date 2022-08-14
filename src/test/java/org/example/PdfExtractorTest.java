package org.example;

import org.example.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PdfExtractorTest {

    private PdfExtractor pdfExtractor;

    @BeforeEach
    void setUp() throws PdfLoadException {
        // This pdf contains highlighted, underlined, squiggled, stroke out text
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
    void shouldPassIfReturnsAllHighlightedWordsForPagesRange() throws GetHighlightedWordsException {
        List<String> highlightedWords = this.pdfExtractor.getHighlightedWords(IntStream.range(0, 1));
        assertNotNull(highlightedWords);
        assertEquals(4, highlightedWords.size());
        assertEquals(highlightedWords, Arrays.asList("flood", "sand", "masses of shore", "kingdom"));
    }

    @Test
    void shouldPassIfThereAreUnderlinedWords() throws GetUnderlinedWordsException {
        List<String> underlinedWords = this.pdfExtractor.getUnderlinedWords();
        assertNotNull(underlinedWords);
        assertTrue(underlinedWords.size() > 0);
    }

    @Test
    void shouldPassIfReturnsAllUnderlinedWords() throws GetUnderlinedWordsException {
        List<String> underlinedWords = this.pdfExtractor.getUnderlinedWords();
        assertEquals(underlinedWords.size(), 5);
        assertEquals(underlinedWords, Arrays.asList("These willows never attain to the dignity  of  trees", "green  swells  like  the  sea", "had", "little", "craft"));
    }

    @Test
    void shouldPassIfReturnsAllUnderlinedWordsForPagesRange() throws GetUnderlinedWordsException {
        List<String> underlinedWords = this.pdfExtractor.getUnderlinedWords(IntStream.range(0, 1));
        assertEquals(underlinedWords.size(), 2);
        assertEquals(underlinedWords, Arrays.asList("These willows never attain to the dignity  of  trees", "green  swells  like  the  sea"));
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
        assertEquals(squiggledWords.size(), 5);
        assertEquals(squiggledWords, Arrays.asList("After leaving Vienna and long before you come to Budapest the Danube", "Sumpfe", "At", "heard", "many"));
    }

    @Test
    void shouldPassIfReturnsAllSquiggledWordsForPagesRange() throws GetSquiggledWordsException {
        List<String> squiggledWords = this.pdfExtractor.getSquiggledWords(IntStream.range(0, 1));
        assertEquals(squiggledWords.size(), 2);
        assertEquals(squiggledWords, Arrays.asList("After leaving Vienna and long before you come to Budapest the Danube", "Sumpfe"));
    }

    @Test
    void shouldPassIfThereAreStrokeOutWords() throws GetStrokeoutWordsException {
        List<String> strokeOutWords = this.pdfExtractor.getStrokeoutWords();
        assertNotNull(strokeOutWords);
        assertTrue(strokeOutWords.size() > 0);
    }

    @Test
    void shouldPassIfReturnsAllStrokeOutWords() throws GetStrokeoutWordsException {
        List<String> strokeOutWords = this.pdfExtractor.getStrokeoutWords();
        assertEquals(strokeOutWords.size(), 5);
        assertEquals(strokeOutWords, Arrays.asList("becomes a swamp for miles upon miles", "marshes", "And", "pleasure", "Passau"));
    }

    @Test
    void shouldPassIfReturnsAllStrokeOutWordsForPagesRange() throws GetStrokeoutWordsException {
        List<String> strokeOutWords = this.pdfExtractor.getStrokeoutWords(IntStream.range(0, 1));
        assertEquals(strokeOutWords.size(), 2);
        assertEquals(strokeOutWords, Arrays.asList("becomes a swamp for miles upon miles", "marshes"));
    }

}
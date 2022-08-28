package org.example;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.example.exceptions.*;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static org.example.util.ThrowingConsumer.throwingConsumer;


public class PdfExtractor {
    private final PDDocument pdfDocument;
    private final List<PDPage> pdPages;

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

        this.pdPages = getPdPages();
    }

    private List<String> getAnnotatedWords(PdfAnnotationType annotationType, IntStream pages) throws GetAnnotatedWordsException {
        List<String> annotatedTexts = new ArrayList<>();

        try {
            pages
                    .mapToObj(this.pdPages::get)
                    .parallel()
                    .forEachOrdered(throwingConsumer(pdfPage -> {
                        System.out.println(Thread.currentThread().getName());
                        var annotations = pdfPage.getAnnotations();
                        var wordsForAnnotations = getWordsForAnnotations(pdfPage, annotations, annotationType);
                        annotatedTexts.addAll(wordsForAnnotations);
                    }));
        } catch (Exception e) {
            throw new GetAnnotatedWordsException("Failed to get annotated words", e.getCause());
        }
        return annotatedTexts;
    }

    /**
     * returns list of all pages for pdf document
     *
     * @return list of PDPages
     */
    private List<PDPage> getPdPages() {
        return StreamSupport
                .stream(this.pdfDocument.getPages().spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * returns list of highlighted words for all pages of pdf document
     *
     * @return list of highlighted words
     * @throws GetHighlightedWordsException
     */
    public List<String> getHighlightedWords(IntStream pages) throws GetHighlightedWordsException {
        try {
            return getAnnotatedWords(PdfAnnotationType.HIGHLIGHTED, pages);
        } catch (GetAnnotatedWordsException e) {
            throw new GetHighlightedWordsException("Failed to get highlighted words", e.getCause());
        }
    }

    public List<String> getHighlightedWords() throws GetHighlightedWordsException {
        try {
            return getAnnotatedWords(PdfAnnotationType.HIGHLIGHTED, IntStream.range(0, this.pdfDocument.getNumberOfPages()));
        } catch (GetAnnotatedWordsException e) {
            throw new GetHighlightedWordsException("Failed to get highlighted words", e.getCause());
        }
    }

    /**
     * returns list of underlined words for all pages of pdf document
     *
     * @return list of underlined words
     * @throws GetUnderlinedWordsException
     */
    public List<String> getUnderlinedWords(IntStream pages) throws GetUnderlinedWordsException {
        try {
            return getAnnotatedWords(PdfAnnotationType.UNDERLINED, pages);
        } catch (GetAnnotatedWordsException e) {
            throw new GetUnderlinedWordsException("Failed to get underlined words", e.getCause());
        }
    }

    public List<String> getUnderlinedWords() throws GetUnderlinedWordsException {
        try {
            return getAnnotatedWords(PdfAnnotationType.UNDERLINED, IntStream.range(0, this.pdfDocument.getNumberOfPages()));
        } catch (GetAnnotatedWordsException e) {
            throw new GetUnderlinedWordsException("Failed to get underlined words", e.getCause());
        }
    }

    /**
     * returns list of squiggled words for all pages of pdf document
     *
     * @return list of squiggled words
     * @throws GetSquiggledWordsException
     */
    public List<String> getSquiggledWords(IntStream pages) throws GetSquiggledWordsException {
        try {
            return getAnnotatedWords(PdfAnnotationType.SQUIGGLY, pages);
        } catch (GetAnnotatedWordsException e) {
            throw new GetSquiggledWordsException("Failed to get squiggled words", e.getCause());
        }
    }

    public List<String> getSquiggledWords() throws GetSquiggledWordsException {
        try {
            return getAnnotatedWords(PdfAnnotationType.SQUIGGLY, IntStream.range(0, this.pdfDocument.getNumberOfPages()));
        } catch (GetAnnotatedWordsException e) {
            throw new GetSquiggledWordsException("Failed to get squiggled words", e.getCause());
        }
    }


    /**
     * returns list of strokeout words for all pages of pdf document
     *
     * @return list of strokeout words
     * @throws GetStrokeoutWordsException
     */
    public List<String> getStrokeoutWords(IntStream pages) throws GetStrokeoutWordsException {
        try {
            return getAnnotatedWords(PdfAnnotationType.STRIKEOUT, pages);
        } catch (GetAnnotatedWordsException e) {
            throw new GetStrokeoutWordsException("Failed to get stroke out words", e.getCause());
        }
    }

    public List<String> getStrokeoutWords() throws GetStrokeoutWordsException {
        try {
            return getAnnotatedWords(PdfAnnotationType.STRIKEOUT, IntStream.range(0, this.pdfDocument.getNumberOfPages()));
        } catch (GetAnnotatedWordsException e) {
            throw new GetStrokeoutWordsException("Failed to get stroke out words", e.getCause());
        }
    }

    private List<String> getWordsForAnnotations(PDPage pdfpage, List<PDAnnotation> annotations, PdfAnnotationType annotationType) throws GetWordsForAnnotationsException {
        List<String> annotationTexts = new ArrayList<>();
        try {
            for (int i = 0; i < annotations.size(); i++) {
                PDAnnotation annot = annotations.get(i);
                var annotNote = annot.getContents(); // Conteudo anotado na nota
                var annotSubType = annot.getSubtype(); // Tipo da nota (Highlight, Text)
                // annotTitle = annot.getTitlePopup(); // Autor da nota
                if (annotSubType.equals(annotationType.getType())) {
                    // extract highlighted text
                    PDFTextStripperByArea stripper = null;
                    try {
                        stripper = new PDFTextStripperByArea();
                    } catch (IOException e) {
                        throw new PDFTextStripperByAreaCreationException("Failed to create PDFTextStripperByArea", e.getCause());
                    }
                    COSArray quadsArray = (COSArray) annot.getCOSObject().getCOSArray(COSName.getPDFName("QuadPoints"));
                    String str = null;
                    for (int j = 1, k = 0; j <= (quadsArray.size() / 8); j++) {
                        //TODO: don't like how this indexes look like, it would be better to get rid of these vague indexes at all if possible
                        Float ULX = ((COSNumber) quadsArray.get(0 + k)).floatValue();
                        Float ULY = ((COSNumber) quadsArray.get(1 + k)).floatValue();
                        Float URX = ((COSNumber) quadsArray.get(2 + k)).floatValue();
                        Float URY = ((COSNumber) quadsArray.get(3 + k)).floatValue();
                        Float LLX = ((COSNumber) quadsArray.get(4 + k)).floatValue();
                        Float LLY = ((COSNumber) quadsArray.get(5 + k)).floatValue();
                        Float LRX = ((COSNumber) quadsArray.get(6 + k)).floatValue();
                        Float LRY = ((COSNumber) quadsArray.get(7 + k)).floatValue();
                        k += 8;
                        float ulx = ULX - 1; // upper left x.
                        float uly = ULY; // upper left y.
                        float width = URX - LLX;          // calculated by upperRightX - lowerLeftX.
                        float height = URY - LLY;         // calculated by upperRightY - lowerLeftY.

                        PDRectangle pageSize = pdfpage.getMediaBox();
                        uly = pageSize.getHeight() - uly;

                        Rectangle2D.Float rectangle_2 = new Rectangle2D.Float(ulx, uly, width, height);
                        stripper.addRegion("annotatedRegion", rectangle_2);
                        try {
                            stripper.extractRegions(pdfpage);
                        } catch (IOException e) {
                            throw new PDFStripperByAreaRegionExtractionException("Failed to extract regions", e.getCause());
                        }

                        String highlightedText = stripper.getTextForRegion("annotatedRegion");


                        if (j > 1) {
                            str = str.concat(highlightedText);
                        } else {
                            str = highlightedText;
                        }
                    }
                    annotationTexts.add(
                            str
                                    .replaceAll("[\\n\\t]", " ")
                                    .replaceAll("[.!?,\\--;]", "")
                                    .trim()
                    );
                }
            }
        } catch (PDFTextStripperByAreaCreationException | PDFStripperByAreaRegionExtractionException e) {
            throw new GetWordsForAnnotationsException("Failed to get words for annotations", e.getCause());
        }

        return annotationTexts;
    }
}

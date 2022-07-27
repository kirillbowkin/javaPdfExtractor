package org.example;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.example.exceptions.PdfLoadException;
import org.example.exceptions.WordsExtractionException;


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

    /**
     * returns list of highlighted words for all pages of pdf document
     * @return list of highlighted words
     * @throws WordsExtractionException
     */
    public List<String> getHighlightedWords() throws WordsExtractionException {
        List<String> highlightedTexts = new ArrayList<>();

        int pageNum = 0;
        //TODO: consider changing foreach to streams
        for (PDPage pdfpage : this.pdfDocument.getPages()) {
            pageNum++;
            List<PDAnnotation> annotations = null;
            try {
                annotations = pdfpage.getAnnotations();
            } catch (IOException e) {
                throw new WordsExtractionException("Failed to extract words from pdf", e.getCause());
            }

            //TODO: extract loop below to method: getHighlightedWordsPerPage or sth like that
            //first setup text extraction regions
            for (int i = 0; i < annotations.size(); i++) {
                PDAnnotation annot = annotations.get(i);
                var annotNote = annot.getContents(); // Conteudo anotado na nota
                var annotSubType = annot.getSubtype(); // Tipo da nota (Highlight, Text)
                // annotTitle = annot.getTitlePopup(); // Autor da nota
                if (annotSubType.equals("Highlight")) {
                    // extract highlighted text
                    PDFTextStripperByArea stripper = null;
                    try {
                        stripper = new PDFTextStripperByArea();
                    } catch (IOException e) {
                        //TODO: consider creating specific exception sth like PdfTextStripperByAreaCreationException and throw it instead of WordsExtractionException
                        // and then use it as cause in one global try catch
                        throw new WordsExtractionException("Failed to extract words from pdf", e.getCause());
                    }
                    COSArray quadsArray = (COSArray) annot.getCOSObject().getCOSArray(COSName.getPDFName("QuadPoints"));
                    String str = null;
                    for (int j = 1, k = 0; j <= (quadsArray.size() / 8); j++) {
                        //TODO: don't like how this indexes look like, it would be better to get rid of these vague indexes at all if possible
                        Float ULX = ((COSInteger) quadsArray.get(0 + k)).floatValue();
                        Float ULY = ((COSInteger) quadsArray.get(1 + k)).floatValue();
                        Float URX = ((COSInteger) quadsArray.get(2 + k)).floatValue();
                        Float URY = ((COSInteger) quadsArray.get(3 + k)).floatValue();
                        Float LLX = ((COSInteger) quadsArray.get(4 + k)).floatValue();
                        Float LLY = ((COSInteger) quadsArray.get(5 + k)).floatValue();
                        Float LRX = ((COSInteger) quadsArray.get(6 + k)).floatValue();
                        Float LRY = ((COSInteger) quadsArray.get(7 + k)).floatValue();
                        k += 8;
                        float ulx = ULX - 1; // upper left x.
                        float uly = ULY; // upper left y.
                        float width = URX - LLX;          // calculated by upperRightX - lowerLeftX.
                        float height = URY - LLY;         // calculated by upperRightY - lowerLeftY.

                        PDRectangle pageSize = pdfpage.getMediaBox();
                        uly = pageSize.getHeight() - uly;

                        Rectangle2D.Float rectangle_2 = new Rectangle2D.Float(ulx, uly, width, height);
                        stripper.addRegion("highlightedRegion", rectangle_2);
                        try {
                            stripper.extractRegions(pdfpage);
                        } catch (IOException e) {
                            throw new WordsExtractionException("Failed to extract words from pdf", e.getCause());
                        }
                        String highlightedText = stripper.getTextForRegion("highlightedRegion")
                                .replaceAll("[\\n\\t ]", " ")
                                .replaceAll("[.!?,\\--]", "")
                                .trim();

                        if (j > 1) {
                            str = str.concat(highlightedText);
                        } else {
                            str = highlightedText;
                        }
                    }
                    highlightedTexts.add(str);
                }
            }

        }
        return highlightedTexts;
    }
}

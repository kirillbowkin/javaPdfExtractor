package org.example;

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationTextMarkup;

public enum PdfAnnotationType {
    HIGHLIGHTED(PDAnnotationTextMarkup.SUB_TYPE_HIGHLIGHT),
    UNDERLINED(PDAnnotationTextMarkup.SUB_TYPE_UNDERLINE),
    SQUIGGLY(PDAnnotationTextMarkup.SUB_TYPE_SQUIGGLY),
    STRIKEOUT(PDAnnotationTextMarkup.SUB_TYPE_STRIKEOUT);

    private final String type;

    PdfAnnotationType(String type) {
        this.type = type;
    }
}

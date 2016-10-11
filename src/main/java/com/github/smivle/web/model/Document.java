package com.github.smivle.web.model;

/**
 *
 * @author pc
 */
public class Document {
    private long docHash;
    private String text;

    public long getDocHash() {
        return docHash;
    }

    public String getText() {
        return text;
    }

    public void setDocHash(long docHash) {
        this.docHash = docHash;
    }

    public void setText(String text) {
        this.text = text;
    }
}

package com.github.smivle.web.model;

/**
 *
 * @author pc
 */
public class TagSearchResult {
    private int index;
    private String text;
    private double score;
    private long docHash;

    public long getDocHash() {
        return docHash;
    }

    public double getScore() {
        return score;
    }

    public String getText() {
        return text;
    }

    public void setDocHash(long docHash) {
        this.docHash = docHash;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

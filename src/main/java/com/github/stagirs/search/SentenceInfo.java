/*
 * Copyright 2016 Dmitriy Malakhov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.stagirs.search;

import com.github.stagirs.common.model.Document;
import com.github.stagirs.common.model.DocumentSerializer;
import com.github.stagirs.common.model.Sentence;


/**
 *
 * @author Dmitriy Malakhov
 */
public class SentenceInfo implements Comparable<SentenceInfo>{
    private Document document;
    private Sentence sentence;
    private double semantic;

    public SentenceInfo(Document document, Sentence sentence, double semantic) {
        this.document = document;
        this.sentence = sentence;
        this.semantic = semantic;
    }

    public Document getDocument() {
        return document;
    }

    public Sentence getSentence() {
        return sentence;
    }
    
    
    public double getSemantic() {
        return semantic;
    }

    @Override
    public int compareTo(SentenceInfo o) {
        return (int) (o.semantic * 1000 - semantic * 1000);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        DocumentSerializer.serialize(sb, sentence);
        return sb.toString();
    }

    
}

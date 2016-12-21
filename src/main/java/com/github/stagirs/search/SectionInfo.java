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
import com.github.stagirs.common.model.Section;

/**
 *
 * @author Dmitriy Malakhov
 */
public class SectionInfo implements Comparable<SectionInfo>{
    private Document document;
    private Section section;
    private double semantic;

    public SectionInfo(Document document, Section section, double semantic) {
        this.document = document;
        this.semantic = semantic;
        this.section = section;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
    
    public double getSemantic() {
        return semantic;
    }

    public Section getSection() {
        return section;
    }
    
    
    
    @Override
    public int compareTo(SectionInfo o) {
        return (int) (o.semantic * 1000 - semantic * 1000);
    }
}

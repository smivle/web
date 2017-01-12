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

import com.github.stagirs.common.model.Block;
import com.github.stagirs.common.model.Document;
import com.github.stagirs.common.model.Point;
import com.github.stagirs.common.model.Section;
import com.github.stagirs.common.model.Sentence;
import com.github.stagirs.common.model.Text;
import com.github.stagirs.common.text.TextUtils;
import com.github.stagirs.lingvo.morpho.MorphoDictionary;
import com.github.stagirs.lingvo.morpho.model.NormMorpho;
import com.github.stagirs.lingvo.morpho.model.RawMorpho;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Dmitriy Malakhov
 */
public class Executor {
    private TreeMap<String, int[]> term2tagIds = new TreeMap<>();
    private List<Object> tagId2object = new ArrayList<>();
    private Map<String, Document> id2doc = new HashMap<String, Document>();
    private MorphoDictionary md;
    
    public Executor(List<Document> docs, MorphoDictionary md){
        this.md = md;
        for (Document doc : docs) {
            index(doc);
            id2doc.put(doc.getId(), doc);
        }
    }
    
    private void index(String text){
        if(text == null){
            return;
        }
        for(String word : TextUtils.splitWords(text, true)){
            List<NormMorpho> list = md.getNormForm(word);
            if(list.isEmpty()){
                if(!term2tagIds.containsKey(word)){
                    term2tagIds.put(word, new int[]{tagId2object.size()});
                }else{
                    int[] old = term2tagIds.get(word);
                    if(old[old.length - 1] == tagId2object.size()){
                        continue;
                    }
                    int[] n = new int[old.length + 1];
                    System.arraycopy(old, 0, n, 0, old.length);
                    n[old.length] = tagId2object.size();
                    term2tagIds.put(word, n);
                }
                continue;
            }
            for(NormMorpho nf : list){
                word = nf.getWord();
                if(!term2tagIds.containsKey(word)){
                    term2tagIds.put(word, new int[]{tagId2object.size()});
                }else{
                    int[] old = term2tagIds.get(word);
                    if(old[old.length - 1] == tagId2object.size()){
                        continue;
                    }
                    int[] n = new int[old.length + 1];
                    System.arraycopy(old, 0, n, 0, old.length);
                    n[old.length] = tagId2object.size();
                    term2tagIds.put(word, n);
                }
            }
        }
    }
    
    private void index(Document document){
        index(document.getTitle());
        tagId2object.add(document);
        for (Block block : document.getBlocks()) {
            if(block instanceof Section){
                index((Section) block);
            }
            if(block instanceof Point){
                index((Point) block);
            }
        }
    }
    
    private void index(Section section){
        index(section.getTitle());
        tagId2object.add(section);
        for (Block block : section.getBlocks()) {
            if(block instanceof Section){
                index((Section) block);
            }
            if(block instanceof Point){
                index((Point) block);
            }
        }
    }
    
    private void index(Point point){
        for (Sentence sentence : point.getSentences()) {
            for (Text text : sentence.getParts()) {
                if(text.getClassName() != null){
                    continue;
                }
                index(text.getText());
            }
            tagId2object.add(sentence);
        }
    }
    
    
    
    public Document getDocument(String id){
        return id2doc.get(id);
    }
    
    public Set<String> getKeywords(Query query){
        Set<String> set = new HashSet<>();
        for (String[] strings : query.getTerms()) {
            for (String s : strings) {
                List<NormMorpho> list = md.getNormForm(s);
                if(list.isEmpty()){
                    set.add(s);
                    continue;
                }
                for(NormMorpho nf : list){
                    for (RawMorpho raw : nf.getRawForms()) {
                        set.add(raw.getWord());
                    }
                } 
            }
        }
        return set;
    }
    
    private void query2norm(Query query){
        for (int i = 0; i < query.getTerms().length; i++) {
            Set<String> set = new HashSet<>();
            for (String word : query.getTerms()[i]) {
                List<NormMorpho> list = md.getNormForm(word);
                if(list.isEmpty()){
                    set.add(word);
                    continue;
                }
                for(NormMorpho nf : list){
                    set.add(nf.getWord());
                }    
            }
            query.getTerms()[i] = set.toArray(new String[set.size()]);
        }
    }
    
    public Set<Integer> sentencesForDoc(String docId, Query query){
        Set<Integer> set = new HashSet<>();
        query2norm(query);
        TIntArrayList result = execute(query);
        for (int id : result.toArray()) {
            Object object = tagId2object.get(id);
            if(object instanceof Sentence){
                fillSentences(docId, (Sentence) object, set);
            }
        }
        return set;
    }
    
    private void fillSentences(String docId, Sentence sentence, Set<Integer> set){
        if(!sentence.getDocId().equals(docId)){
            return;
        }
        set.add(sentence.getNumber());
    }
    
    public List<SentenceInfo> sentences(Query query){
        query2norm(query);
        TIntArrayList result = execute(query);
        List<SentenceInfo> list = new ArrayList<>();
        for (int id : result.toArray()) {
            Object object = tagId2object.get(id);
            if(object instanceof Sentence){
                processSentences((Sentence) object, list);
            }
            if(object instanceof Section){
                processSentences((Section) object, list);
            }
            if(object instanceof Document){
                processSentences((Document) object, list);
            }
        }
        Collections.sort(list);
        return list.size() < 1000 ? list : list.subList(0, 1000);
    }
    
    private void processSentences(Sentence sentence, List<SentenceInfo> list){
        list.add(new SentenceInfo(id2doc.get(sentence.getDocId()), sentence, sentence.getSemantic()));
    }
    
    private void processSentences(Section section, List list){
        list.add(new SectionInfo(id2doc.get(section.getDocId()), section, section.getTitleSemantic()));
    }
    
    private void processSentences(Document doc, List list){
        list.add(new DocumentInfo(doc, doc.getTitleSemantic()));
    }
    
    
    public List<DocumentInfo> docs(Query query){
        query2norm(query);
        TIntArrayList result = execute(query);
        TObjectDoubleHashMap<Document> map = new TObjectDoubleHashMap<>();
        for (int id : result.toArray()) {
            Object object = tagId2object.get(id);
            if(object instanceof Sentence){
                processDocs((Sentence) object, map);
            }
            if(object instanceof Section){
                processDocs((Section) object, map);
            }
            if(object instanceof Document){
                processDocs((Document) object, map);
            }
        }
        List<DocumentInfo> list = new ArrayList<>();
        map.forEachEntry((k, v) -> {
            list.add(new DocumentInfo(k, v));
            return true;
        });
        Collections.sort(list);
        return list.size() < 1000 ? list : list.subList(0, 1000);
    }
    
    private void processDocs(Sentence sentence, TObjectDoubleHashMap<Document> map){
        map.adjustOrPutValue(id2doc.get(sentence.getDocId()), sentence.getSemantic(), sentence.getSemantic());
    }
    
    private void processDocs(Section section, TObjectDoubleHashMap<Document> map){
        map.adjustOrPutValue(id2doc.get(section.getDocId()), section.getTitleSemantic(), section.getTitleSemantic());
    }
    
    private void processDocs(Document doc, TObjectDoubleHashMap<Document> map){
        map.adjustOrPutValue(id2doc.get(doc.getId()), doc.getTitleSemantic(), doc.getTitleSemantic());
    }
    
    private TIntArrayList execute(Query query){
        TIntArrayList result = new TIntArrayList();
        int[][][] index = getSubIndex(query);
        int[][] counter = fillCounter(index, new int[index.length][]);
        int[] current = fillCurrent(index, counter, new int[index.length]);
        int max = max(current);
        while(max != Integer.MAX_VALUE){
            if(nextAnd(index, counter, current, max)){
                result.add(max);
                fillCurrent(index, counter, current);
            }
            max = max(current);
        }
        return result;
    }
    
    private int[][][] getSubIndex(Query query){
        int[][][] index = new int[query.getTerms().length][][];
        for (int i = 0; i < query.getTerms().length; i++) {
            index[i] = new int[query.getTerms()[i].length][];
            for (int j = 0; j < index[i].length; j++) {
                index[i][j] = term2tagIds.get(query.getTerms()[i][j]);
            }
        }
        return index;
    }
    
    private int[][] fillCounter(int[][][] index, int[][] counter){
        for (int i = 0; i < counter.length; i++) {
            counter[i] = new int[index[i].length];
        }
        return counter;
    }
    
    private int[] fillCurrent(int[][][] index, int[][] counter, int[] current){
        for (int i = 0; i < current.length; i++) {
            current[i] = nextOr(index[i], counter[i]);
        }
        return current;
    }
    
    private boolean nextAnd(int[][][] index, int[][] counter, int[] current, int max){
        for (int i = 0; i < current.length; i++) {
            while(current[i] < max){
                current[i] = nextOr(index[i], counter[i]);
            }
            if(current[i] > max){
                return false;
            }
        }
        return true;
    }
    
    private int max(int[] current){
        int max = Integer.MIN_VALUE;
        for (int i : current) {
            max = Math.max(max, i);
        }
        return max;
    }
    
    
    
    private int nextOr(int[][] index, int[] counter){
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < index.length; i++) {
            if(index[i] == null){
                continue;
            }
            min = Math.min(index[i][counter[i]], min);
        }
        for (int i = 0; i < index.length; i++) {
            if(index[i] == null){
                continue;
            }
            if(index[i][counter[i]] == min){
                counter[i]++;
                if(index[i].length <= counter[i]){
                    index[i] = null;
                }
            }
        }
        return min;
    }
    
}

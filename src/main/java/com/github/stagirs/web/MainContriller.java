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
package com.github.stagirs.web;

import com.github.stagirs.common.model.Document;
import com.github.stagirs.common.model.DocumentParser;
import com.github.stagirs.lingvo.morpho.MorphoDictionary;
import com.github.stagirs.lingvo.morpho.MorphoDictionaryFactory;
import com.github.stagirs.search.Executor;
import com.github.stagirs.search.Query;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Dmitriy Malakhov
 */
@Controller
public class MainContriller {
    private Executor executor;
    
    @PostConstruct
    public void init(){
        try{
            MorphoDictionary md = MorphoDictionaryFactory.get(new File("../work/stagirs/dict.opcorpora.xml"));
            List<Document> documents = new ArrayList<>();
            for(File file : new File("../work/stagirs/docs/processed").listFiles()){
                documents.add(DocumentParser.parse(file));
            }
            executor = new Executor(documents, md);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    @RequestMapping("/searchTags")
    public String searchTags(Model model, @RequestParam(value="query", required=false, defaultValue="") String query) {
        model.addAttribute("query", query);
        model.addAttribute("tags", query.isEmpty() ? null: executor.sentences(new Query(query)));
        return "search";
    }
    
    @RequestMapping("/searchDocs")
    public String searchDocs(Model model, @RequestParam(value="query", required=false, defaultValue="") String query) {
        model.addAttribute("query", query);
        model.addAttribute("tags", query.isEmpty() ? null: executor.docs(new Query(query)));
        return "search";
    }
    
    @RequestMapping("/doc")
    public String doc(Model model, 
            @RequestParam(value="docId", defaultValue="") String docId,
            @RequestParam(value="query", defaultValue="") String query) {
        model.addAttribute("query", query);
        model.addAttribute("sentenceIds", query.isEmpty() ? null: executor.sentencesForDoc(docId, new Query(query)));
        model.addAttribute("doc", executor.getDocument(docId));
        return "doc";
    }
}

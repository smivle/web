package com.github.smivle.web.controller;

//import com.github.smivle.backend.processing.model.Dictionary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.github.smivle.web.dao.MainDao;
import com.github.smivle.web.model.TagSearchResult;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * 
 * @author pc
 */
@Controller
public class MainController {
    @Autowired
    private MainDao mainDao;
    
//    @Autowired
//    private Dictionary dictionary;
//    
    @RequestMapping("/")
    public String main(){
        return "redirect:search";
    }
    
    @RequestMapping("/search")
    public String search(Model model, @RequestParam(value="query", required=false, defaultValue="") String query) {
        model.addAttribute("query", query);
        List<TagSearchResult> list = query.isEmpty() ? mainDao.search() : mainDao.search(/*dictionary.toTSQuery(*/query/*)*/);
        for (TagSearchResult tagSearchResult : list) {
            String text = tagSearchResult.getText();
//            for(String word : dictionary.getRawKeyword(query, tagSearchResult.getText())){
//                text = text.replace( word, "<b>" + word + "</b>");
//            }
            tagSearchResult.setText(text);
        }
        model.addAttribute("tags", list);
        return "search";
    }
    
    @RequestMapping("/doc")
    public String doc(Model model, @RequestParam(value="id", required=true) long id, @RequestParam(value="query", required=false, defaultValue="") String query) {
        model.addAttribute("id", id);
        model.addAttribute("query", query);
        List<TagSearchResult> list = query.isEmpty() ? mainDao.docSearch(id) : mainDao.docSearch(id, /*dictionary.toTSQuery(*/query/*)*/);
        Collections.sort(list, new Comparator<TagSearchResult>() {
            @Override
            public int compare(TagSearchResult o1, TagSearchResult o2) {
                return new Integer(o1.getIndex()).compareTo(o2.getIndex());
            }
        });
        for (TagSearchResult tagSearchResult : list) {
            String text = tagSearchResult.getText();
//            for(String word : dictionary.getRawKeyword(query, tagSearchResult.getText())){
//                text = text.replace( word, "<b>" + word + "</b>");
//            }
            tagSearchResult.setText(text);
        }
        model.addAttribute("tags", list);
        model.addAttribute("info", mainDao.docInfo(id).get(0));
        model.addAttribute("clusters", mainDao.getClusters(id));
        return "doc";
    }
    
    @RequestMapping("/cluster")
    public String cluster(Model model, @RequestParam(value="id", required=true) long id) {
        model.addAttribute("docs", mainDao.getDocuments(id));
        model.addAttribute("cluster", mainDao.getCluster(id));
        return "cluster";
    }
}

package com.github.smivle.web.filling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author pc
 */
public class Table {

    final String name;
    final Map<String, String> fields;
    final Map<String, String> indexes;

    public Table(String name, TreeMap<String, String> fields, Map<String, String> indexes) {
        this.name = name;
        this.fields = fields;
        this.indexes = indexes;
    }
            
    public String create(){
        List<String> flist = new ArrayList<>();
        for (Entry<String, String> e : fields.entrySet()) {
            flist.add(e.getKey() + " " + e.getValue());
        }
        return "CREATE TABLE "+name+" ("+StringUtils.join(flist, ",")+");";
    }
    
    public String drop(){
        return "DROP TABLE IF EXISTS "+name;
    }
    
    public String copy(){
        return "COPY "+name+"("+StringUtils.join(fields.keySet(), ",")+") from stdin with delimiter '\t'";
    }
    
    public List<String> indexes(){
        List<String> indexesStr = new ArrayList<>();
        for (Entry<String, String> e : indexes.entrySet()) {
            indexesStr.add("CREATE INDEX ii_"+name+"_"+e.getKey()+" ON "+name+" USING "+e.getValue()+" ("+e.getKey()+")");
        }
        return indexesStr;
    }
    
    public static Table getTagTable(){
        return new Table("tag", new TreeMap<String, String>(){{
            put("text", "text");
            put("text_index", "tsvector");
            put("tag_hash", "bigint");
        }}, new HashMap<String, String>(){{
            put("text_index", "gin");
        }});
    }
    
    public static Table getClusterTable(){
        return new Table("cluster", new TreeMap<String, String>(){{
            put("cluster_hash", "bigint");
            put("cluster_path", "text");
        }}, new HashMap<>());
    }
    
    public static Table getClusterMappingTable(){
        return new Table("cluster_mapping", new TreeMap<String, String>(){{
            put("cluster_hash", "bigint");
            put("doc_hash", "bigint");
        }}, new HashMap<>());
    }
    
    public static Table getTagMappingTable(){
        return new Table("tag_mapping", new TreeMap<String, String>(){{
            put("tag_hash", "bigint");
            put("doc_hash", "bigint");
            put("index", "integer");
            put("relevance", "double precision");
        }}, new HashMap<>());
    }
    
    public static Table getDocTable(){
        return new Table("doc", new TreeMap<String, String>(){{
            put("doc_hash", "bigint");
            put("path", "text");
            put("info", "text");
            put("info_index", "tsvector");
        }}, new HashMap<String, String>(){{
            put("info_index", "gin");
        }});
    }
    
    public static Table getKeywordClusterTable(){
        return new Table("keyword_cluster", new TreeMap<String, String>(){{
            put("cluster_hash", "bigint");
            put("word", "text");
            put("doc_with_word_in_cluster", "integer");
            put("doc_in_cluster", "integer");
            put("doc_with_word_all", "integer");
            put("doc_all", "integer");
        }}, new HashMap<>());
    }
    
    public static Table getKeywordDocTable(){
        return new Table("keyword_doc", new TreeMap<String, String>(){{
            put("doc_hash", "bigint");
            put("word", "text");
            put("doc_with_word_in_cluster", "integer");
            put("doc_in_cluster", "integer");
            put("doc_with_word_all", "integer");
            put("doc_all", "integer");
        }}, new HashMap<>());
    }
}

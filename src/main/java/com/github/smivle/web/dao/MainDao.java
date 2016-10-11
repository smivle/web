package com.github.smivle.web.dao;

import com.github.smivle.web.model.Cluster;
import com.github.smivle.web.model.Document;
import com.github.smivle.web.model.TagSearchResult;
import java.sql.ResultSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author pc
 */
@Component
public class MainDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public List<String> docInfo(long docHash){
        return jdbcTemplate.query("select info from doc where doc_hash=" + docHash, 
            (ResultSet rs, int i) -> {
                return rs.getString("info");
            }
        );
    }
    
    public List<TagSearchResult> search(String query){
        return jdbcTemplate.query(
            "select text, tm.doc_hash as docHash, ts_rank_cd(text_index, '"+query+"', 1) * relevance AS score " +
            "from tag t join tag_mapping tm on t.tag_hash = tm.tag_hash " +
            "where text_index @@ '"+query+"' order by score desc limit 100", 
            (ResultSet rs, int i) -> {
                TagSearchResult tsr = new TagSearchResult();
                tsr.setText(rs.getString("text"));
                tsr.setScore(rs.getDouble("score"));
                tsr.setDocHash(rs.getLong("docHash"));
                return tsr;
            }
        );
    }
    
    public List<TagSearchResult> search(){
        return jdbcTemplate.query(
            "select text, tm.doc_hash as docHash, relevance AS score " +
            "from tag t join tag_mapping tm on t.tag_hash = tm.tag_hash " +
            " order by score desc limit 100", 
            (ResultSet rs, int i) -> {
                TagSearchResult tsr = new TagSearchResult();
                tsr.setText(rs.getString("text"));
                tsr.setScore(rs.getDouble("score"));
                tsr.setDocHash(rs.getLong("docHash"));
                return tsr;
            }
        );
    }
    
    public List<TagSearchResult> docSearch(long docHash, String query){
        return jdbcTemplate.query(
            "select index, text, tm.doc_hash as docHash, ts_rank_cd(text_index, '"+query+"', 1) * relevance AS score " +
            "from tag t join tag_mapping tm on t.tag_hash = tm.tag_hash and tm.doc_hash=" + docHash +
            "where text_index @@ '"+query+"' order by score desc limit 10", 
            (ResultSet rs, int i) -> {
                TagSearchResult tsr = new TagSearchResult();
                tsr.setText(rs.getString("text"));
                tsr.setScore(rs.getDouble("score"));
                tsr.setDocHash(rs.getLong("docHash"));
                tsr.setIndex(rs.getInt("index"));
                return tsr;
            }
        );
    }
    
    public List<TagSearchResult> docSearch(long docHash){
        return jdbcTemplate.query(
            "select index, text, tm.doc_hash as docHash, relevance AS score " +
            "from tag t join tag_mapping tm on t.tag_hash = tm.tag_hash and tm.doc_hash=" + docHash +
            " order by score desc limit 10", 
            (ResultSet rs, int i) -> {
                TagSearchResult tsr = new TagSearchResult();
                tsr.setText(rs.getString("text"));
                tsr.setScore(rs.getDouble("score"));
                tsr.setDocHash(rs.getLong("docHash"));
                tsr.setIndex(rs.getInt("index"));
                return tsr;
            }
        );
    }
    
    public List<Cluster> getClusters(long docHash){
        return jdbcTemplate.query(
            "with t as (select c.cluster_hash from cluster c join cluster_mapping cm on c.cluster_hash = cm.cluster_hash where doc_hash = "+docHash+")\n" +
            "select t.cluster_hash, count(*) as val from t join cluster_mapping cm on t.cluster_hash = cm.cluster_hash group by t.cluster_hash having count(*) < 200 order by val desc ", 
            (ResultSet rs, int i) -> {
                Cluster tsr = new Cluster();
                tsr.setClusterHash(rs.getLong("cluster_hash"));
                tsr.setCount(rs.getInt("val"));
                return tsr;
            }
        );
    }
    
    public Cluster getCluster(long clusterHash){
        return jdbcTemplate.query(
            "select cluster_hash, count(*) as val from cluster_mapping where cluster_hash = " + clusterHash + " group by cluster_hash", 
            (ResultSet rs, int i) -> {
                Cluster tsr = new Cluster();
                tsr.setClusterHash(rs.getLong("cluster_hash"));
                tsr.setCount(rs.getInt("val"));
                return tsr;
            }
        ).get(0);
    }
    
    public List<Document> getDocuments(long clusterHash){
        return jdbcTemplate.query(
            "select d.doc_hash, d.info from doc d join cluster_mapping cm on d.doc_hash = cm.doc_hash and cm.cluster_hash = " + clusterHash, 
            (ResultSet rs, int i) -> {
                Document tsr = new Document();
                tsr.setDocHash(rs.getLong("doc_hash"));
                tsr.setText(rs.getString("info"));
                return tsr;
            }
        );
    }
}

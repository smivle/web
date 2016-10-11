/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.smivle.web.filling;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

/**
 *
 * @author pc
 */
public class Uploader {
    private String url;
    private String username;
    private String password;
    
    public void process(Table table, File file) throws SQLException, UnsupportedEncodingException, IOException{
        try(Connection con = DriverManager.getConnection(url, username, password)) {
            execute(con, table.drop());
            execute(con, table.create());
            CopyIn copyIn = new CopyManager((BaseConnection) con).copyIn(table.copy());
            try{
                for(String row : FileUtils.readLines(file, "utf-8")){
                    row = row.replaceAll("[^a-zA-Zа-яА-Я0-9<>\\:'\\-,\\.\\;\t]", " ");
                    byte[] insert_values=(row + "\n").getBytes("utf-8");
                    try{
                        copyIn.writeToCopy(insert_values, 0, insert_values.length);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }finally{
                if(copyIn.isActive()){
                    copyIn.endCopy();
                }
            }
            for (String i : table.indexes()) {
                execute(con, i);
            }
        }
    }
    
    private void execute(Connection c, String sql) throws SQLException{
        try(Statement st = c.createStatement()) {
            st.execute(sql);
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Burhan
 */
public class Database {

    private static PreparedStatement st;
    private static Statement stm;
    private static ResultSet rs;

    public static void addArticle(Connection c, String articleName, String articleLink,
            String desc, String citingLink, int authorID, int citingID) throws SQLException {

        String sql = "INSERT INTO ARTICLE (article_id,article_name,article_link,"
                + "article_desc,article_citing_link,author_id,article_citing_id)"
                + "VALUES (?,?,?,?,?,?,?)";
        st = c.prepareStatement(sql);
        stm = c.createStatement();
        String sql2 = "SELECT * FROM ARTICLE";
        rs = stm.executeQuery(sql2);
        int iter = 0;
        while (rs.next()) {
            iter = rs.getInt("article_id");
        }
        iter++;
        st.setInt(1, iter);
        st.setString(2, articleName);
        st.setString(3, articleLink);
        st.setString(4, desc);
        st.setString(5, citingLink);
        st.setInt(6, authorID);
        st.setInt(7, citingID);
        st.executeUpdate();

    }

    public static void addAuthor(Connection c, String authorName, String authorLink) throws SQLException {

        String sql = "INSERT INTO AUTHOR (author_id,author_name,author_link,)"
                + "VALUES (?,?,?)";
        st = c.prepareStatement(sql);
        stm = c.createStatement();
        String sql2 = "SELECT * FROM author";
        rs = stm.executeQuery(sql2);
        int iter = 0;
        while (rs.next()) {
            iter = rs.getInt("author_id");
        }
        iter++;
        st.setInt(1, iter);
        st.setString(2, authorName);
        st.setString(3, authorLink);
        st.executeUpdate();

    }

    public static int getAuthor(Connection c, String authorLink) throws SQLException {

        String sql = "SELECT author_id FROM AUTHOR WHERE author_link = ?";
        st = c.prepareStatement(sql);

        st.setString(1, authorLink);
        rs = st.executeQuery();

        if(rs.next()){
            

        return rs.getInt("author_id");
        }
        
        return 0;

    }

    public static PreparedStatement getSt() {
        return st;
    }

    public static void setSt(PreparedStatement st) {
        Database.st = st;
    }

    public static Statement getStm() {
        return stm;
    }

    public static void setStm(Statement stm) {
        Database.stm = stm;
    }

    public static ResultSet getRs() {
        return rs;
    }

    public static void setRs(ResultSet rs) {
        Database.rs = rs;
    }

}

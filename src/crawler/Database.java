/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Burhan
 */
public class Database {

    private static PreparedStatement st;
    private static Statement stm;
    private static ResultSet rs;
    private static DateFormat dateFormat;
    private static Date date;
    private static String ip;

    public static void addArticle(Connection c, String articleName, String articleLink,
            String desc, String citingLink, int authorID, String articlePath, int queryID, int citingNumber) throws SQLException {

        String sql = "INSERT INTO ARTICLE (article_id,article_name,article_link,"
                + "article_desc,article_citing_link,author_id,article_path,article_query,article_citing_number)"
                + "VALUES (?,?,?,?,?,?,?,?,?)";
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
        st.setString(7, articlePath + iter + "/");
        st.setInt(8, queryID);
        st.setInt(9, citingNumber);
        st.executeUpdate();

    }

    public static void addArticle(Connection c, String articleName, String articleLink,
            String desc, String citingLink, int authorID, int citingNumber, int queryID) throws SQLException {

        String sql = "INSERT INTO ARTICLE (article_id,article_name,article_link,"
                + "article_desc,article_citing_link,author_id,article_path,article_citing_number,article_query)"
                + "VALUES (?,?,?,?,?,?,?,?,?)";
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
        st.setString(7, iter + "/");
        st.setInt(8, citingNumber);
        st.setInt(9, queryID);
        st.executeUpdate();

    }

    public static void addAuthor(Connection c, String authorName, String authorLink) throws SQLException {

        String sql = "INSERT INTO AUTHOR (author_id,author_name,author_link)"
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

    public static int addQuery(Connection c, String query) throws SQLException, MalformedURLException, IOException {

        String sql = "INSERT INTO QUERY (query_id,query_date,query_query,query_ip)"
                + "VALUES (?,?,?,?)";
        st = c.prepareStatement(sql);
        stm = c.createStatement();
        String sql2 = "SELECT * FROM query";
        rs = stm.executeQuery(sql2);
        int iter = 0;
        while (rs.next()) {
            iter = rs.getInt("query_id");
        }
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();

        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        ip = in.readLine(); //you get the IP as a String

        iter++;

        st.setInt(1, iter);
        st.setString(2, dateFormat.format(date));
        st.setString(3, query);
        st.setString(4, ip);
        st.executeUpdate();
        return iter;
    }

    public static int getAuthor(Connection c, String authorLink) throws SQLException {

        String sql = "SELECT author_id FROM AUTHOR WHERE author_link = ?";
        st = c.prepareStatement(sql);

        st.setString(1, authorLink);
        rs = st.executeQuery();

        if (rs.next()) {

            return rs.getInt("author_id");
        }

        return 0;

    }

    public static ArrayList<Integer> getArticleIDs(Connection c, int queryID) throws SQLException {
        ArrayList<Integer> returned = new ArrayList();
        String sql = "SELECT article_id FROM ARTICLE WHERE article_query = ?";
        st = c.prepareStatement(sql);

        st.setInt(1, queryID);
        rs = st.executeQuery();

        while (rs.next()) {

            returned.add(rs.getInt("article_id"));
        }

        return returned;

    }

    public static String getArticleCitingLink(Connection c, int articleID) throws SQLException {

        String sql = "SELECT article_citing_link FROM ARTICLE WHERE article_id = ?";
        st = c.prepareStatement(sql);

        st.setInt(1, articleID);
        rs = st.executeQuery();

        if (rs.next()) {

            return rs.getString("article_citing_link");
        }

        return "";

    }

    public static String getArticlePath(Connection c, int articleID) throws SQLException {

        String sql = "SELECT article_path FROM ARTICLE WHERE article_id = ?";
        st = c.prepareStatement(sql);

        st.setInt(1, articleID);
        rs = st.executeQuery();

        if (rs.next()) {

            return rs.getString("article_path");
        }

        return "";

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

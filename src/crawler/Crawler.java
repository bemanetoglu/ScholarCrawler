/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

/**
 *
 * @author Burhan
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

    private static ArrayList<Article> articleList = new ArrayList();
    private static ArrayList<Author> authorList = new ArrayList();
    private static List<Node<Article>> childrenList = new ArrayList();
    private static Node<Article> parentNode = null;
    private static Node<Article> childNode = null;
    private static ArrayList<Node> parents = new ArrayList();
    private static Article tempArticle = null;
    private static Author tempAuthor = null;
    private static String query = null;
    private static String http = "https://scholar.google.com/scholar?hl=tr&q=";
    private static int articleID = 0;
    private static int iter = 0;
    private static Document doc = null;
    private static Document childDoc = null;
    private static String authorName;
    private static String articleName;
    private static String articleLink;
    private static String articleDesc;
    private static String citingLink;
    private static int citingNumber;
    private static String authorLink;
    private static String articlePath = "";
    private static int emptycounter = 0;
    private static Elements authors, articles;
    private static Connection c1 = null;

    public static void openConnection() throws SQLException {
        c1 = (Connection) DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");
    }

    public static void crawl() throws UnsupportedEncodingException, IOException, SQLException {
        clearArticleList();
        clearAuthorList();
        http = "https://scholar.google.com/scholar?hl=tr&q=";
        //creating url
        query = URLEncoder.encode(query, StandardCharsets.UTF_8.name());
        if (!"".equals(query)) {
            http += query;
            System.out.println("article query: " + http);
        } else {
            articleID = Database.getArticleID(c1, articleName);
            http = Database.getArticleCitingLink(c1, articleID);
            articlePath = Database.getArticlePath(c1, articleID);
        }
        Document doc = Jsoup.connect((String) (http))
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36"
                        + " (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
        //parsing the result
        articles = doc.select("div[class=gs_r]");
        int i = 0;
        for (Element article : articles) {

            articleName = article.select("div[class=gs_ri]").select("h3[class=gs_rt]").text();
            articleLink = article.select("div[class=gs_ri]").select("h3[class=gs_rt]").select("a[href]").attr("href");
            articleDesc = article.select("div[class=gs_ri]").select("div[class=gs_rs]").text();
            citingLink = "https://scholar.google.com" + article.select("div[class=gs_ri]").select("div[class=gs_fl]").select("a[href]").first().attr("href");
            citingNumber = Integer.parseInt(article.select("div[class=gs_ri]").select("div[class=gs_fl]").select("a[href]").first().text().replaceAll("\\D+", ""));
            authors = article.select("div[class=gs_ri]").select(".gs_a>a");
            authorList = new ArrayList();
            tempAuthor = new Author();
            if (authors.isEmpty()) {
                emptycounter++;
                authorName = article.select("div[class=gs_ri]").select("div[class=gs_a]").text();
                tempAuthor.setName(authorName);
                tempAuthor.setLink("empty" + emptycounter);
                authorList.add(tempAuthor);
                Database.addAuthor(c1, tempAuthor.getName(), tempAuthor.getLink());
            } else {
                authors.stream().map((a) -> {
                    authorName = a.text();
                    authorLink = "https://scholar.google.com" + a.attr("href");
                    //tempAuthor = new Author(authorName, authorLink);
                    tempAuthor.setName(authorName);
                    tempAuthor.setLink(authorLink);
                    return a;
                }).forEach((_item) -> {
                    try {
                        authorList.add(tempAuthor);
                        Database.addAuthor(c1, tempAuthor.getName(), tempAuthor.getLink());
                    } catch (SQLException ex) {
                        Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
            tempArticle = new Article();
            tempArticle.setName(articleName);
            tempArticle.setAuthor(authorList);
            tempArticle.setLink(articleLink);
            tempArticle.setDescription(articleDesc);
            tempArticle.setCitingLink(citingLink);
            tempArticle.setCiting(citingNumber);
            tempArticle.isParent = true;
            parentNode = new Node<>(tempArticle);
            articleList.add(tempArticle);
            parents.add(parentNode);

            int authorID = Database.getAuthor(c1, authorList.get(0).getLink());
            if ("".equals(articlePath)) {
                Database.addArticle(c1, tempArticle.getName(), tempArticle.getLink(),
                        tempArticle.getDescription(), tempArticle.getCitingLink(), authorID);
            } else {
                Database.addArticle(c1, tempArticle.getName(), tempArticle.getLink(),
                        tempArticle.getDescription(), tempArticle.getCitingLink(), authorID,
                        articlePath);
            }

        }
        articleID = 0;
        articlePath = "";
    }

    public static ArrayList<Article> getArticleList() {
        return articleList;
    }

    public static void setArticleList(ArrayList<Article> articleList) {
        Crawler.articleList = articleList;
    }

    public static void clearArticleList() {
        articleList.clear();
    }

    public static ArrayList<Author> getAuthorList() {
        return authorList;
    }

    public static void setAuthorList(ArrayList<Author> authorList) {
        Crawler.authorList = authorList;
    }

    public static void clearAuthorList() {
        authorList.clear();
    }

    public static List<Node<Article>> getChildrenList() {
        return childrenList;
    }

    public static void setChildrenList(List<Node<Article>> childrenList) {
        Crawler.childrenList = childrenList;
    }

    public static Node<Article> getParentNode() {
        return parentNode;
    }

    public static void setParentNode(Node<Article> parentNode) {
        Crawler.parentNode = parentNode;
    }

    public static Node<Article> getChildNode() {
        return childNode;
    }

    public static void setChildNode(Node<Article> childNode) {
        Crawler.childNode = childNode;
    }

    public static ArrayList<Node> getParents() {
        return parents;
    }

    public static void setParents(ArrayList<Node> parents) {
        Crawler.parents = parents;
    }

    public static Article getTempArticle() {
        return tempArticle;
    }

    public static void setTempArticle(Article tempArticle) {
        Crawler.tempArticle = tempArticle;
    }

    public static Author getTempAuthor() {
        return tempAuthor;
    }

    public static void setTempAuthor(Author tempAuthor) {
        Crawler.tempAuthor = tempAuthor;
    }

    public static String getQuery() {
        return query;
    }

    public static void setQuery(String query) {
        Crawler.query = query;
    }

    public static String getHttp() {
        return http;
    }

    public static void setHttp(String http) {
        Crawler.http = http;
    }

    public static int getIter() {
        return iter;
    }

    public static void setIter(int iter) {
        Crawler.iter = iter;
    }

    public static Document getDoc() {
        return doc;
    }

    public static void setDoc(Document doc) {
        Crawler.doc = doc;
    }

    public static Document getChildDoc() {
        return childDoc;
    }

    public static void setChildDoc(Document childDoc) {
        Crawler.childDoc = childDoc;
    }

    public static String getAuthorName() {
        return authorName;
    }

    public static void setAuthorName(String authorName) {
        Crawler.authorName = authorName;
    }

    public static String getArticleName() {
        return articleName;
    }

    public static void setArticleName(String articleName) {
        Crawler.articleName = articleName;
    }

    public static String getArticleLink() {
        return articleLink;
    }

    public static void setArticleLink(String articleLink) {
        Crawler.articleLink = articleLink;
    }

    public static String getArticleDesc() {
        return articleDesc;
    }

    public static void setArticleDesc(String articleDesc) {
        Crawler.articleDesc = articleDesc;
    }

    public static String getCitingLink() {
        return citingLink;
    }

    public static void setCitingLink(String citingLink) {
        Crawler.citingLink = citingLink;
    }

    public static int getCitingNumber() {
        return citingNumber;
    }

    public static void setCitingNumber(int citingNumber) {
        Crawler.citingNumber = citingNumber;
    }

    public static String getAuthorLink() {
        return authorLink;
    }

    public static void setAuthorLink(String authorLink) {
        Crawler.authorLink = authorLink;
    }

    public static int getEmptycounter() {
        return emptycounter;
    }

    public static void setEmptycounter(int emptycounter) {
        Crawler.emptycounter = emptycounter;
    }

    public static Elements getAuthors() {
        return authors;
    }

    public static void setAuthors(Elements authors) {
        Crawler.authors = authors;
    }

    public static Elements getArticles() {
        return articles;
    }

    public static void setArticles(Elements articles) {
        Crawler.articles = articles;
    }

    public static Connection getC1() {
        return c1;
    }

    public static void setC1(Connection c1) {
        Crawler.c1 = c1;
    }
}

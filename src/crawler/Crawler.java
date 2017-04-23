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
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
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
    private static ArrayList<Integer> returnedIDs = new ArrayList();
    private static List<Node<Article>> childrenList = new ArrayList();
    private static Node<Article> parentNode = null;
    private static Node<Article> childNode = null;
    private static ArrayList<Node> parents = new ArrayList();
    private static Article tempArticle = null;
    private static Author tempAuthor = null;
    private static String query = null;
    private static String http = "";
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
    private static int queryID = 0;
    private static String authorLink;
    private static String articlePath = "";
    private static String didYouMean = "";
    private static Elements authors, articles;
    private static Connection c1 = null;
    private static String proxydizi[][] = {{"http", "84.39.41.57", "443"}, {"http", "89.188.124.66", "8081"}, {"http", "74.84.131.34", "80"}, {"https", "50.206.232.202", "3128"}, {"http", "177.42.129.227", "8080"}, {"https", "45.55.249.0", "3128"}, {"http", "123.116.192.16", "8998"}, {"http", "52.67.4.23", "80"}, {"http", "52.208.118.39", "3128"}, {"https", "94.177.207.48", "3128"}, {"http", "221.180.170.17", "8080"}, {"https", "180.183.202.240", "3128"}, {"http", "192.198.80.180", "80"}, {"http", "177.67.84.231", "3128"}, {"http", "39.184.189.117", "8998"}, {"http", "86.121.255.215", "3128"}, {"http", "167.114.82.173", "8799"}, {"http", "138.68.91.5", "3128"}, {"http", "35.154.198.19", "8080"}, {"https", "45.55.0.45", "3128"}, {"http", "52.9.175.167", "8888"}, {"http", "35.161.110.122", "3128"}, {"https", "120.198.243.46", "8080"}, {"https", "120.52.73.173", "8080"}, {"http", "177.54.144.218", "3128"}, {"http", "111.13.109.27", "80"}, {"http", "180.97.235.30", "80"}, {"http", "1.82.216.134", "80"}, {"https", "46.44.33.51", "8081"}, {"https", "89.40.125.130", "3128"}, {"http", "200.201.216.72", "80"}, {"https", "200.229.202.93", "3128"}, {"https", "221.180.170.24", "8080"}, {"http", "120.132.71.212", "80"}, {"https", "200.229.202.93", "8080"}, {"http", "89.40.125.130", "8080"}, {"http", "167.114.82.173", "3128"}, {"http", "187.0.169.162", "3128"}, {"http", "190.248.128.122", "3128"}, {"https", "149.56.145.48", "3128"}, {"http", "149.56.89.97", "80"}, {"https", "217.61.16.201", "8080"}, {"https", "89.36.212.227", "3128"}, {"https", "217.61.1.12", "80"}, {"http", "151.80.149.147", "8080"}, {"http", "80.123.100.194", "3128"}, {"http", "221.180.170.10", "80"}, {"http", "104.196.226.54", "80"}, {"http", "103.14.8.239", "80"}, {"https", "177.67.82.111", "8080"}, {"https", "121.8.98.202", "82"}, {"http", "177.67.82.111", "80"}, {"http", "45.79.219.25", "3128"}, {"http", "86.105.55.21", "3128"}, {"http", "89.40.122.35", "8080"}, {"http", "144.217.139.230", "3128"}, {"https", "177.67.84.157", "80"}, {"http", "221.180.170.53", "8080"}, {"https", "124.88.67.14", "80"}, {"http", "181.40.78.174", "3128"}, {"https", "185.43.210.238", "80"}, {"http", "177.67.81.248", "8799"}, {"https", "94.177.234.100", "3128"}, {"http", "89.40.122.35", "3128"}, {"https", "200.201.216.73", "3128"}, {"http", "5.196.29.172", "8080"}, {"https", "94.177.234.100", "8080"}, {"https", "51.15.46.137", "3128"}, {"https", "192.241.148.190", "80"}, {"https", "58.176.46.248", "8380"}, {"http", "177.54.158.135", "80"}, {"https", "107.191.42.31", "3128"}, {"http", "94.177.251.156", "3128"}, {"https", "94.177.243.65", "8080"}, {"http", "177.67.82.121", "3128"}, {"http", "177.54.144.233", "3128"}, {"http", "94.177.198.243", "3128"}, {"http", "89.40.113.177", "3128"}, {"https", "94.177.243.108", "80"}, {"https", "89.36.223.59", "3128"}, {"https", "47.88.3.213", "8080"}, {"http", "138.197.208.210", "8888"}, {"http", "144.217.139.230", "8080"}, {"https", "89.40.114.200", "80"}, {"http", "200.201.216.68", "3128"}, {"http", "94.177.198.243", "8080"}, {"https", "138.197.32.85", "8080"}, {"http", "162.243.45.128", "8080"}, {"https", "46.0.196.220", "8081"}, {"https", "121.135.146.184", "8080"}, {"http", "89.36.212.227", "8080"}, {"http", "94.177.233.100", "3128"}, {"http", "138.197.213.196", "8080"}, {"https", "86.105.55.118", "80"}, {"https", "94.177.242.47", "8080"}, {"http", "112.87.43.47", "8081"}, {"https", "177.67.82.40", "3128"}, {"http", "94.177.242.141", "80"}, {"https", "94.209.9.243", "80"}, {"https", "190.242.119.194", "3128"}, {"https", "94.177.241.171", "3128"}, {"http", "89.40.114.206", "8080"}, {"https", "158.69.83.111", "3128"}, {"http", "89.36.212.56", "3128"}, {"http", "191.96.43.144", "8080"}, {"http", "94.177.252.138", "3128"}, {"http", "94.177.243.25", "8080"}, {"https", "177.53.140.81", "8080"}, {"http", "144.217.133.42", "8080"}, {"https", "54.206.62.2", "8083"}, {"http", "177.67.82.116", "8080"}, {"https", "185.35.64.83", "8080"}, {"http", "89.40.114.134", "3128"}, {"http", "77.81.104.232", "3128"}, {"https", "176.126.245.23", "3128"}, {"http", "52.3.186.199", "3128"}, {"http", "132.148.74.148", "80"}, {"http", "52.59.61.67", "8083"}, {"https", "46.218.85.101", "3129"}, {"http", "89.40.114.206", "3128"}, {"https", "138.68.97.204", "3128"}, {"http", "89.163.152.242", "3128"}, {"http", "119.81.165.202", "8080"}, {"https", "200.201.216.79", "8080"}, {"https", "86.105.51.43", "3128"}, {"http", "94.177.173.201", "80"}, {"https", "86.105.55.21", "80"}, {"http", "177.67.84.218", "8080"}, {"http", "125.164.98.22", "80"}, {"http", "94.177.198.243", "8799"}, {"https", "177.67.82.80", "3128"}, {"http", "144.217.188.52", "80"}, {"http", "144.217.203.148", "3128"}, {"http", "144.217.48.67", "80"}, {"http", "89.36.213.168", "3128"}, {"http", "109.170.94.97", "8081"}, {"http", "119.196.16.236", "3128"}, {"https", "89.185.234.153", "80"}, {"http", "89.40.114.200", "8080"}, {"http", "200.196.230.168", "8080"}, {"https", "86.105.55.118", "8080"}, {"http", "89.40.114.200", "3128"}, {"https", "138.197.145.234", "3128"}, {"http", "217.61.1.12", "8080"}, {"http", "159.203.4.156", "80"}, {"https", "67.205.159.165", "8080"}, {"http", "221.180.170.22", "80"}, {"http", "54.235.252.205", "3128"}, {"https", "217.174.227.10", "3128"}, {"http", "89.40.113.120", "8080"}, {"http", "94.177.233.100", "8080"}, {"https", "94.177.252.138", "8080"}, {"https", "185.35.64.165", "3128"}, {"https", "201.73.145.20", "8080"}, {"http", "162.243.95.205", "8080"}, {"http", "161.139.18.76", "9000"}, {"http", "149.56.81.59", "8080"}, {"http", "45.40.176.236", "8799"}, {"http", "138.68.173.183", "8080"}, {"https", "89.38.146.208", "8080"}, {"https", "177.54.144.233", "8080"}, {"http", "201.73.145.68", "8080"}, {"https", "138.197.213.196", "3128"}, {"https", "94.177.243.65", "80"}, {"https", "138.197.21.105", "3128"}, {"http", "94.177.240.61", "3128"}, {"http", "54.149.134.149", "8083"}, {"http", "121.32.251.45", "80"}, {"http", "167.114.34.65", "3128"}, {"http", "89.40.112.244", "80"}, {"http", "178.32.108.101", "8080"}, {"https", "89.38.146.208", "80"}, {"http", "89.38.146.208", "3128"}, {"https", "94.177.252.138", "80"}, {"http", "120.77.242.32", "28080"}, {"http", "80.1.116.80", "80"}, {"http", "219.131.195.62", "3128"}, {"http", "168.83.7.40", "8080"}, {"http", "191.96.42.174", "8080"}, {"http", "89.40.125.130", "80"}, {"http", "121.32.251.44", "80"}, {"http", "177.67.84.231", "80"}, {"https", "217.61.16.201", "3128"}, {"https", "65.152.200.93", "3128"}, {"http", "37.46.192.226", "3128"}, {"http", "94.177.242.85", "8080"}, {"http", "46.31.123.225", "3128"}, {"http", "177.67.82.16", "80"}, {"http", "200.229.202.72", "8080"}, {"https", "200.229.193.112", "3128"}, {"https", "177.54.144.218", "8080"}, {"http", "94.177.241.171", "80"}, {"https", "89.40.113.177", "80"}, {"https", "89.40.112.244", "8080"}, {"http", "177.67.84.218", "80"}, {"https", "183.95.80.165", "8080"}, {"http", "177.67.82.116", "3128"}, {"http", "200.201.216.73", "8080"}, {"http", "94.177.241.47", "3128"}, {"https", "94.177.241.171", "8080"}, {"http", "221.180.170.25", "8080"}, {"http", "89.36.213.253", "80"}, {"http", "177.53.140.81", "3128"}, {"https", "200.201.216.74", "80"}, {"http", "200.201.216.68", "8080"}, {"http", "52.39.114.6", "443"}, {"https", "94.177.251.156", "8080"}, {"http", "159.203.123.37", "3128"}, {"http", "112.214.73.253", "80"}, {"http", "107.180.64.121", "8080"}, {"http", "185.35.64.83", "3128"}, {"http", "200.105.249.242", "3128"}, {"http", "200.201.216.74", "3128"}, {"http", "94.177.252.138", "8799"}, {"https", "177.67.82.121", "8080"}, {"https", "89.40.114.206", "80"}, {"http", "144.217.46.198", "80"}, {"https", "212.51.136.242", "3128"}, {"http", "89.36.219.166", "3128"}, {"https", "91.211.106.4", "8081"}, {"https", "94.181.34.64", "80"}, {"http", "138.197.32.85", "80"}, {"http", "177.54.158.146", "8080"}, {"http", "192.95.47.171", "8799"}, {"https", "89.36.212.56", "80"}, {"http", "37.187.100.23", "3128"}, {"http", "64.137.168.137", "3128"}, {"http", "158.69.77.48", "80"}, {"https", "94.177.243.25", "3128"}, {"http", "202.84.73.126", "8080"}, {"http", "185.35.64.83", "80"}, {"http", "158.69.52.120", "8888"}, {"http", "63.223.86.116", "3128"}, {"http", "200.229.202.214", "8080"}, {"https", "177.67.86.121", "8080"}, {"https", "54.193.32.5", "3128"}, {"http", "192.95.47.171", "3128"}, {"http", "89.38.149.185", "8080"}, {"https", "217.61.16.201", "80"}, {"http", "154.16.127.141", "8080"}, {"http", "149.56.147.46", "8080"}, {"https", "89.40.113.177", "8080"}, {"http", "113.252.129.133", "8380"}, {"http", "195.3.247.118", "3128"}, {"http", "45.55.209.249", "3128"}, {"https", "144.217.139.57", "3128"}, {"http", "200.201.202.90", "8080"}, {"https", "177.67.82.12", "80"}, {"http", "89.40.114.134", "8080"}, {"https", "131.0.246.91", "3128"}, {"http", "158.69.73.162", "8080"}, {"http", "54.215.219.173", "8083"}, {"http", "77.73.66.26", "8080"}, {"http", "198.211.110.52", "3128"}, {"http", "144.217.188.52", "3128"}, {"http", "138.197.148.203", "80"}, {"https", "198.50.235.188", "3128"}, {"https", "138.197.12.124", "3128"}, {"http", "89.36.223.59", "8080"}, {"http", "104.236.1.180", "3128"}, {"http", "144.217.162.29", "8080"}, {"http", "200.229.202.94", "8080"}};
    private static int[] resultList;
    private static ArrayList<String> resultNameList = new ArrayList();

    public static void openConnection() throws SQLException {
        c1 = (Connection) DriverManager.getConnection("jdbc:derby://localhost:1527/sample", "app", "app");
    }

    public static void crawlSettings() throws UnsupportedEncodingException, IOException, SQLException {
        clearArticleList();
        clearAuthorList();
        clearReturnedIDs();
        iter = 0;
        didYouMean = "";
        http = "https://scholar.google.com/scholar?start=" + iter + "hl=tr&q=";
        queryID = Database.addQuery(c1, query);
        System.out.println("query id: " + queryID);
        //creating url
        query = URLEncoder.encode(query, StandardCharsets.UTF_8.name());
        http += query;
        System.out.println(http);
        System.out.println("article query: " + http);
        startCrawl();

    }

    public static void startCrawl() throws UnsupportedEncodingException, IOException, SQLException {

//            //creating html document to parsing.
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                InetSocketAddress.createUnresolved("148.72.248.150", 8080));

        doc = Jsoup.connect((String) (http))
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36"
                        + " (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36").get();

        //checking did you mean proposal.
        Elements didyoumean = doc.select("p[class=gs_med]").select("a[href]");
        if (didyoumean.size() > 0) {
            didYouMean = didyoumean.get(0).text();
        } else {
            //if there is no misspelling, parsing of html document start here.
            //all articles have gs_r tag.
            articles = doc.select("div[class=gs_r]");
            int i = 0;
            for (Element article : articles) {
                //we took articles. now getting informations article by article
                articleName = article.select("div[class=gs_ri]").select("h3[class=gs_rt]").text();
                articleLink = article.select("div[class=gs_ri]").select("h3[class=gs_rt]").select("a[href]").attr("href");
                articleDesc = article.select("div[class=gs_ri]").select("div[class=gs_rs]").text();
                citingLink = "https://scholar.google.com" + article.select("div[class=gs_ri]").select("div[class=gs_fl]").select("a[href]").first().attr("href");
                try {
                    citingNumber = Integer.parseInt(article.select("div[class=gs_ri]").select("div[class=gs_fl]").select("a[href]").first().text().replaceAll("\\D+", ""));
                } catch (NumberFormatException ex) {
                    citingNumber = 0;
                }
                authors = article.select("div[class=gs_ri]").select(".gs_a>a");
                authorList = new ArrayList();
                tempAuthor = new Author();
                if (authors.isEmpty()) {
                    authorName = article.select("div[class=gs_ri]").select("div[class=gs_a]").text();
                    tempAuthor.setName(authorName);
                    tempAuthor.setLink("empty");
                    authorList.add(tempAuthor);
                    Database.addAuthor(c1, tempAuthor.getName(), tempAuthor.getLink());
                } else {
                    authors.stream().map((a) -> {
                        authorName = a.text();
                        authorLink = "https://scholar.google.com" + a.attr("href");
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
                //we took every article in the page. now creating new article object with parameters.
                tempArticle = new Article();
                tempArticle.setName(articleName);
                tempArticle.setAuthor(authorList);
                tempArticle.setLink(articleLink);
                tempArticle.setDescription(articleDesc);
                tempArticle.setCitingLink(citingLink);
                tempArticle.setCiting(citingNumber);
                articleList.add(tempArticle);

                int authorID = Database.getAuthor(c1, authorList.get(0).getLink());
                if ("".equals(articlePath)) {
                    Database.addArticle(c1, tempArticle.getName(), tempArticle.getLink(),
                            tempArticle.getDescription(), tempArticle.getCitingLink(), authorID, citingNumber, queryID);
                } else {
                    Database.addArticle(c1, tempArticle.getName(), tempArticle.getLink(),
                            tempArticle.getDescription(), tempArticle.getCitingLink(), authorID,
                            articlePath, queryID, citingNumber);
                }

            }
        }
        articleID = 0;
        articlePath = "";

        if (iter == 0) {
            returnedIDs = Database.getArticleIDs(c1, queryID);
            iter = 1;
            System.out.println("ikinci level");
            for (int i : returnedIDs) {
                http = Database.getArticleCitingLink(c1, i);
                articlePath = Database.getArticlePath(c1, i);
                startCrawl();
            }
            iter = 2;
        }
        if (iter == 2) {
            iter = 3;
            System.out.println("üçüncü level");
            returnedIDs = Database.getArticleIDs(c1, queryID);
            for (int i : returnedIDs.subList(10, 110)) {
                articlePath = Database.getArticlePath(c1, i);
                http = Database.getArticleCitingLink(c1, i);
                startCrawl();
            }
            iter = 3;
        }
        if (iter == 3) {
            clearArticleList();
            getResults();
        }
    }

    public static void getResults() throws SQLException {
        Database.deleteDuplicateArticles(c1, queryID);
        resultList = Database.getFirstTen(c1, queryID);
        for (int a : resultList) {
            resultNameList.add(Database.getArticleName(c1, a));
        }

    }

    public static void setQueryID(int queryID) {
        Crawler.queryID = queryID;
    }

    public static String getDidYouMean() {
        return didYouMean;
    }

    public static ArrayList<String> getResultNameList() {
        return resultNameList;
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

    public static void clearReturnedIDs() {
        returnedIDs.clear();
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

    public static int[] getResultList() {
        return resultList;
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

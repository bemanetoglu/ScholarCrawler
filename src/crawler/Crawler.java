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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

    static ArrayList<Article> articleList = new ArrayList();
    static ArrayList<Author> authorList = new ArrayList();
    static List<Node<Article>> childrenList = new ArrayList();
    static Node<Article> parentNode = null;
    static Node<Article> childNode = null;
    static ArrayList<Node> parents = new ArrayList();
    static Article tempArticle = null;
    static Author tempAuthor = null;
    static String var = null;
    static String http = null;
    static int iter = 0;
    static Document doc = null;
    static Document childDoc = null;

    public static void crawl() throws UnsupportedEncodingException, IOException {
        //creating url
        var = URLEncoder.encode(var, StandardCharsets.UTF_8.name());
        Document doc = Jsoup.connect((String) ("https://scholar.google.com/scholar?hl=tr&q=" + var))
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36"
                        + " (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
        //parsing the result
        Elements articles = doc.select("div[class=gs_r]");
        int i = 0;
        for (Element article : articles) {
            String articleName = article.select("div[class=gs_ri]").select("h3[class=gs_rt]").text();
            String articleLink = "https://scholar.google.com" + article.select("div[class=gs_ri]").select("h3[class=gs_rt]").select("a[href]").attr("href");
            String articleDesc = article.select("div[class=gs_ri]").select("h3[class=gs_rs]").text();
            String citingLink = "https://scholar.google.com" + article.select("div[class=gs_ri]").select("div[class=gs_fl]").select("a[href]").first().attr("href");
            int citingNumber = Integer.parseInt(article.select("div[class=gs_ri]").select("div[class=gs_fl]").select("a[href]").first().text().replaceAll("\\D+", ""));
            Elements authors = article.select("div[class=gs_ri]").select(".gs_a>a");
            authorList = new ArrayList();
            authors.stream().map((a) -> {
                String authorName = a.text();
                String authorLink = "https://scholar.google.com" + a.attr("href");
                tempAuthor = new Author(authorName, authorLink);
                return a;
            }).forEach((_item) -> {
                authorList.add(tempAuthor);
            });
            tempArticle = new Article(articleName, authorList, articleLink, articleDesc, citingLink, citingNumber);
            parentNode = new Node<>(tempArticle);
            articleList.add(tempArticle);
            parents.add(parentNode);
        }

    }
}

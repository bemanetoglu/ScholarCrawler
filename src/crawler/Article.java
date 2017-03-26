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
import java.util.ArrayList;

public class Article {

    private String name;
    private String link;
    private String description;
    private String citingLink;
    private ArrayList<Author> author;
    private int citing;

    Article(String articleName, ArrayList<Author> authors, String articleLink, String desc, String citingLink, int citingNumber) {
        this.name = articleName;
        this.author = authors;
        this.link = articleLink;
        this.description = desc;
        this.citingLink = citingLink;
        this.citing = citingNumber;
    }

    Article() {

    }

    public String getName() {
        return this.name;
    }

    public String getLink() {
        return this.link;
    }

    public void getAuthor() {
        for (int i = 0; i < this.author.size(); ++i) {
            System.out.println("Author Name: " + this.author.get(i).getName());
            System.out.println("Author Link: " + this.author.get(i).getLink());
        }
    }

    public int authorCount() {
        return this.author.size();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setCitingLink(String citingLink) {
        this.citingLink = citingLink;
    }

    public void setAuthor(ArrayList<Author> author) {
        this.author = author;
    }

    public void setCiting(int citing) {
        this.citing = citing;
    }

    public String getCitingLink() {
        return this.citingLink;
    }

    public int getCiting() {
        return this.citing;
    }
}

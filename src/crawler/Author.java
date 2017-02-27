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
public class Author {

    private String name;
    private String link;

    Author(String authorName, String authorLink) {
        this.name = authorName;
        this.link = authorLink;
    }

    Author() {

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return this.link;
    }
}

package com.wink.anu.look4book;

/**
 * Created by WELLCOME on 31-07-2017.
 */

public class Book {
    private String title;
    private String author;
    private String Url;
    private String imgUrl;

    public Book(String title, String author, String url, String imgUrl) {
        this.title = title;
        this.author = author;
        Url = url;
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return Url;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}

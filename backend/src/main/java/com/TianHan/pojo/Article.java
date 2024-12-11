package com.TianHan.pojo;

@lombok.Data
public class Article {
    private int id;
    private String title;
    private String img;
    private String description;
    private String content;
    private String time;
    private int authorId;
    private int viewCount;
    private int categoryId;
    private int comment_count;
    private String author;
    private String categoryName;
}

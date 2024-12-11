package com.TianHan.pojo;

@lombok.Data
public class Article {
    private int comment_count;
    private int categoryId;
    private int id;
    private String title;
    private String img;
    private String description;
    private String content;
    private String time;
    private int authorId;
    private int viewCount;
    private String author;
    private String categoryName;
}

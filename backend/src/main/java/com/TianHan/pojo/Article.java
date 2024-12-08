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
}

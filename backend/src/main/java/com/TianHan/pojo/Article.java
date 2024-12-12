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
    private Integer commentCount;
    private String departmentName;

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }
}

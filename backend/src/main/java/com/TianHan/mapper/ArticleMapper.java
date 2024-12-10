package com.TianHan.mapper;

import com.TianHan.pojo.Article;

import java.util.List;

public interface ArticleMapper {

    List<Article> queryAll(Article article);

    int insert(Article article);
    int update(Article article);
    int deleteById(Integer id);
    Article selectById(Integer id);

    List<Article> selectAll(Article article);

    Integer getArticleCountByDate(String dateStr);
}

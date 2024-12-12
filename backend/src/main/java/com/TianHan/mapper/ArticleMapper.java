package com.TianHan.mapper;

import com.TianHan.pojo.Article;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ArticleMapper {
    /**
     * 插入新文章
     */
    int insert(Article article);

    /**
     * 查询所有文章（带条件）
     */
    List<Article> queryAll(Article article);
    List<Article> queryAll();
    /**
     * 根据ID查询文章
     */
    Article selectById(@Param("id") Integer id);

    /**
     * 查询所有文章（带条件）
     */
    Integer update(Article article);
    Integer deleteById(@Param("id")int id);
    List<Article> selectAll(Article article);
    List<Article> selectAllWithAuthorId(Integer authorId);

    List<Article> selectAllWithAuthor();
    Integer getArticleCountByDate(String dateStr);
    List<Article>selectHotArticles();
    Integer incrementViewCount(@Param("id")Integer id);
    List<Article> selectByCategory(Integer categoryId, String title);
}

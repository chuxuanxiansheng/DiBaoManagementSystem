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

    /**
     * 根据ID查询文章
     */
    Article selectById(@Param("id") Integer id);

    /**
     * 查询所有文章（带条件）
     */


    List<Article> selectAll(Article article);
    List<Article> selectAllWithAuthorId();

    List<Article> selectAllWithAuthor();
    Integer getArticleCountByDate(String dateStr);
}

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

    /**
     * 更新文章
     */
    int update(Article article);

    /**
     * 根据ID删除文章
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 获取指定日期的文章数量
     */
    Integer getArticleCountByDate(@Param("dateStr") String dateStr);

    /**
     * 根据分类ID和标题查询文章
     */
    List<Article> selectByCategory(@Param("categoryId") Integer categoryId, @Param("title") String title);

    /**
     * 查询热门文章
     */
    List<Article> selectHotArticles();

    /**
     * 增加文章浏览量
     */
    void incrementViewCount(@Param("id") Integer id);
}

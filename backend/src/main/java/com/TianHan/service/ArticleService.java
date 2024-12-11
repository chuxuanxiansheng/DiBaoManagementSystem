package com.TianHan.service;

import cn.hutool.core.date.DateUtil;
import com.TianHan.mapper.ArticleMapper;
import com.TianHan.pojo.Article;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    public int addArticle(Article article) {
        article.setTime(DateUtil.now());
        int result = articleMapper.insert(article);
        log.info("新增文章成功:{}", article);
        return result;
    }

    public PageInfo<Article> findList(Article article,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articles = articleMapper.queryAll(article);
        log.info("分页查询数据:{}", articles);
        return PageInfo.of(articles);
    }
    public PageInfo<Article>getArticlesByCategory(Article article,Integer pageNum, Integer pageSize,Integer categoryId){
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articles = articleMapper.selectByCategory(categoryId, article.getTitle());
        log.info("分页查询数据:{}", articles);
        return PageInfo.of(articles);
    }

    public Article findById(int id) {
        Article article = articleMapper.selectById(id);
        log.info("查询id为{}的数据:{}", id, article);
        return article;
    }

    public PageInfo<Article> selectPage(Article article, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articles = articleMapper.selectAll(article);
        log.info("分页查询数据:{}", articles);
        return PageInfo.of(articles);
    }

    public int updateArticle(Article article) {
        int result = articleMapper.update(article);
        log.info("更新文章成功:{}", article);
        return result;
    }

    public int deleteArticle(int id) {
        int result = articleMapper.deleteById(id);
        log.info("删除id为{}的文章成功", id);
        return result;
    }

    public Integer getArticleCountByDate(String dateStr) {
        return articleMapper.getArticleCountByDate(dateStr);
    }

    public boolean batchDeleteArticle(List<Integer> ids) {
        for (int id : ids) {
            int result = this.deleteArticle(id);
            if (result == 0) {
                log.error("删除id为{}的文章失败", id);
                return false;
            }
        }
        log.info("批量删除文章成功:{}", ids);
        return true;
    }


    public List<Article> getArticlesByCategory(Integer categoryId, String title) {
        return articleMapper.selectByCategory(categoryId, title);
    }


    public List<Article> getHotArticles() {
        return articleMapper.selectHotArticles();
    }


    public void incrementViewCount(Integer id) {
        articleMapper.incrementViewCount(id);
    }
}

package com.TianHan.service;

import cn.hutool.core.date.DateUtil;
import com.TianHan.mapper.ArticleMapper;
import com.TianHan.pojo.Article;
import com.TianHan.pojo.User;
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

    @Resource
    private CommentService commentService;

    public int addArticle(Article article) {
        article.setTime(DateUtil.now());
        int result = articleMapper.insert(article);
        log.info("新增文章成功:{}", article);
        return result;
    }

    public PageInfo<Article> findList(Article article, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articles = articleMapper.queryAllp(article);

        for (Article art : articles) {
            int commentCount = commentService.getCommentCount(art.getId());
            art.setComment_count(commentCount);
        }

        log.info("分页查询数据:{}", articles);
        return PageInfo.of(articles);
    }
    public PageInfo<Article>getArticlesByCategory(Article article,Integer pageNum, Integer pageSize,Integer categoryId){
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articles = articleMapper.selectByCategory(categoryId, article.getTitle());

        for (Article art : articles) {
            int commentCount = commentService.getCommentCount(art.getId());
            art.setComment_count(commentCount);
        }

        log.info("分页查询数据:{}", articles);
        return PageInfo.of(articles);
    }

    public Article findById(int id) {
        Article article = articleMapper.selectById(id);
        if (article != null) {
            int commentCount = commentService.getCommentCount(article.getId());
            article.setComment_count(commentCount);
            log.info("查询id为{}的数据:{}, 评论数:{}", id, article, commentCount);
        }
        return article;
    }

    public PageInfo<Article> selectPage(Article article, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articles = articleMapper.queryAll(article);
        for (Article art : articles) {
            int commentCount = commentService.getCommentCount(art.getId());
            art.setComment_count(commentCount);
        }
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

    public List<Article> findAllArticlesWithAuthorId(Integer authorId) {
        List<Article> articles = articleMapper.selectAllWithAuthorId(authorId);
        for (Article art : articles) {
            int commentCount = commentService.getCommentCount(art.getId());
            art.setComment_count(commentCount);
        }
        return articles;
    }

    public List<Article> findAllArticlesWithAuthor() {
        return articleMapper.selectAllWithAuthor();
    }


    public List<Article> getArticlesByCategory(Integer categoryId, String title) {
        return articleMapper.selectByCategory(categoryId, title);
    }


    public List<Article> getHotArticles() {
        List<Article> hotArticles = articleMapper.selectHotArticles();
        for (Article article : hotArticles) {
            int commentCount = commentService.getCommentCount(article.getId());
            article.setComment_count(commentCount);
        }
        return hotArticles;
    }


    public void incrementViewCount(Integer id) {
        articleMapper.incrementViewCount(id);
    }

    public List<Article> findArticlesByAuthorId(int authorId) {
        List<Article> articles = articleMapper.selectAllWithAuthorId(authorId);
        for (Article article : articles) {
            int commentCount = commentService.getCommentCount(article.getId());
            article.setComment_count(commentCount);
        }
        return articles;
    }

    public List<Article> findArticlesAll() {
        List<Article> articles = articleMapper.selectAllW();
        for (Article art : articles) {
            int commentCount = commentService.getCommentCount(art.getId());
            art.setComment_count(commentCount);
        }
        return articles;
    }

    public PageInfo<Article> selectPageAll(Article article, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articles = articleMapper.queryAllp(article);
        for (Article art : articles) {
            int commentCount = commentService.getCommentCount(art.getId());
            art.setComment_count(commentCount);
        }
        log.info("分页查询数据:{}", articles);
        return PageInfo.of(articles);
    }
}

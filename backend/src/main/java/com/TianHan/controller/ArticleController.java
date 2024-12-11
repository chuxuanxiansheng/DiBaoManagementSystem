package com.TianHan.controller;

import com.TianHan.mapper.ArticleMapper;
import com.TianHan.pojo.Article;
import com.TianHan.pojo.Comment;
import com.TianHan.service.ArticleService;
import com.TianHan.service.CommentService;
import com.TianHan.utils.AuthAccess;
import com.TianHan.utils.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentService commentService;

    @AuthAccess
    @GetMapping("/selectById/{id}")
    @ResponseBody
    public Result getArticleById(@PathVariable("id") Integer id){
        Article article = articleService.findById(id);
        log.info("查询的article数据为:{}",article);
        if(article == null){
            return Result.error("404","文章不存在");
        }
        return Result.success(article);
    }
    @PostMapping("/insert")
    @ResponseBody
    public Result insertArticle(@RequestBody Article article){
        int result = articleService.addArticle(article);
        if(result == 1){
            log.info("插入成功");
            return Result.success();
        }else{
            log.info("插入失败");
            return Result.error();
        }
    }
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public Result deleteArticle(@PathVariable("id") int id){
        int result = articleService.deleteArticle(id);
        if(result == 1){
            log.info("删除成功");
            return Result.success();
        }else{
            log.info("删除失败");
            return Result.error();
        }
    }
    @DeleteMapping("/batchDelete")
    public Result batchDeleteArticle(@RequestBody List<Integer> ids){
        boolean result = articleService.batchDeleteArticle(ids);
        if(result){
            log.info("删除成功");
            return Result.success();
        }else{
            log.info("删除失败");
            return Result.error();
        }
    }

    @PutMapping("/update")
    @ResponseBody
    public Result updateArticle(@RequestBody Article article){
        int result = articleService.updateArticle(article);
        if(result == 1){
            log.info("更新成功");
            return Result.success();
        }else{
            log.info("更新失败");
            return Result.error();
        }
    }

    @GetMapping("/selectPage")
    @ResponseBody
    public Result selectPage(Article article, @RequestParam() Integer authorId, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize){
        article.setAuthorId(authorId);
        PageInfo<Article> articlePageInfo = articleService.selectPage(article, pageNum, pageSize);
        return Result.success(articlePageInfo);
    }

    @GetMapping("/category/{categoryId}")
    public Result getArticlesByCategory(
            @PathVariable(required = false) Integer categoryId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String title) {
        
        PageHelper.startPage(pageNum, pageSize);
        List<Article> articles = articleService.getArticlesByCategory(categoryId, title);
        PageInfo<Article> pageInfo = new PageInfo<>(articles);
        return Result.success(pageInfo);
    }
    @AuthAccess
    @GetMapping("/hot")
    public Result getHotArticles() {
        List<Article> hotArticles = articleService.getHotArticles();
        return Result.success(hotArticles);
    }
    @AuthAccess
    @PostMapping("/view/{id}")
    public Result incrementViewCount(@PathVariable Integer id) {
        articleService.incrementViewCount(id);
        return Result.success();
    }
    @AuthAccess
    @GetMapping("/comments/{articleId}")
    public Result getComments(@PathVariable Integer articleId) {
        try {
            if (articleId == null || articleId <= 0) {
                return Result.error("400", "无效的文章ID");
            }
            List<Comment> comments = commentService.getCommentsByArticleId(articleId);
            return Result.success(comments);
        } catch (Exception e) {
            log.error("获取评论失败", e);
            return Result.error("500", "获取评论失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/comment")
    public Result addComment(@RequestBody Comment comment) {
        try {
            if (comment == null || comment.getArticleId() == null) {
                return Result.error("400", "评论信息不完整");
            }
            commentService.addComment(comment);
            return Result.success();
        } catch (Exception e) {
            log.error("添加评论失败", e);
            return Result.error("500", "添加评论失败：" + e.getMessage());
        }
    }
}

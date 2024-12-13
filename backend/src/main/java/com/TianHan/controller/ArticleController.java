package com.TianHan.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.TianHan.mapper.ArticleMapper;
import com.TianHan.pojo.Article;
import com.TianHan.pojo.User;
import com.TianHan.pojo.Comment;
import com.TianHan.service.ArticleService;
import com.TianHan.service.CommentService;
import com.TianHan.utils.AuthAccess;
import com.TianHan.utils.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.Resource;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;

    @Resource
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
    @GetMapping("/selectPageAll")
    @ResponseBody
    public Result selectPageAll(Article article, @RequestParam() Integer authorId, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize){
        article.setAuthorId(authorId);
        PageInfo<Article> articlePageInfo = articleService.selectPageAll(article, pageNum, pageSize);
        return Result.success(articlePageInfo);
    }
    @AuthAccess
    @GetMapping("/exportWithAuthorId")
    @ResponseBody
    public void exportWithAuthorId(HttpServletResponse response,@RequestParam() Integer authorId) throws Exception {
       List<Article> articleList = articleService.findAllArticlesWithAuthorId(authorId);
       //打印全部数据
        log.info("查询到的数据为:{}",articleList);
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("title", "文章标题");
        writer.addHeaderAlias("img", "文章封面");
        writer.addHeaderAlias("description", "文章描述");
        writer.addHeaderAlias("content", "内容");
        writer.addHeaderAlias("time", "发布时间");
        writer.addHeaderAlias("authorId", "作者id");
        writer.addHeaderAlias("author","作者名");
        writer.addHeaderAlias("viewCount","浏览量");
        writer.addHeaderAlias("categoryId","文章分类");
        writer.addHeaderAlias("categoryName","分类名");
        writer.addHeaderAlias("commentCount","评论数");
        //默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(articleList, true);

        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset = UTF-8");
        //设置文件名
        String fileName = URLEncoder.encode("文章信息", StandardCharsets.UTF_8) ;  //中文文件名需要进行URL编码
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        out.close();
    }
    @AuthAccess
    @GetMapping("/exportAll")
    public void exportAll(HttpServletResponse response) throws Exception {
        List<Article> articleList = articleService.findArticlesAll();
        //打印全部数据
        log.info("查询到的数据为:{}",articleList);
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("title", "文章标题");
        writer.addHeaderAlias("img", "文章封面");
        writer.addHeaderAlias("description", "文章描述");
        writer.addHeaderAlias("content", "内容");
        writer.addHeaderAlias("time", "发布时间");
        writer.addHeaderAlias("authorId", "作者id");
        writer.addHeaderAlias("author","作者名");
        writer.addHeaderAlias("viewCount","浏览量");
        writer.addHeaderAlias("categoryId","文章分类");
        writer.addHeaderAlias("categoryName","分类名");
        writer.addHeaderAlias("commentCount","评论数");
        //默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(articleList, true);

        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset = UTF-8");
        //设置文件名
        String fileName = URLEncoder.encode("文章信息", StandardCharsets.UTF_8) ;  //中文文件名需要进行URL编码
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        out.close();
    }
    @AuthAccess
    @GetMapping("/exportWithAuthor")
    public void exportWithAuthor(HttpServletResponse response) throws Exception {
        List<Article> articleList = articleService.findAllArticlesWithAuthor();

        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名

        writer.addHeaderAlias("title", "文章标题");
        writer.addHeaderAlias("img", "文章封面");
        writer.addHeaderAlias("description", "文章描述");
        writer.addHeaderAlias("content", "内容");
        writer.addHeaderAlias("time", "发布时间");
        writer.addHeaderAlias("authorId", "作者id");
        writer.addHeaderAlias("viewCount","浏览量");
        writer.addHeaderAlias("author","作者名");
        writer.addHeaderAlias("categoryName","分类名");
        writer.addHeaderAlias("categoryId","文章分类");
        writer.addHeaderAlias("comment_count","评论数");
        //默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(articleList, true);

        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset = UTF-8");
        //设置文件名
        String fileName = URLEncoder.encode("文章信息", StandardCharsets.UTF_8) ;  //中文文件名需要进行URL编码
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        writer.close();
        out.close();
    }
    @AuthAccess
    @PostMapping("/batchInsert")
    public Result batchInsert(MultipartFile file) throws Exception {
        // 拿到输入流构建 reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        // 读取数据
        reader.addHeaderAlias("文章标题", "title");
        reader.addHeaderAlias("文章封面", "img");
        reader.addHeaderAlias("文章描述", "description");
        reader.addHeaderAlias("内容", "content");
        reader.addHeaderAlias("发布时间", "time");
        reader.addHeaderAlias("作者id", "authorId");
        reader.addHeaderAlias("文章分类", "categoryId");
        reader.addHeaderAlias("分类名", "categoryName");
        reader.addHeaderAlias("浏览量", "viewCount");


        List<Article> articleList = reader.readAll(Article.class);

        // 批量插入
        for (Article article : articleList) {
            article.setAuthorId(article.getAuthorId()); // 设置当前用户的 ID
            articleService.addArticle(article);
        }
        return Result.success();
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

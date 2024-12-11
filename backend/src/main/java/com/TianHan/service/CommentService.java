package com.TianHan.service;

import com.TianHan.mapper.CommentMapper;
import com.TianHan.pojo.Comment;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentService {
    @Resource
    private CommentMapper commentMapper;

    public List<Comment> getCommentsByArticleId(Integer articleId){
        List<Comment> comments = commentMapper.selectByArticleId(articleId);
        log.info("根据文章id查询评论成功，评论为：{}",comments);
        return comments;
    }
    public void addComment(Comment comment){
        int insert = commentMapper.insert(comment);
        if (insert > 0){
            log.info("添加评论成功，评论为：{}",comment);
        }
    }
}

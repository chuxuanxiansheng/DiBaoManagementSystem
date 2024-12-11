package com.TianHan.mapper;

import com.TianHan.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    // 根据文章ID查询评论列表
    List<Comment> selectByArticleId(@Param("articleId") Integer articleId);
    
    // 添加评论
    int insert(Comment comment);
    
    // 删除评论
    int deleteById(@Param("id") Integer id);
    
    // 获取文章评论数量
    int getCommentCount(@Param("articleId") Integer articleId);
    
    // 根据用户ID查询评论列表
    List<Comment> selectByUserId(@Param("userId") Integer userId);
    
    // 批量删除评论
    int batchDelete(@Param("ids") List<Integer> ids);
    
    // 更新评论内容
    int updateContent(@Param("id") Integer id, @Param("content") String content);
} 
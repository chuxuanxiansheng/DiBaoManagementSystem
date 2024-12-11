package com.TianHan.pojo;

import lombok.Data;
import java.util.Date;

@Data
public class Comment {
    private Integer id;
    private Integer articleId;
    private Integer userId;
    private String content;
    private Date createTime;
    private  String  username;
    private String avatar;
} 
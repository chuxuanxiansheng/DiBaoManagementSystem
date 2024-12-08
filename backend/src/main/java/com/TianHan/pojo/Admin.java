package com.TianHan.pojo;


import lombok.Data;

@Data
public class Admin extends Account {
    private Integer uid;
    private String username;
    private String password;
    private String nickname;
    private String gender;
    private String occupation;
}

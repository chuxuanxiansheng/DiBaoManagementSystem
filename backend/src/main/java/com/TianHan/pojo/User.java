package com.TianHan.pojo;


import lombok.Data;

@Data
public class User extends Account {
    private Integer uid;
    private String username;
    private String password;
    private String newPassword;
    private String nickname;
    private String gender;
    private String status;
    private Integer departmentId;
    private String departmentName;
    private String email;
    private String phone;
    private String address;
    private String description;
    private String avatar;
    private String role;
    private String createDate;
    private String token;
}

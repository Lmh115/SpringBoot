package com.hui.SpringBoot22.pojo;

import javax.persistence.*;

@Entity
@Table(name = "test_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//默认为AUTO，这里设置为自增
    private Long id;
    @Column(name = "username",length = 50)
    private String username;
    @Column(name = "password",length = 50)
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

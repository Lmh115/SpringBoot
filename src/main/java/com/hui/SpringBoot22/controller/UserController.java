package com.hui.SpringBoot22.controller;

import com.hui.SpringBoot22.pojo.User;
import com.hui.SpringBoot22.repository.UserRepository;
import com.hui.SpringBoot22.utils.Md5;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/userLogin")
    public String userLogin(String username, String password, Map<String,Object> map){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        try{
            subject.login(token);
            map.put("loginName",username);
        }catch(Exception e){
            map.put("msg","登陆失败");
            return "login";
        }
        return "forward:userList";
    }

    @RequestMapping("/userList")
    @RequiresPermissions("user:select")
    public String list(Map<String,Object> map){
        List<User> users = userRepository.findAll();
        map.put("userList",users);
        return "userList";
    }

    @RequestMapping("/toUserAdd")
    public String toAdd(){
        return "userAdd";
    }

    @RequestMapping("/userAdd")
    @RequiresPermissions("user:add")
    public String userAdd(User user){
        user.setPassword(Md5.md5(user.getPassword()));
        userRepository.save(user);
        return "forward:userList";
    }

    @RequestMapping("/toUserEdit")
    public String toEdit(Long id,Map<String,Object> map){
        User user = userRepository.findUserById(id);
        map.put("user",user);
        return "userEdit";
    }

    @RequestMapping("/userEdit")
    @RequiresPermissions("user:update")
    public String userEdit(Long id,String username,String password){
        password = Md5.md5(password);
        userRepository.updateUserById(id,username,password);
        return "forward:userList";
    }

    @RequestMapping("/userDelete")
    @RequiresPermissions("user:delete")
    public String deleteUser(Long id){
        userRepository.deleteUserById(id);
        return "forward:userList";
    }
}

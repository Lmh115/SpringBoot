package com.hui.SpringBoot22.controller;

import com.hui.SpringBoot22.pojo.Role;
import com.hui.SpringBoot22.repository.RoleRepository;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequiresRoles("admin")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping("/toUpdateRole")
    public String toSelectRole(Long id,Map<String,Object> map){
        Role role = roleRepository.findRoleByUserid(id);
        List<String> roleNameList = Arrays.asList("admin","user");
        if(role != null) {
            map.put("role", role);
            map.put("roleNameList", roleNameList);
            return "updateRole";
        }else{
            map.put("msg","该用户还未分配角色");
            return "forward:userList";
        }
    }

    @RequestMapping("/roleUpdate")
    @RequiresPermissions("user:updateRole")
    public String selectRole(Long id,String roles){
        roleRepository.updateRoleForUserById(id,roles);
        return "forward:userList";
    }

}

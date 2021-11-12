package com.cheng.service.role;

import com.cheng.pojo.Role;

import java.sql.Connection;
import java.util.List;

public interface RoleService {

    //获取角色列表
    public List<Role> getRoleList();

}

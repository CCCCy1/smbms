package com.cheng.service.role;

import com.cheng.dao.BaseDao;
import com.cheng.dao.role.RoleDao;
import com.cheng.dao.role.RoleDaoimpl;
import com.cheng.pojo.Role;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RoleServiceImpl implements RoleService{

    RoleDao roleDao = new RoleDaoimpl();

    //获取角色列表
    public List<Role> getRoleList() {
        Connection connection = null;
        List<Role> roleList = null;

        try {
            connection = BaseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return roleList;
    }

    @Test
    public void test() {
        for (Role role : this.getRoleList()) {
            System.out.println(role.getRoleName());
        }
    }
}

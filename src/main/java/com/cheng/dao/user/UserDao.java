package com.cheng.dao.user;

import com.cheng.pojo.Role;
import com.cheng.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {

    //得到要登陆的用户
    public User getLoginUser(Connection connection, String userCode);

    //修改当前用户密码
    public int updatePwd(Connection connection, int id, String password) throws SQLException;

    ////根据用户名或者角色查询用户
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException;

    //获取用户列表
    public List<User> getUsertList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception;

}

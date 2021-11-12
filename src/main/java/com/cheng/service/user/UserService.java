package com.cheng.service.user;

import com.cheng.dao.BaseDao;
import com.cheng.pojo.User;
import com.mysql.jdbc.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface UserService {
    //用户登陆
    public User login(String userCode, String password);

    //修改当前用户密码
    public boolean updatePwd(int id, String password);

    //查询用户数量
    public int getUserCount(String userName, int userRole);

    //获取用户列表
    public List<User> getUsertList(String userName, int userRole, int currentPageNo, int pageSize) throws Exception;

}

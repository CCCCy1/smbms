package com.cheng.service.user;

import com.cheng.dao.BaseDao;
import com.cheng.dao.user.UserDao;
import com.cheng.dao.user.UserDaoImpl;
import com.cheng.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService{
    //业务层调用dao层
    private UserDao userDao;
    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    public User login(String userCode, String password) {

        Connection connection = null;
        User user = null;

        try {
            connection = BaseDao.getConnection();
            //通过业务层调用对应的具体的数据库操作
            user = userDao.getLoginUser(connection, userCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }

        return user;
    }

    public boolean updatePwd(int id, String password) {
        Connection connection = null;

        int i;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if ((i = userDao.updatePwd(connection, id, password)) > 0) {
                flag = true;
            }
            System.out.println(i);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    //查询用户数量
    public int getUserCount(String userName, int userRole) {
        Connection connection = null;
        int count = 0;

        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, userName, userRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return count;
    }

    //获取用户列表
    public List<User> getUsertList(String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        Connection connection = null;
        List<User> userList = null;
        System.out.println("UserService->userName: " + userName);
        System.out.println("UserService->userRole: " + userRole);
        System.out.println("UserService->currentPageNo: " + currentPageNo);
        System.out.println("UserService->pageSize: " + pageSize);

        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUsertList(connection, userName, userRole, currentPageNo, pageSize);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            BaseDao.closeResource(connection,null,null);
        }
        return userList;
    }

    @Test
    public void test() {
        UserServiceImpl userService = new UserServiceImpl();
        try {
            for (User user : userService.getUsertList(null, 0, 1, 5)) {
                System.out.println(user.getUserName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

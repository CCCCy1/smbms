package com.cheng.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.cheng.pojo.Role;
import com.cheng.pojo.User;
import com.cheng.service.role.RoleService;
import com.cheng.service.role.RoleServiceImpl;
import com.cheng.service.user.UserService;
import com.cheng.service.user.UserServiceImpl;
import com.cheng.util.Constants;
import com.cheng.util.PageSupport;
import com.mysql.jdbc.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class UserSelvlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method.equals("savepwd") && method != null) {
            this.updatePwd(req,resp);
        } else if (method.equals("pwdmodify") && method != null) {
            this.pwdModify(req,resp);
        } else if (method.equals("query") && method != null) {
            this.query(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //修改密码
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从Session里获取用户信息
        User o = (User)req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");
        String newpassword = req.getParameter("newpassword");

        boolean flag;

        if (o != null && !StringUtils.isNullOrEmpty(newpassword)) {
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(o.getId(), newpassword);
            if (flag) {
                req.setAttribute("message","修改密码成功。请退出，使用新密码重新登陆");
                //密码修改成功，移除当前Session
                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                req.setAttribute("message","密码修改失败");
            }
        } else {
            req.setAttribute("message","新密码不符合规则");
        }

        req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
    }

    //验证旧密码,Session中用户的密码
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
        //从Session里获取用户信息
        User o = (User)req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        HashMap<String, String> resultMap = new HashMap<String, String>();

        if(o == null) { //Session失效了
            resultMap.put("result","sessionerror");
        } else if(StringUtils.isNullOrEmpty(oldpassword)) { //输入的密码为空
            resultMap.put("result","error");
        } else {
            String userPassword = o.getUserPassword();
            if (userPassword.equals(oldpassword)) {
                resultMap.put("result","true");
            } else {
                resultMap.put("result","false");
            }
        }

        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();

            //JSONArray 阿里巴巴的JSON工具类，转换格式
            writer.write(JSONArray.toJSONString(resultMap));

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //用户管理
    public void query(HttpServletRequest req, HttpServletResponse resp) {

        //从前端获取数据
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        //获取用户列表
        UserService userService = new UserServiceImpl();

        int pageSize = 5;
        int currentPageNo = 1;

        if (queryUserName == null) {
            queryUserName = "";
        }
        if (!StringUtils.isNullOrEmpty(temp)) {
            queryUserRole = Integer.parseInt(temp); //0 1 2 3
        }
        if (pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex);
        }

        //获取用户总数
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //分页
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = pageSupport.getTotalPageCount();

        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }

        //获取用户展示列表
        List<User> usertList = null;
        try {
            usertList = userService.getUsertList(queryUserName, queryUserRole, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.setAttribute("userList",usertList);

        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList",roleList);
        req.setAttribute("totalCount",totalCount);
        req.setAttribute("currentPageNo",currentPageNo);
        req.setAttribute("totalPageCount",totalPageCount);
        req.setAttribute("queryUserName",queryUserName);
        req.setAttribute("queryUserRole",queryUserRole);

        try {
            req.getRequestDispatcher("userlist.jsp").forward(req,resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

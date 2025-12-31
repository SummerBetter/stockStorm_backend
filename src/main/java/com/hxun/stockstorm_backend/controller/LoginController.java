package com.hxun.stockstorm_backend.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.hxun.stockstorm_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录认证控制器
 */
@RestController
@RequestMapping("/acc/")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * 
     * @param username 用户名（必填）
     * @param password 密码（必填）
     * @param phone    手机号（可选）
     * @param email    邮箱（可选）
     * @return 注册结果
     */
    @PostMapping("register")
    public SaResult register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email) {

        // 参数验证
        if (username == null || username.trim().isEmpty()) {
            return SaResult.error("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return SaResult.error("密码不能为空");
        }

        // 调用服务层注册
        String result = userService.register(username, password, phone, email);

        if ("注册成功".equals(result)) {
            return SaResult.ok(result);
        } else {
            return SaResult.error(result);
        }
    }

    /**
     * 用户登录
     * 
     * @param account  账号（用户名或手机号）
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping("doLogin")
    public SaResult doLogin(
            @RequestParam String account,
            @RequestParam String password) {

        // 参数验证
        if (account == null || account.trim().isEmpty()) {
            return SaResult.error("账号不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return SaResult.error("密码不能为空");
        }

        // 调用服务层验证登录
        Long userId = userService.login(account, password);

        if (userId != null) {
            // 登录成功，使用 Sa-Token 进行会话管理
            StpUtil.login(userId);
            return SaResult.ok("登录成功").setData(StpUtil.getTokenInfo());
        } else {
            return SaResult.error("账号或密码错误");
        }
    }

    /**
     * 查询登录状态
     * 
     * @return 登录状态
     */
    @RequestMapping("isLogin")
    public SaResult isLogin() {
        boolean isLogin = StpUtil.isLogin();
        return SaResult.ok("是否登录：" + isLogin);
    }

    /**
     * 查询 Token 信息
     * 
     * @return Token信息
     */
    @RequestMapping("tokenInfo")
    public SaResult tokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    /**
     * 用户注销
     * 
     * @return 注销结果
     */
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("注销成功");
    }

    /**
     * 获取当前登录用户信息
     * 
     * @return 用户信息
     */
    @RequestMapping("getUserInfo")
    public SaResult getUserInfo() {
        // 检查是否登录
        if (!StpUtil.isLogin()) {
            return SaResult.error("未登录");
        }

        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 获取用户信息
        var user = userService.getUserById(userId);
        if (user == null) {
            return SaResult.error("用户不存在");
        }

        // 返回用户信息（不包含密码）
        user.setPassword(null);
        return SaResult.data(user);
    }

    /**
     * 更新用户角色（需要管理员权限）
     * 
     * @param userId 用户ID
     * @param role   新角色（NORMAL/VIP/ADMIN）
     * @return 更新结果
     */
    @PostMapping("updateRole")
    public SaResult updateRole(
            @RequestParam Long userId,
            @RequestParam String role) {

        // 检查是否登录
        if (!StpUtil.isLogin()) {
            return SaResult.error("未登录");
        }

        // 获取当前登录用户信息
        Long currentUserId = StpUtil.getLoginIdAsLong();
        var currentUser = userService.getUserById(currentUserId);

        // 检查是否为管理员
        if (currentUser == null || currentUser.getRole() != com.hxun.stockstorm_backend.entity.UserRole.ADMIN) {
            return SaResult.error("无权限，仅管理员可以修改用户角色");
        }

        // 验证角色参数
        com.hxun.stockstorm_backend.entity.UserRole newRole;
        try {
            newRole = com.hxun.stockstorm_backend.entity.UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SaResult.error("无效的角色类型，可选值：NORMAL、VIP、ADMIN");
        }

        // 更新角色
        String result = userService.updateUserRole(userId, newRole);

        if ("角色更新成功".equals(result)) {
            return SaResult.ok(result);
        } else {
            return SaResult.error(result);
        }
    }
}

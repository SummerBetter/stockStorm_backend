package com.hxun.stockstorm_backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hxun.stockstorm_backend.entity.User;
import com.hxun.stockstorm_backend.entity.UserRole;
import com.hxun.stockstorm_backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户服务类
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 用户注册
     * 
     * @param username 用户名
     * @param password 密码（明文）
     * @param phone    手机号（可选）
     * @param email    邮箱（可选）
     * @return 注册结果消息
     */
    public String register(String username, String password, String phone, String email) {
        // 检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        if (userMapper.selectCount(queryWrapper) > 0) {
            return "用户名已存在";
        }

        // 如果提供了手机号，检查手机号是否已存在
        if (phone != null && !phone.isEmpty()) {
            QueryWrapper<User> phoneQuery = new QueryWrapper<>();
            phoneQuery.eq("phone", phone);
            if (userMapper.selectCount(phoneQuery) > 0) {
                return "手机号已被注册";
            }
        }

        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // BCrypt加密
        user.setPhone(phone);
        user.setEmail(email);
        user.setRole(UserRole.NORMAL); // 默认为普通用户
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 插入数据库
        userMapper.insert(user);
        return "注册成功";
    }

    /**
     * 用户登录
     * 
     * @param account  账号（用户名或手机号）
     * @param password 密码（明文）
     * @return 用户ID，登录失败返回null
     */
    public Long login(String account, String password) {
        // 查询用户（支持用户名或手机号登录）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", account).or().eq("phone", account);
        User user = userMapper.selectOne(queryWrapper);

        // 用户不存在
        if (user == null) {
            return null;
        }

        // 验证密码
        if (passwordEncoder.matches(password, user.getPassword())) {
            return user.getId();
        }

        return null;
    }

    /**
     * 根据ID获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户对象
     */
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    /**
     * 更新用户角色
     * 
     * @param userId 用户ID
     * @param role   新角色
     * @return 更新结果消息
     */
    public String updateUserRole(Long userId, UserRole role) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return "用户不存在";
        }

        user.setRole(role);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);

        return "角色更新成功";
    }
}

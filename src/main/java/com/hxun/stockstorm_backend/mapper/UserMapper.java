package com.hxun.stockstorm_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hxun.stockstorm_backend.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // BaseMapper 已经提供了基本的 CRUD 方法
    // 包括: insert, deleteById, updateById, selectById, selectList 等
    // 可以使用 QueryWrapper 进行复杂查询，无需编写 XML
}

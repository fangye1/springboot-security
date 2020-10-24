package com.springboot.security.service;

import com.github.pagehelper.PageInfo;
import com.springboot.security.po.User;

/**
 * @author qizenan
 * @date 2020/10/08 13:12
 */
public interface UserService {
    /**
     * 创建用户信息
     *
     * @param user 实体
     * @return User
     */
    User createUser(User user);

    /**
     * 更新用户信息
     *
     * @param user 实体
     * @return true 成功，false 失败
     */
    boolean updateUser(User user);

    /**
     * 删除用户信息
     *
     * @param userId 唯一标识id
     * @return true 成功，false 失败
     */
    boolean deleteUser(long userId);

    /**
     * 按name查询用户信息
     *
     * @param name 账号
     * @return User
     */
    User getUserByName(String name);

    /**
     * 按name查询用户信息
     *
     * @param mobile 手机号
     * @return User
     */
    User getUserByMobile(String mobile);

    /**
     * 分页查询所有用户信息
     *
     * @param pageNum  页码
     * @param pageSize 页数
     * @return User 集合
     */
    PageInfo<User> getUserList(int pageNum, int pageSize);
}

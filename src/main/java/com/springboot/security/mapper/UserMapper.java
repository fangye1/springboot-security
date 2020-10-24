package com.springboot.security.mapper;

import com.springboot.security.po.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author qizenan
 * @date 2020/10/20 17:08
 */
@Mapper
public interface UserMapper {
    /**
     * 创建用户信息
     *
     * @param user 实体
     * @return int
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert(" INSERT INTO users ( real_name ,name ,password ,mobile,age ,enabled_status ,created_at ,active) " +
            " VALUES ( #{realName} ,#{name} ,#{password} ,#{mobile},#{age} ,#{enabledStatus} ,NOW() ,1) ")
    int createUser(User user);

    /**
     * 更新用户信息
     *
     * @param user 实体
     * @return int
     */
    @Update(" UPDATE users SET real_name=#{realName},name=#{name},mobile=#{mobile},password=#{password},age=#{age},enabled_status=#{enabledStatus} WHERE id=#{id} ")
    int updateUser(User user);

    /**
     * 删除用户信息
     *
     * @param id 唯一标识id
     * @return int
     */
    @Update("UPDATE users SET active=0 WHERE id=#{id}")
    int deleteUser(@Param("id") long id);

    /**
     * 按id查找用户信息
     *
     * @param name 账号
     * @return User
     */
    @Select("SELECT id,real_name,name,password,mobile,age,enabled_status,created_at,active FROM users " +
            "WHERE name=#{name} AND active=1 LIMIT 1 ")
    User getUserByName(@Param("name") String name);

    /**
     * 按id查找用户信息
     *
     * @param mobile 手机号
     * @return User
     */
    @Select("SELECT id,real_name,name,password,mobile,age,enabled_status,created_at,active FROM users " +
            "WHERE mobile=#{mobile} AND active=1 LIMIT 1 ")
    User getUserByMobile(@Param("mobile") String mobile);

    /**
     * 查找所有用户信息
     *
     * @return User 集合
     */
    @Select("SELECT id,real_name,name,mobile,password,age,enabled_status,created_at,active FROM users ORDER BY id DESC ")
    List<User> getUserList();
}

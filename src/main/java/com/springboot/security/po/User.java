package com.springboot.security.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

/**
 * @author qizenan
 * @date 2020/10/20 17:08
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Alias("User")
public class User {
    /**
     * 唯一标识
     */
    private Long id;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 账号
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 企业状态：0 启用，1 停用
     */
    private Integer enabledStatus;
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 删除标识：0删除，1存活
     */
    private Integer active;
}

package com.springboot.security.form;

import com.springboot.security.po.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qizenan
 * @date 2020/10/20 17:08
 */
@ApiModel(value = "UserForm", description = "用户信息")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserForm {
    @ApiModelProperty(value = "用户信息ID")
    private long userId;
    @ApiModelProperty(value = "姓名")
    private String realName;
    @ApiModelProperty(value = "账号")
    private String name;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "年龄")
    private int age;

    public User toUser() {
        User createUser = User.builder()
                .id(this.userId)
                .realName(this.realName == null ? "" : this.realName)
                .name(this.name == null ? "" : this.name)
                .password(this.password == null ? "" : this.password)
                .age(this.age)
                .build();
        return createUser;
    }
}

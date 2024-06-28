package com.zrh.service;

import com.zrh.vo.Tree;
import com.zrh.dto.UpdatePasswordDto;
import com.zrh.dto.User;

import java.util.List;

public interface UserService {
    /**
     * 根据level和id拿到用户信息
     *
     * @param level
     * @param id
     * @return
     */
    User getUserByLevelAndId(String level, String id);

    /**
     * 生成token
     *
     * @param user user信息
     * @param time 过期时间
     * @return
     */
    String getToken(User user, long time);

    /**
     * 生成token，时间为1小时
     *
     * @param user user信息
     * @return
     */
    String getToken(User user);

    /**
     * 生成token，时间为1天
     *
     * @param user user信息
     * @return
     */
    String getRefreshToken(User user);

    /**
     * 拿到用户信息
     *
     * @param username
     * @param password
     * @param level
     * @return
     */
    User getUserInfo(String username, String password, String level);

    /**
     * 获取深色模式
     *
     * @return
     */
    boolean getSilent();

    /**
     * 更改深色模式
     *
     * @param state
     * @return
     */
    boolean setSilent(Integer state);

    /**
     * 更改密码
     *
     * @param updatePasswordDto
     * @return
     */
    boolean updatePassword(UpdatePasswordDto updatePasswordDto);

    /**
     * 获取树形列表
     *
     * @return
     */
    List<Tree> getTree();
}

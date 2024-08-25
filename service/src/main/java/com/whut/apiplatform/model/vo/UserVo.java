package com.whut.apiplatform.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import javafx.scene.control.Tab;
import lombok.Data;

import java.util.Date;

/**
 * @author whut2024
 * @since 2024-08-25
 */
@Data
public class UserVo {


    private Long id;


    private String userName;


    private String userAvatar;


    private String userAccount;


    private Date createTime;


    private Date updateTime;
}

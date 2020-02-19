package com.question.admin.domain.entity.exam;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.question.admin.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bt_swiper_config")
public class SwiperConfig extends BaseEntity<SwiperConfig> implements Serializable {

    @NotNull
    private String url;
    @TableField("order_item")
    private Integer orderItem;
    @NotNull
    private Integer status;
    @TableField("created_time")
    private Date createdTime;

    /**
     * 修改时间
     */
    @TableField("modified_time")
    private Date modifiedTime;
}

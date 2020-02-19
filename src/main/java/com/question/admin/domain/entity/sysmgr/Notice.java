package com.question.admin.domain.entity.sysmgr;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.question.admin.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("st_notice")
public class Notice extends BaseEntity<Notice> {

    private static final long serialVersionUID = -4401913568806243090L;
	private String title;
	private String content;
	private Integer status;
	/**
	 * 创建时间
	 */
	@TableField("created_time")
	private Date createdTime;

	/**
	 * 修改时间
	 */
	@TableField("modified_time")
	private Date modifiedTime;
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	public interface Status {
		int DRAFT = 0;
		int PUBLISH = 1;
	}

}

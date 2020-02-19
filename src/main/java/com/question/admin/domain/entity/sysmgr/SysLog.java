package com.question.admin.domain.entity.sysmgr;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
//@AllArgsConstructor
//@Document(collection="syslog")
//@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("st_sys_log")
public class SysLog {
    @TableId(type=IdType.INPUT)
    private String id;
    private String account;
    private String ip;
    //    @Field("requestmethod")
    private String method; //请求方式
    private String url;
    private String uri;
    private String clazz;
    @TableField("method_name")
    private String methodName;
    @TableField("visit_time")
    private Date visitTime;
    @TableField("spend_time")
    private Long spendTime;
    private String params;

    public SysLog() {
    }

    public SysLog(String id, String account, String ip, String method, String url, String uri
            , String clazz, String methodName, Date visitTime, Long spendTime, String params) {
        this.id = id;
        this.account = account;
        this.ip = ip;
        this.method = method;
        this.url = url;
        this.uri = uri;
        this.clazz = clazz;
        this.methodName = methodName;
        this.visitTime = visitTime;
        this.spendTime = spendTime;
        this.params = params;
    }

    //    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
//    private Date startDate;
//    @DateTimeFormat(pattern = "yyyy-MM-ddHH:mm:ss")
//    private Date endDate;

}
package com.question.admin.config.wx;

import com.question.admin.config.QnConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;



/**
 * @author alvis
 */
@ConfigurationProperties(prefix = "system")
@Data
public class SystemConfig {
    private WxConfig wx;
    private QnConfig qn;
}

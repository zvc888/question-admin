package com.question.admin.config;

import com.question.admin.config.shiro.security.SystemLogoutFilter;
import com.question.admin.service.sysmgr.UserService;
import com.question.admin.config.shiro.ShiroFilterProperties;
import com.question.admin.config.shiro.ShiroRealm;
import com.question.admin.config.shiro.cache.ShiroCacheManager;
import com.question.admin.config.shiro.security.JwtFilter;
import com.question.admin.config.shiro.security.JwtProperties;
import com.question.admin.service.common.ISyncCacheService;
import com.question.admin.utils.JedisUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {

//    @Autowired
//    ShiroFilterProperties shiroFilterProperties;

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),
     * 需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public static DefaultAdvisorAutoProxyCreator getLifecycleBeanPostProcessor() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
//        defaultAdvisorAutoProxyCreator.setUsePrefix(true);//
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public DefaultWebSecurityManager  securityManager(ShiroRealm shiroRealm, ShiroCacheManager shiroCacheManager){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();

        securityManager.setRealm(shiroRealm);

        //关闭shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        securityManager.setCacheManager(shiroCacheManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager, JedisUtils jedisUtils, JwtProperties jwtProp, ISyncCacheService syncCacheService, UserService userService) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        // 添加jwt过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", new JwtFilter(jwtProp,syncCacheService,jedisUtils,userService));
        filterMap.put("logout", new SystemLogoutFilter(jedisUtils));
        shiroFilter.setFilters(filterMap);

        //动态配置拦截器注入
        Map<String, String> filterRuleMap = new HashMap<>(16);

        List<Map<String, String>> perms = this.getShiroFilterProperties().getPerms();
        perms.forEach(perm -> filterRuleMap.put(perm.get("key"), perm.get("value")));


        shiroFilter.setFilterChainDefinitionMap(filterRuleMap);

        return shiroFilter;
    }

    @Bean
    public ShiroFilterProperties getShiroFilterProperties(){
        return new ShiroFilterProperties();
    }

}

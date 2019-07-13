package com.hui.SpringBoot22;

import com.hui.SpringBoot22.realm.MyJdbcRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.sql.DataSource;
import java.util.*;

@Configuration
public class ShiroConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //配置login页、登陆成功页、没有权限页
        shiroFilterFactoryBean.setLoginUrl("/");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        //配置访问权限(顺序执行拦截)
        //   “/**” 放到最下面，如果将("/**","authc")放到("/userLogin","anon")的上面
        //          则“/userLogin”可能会被拦截
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/logout","logout");
        filterChainDefinitionMap.put("/userLogin","anon");
        filterChainDefinitionMap.put("/403","roles");
        filterChainDefinitionMap.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean(name = "securityManager")
    public SecurityManager securityManager(@Qualifier("myJdbcRealm")MyJdbcRealm myJdbcRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myJdbcRealm);
        return securityManager;
    }

    @Bean(name = "myJdbcRealm")
    public MyJdbcRealm myJdbcRealm(@Qualifier("credentialsMatcher") HashedCredentialsMatcher credentialsMatcher,
                                 @Qualifier("dataSource") DataSource dataSource){
        MyJdbcRealm myJdbcRealm = new MyJdbcRealm();
        //打开shiro的权限  (默认为false)  (不开启则不会检查权限 --> 点击“修改”，不管有没有权限都能进行跳转)
        myJdbcRealm.setPermissionsLookupEnabled(true);
        //设置datasource
        myJdbcRealm.setDataSource(dataSource);
        //设置密码加密器
        myJdbcRealm.setCredentialsMatcher(credentialsMatcher);
        //设置登陆验证sql语句
        String sql = "select password from test_user where username = ?";
        myJdbcRealm.setAuthenticationQuery(sql);
        //设置权限验证sql语句
        String permissionSql = "select permission from permissions where role_name = ?";
        myJdbcRealm.setPermissionsQuery(permissionSql);
        return myJdbcRealm;
    }

    //设置加密算法为MD5。加密次数为1
    @Bean(name = "credentialsMatcher")
    public HashedCredentialsMatcher credentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(1);
        return credentialsMatcher;
    }

    /**
     * 开启aop注解支持 -- 借助SpringAOP扫描使用shiro注解的类
     *      (不开启则不能扫描到shiro的@RequiresPermissions等注解)
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    //配置无权限异常处理，跳转到403
    @Bean(name="simpleMappingExceptionResolver")
    public SimpleMappingExceptionResolver
    createSimpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
        Properties mappings = new Properties();
        mappings.setProperty("DatabaseException", "databaseError");//数据库异常处理
        mappings.setProperty("UnauthorizedException", "403");
        r.setExceptionMappings(mappings);  // None by default
        r.setDefaultErrorView("error");    // No default
        r.setExceptionAttribute("ex");     // Default is "exception"
        return r;
    }

}

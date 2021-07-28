package com.simple.mybatis.config;

import com.simple.mybatis.constant.DBConstants;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan(basePackages = "com.simple.mybatis.mapper.orders", sqlSessionTemplateRef = "ordersSqlSessionTemplate")
public class MyBatisOrdersConfig {
    /**
     * 创建 orders 数据源
     */
    @Bean(name = "orderDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.orders")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 创建 MyBatis SqlSessionFactory
     */
    @Bean(name = "ordersSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        // 设置 orders 数据源
        bean.setDataSource(this.dataSource());
        // 设置 entity 所在包
        bean.setTypeAliasesPackage("com.simple.mybatis.entity");
        // 设置 config 路径
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
        // 设置 mapper 路径
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/orders/*.xml"));
        return bean.getObject();
    }


    /**
     * 创建 MyBatis SqlSessionTemplate
     */
    @Bean(name = "ordersSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate() throws Exception {
        return new SqlSessionTemplate(this.sqlSessionFactory());
    }

    /**
     * 创建 orders 数据源的 TransactionManager 事务管理器
     */
    @Bean(name = DBConstants.TX_MANAGER_ORDERS)
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(this.dataSource());
    }

}

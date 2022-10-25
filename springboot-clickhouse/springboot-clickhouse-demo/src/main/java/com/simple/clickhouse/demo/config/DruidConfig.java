package com.simple.clickhouse.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DruidConfig {

    @Configuration
    @MapperScan(value = "com.simple.clickhouse.demo.mapper.clickhouse", sqlSessionTemplateRef = "clickhouseSqlSessionTemplate")
    public class ClickHouseDataSourceConfig {

        @Bean
        @ConfigurationProperties("spring.datasource.click")
        public DataSource clickhouseDataSource() {
            return new DruidDataSource();
        }

        @Bean
        public SqlSessionFactory clickhouseSqlSessionFactory() throws Exception {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(clickhouseDataSource());
            sqlSessionFactoryBean.setTypeAliasesPackage("com.simple.clickhouse.demo.pojo");
            return sqlSessionFactoryBean.getObject();
        }

        @Bean
        public SqlSessionTemplate clickhouseSqlSessionTemplate() throws Exception {
            return new SqlSessionTemplate(clickhouseSqlSessionFactory());
        }

        @Bean
        public PlatformTransactionManager clickhouseTransactionManager() {
            return new DataSourceTransactionManager(clickhouseDataSource());
        }
    }

    @Configuration
    @MapperScan(value = "com.simple.clickhouse.demo.mapper.mysql", sqlSessionTemplateRef = "mysqlSqlSessionTemplate")
    public static class MysqlDataSourceConfig {

        @Bean
        @ConfigurationProperties("spring.datasource.order")
        public DataSource mysqlDataSource() {
            return new DruidDataSource();
        }

        @Bean
        public SqlSessionFactory mysqlSqlSessionFactory() throws Exception {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(mysqlDataSource());
            sqlSessionFactoryBean.setTypeAliasesPackage("com.simple.clickhouse.demo.pojo");
            return sqlSessionFactoryBean.getObject();
        }

        @Bean
        public SqlSessionTemplate mysqlSqlSessionTemplate() throws Exception {
            return new SqlSessionTemplate(mysqlSqlSessionFactory());
        }

        @Bean
        public PlatformTransactionManager mysqlTransactionManager() {
            return new DataSourceTransactionManager(mysqlDataSource());
        }
    }

}

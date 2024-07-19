package com.bonjour.practice.common.mabtis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
@MapperScan({"com.bonjour.practice.**.mapper"})
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusSqlInjector mySqlInjector(){
        return new MybatisPlusSqlInjector();
    }
    /**
     * 全局配置 -逻辑删除，自动填充
     */
    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig=new GlobalConfig();
//        globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setIdType(IdType.INPUT);
        dbConfig.setLogicDeleteValue("0");
        dbConfig.setLogicNotDeleteValue("1");
        globalConfig.setDbConfig(dbConfig);
        globalConfig.setSqlInjector(mySqlInjector());
        return globalConfig;
    }
    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

//	/**
//     * SQL执行效率插件
//     */
//    @Bean
//    @Profile({"dev","test"})// 设置 dev test 环境开启
//    public PerformanceInterceptor performanceInterceptor() {
//        return new PerformanceInterceptor();
//    }

//    @Bean(name = "masterDb")
//    @ConfigurationProperties(prefix = "spring.datasource.druid.master-db")
//    public DataSource masterDb() {
//        return DruidDataSourceBuilder.create().build();
//    }

//    @Bean(name = "jycoreDb")
//    @ConfigurationProperties(prefix = "spring.datasource.druid.jycore-db")
//    public DataSource mysqlDb() {
//        return DruidDataSourceBuilder.create().build();
//    }

    /**
     * 动态数据源配置
     *
     * @return
     */
   /* @Bean
    @Primary
    public DynamicDataSource dynamicDataSource(@Qualifier("masterDb") DataSource masterDb){
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DBTypeEnum.Master, masterDb);
//        targetDataSources.put(DBTypeEnum.JyCore,jycoreDb);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(masterDb);
        return dynamicDataSource;
    }*/

//    @Bean("sqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactory(DynamicDataSource dynamicDataSource) throws Exception {
//        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
//        sqlSessionFactory.setDataSource(dynamicDataSource);
//        // sqlSessionFactory.setMapperLocations(new
//        // PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*/*Mapper.xml"));
//        sqlSessionFactory.setGlobalConfig(globalConfig());
//        MybatisConfiguration configuration = new MybatisConfiguration();
//        // configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
//        configuration.setJdbcTypeForNull(JdbcType.NULL);
//        configuration.setMapUnderscoreToCamelCase(true);
//        configuration.setCacheEnabled(false);
//        sqlSessionFactory.setConfiguration(configuration);
//        sqlSessionFactory.setPlugins(new Interceptor[]{ // performanceInterceptor(),OptimisticLockerInterceptor()
//                paginationInterceptor() // 添加分页功能
//        });
//        return sqlSessionFactory.getObject();
//    }
}

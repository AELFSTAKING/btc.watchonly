package io.seg.kofo.bitcoinwo.common.config;

import io.seg.framework.dao.BaseDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author dongweizhao
 * @Date: 2018/8/14 上午11:48
 */
@Slf4j
@Configuration
public class DataSourceConfig {

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;
    @Value("${mybatis.config-locations}")
    private String configLocation;

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("dataSource") DataSource dataSource) {
        try {
            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSource);
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
            bean.setConfigLocation(new DefaultResourceLoader().getResource(configLocation));
            return bean.getObject();
        } catch (Exception e) {
            log.error("set sqlSessionFactoryBean error", e);
            throw new RuntimeException(e);
        }
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(initMethod = "initPropertyColumnMapper")
    public BaseDao baseDao(@Qualifier("sqlSessionTemplate") SqlSessionTemplate sessionTemplate) {
        return new BaseDao(sessionTemplate);
    }


    @Bean(name = "annotationDrivenTransactionManager")
    public PlatformTransactionManager annotationDrivenTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}

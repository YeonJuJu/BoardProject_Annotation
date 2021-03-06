package com.juju.spring.config;

import javax.annotation.Resource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.juju.spring.dto.UserDTO;
import com.juju.spring.interceptor.CheckLoginInterceptor;
import com.juju.spring.interceptor.CheckWriterInterceptor;
import com.juju.spring.interceptor.TopMenuInterceptor;
import com.juju.spring.mapper.BoardMapper;
import com.juju.spring.mapper.TopMenuMapper;
import com.juju.spring.mapper.UserMapper;
import com.juju.spring.service.BoardService;
import com.juju.spring.service.TopMenuService;

/* XML방식의 servlet-context.xml 역할을 하는 파일 : spring mvc 설정 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.juju.spring.controller", "com.juju.spring.service", "com.juju.spring.dao"})
@PropertySource("/WEB-INF/properties/db.properties")
public class ServletAppContext implements WebMvcConfigurer{
	
	// DB 관련 설정
	
	@Value("${db.classname}")
	private String db_classname;
		
	@Value("${db.url}")
	private String db_url;
		
	@Value("${db.username}")
	private String db_username;
		
	@Value("${db.password}")
	private String db_password;
	
	// Interceptor 등록하기 위한 객체
	
	@Autowired
	private TopMenuService topMenuService;
		
	@Resource(name="loginUserDTO")
	private UserDTO loginUserDTO;
	
	@Autowired
	private BoardService boardService;
	
	// Controller 에서 return 하는 경로에 접두사와 접미사를 설정
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		WebMvcConfigurer.super.configureViewResolvers(registry);
		registry.jsp("/WEB-INF/view/", ".jsp");
	}
	
	// 프로젝트에서 사용하는 정적 파일 경로 설정 (이미지, 사운드, 동영상, js, css 등)
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/**").addResourceLocations("/resources/");
	}

	// DB 접속 정보 관리 객체 Bean
	@Bean
	public BasicDataSource dataSource() {
		
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName(db_classname);
		source.setUrl(db_url);
		source.setUsername(db_username);
		source.setPassword(db_password);
		
		return source;
	}
	
	// Query문과 접속정보 관리 객체 Bean
	@Bean
	public SqlSessionFactory factory(BasicDataSource source) throws Exception{
		
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(source);
		
		SqlSessionFactory factory = factoryBean.getObject();
		
		return factory;
	}
	
	// Query문 실행 객체 (Mapper 관리)
	
	// 1. BoardMapper 등록
	@Bean
	public MapperFactoryBean<BoardMapper> getBoardMapper(SqlSessionFactory factory) throws Exception{
		
		MapperFactoryBean<BoardMapper> factoryBean = new MapperFactoryBean<BoardMapper>(BoardMapper.class);
		factoryBean.setSqlSessionFactory(factory);

		return factoryBean;
	}
	
	// 2. TopMenuMapper 등록
	@Bean
	public MapperFactoryBean<TopMenuMapper> getTopMenuMapper(SqlSessionFactory factory) throws Exception{
		
		MapperFactoryBean<TopMenuMapper> factoryBean = new MapperFactoryBean<TopMenuMapper>(TopMenuMapper.class);
		factoryBean.setSqlSessionFactory(factory);

		return factoryBean;
	}
	
	// 3. UserMapper 등록
	@Bean
	public MapperFactoryBean<UserMapper> getUserMapper(SqlSessionFactory factory) throws Exception {

		MapperFactoryBean<UserMapper> factoryBean = new MapperFactoryBean<UserMapper>(UserMapper.class);
		factoryBean.setSqlSessionFactory(factory);

		return factoryBean;
	}
	
	// Interceptor 등록
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		WebMvcConfigurer.super.addInterceptors(registry);
		
		TopMenuInterceptor topMenuInterceptor = new TopMenuInterceptor(topMenuService, loginUserDTO);
		CheckLoginInterceptor checkLoginInterceptor = new CheckLoginInterceptor(loginUserDTO);
		CheckWriterInterceptor checkWriterInterceptor = new CheckWriterInterceptor(loginUserDTO, boardService);
		
		InterceptorRegistration reg1 = registry.addInterceptor(topMenuInterceptor);
		InterceptorRegistration reg2 = registry.addInterceptor(checkLoginInterceptor);
		InterceptorRegistration reg3 = registry.addInterceptor(checkWriterInterceptor);
		
		// "/**" 로 설정했기 때문에 모든 요청 주소에 대해 interceptor 작동
		reg1.addPathPatterns("/**");
		reg2.addPathPatterns("/user/modify", "/user/logout", "/board/*");
		reg2.excludePathPatterns("/board/main");
		reg3.addPathPatterns("/board/modify", "/board/delete");
	}
	
	// properties 폴더 안에 있는 파일들이 충돌되지 않도록 개별관리하는 함수
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	// 오류 메시지 등록
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		
		ReloadableResourceBundleMessageSource res = new ReloadableResourceBundleMessageSource();
		res.setBasenames("/WEB-INF/properties/error_message");
		
		return res;
	}
	
	// 데이터 전송 관련
	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
}



















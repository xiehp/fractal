package xie.fractal.web.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.annotation.Resource;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;
import xie.fractal.web.interceptor.TimeInterceptor;

@Configuration
@Slf4j
public class AppWebMvcConfigurer implements WebMvcConfigurer {

	/**
	 * ObjectMapper配置
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer customJackson() {
		return jacksonObjectMapperBuilder -> {
			// jacksonObjectMapperBuilder.serializationInclusion(Include.NON_NULL);
			// jacksonObjectMapperBuilder.failOnUnknownProperties(false);
			// jacksonObjectMapperBuilder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
			// jacksonObjectMapperBuilder.dateFormat(new SimpleDateFormat("yyyy-MM-dd
			// HH:mm:ss"));
			jacksonObjectMapperBuilder.failOnEmptyBeans(false);
		};
	}

	// @Bean
	// // TODO 没有效果？？？
	// public ConfigurableWebBindingInitializer configurableWebBindingInitializer()
	// {
	// return new AppWebBindingInitializer();
	// }

	@Bean
	// TODO 没有效果？？？
	public ConfigurableWebBindingInitializer configurableWebBindingInitializer(
			FormattingConversionService mvcConversionService, Validator mvcValidator) {
		ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
		initializer.setConversionService(mvcConversionService);
		initializer.setValidator(mvcValidator);
		// 装配自定义属性编辑器
		initializer.setPropertyEditorRegistrar(propertyEditorRegistry -> {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dateFormat.setLenient(false);
			log.info("dateFormat: {}", dateFormat);
			propertyEditorRegistry.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
			log.info("Date: {}", CustomDateEditor.class);
			propertyEditorRegistry.registerCustomEditor(String.class, new StringTrimmerEditor(true));
			log.info("String: {}", StringTrimmerEditor.class);
		});
		return initializer;
	}

	@Resource
	private TimeInterceptor timeWatchInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(timeWatchInterceptor).addPathPatterns("/**");
		// registry.addWebRequestInterceptor(new
		// BaseWebRequestInterceptor()).addPathPatterns("/**");
	}

}
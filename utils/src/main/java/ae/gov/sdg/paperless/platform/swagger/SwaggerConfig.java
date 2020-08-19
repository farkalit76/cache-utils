package ae.gov.sdg.paperless.platform.swagger;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import ae.gov.sdg.paperless.platform.common.PlatformConfig;
import ae.gov.sdg.paperless.platform.security.AuthenticationInterceptor;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig.java
 * Purpose: Configuration class to dynamically generate service APIs, it is accessible via
 * <br/>link http://{host}:{port}/{context-path}/v2/api-docs
 * 
 * @author Ibrahim Abdel Aziz
 * @version 1.0
 *
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
@Import({ springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class })
public class SwaggerConfig implements WebMvcConfigurer{
	
	@Autowired
	private PlatformConfig serviceConfig;

    /**
     * Create Docket document which is responsible to render service API on Swagger UI.
     * 
     * @return Docket document.
     */
    @Bean
    public Docket journeyApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(getGlobalParams())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ae.gov.sdg.service"))
                .paths(regex(serviceConfig.getSwaggerRegex()))
                .build()
                .apiInfo(metadata())
                .forCodeGeneration(true);

    }
    
    private List<Parameter> getGlobalParams() {
        return Arrays.asList(new ParameterBuilder()
                    .name(AuthenticationInterceptor.HEADER_AUTHORIZATION_DUBAINOW_JWT)
                    .description("JWT authentictaion header")
                    .modelRef(new ModelRef("string"))
                    .parameterType("header")
                    .required(true)
                    .build());
    }

    /**
     * Determine where is the location that Swagger UI depends on
     */
    @Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * Create default meta information for services documentation.
     * 
     * @return API Meta-Info
     */
    private ApiInfo metadata() {

        final ApiInfo info = new ApiInfo("REST API Docs",
                "Microservice API", "1.0", "urn:tos",
                ApiInfo.DEFAULT_CONTACT, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0",
                Collections.emptyList());

        return info;
    }

}
package com.ssafy.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("EnjoyTrip 명세서")
                .description("<h3>SSAFY12_광주5_관통_Framework_10팀_ 정찬환_김의현 TripAPI Reference for Developers</h3>Swagger를 이용한 Trip API<br><img src=\"/assets/img/images.png\" width=\"150\"><br>")
                .version("v1")
                .contact(new Contact()
                        .name("정찬환/김의현")
                        .email("keh0885@gmail.com")
                        .url("http://edu.ssafy.com"));

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("member")
                .pathsToMatch("/member/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Member API Documentation")
                        .description("회원 관련 API 문서")
                        .version("1.0"));
    }

    @Bean
    public GroupedOpenApi tripApi() {
        return GroupedOpenApi.builder()
                .group("trip")
                .pathsToMatch("/trip/**")
                .build();
    }
    
    @Bean
    public GroupedOpenApi aiApi() {
        return GroupedOpenApi.builder()
                .group("ai")
                .pathsToMatch("/ai/**")
                .build();
    }
    
    @Bean
    public GroupedOpenApi boardApi() {
        return GroupedOpenApi.builder()
                .group("board")
                .pathsToMatch("/board/**")
                .build();
    }

    @Bean
    public GroupedOpenApi tourPlanApi() {
        return GroupedOpenApi.builder()
                .group("tour-plan")
                .pathsToMatch("/tour-plan/**")
                .build();
    }

    @Bean
    public GroupedOpenApi carRentalCompanyApi() {
        return GroupedOpenApi.builder()
                .group("car-rental-company")
                .pathsToMatch("/car-rental/**")
                .build();
    }

    @Bean
    public GroupedOpenApi tourMatchApi() {
        return GroupedOpenApi.builder()
                .group("tour-match")
                .pathsToMatch("/tour-match/**")
                .build();
    }

    @Bean
    public GroupedOpenApi CommentApi() {
        return GroupedOpenApi.builder()
                .group("Comment")
                .pathsToMatch("/comment/**")
                .build();
    }

    @Bean
    public GroupedOpenApi EmailApi() {
        return GroupedOpenApi.builder()
                .group("Email")
                .pathsToMatch("/email/**")
                .build();
    }

    @Bean
    public GroupedOpenApi chatApi() {
        return GroupedOpenApi.builder()
                .group("Chat")
                .pathsToMatch("/chat/**")
                .build();
    }

    @Bean
    public GroupedOpenApi fileApi() {
        return GroupedOpenApi.builder()
                .group("file")
                .pathsToMatch("/file/**")
                .build();
    }
}

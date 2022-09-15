package com.nnit.pb.swagger2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {
//    @Bean
//    public Docket pdaApi() {
//        List<Parameter> parameters = new ArrayList<>();
//        parameters.add(new ParameterBuilder()
//                .name("token")
//                .description("认证token")
//                .modelRef(new ModelRef("string"))
//                .parameterType("header")
//                .required(false)
//                .build());
//        return new Docket(DocumentationType.SWAGGER_2)               
//                .globalOperationParameters(parameters)
//                .select()
//                .build();
//    }


}

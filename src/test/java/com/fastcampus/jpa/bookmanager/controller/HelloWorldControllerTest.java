package com.fastcampus.jpa.bookmanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class) // JPA를 마치 있는 거 처럼 막아주는 방법
class HelloWorldControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void helloWorld() throws Exception {
        mockMvc.perform(get("/hello-world"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("hello world!"));
    }
}
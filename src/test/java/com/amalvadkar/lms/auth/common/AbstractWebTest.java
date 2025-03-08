package com.amalvadkar.lms.auth.common;

import com.amalvadkar.lms.auth.ApplicationProperties;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@Tag("web")
public class AbstractWebTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected ApplicationProperties appProps;


}

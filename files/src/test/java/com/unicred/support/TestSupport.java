package com.unicred.support;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class TestSupport {
    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }
}

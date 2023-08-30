package com.unicred.support;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public class TestSupport {
    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }
}

package com.innobank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
public class ModularityTests {

    @Test
    void verifiesModuleStructure() {
        ApplicationModules.of(InnobankApplication.class).verify();
    }
}

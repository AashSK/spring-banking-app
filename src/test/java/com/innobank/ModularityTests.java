package com.innobank;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ModularityTests {

    @Test
    void verifiesModuleStructure() {
        ApplicationModules.of(InnobankApplication.class).verify();
    }
}

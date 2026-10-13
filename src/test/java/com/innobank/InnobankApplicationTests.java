package com.innobank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(PgTestConfiguration.class)
class InnobankApplicationTests {

	@Test
	void contextLoads() {
	}

}

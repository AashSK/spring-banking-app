package com.innobank.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.innobank.PgTestConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PgTestConfiguration.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateValidCustomer() throws Exception {
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                    "firstName": "John",
                                    "lastName": "Doe",
                                    "email": "john@example.com"
                                }
                                """))
                .andExpect(status().isCreated());

        var customer = customerRepository.findByEmail("john@example.com")
                .orElseThrow(() -> new AssertionError("Customer was not persisted"));

        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("john@example.com", customer.getEmail());
    }

    @Test
    void shouldRejectInvalidFirstname() throws Exception {
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        """
                                {
                                    "firstName": "",
                                    "lastName": "Doe",
                                    "email": "john@example.com"
                                }
                                """))
                .andExpect(status().isBadRequest());

        assertFalse(customerRepository.findByEmail("john@example.com").isPresent());
    }

    @Test
    void shouldRejectInvalidEmail() throws Exception {
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "firstName": "John",
                            "lastName": "Doe",
                            "email": "not-an-email"
                        }
                        """))
                .andExpect(status().isBadRequest());
        assertEquals(0, customerRepository.count());
    }

    @Test
    void shouldRejectDuplicateEmail() throws Exception {
        String json = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john@example.com"
                }
                """;

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isConflict());

        assertEquals(1, customerRepository.count());
    }

}
package com.outsera.raspberry;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "app.csv.path=classpath:datasets/no-multiwinner.csv")
class EmptyScenarioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnEmptyListsWhenNoProducerHasTwoWins() throws Exception {
        mockMvc.perform(get("/api/producers/award-intervals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.min.length()").value(0))
                .andExpect(jsonPath("$.max.length()").value(0));
    }
}

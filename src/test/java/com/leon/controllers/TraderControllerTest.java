package com.leon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.models.Trader;
import com.leon.services.TraderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TraderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TraderService traderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void reconfigure_shouldCallServiceReconfigure() throws Exception {
        mockMvc.perform(get("/trader/reconfigure"))
                .andExpect(status().isNoContent());
        verify(traderService, times(1)).reconfigure();
    }

    @Test
    public void getAllTraders_shouldReturnListOfTraders() throws Exception {
        List<Trader> traders = new ArrayList<>();
        Trader trader1 = new Trader();
        trader1.setTraderId(UUID.randomUUID());
        trader1.setFirstName("John");
        trader1.setLastName("Doe");
        Trader trader2 = new Trader();
        trader2.setTraderId(UUID.randomUUID());
        trader2.setFirstName("Jane");
        trader2.setLastName("Smith");
        traders.add(trader1);
        traders.add(trader2);

        when(traderService.getAllTraders()).thenReturn(traders);

        mockMvc.perform(get("/trader"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));
    }

    @Test
    public void createTrader_withValidData_shouldCreateTrader() throws Exception {
        Trader trader = new Trader();
        trader.setTraderId(UUID.randomUUID());
        trader.setFirstName("New");
        trader.setLastName("Trader");
        when(traderService.createTrader(any(Trader.class))).thenReturn(trader);

        mockMvc.perform(post("/trader")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trader)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("New"))
                .andExpect(jsonPath("$.lastName").value("Trader"));
    }

    @Test
    public void createTrader_withNullTrader_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/trader")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createTrader_withEmptyName_shouldReturnBadRequest() throws Exception {
        Trader trader = new Trader();
        trader.setTraderId(UUID.randomUUID());
        trader.setFirstName("");
        trader.setLastName("");

        mockMvc.perform(post("/trader")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trader)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTrader_withValidData_shouldUpdateTrader() throws Exception {
        Trader trader = new Trader();
        trader.setTraderId(UUID.randomUUID());
        trader.setFirstName("Updated");
        trader.setLastName("Trader");
        when(traderService.updateTrader(any(Trader.class))).thenReturn(trader);

        mockMvc.perform(put("/trader")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trader)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Trader"));
    }

    @Test
    public void updateTrader_withNullTrader_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/trader")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateTrader_withEmptyName_shouldReturnBadRequest() throws Exception {
        Trader trader = new Trader();
        trader.setTraderId(UUID.randomUUID());
        trader.setFirstName("");
        trader.setLastName("");

        mockMvc.perform(put("/trader")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trader)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteTrader_withEmptyId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/trader/")
                .param("traderId", ""))
                .andExpect(status().isBadRequest());
    }
} 
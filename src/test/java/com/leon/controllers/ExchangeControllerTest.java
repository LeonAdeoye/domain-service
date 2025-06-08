package com.leon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.models.Exchange;
import com.leon.services.ExchangeService;
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
public class ExchangeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeService exchangeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void reconfigure_shouldCallServiceReconfigure() throws Exception {
        mockMvc.perform(get("/exchange/reconfigure"))
                .andExpect(status().isNoContent());
        verify(exchangeService, times(1)).reconfigure();
    }

    @Test
    public void getAll_shouldReturnListOfExchanges() throws Exception {
        List<Exchange> exchanges = new ArrayList<>();
        Exchange exchange1 = new Exchange();
        exchange1.setExchangeId(UUID.randomUUID());
        exchange1.setExchangeName("Exchange 1");
        Exchange exchange2 = new Exchange();
        exchange2.setExchangeId(UUID.randomUUID());
        exchange2.setExchangeName("Exchange 2");
        exchanges.add(exchange1);
        exchanges.add(exchange2);

        when(exchangeService.getAll()).thenReturn(exchanges);

        mockMvc.perform(get("/exchange"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].exchangeName").value("Exchange 1"))
                .andExpect(jsonPath("$[1].exchangeName").value("Exchange 2"));
    }

    @Test
    public void createExchange_withValidData_shouldCreateExchange() throws Exception {
        Exchange exchange = new Exchange();
        exchange.setExchangeId(UUID.randomUUID());
        exchange.setExchangeName("New Exchange");
        when(exchangeService.createExchange(any(Exchange.class))).thenReturn(exchange);

        mockMvc.perform(post("/exchange")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exchange)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.exchangeName").value("New Exchange"));
    }

    @Test
    public void createExchange_withNullExchange_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/exchange")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createExchange_withEmptyName_shouldReturnBadRequest() throws Exception {
        Exchange exchange = new Exchange();
        exchange.setExchangeId(UUID.randomUUID());
        exchange.setExchangeName("");

        mockMvc.perform(post("/exchange")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exchange)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteExchange_withValidId_shouldDeleteExchange() throws Exception {
        String exchangeId = UUID.randomUUID().toString();
        doNothing().when(exchangeService).deleteExchange(exchangeId);

        mockMvc.perform(delete("/exchange/" + exchangeId))
                .andExpect(status().isNoContent());
        verify(exchangeService, times(1)).deleteExchange(exchangeId);
    }
} 
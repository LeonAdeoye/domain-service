package com.leon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.models.Broker;
import com.leon.services.BrokerService;
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
public class BrokerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrokerService brokerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void reconfigure_shouldCallServiceReconfigure() throws Exception {
        mockMvc.perform(get("/broker/reconfigure"))
                .andExpect(status().isNoContent());
        verify(brokerService, times(1)).reconfigure();
    }

    @Test
    public void getAll_shouldReturnListOfBrokers() throws Exception {
        List<Broker> brokers = new ArrayList<>();
        Broker broker1 = new Broker("BRK1", "Broker 1");
        Broker broker2 = new Broker("BRK2", "Broker 2");
        brokers.add(broker1);
        brokers.add(broker2);

        when(brokerService.getAll()).thenReturn(brokers);

        mockMvc.perform(get("/broker"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].brokerAcronym").value("BRK1"))
                .andExpect(jsonPath("$[1].brokerAcronym").value("BRK2"));
    }

    @Test
    public void createBroker_withValidData_shouldCreateBroker() throws Exception {
        Broker broker = new Broker("BRK1", "Broker 1");
        when(brokerService.createBroker(any(Broker.class))).thenReturn(broker);

        mockMvc.perform(post("/broker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(broker)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brokerAcronym").value("BRK1"))
                .andExpect(jsonPath("$.brokerDescription").value("Broker 1"));
    }

    @Test
    public void createBroker_withNullId_shouldReturnBadRequest() throws Exception {
        Broker broker = new Broker("BRK1", "Broker 1");
        broker.setBrokerId(null);

        mockMvc.perform(post("/broker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(broker)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createBroker_withEmptyAcronym_shouldReturnBadRequest() throws Exception {
        Broker broker = new Broker("", "Broker 1");

        mockMvc.perform(post("/broker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(broker)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateBroker_withValidData_shouldUpdateBroker() throws Exception {
        Broker broker = new Broker("BRK1", "Updated Broker");
        when(brokerService.updateBroker(any(Broker.class))).thenReturn(broker);

        mockMvc.perform(put("/broker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(broker)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brokerAcronym").value("BRK1"))
                .andExpect(jsonPath("$.brokerDescription").value("Updated Broker"));
    }

    @Test
    public void updateBroker_withNullId_shouldReturnBadRequest() throws Exception {
        Broker broker = new Broker("BRK1", "Broker 1");
        broker.setBrokerId(null);

        mockMvc.perform(put("/broker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(broker)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateBroker_withNonExistentBroker_shouldReturnNotFound() throws Exception {
        Broker broker = new Broker("BRK1", "Broker 1");
        when(brokerService.updateBroker(any(Broker.class))).thenReturn(null);

        mockMvc.perform(put("/broker")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(broker)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteBroker_withValidId_shouldDeleteBroker() throws Exception {
        String brokerId = UUID.randomUUID().toString();
        doNothing().when(brokerService).deleteBroker(brokerId);

        mockMvc.perform(delete("/broker/" + brokerId))
                .andExpect(status().isNoContent());
        verify(brokerService, times(1)).deleteBroker(brokerId);
    }
} 
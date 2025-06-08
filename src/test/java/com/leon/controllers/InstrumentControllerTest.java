package com.leon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.models.AssetType;
import com.leon.models.Currency;
import com.leon.models.Instrument;
import com.leon.models.SettlementType;
import com.leon.services.InstrumentService;
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
public class InstrumentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstrumentService instrumentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void reconfigure_shouldCallServiceReconfigure() throws Exception {
        mockMvc.perform(get("/instrument/reconfigure"))
                .andExpect(status().isNoContent());
        verify(instrumentService, times(1)).reconfigure();
    }

    @Test
    public void getAll_shouldReturnListOfInstruments() throws Exception {
        List<Instrument> instruments = new ArrayList<>();
        Instrument instrument1 = new Instrument();
        instrument1.setInstrumentId(UUID.randomUUID());
        instrument1.setInstrumentCode("INST1");
        Instrument instrument2 = new Instrument();
        instrument2.setInstrumentId(UUID.randomUUID());
        instrument2.setInstrumentCode("INST2");
        instruments.add(instrument1);
        instruments.add(instrument2);

        when(instrumentService.getAll()).thenReturn(instruments);

        mockMvc.perform(get("/instrument"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].instrumentCode").value("INST1"))
                .andExpect(jsonPath("$[1].instrumentCode").value("INST2"));
    }

    @Test
    public void createInstrument_withValidData_shouldCreateInstrument() throws Exception {
        Instrument instrument = new Instrument();
        instrument.setInstrumentId(UUID.randomUUID());
        instrument.setInstrumentCode("NEW_INST");
        instrument.setInstrumentDescription("New Instrument Description");
        instrument.setAssetType(AssetType.STOCK);
        instrument.setBlgCode("BLG123");
        instrument.setRic("RIC123");
        instrument.setLotSize(100);
        instrument.setSettlementCurrency(Currency.HKD);
        instrument.setExchangeAcronym("NYSE");
        instrument.setSettlementType(SettlementType.T_PLUS_ONE);

        when(instrumentService.createInstrument(any(Instrument.class))).thenReturn(instrument);

        mockMvc.perform(post("/instrument")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(instrument)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.instrumentCode").value("NEW_INST"));
    }

    @Test
    public void createInstrument_withInvalidData_shouldReturnBadRequest() throws Exception {
        Instrument instrument = new Instrument();
        instrument.setInstrumentId(UUID.randomUUID());

        mockMvc.perform(post("/instrument")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(instrument)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteInstrument_withValidCode_shouldDeleteInstrument() throws Exception {
        String instrumentId = UUID.randomUUID().toString();
        doNothing().when(instrumentService).deleteInstrument(instrumentId);

        mockMvc.perform(delete("/instrument/" + instrumentId))
                .andExpect(status().isNoContent());
        verify(instrumentService, times(1)).deleteInstrument(instrumentId);
    }
} 
package com.leon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.models.Desk;
import com.leon.services.DeskService;
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
public class DeskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeskService deskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void reconfigure_shouldCallServiceReconfigure() throws Exception {
        mockMvc.perform(get("/desk/reconfigure"))
                .andExpect(status().isNoContent());
        verify(deskService, times(1)).reconfigure();
    }

    @Test
    public void getAll_shouldReturnListOfDesks() throws Exception {
        List<Desk> desks = new ArrayList<>();
        Desk desk1 = new Desk();
        desk1.setDeskId(UUID.randomUUID());
        desk1.setDeskName("Desk 1");
        Desk desk2 = new Desk();
        desk2.setDeskId(UUID.randomUUID());
        desk2.setDeskName("Desk 2");
        desks.add(desk1);
        desks.add(desk2);

        when(deskService.getAllDesks()).thenReturn(desks);

        mockMvc.perform(get("/desk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].deskName").value("Desk 1"))
                .andExpect(jsonPath("$[1].deskName").value("Desk 2"));
    }

    @Test
    public void createDesk_withValidData_shouldCreateDesk() throws Exception {
        Desk desk = new Desk();
        desk.setDeskId(UUID.randomUUID());
        desk.setDeskName("New Desk");
        when(deskService.createDesk(any(Desk.class))).thenReturn(desk);

        mockMvc.perform(post("/desk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(desk)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deskName").value("New Desk"));
    }

    @Test
    public void createDesk_withNullDesk_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/desk")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createDesk_withEmptyName_shouldReturnBadRequest() throws Exception {
        Desk desk = new Desk();
        desk.setDeskId(UUID.randomUUID());
        desk.setDeskName("");

        mockMvc.perform(post("/desk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(desk)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateDesk_withValidData_shouldUpdateDesk() throws Exception {
        Desk desk = new Desk();
        desk.setDeskId(UUID.randomUUID());
        desk.setDeskName("Updated Desk");
        when(deskService.updateDesk(any(Desk.class))).thenReturn(desk);

        mockMvc.perform(put("/desk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(desk)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deskName").value("Updated Desk"));
    }

    @Test
    public void updateDesk_withNullDesk_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/desk")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateDesk_withEmptyName_shouldReturnBadRequest() throws Exception {
        Desk desk = new Desk();
        desk.setDeskId(UUID.randomUUID());
        desk.setDeskName("");

        mockMvc.perform(put("/desk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(desk)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteDesk_withValidId_shouldDeleteDesk() throws Exception {
        String deskId = UUID.randomUUID().toString();
        doNothing().when(deskService).deleteDesk(deskId);

        mockMvc.perform(delete("/desk/" + deskId))
                .andExpect(status().isNoContent());
        verify(deskService, times(1)).deleteDesk(deskId);
    }
} 
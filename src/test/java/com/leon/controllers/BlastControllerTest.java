package com.leon.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.models.Blast;
import com.leon.services.BlastService;
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
public class BlastControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlastService blastService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void reconfigure_shouldCallServiceReconfigure() throws Exception {
        mockMvc.perform(get("/blast/reconfigure"))
                .andExpect(status().isNoContent());
        verify(blastService, times(1)).reconfigure();
    }

    @Test
    public void getBlasts_withValidOwnerId_shouldReturnListOfBlasts() throws Exception {
        String ownerId = UUID.randomUUID().toString();
        List<Blast> blasts = new ArrayList<>();
        Blast blast1 = new Blast();
        blast1.setBlastId(UUID.randomUUID());
        blast1.setOwnerId(ownerId);
        blast1.setBlastName("Blast 1");
        Blast blast2 = new Blast();
        blast2.setBlastId(UUID.randomUUID());
        blast2.setOwnerId(ownerId);
        blast2.setBlastName("Blast 2");
        blasts.add(blast1);
        blasts.add(blast2);

        when(blastService.getBlasts(ownerId)).thenReturn(blasts);

        mockMvc.perform(get("/blast")
                .param("ownerId", ownerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].blastName").value("Blast 1"))
                .andExpect(jsonPath("$[1].blastName").value("Blast 2"));
    }

    @Test
    public void getBlasts_withEmptyOwnerId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/blast")
                .param("ownerId", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveBlast_withValidData_shouldSaveBlast() throws Exception {
        Blast blast = new Blast();
        blast.setBlastId(UUID.randomUUID());
        blast.setOwnerId(UUID.randomUUID().toString());
        blast.setBlastName("New Blast");
        when(blastService.saveBlast(any(Blast.class))).thenReturn(blast);

        mockMvc.perform(post("/blast")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blast)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.blastName").value("New Blast"));
    }

    @Test
    public void saveBlast_withNullBlast_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/blast")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveBlast_withEmptyName_shouldReturnBadRequest() throws Exception {
        Blast blast = new Blast();
        blast.setBlastId(UUID.randomUUID());
        blast.setOwnerId(UUID.randomUUID().toString());
        blast.setBlastName("");

        mockMvc.perform(post("/blast")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blast)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateBlast_withValidData_shouldUpdateBlast() throws Exception {
        Blast blast = new Blast();
        blast.setBlastId(UUID.randomUUID());
        blast.setOwnerId(UUID.randomUUID().toString());
        blast.setBlastName("Updated Blast");
        when(blastService.updateBlast(any(Blast.class))).thenReturn(blast);

        mockMvc.perform(put("/blast")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blast)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.blastName").value("Updated Blast"));
    }

    @Test
    public void updateBlast_withNullBlast_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/blast")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateBlast_withEmptyName_shouldReturnBadRequest() throws Exception {
        Blast blast = new Blast();
        blast.setBlastId(UUID.randomUUID());
        blast.setOwnerId(UUID.randomUUID().toString());
        blast.setBlastName("");

        mockMvc.perform(put("/blast")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blast)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteBlast_withValidIds_shouldDeleteBlast() throws Exception {
        String ownerId = UUID.randomUUID().toString();
        String blastId = UUID.randomUUID().toString();
        doNothing().when(blastService).deleteBlast(ownerId, blastId);

        mockMvc.perform(delete("/blast")
                .param("ownerId", ownerId)
                .param("blastId", blastId))
                .andExpect(status().isNoContent());
        verify(blastService, times(1)).deleteBlast(ownerId, blastId);
    }

    @Test
    public void deleteBlast_withEmptyOwnerId_shouldReturnBadRequest() throws Exception {
        String blastId = UUID.randomUUID().toString();
        mockMvc.perform(delete("/blast")
                .param("ownerId", "")
                .param("blastId", blastId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteBlast_withEmptyBlastId_shouldReturnBadRequest() throws Exception {
        String ownerId = UUID.randomUUID().toString();
        mockMvc.perform(delete("/blast")
                .param("ownerId", ownerId)
                .param("blastId", ""))
                .andExpect(status().isBadRequest());
    }
} 
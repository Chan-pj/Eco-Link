package com.ecolink.backend;

import com.ecolink.backend.entity.Worker;
import com.ecolink.backend.repository.WorkerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WorkerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkerRepository workerRepository;

    private Worker worker;

    @BeforeEach
    void setUp() {
        workerRepository.deleteAll();
        worker = workerRepository.save(new Worker("worker1", "worker1234", 5, "12가3456"));
    }

    @Test
    void 작업자_조회_응답에_비밀번호가_포함되지_않는다() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("worker1"))
                .andExpect(jsonPath("$[0].password").doesNotExist());

        mockMvc.perform(get("/api/users/{id}", worker.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void 존재하지_않는_작업자를_조회하면_404를_반환한다() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 9999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("작업자를 찾을 수 없습니다. id: 9999"));
    }

    @Test
    void 비밀번호를_비우고_수정하면_기존_비밀번호를_유지한다() throws Exception {
        mockMvc.perform(put("/api/users/{id}", worker.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"worker1\",\"vehicleNumber\":\"34나5678\"}"))
                .andExpect(status().isOk());

        Worker updated = workerRepository.findById(worker.getId()).orElseThrow();
        assertThat(updated.getPassword()).isEqualTo("worker1234");
        assertThat(updated.getVehicleNumber()).isEqualTo("34나5678");
    }
}

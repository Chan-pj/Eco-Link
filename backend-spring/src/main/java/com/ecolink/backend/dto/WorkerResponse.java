package com.ecolink.backend.dto;

import com.ecolink.backend.entity.Worker;
import lombok.Getter;

@Getter
public class WorkerResponse {

    private final Long id;
    private final String username;
    private final Integer grade;
    private final String vehicleNumber;

    private WorkerResponse(Worker worker) {
        this.id = worker.getId();
        this.username = worker.getUsername();
        this.grade = worker.getGrade();
        this.vehicleNumber = worker.getVehicleNumber();
    }

    public static WorkerResponse from(Worker worker) {
        return new WorkerResponse(worker);
    }
}

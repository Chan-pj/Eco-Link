package com.ecolink.backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "can_status_log")
@Getter
@NoArgsConstructor
public class CanStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "can_id", nullable = false)
    private TrashCan trashCan;

    @JsonProperty("canId")
    public Long getCanId() {
        return trashCan != null ? trashCan.getId() : null;
    }

    @Column(name = "prev_status", length = 50)
    private String prevStatus;

    @Column(name = "curr_status", nullable = false, length = 50)
    private String currStatus;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @PrePersist
    public void prePersist() {
        this.changedAt = LocalDateTime.now();
    }
}
package com.ecolink.backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "collection_history")
@Getter
@NoArgsConstructor
public class CollectionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private CollectionRoute collectionRoute;

    @JsonProperty("routeId")
    public Long getRouteId() {
        return collectionRoute != null ? collectionRoute.getId() : null;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "can_id", nullable = false)
    private TrashCan trashCan;

    @JsonProperty("canId")
    public Long getCanId() {
        return trashCan != null ? trashCan.getId() : null;
    }

    @Column(name = "before_level", nullable = false)
    private Integer beforeLevel;

    @Column(name = "after_level", nullable = false)
    private Integer afterLevel;

    @Column(name = "collected_at", nullable = false)
    private LocalDateTime collectedAt;

    @PrePersist
    public void prePersist() {
        this.collectedAt = LocalDateTime.now();
    }
}
-- EcoLink BinGo 스키마 (MariaDB 11 / MySQL 8)
-- JPA 엔티티(com.ecolink.backend.entity)와 1:1로 대응합니다.

SET NAMES utf8mb4;

DROP TABLE IF EXISTS collection_history;
DROP TABLE IF EXISTS collection_route;
DROP TABLE IF EXISTS empty_history;
DROP TABLE IF EXISTS can_status_log;
DROP TABLE IF EXISTS sensor_log;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS trash_can;

CREATE TABLE trash_can (
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    loc_name  VARCHAR(100) NOT NULL,
    loc_lat   DOUBLE       NOT NULL,
    loc_lng   DOUBLE       NOT NULL,
    max_capa  INT          NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- grade: 1 = 관리자, 5 = 수거 작업자
CREATE TABLE users (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    username       VARCHAR(50)  NOT NULL,
    password       VARCHAR(255) NOT NULL,
    grade          INT          NULL,
    vehicle_number VARCHAR(20)  NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE sensor_log (
    id            BIGINT   NOT NULL AUTO_INCREMENT,
    can_id        BIGINT   NOT NULL,
    fill_level    INT      NOT NULL,
    battery_level INT      NOT NULL,
    log_time      DATETIME NOT NULL,
    PRIMARY KEY (id),
    KEY idx_sensor_log_can_time (can_id, log_time),
    CONSTRAINT fk_sensor_log_can FOREIGN KEY (can_id) REFERENCES trash_can (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE can_status_log (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    can_id      BIGINT      NOT NULL,
    prev_status VARCHAR(50) NULL,
    curr_status VARCHAR(50) NOT NULL,
    reason      TEXT        NULL,
    changed_at  DATETIME    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_can_status_log_can FOREIGN KEY (can_id) REFERENCES trash_can (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE empty_history (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    can_id       BIGINT       NOT NULL,
    before_level FLOAT        NOT NULL,
    after_level  FLOAT        NOT NULL,
    emptied_at   DATETIME     NOT NULL,
    note         VARCHAR(255) NULL,
    PRIMARY KEY (id),
    KEY idx_empty_history_can_time (can_id, emptied_at),
    CONSTRAINT fk_empty_history_can FOREIGN KEY (can_id) REFERENCES trash_can (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE collection_route (
    id             BIGINT   NOT NULL AUTO_INCREMENT,
    worker_id      BIGINT   NOT NULL,
    optimized_path JSON     NOT NULL,
    total_distance DOUBLE   NOT NULL,
    created_at     DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_collection_route_worker FOREIGN KEY (worker_id) REFERENCES users (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE collection_history (
    id           BIGINT   NOT NULL AUTO_INCREMENT,
    route_id     BIGINT   NOT NULL,
    can_id       BIGINT   NOT NULL,
    before_level INT      NOT NULL,
    after_level  INT      NOT NULL,
    collected_at DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_collection_history_route FOREIGN KEY (route_id) REFERENCES collection_route (id),
    CONSTRAINT fk_collection_history_can FOREIGN KEY (can_id) REFERENCES trash_can (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

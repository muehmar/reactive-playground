create table Task
(
    id        VARCHAR(20)  NOT NULL,
    title     VARCHAR(190) NOT NULL,
    insertion timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
)
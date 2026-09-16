CREATE TABLE submission (
                            id            UUID PRIMARY KEY,
                            email         VARCHAR(255) NOT NULL,
                            original_key  VARCHAR(512) NOT NULL,
                            thumbnail_key VARCHAR(512),
                            created_at    TIMESTAMP NOT NULL
);
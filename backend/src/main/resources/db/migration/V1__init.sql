-- Migration initiale BookHub
-- Cette migration sera enrichie en S2-S3

CREATE TABLE IF NOT EXISTS app_info (
                                        id BIGSERIAL PRIMARY KEY,
                                        key VARCHAR(100) NOT NULL UNIQUE,
    value VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO app_info (key, value) VALUES ('schema_version', '1')
    ON CONFLICT (key) DO NOTHING;
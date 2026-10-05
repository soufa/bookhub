CREATE TABLE refresh_tokens (
                                id          BIGSERIAL PRIMARY KEY,
                                token       VARCHAR(255) NOT NULL UNIQUE,
                                username    VARCHAR(100) NOT NULL,
                                expires_at  TIMESTAMP WITH TIME ZONE NOT NULL,
                                revoked     BOOLEAN NOT NULL DEFAULT FALSE,
                                created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_refresh_tokens_username ON refresh_tokens(username);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE customer (
  customer_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  first_name VARCHAR (50) NOT NULL,
  last_name VARCHAR (50) NOT NULL,
  email VARCHAR (255) UNIQUE NOT NULL,
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
  --last_login TIMESTAMP
  --address_id BIGSERIAL
  --account_id BIGSERIAL
  --email_verified_at TIMESTAMPTZ,
);
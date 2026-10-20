CREATE TABLE account (
    account_id UUID PRIMARY KEY,

    customer_id UUID NOT NULL
        REFERENCES customer(customer_id) ON DELETE RESTRICT,

    account_number VARCHAR(34) NOT NULL UNIQUE,

    account_type VARCHAR(7) NOT NULL
        CHECK (account_type IN ('CURRENT', 'SAVINGS')),

    currency VARCHAR(3) NOT NULL
        CHECK (currency = 'EUR'),

    balance_minor BIGINT NOT NULL DEFAULT 0
        CHECK (balance_minor >= 0),


    status VARCHAR(7) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN (
            'PENDING', 'ACTIVE', 'FROZEN', 'CLOSED'
        )),

    opened_at TIMESTAMPTZ,
    closed_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT chk_account_closed_at_requires_opened_at
        CHECK (closed_at IS NULL OR opened_at IS NOT NULL),

    CONSTRAINT chk_account_closed_at_after_opened_at
        CHECK (closed_at IS NULL OR closed_at >= opened_at),

    CONSTRAINT chk_account_opened_at_matches_status
    CHECK ((status = 'PENDING' AND opened_at IS NULL)
        OR (status IN ('ACTIVE', 'FROZEN') AND opened_at IS NOT NULL)
        OR (status = 'CLOSED' AND opened_at IS NOT NULL)
    ),

CONSTRAINT chk_account_closed_at_matches_status
    CHECK ((status = 'CLOSED' AND closed_at IS NOT NULL)
        OR (status <> 'CLOSED' AND closed_at IS NULL)
    )
);
CREATE TABLE IF NOT EXISTS clients (// СТАРЫЙ ЗАПРОС, БД ИЗМЕНИЛАСЬ
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100) NOT NULL,
    client_id UUID UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL PRIMARY KEY,
    client_id INTEGER NOT NULL REFERENCES clients(id),
    type VARCHAR(10) NOT NULL CHECK (type IN ('CREDIT', 'DEBIT')),
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_account_client FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    account_id INTEGER NOT NULL REFERENCES accounts(id),
    amount DECIMAL(15,2) NOT NULL,
    transaction_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE TABLE IF NOT EXISTS data_source_error_logs (
    id SERIAL PRIMARY KEY,
    stack_trace TEXT NOT NULL,
    text TEXT NOT NULL,
    method_signature VARCHAR(255) NOT NULL
);

CREATE TABLE IF MOT EXISTS time_limit_exceed_log(
    id SERIAL PRIMARY KEY,
    error JSONB NOT NULL
);
-- This is an empty migration file
-- The schema already exists from Hibernate ddl-auto=update
-- This file serves as a baseline marker

-- Missing constraints or indexes that Hibernate might not have created
CREATE INDEX IF NOT EXISTS idx_user_email ON application_users(email);
CREATE INDEX IF NOT EXISTS idx_user_username ON application_users(username);
CREATE INDEX IF NOT EXISTS idx_ticket_status ON tickets(status);
CREATE INDEX IF NOT EXISTS idx_ticket_priority ON tickets(priority);
CREATE INDEX IF NOT EXISTS idx_ticket_client ON tickets(client_id);
CREATE INDEX IF NOT EXISTS idx_ticket_assigned ON tickets(assigned_user_id);
CREATE INDEX IF NOT EXISTS idx_client_source ON clients(source);
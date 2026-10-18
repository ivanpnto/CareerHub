-- Calendar dates: timestamp with time zone -> date
-- The explicit time zone makes the conversion deterministic,
-- independent of the database session time zone.

ALTER TABLE job_application
    ALTER COLUMN applied_at TYPE DATE
    USING (applied_at AT TIME ZONE 'Europe/Madrid')::DATE;

ALTER TABLE job_offer
    ALTER COLUMN published_at TYPE DATE
    USING (published_at AT TIME ZONE 'Europe/Madrid')::DATE;

ALTER TABLE job_offer
    ALTER COLUMN expires_at TYPE DATE
    USING (expires_at AT TIME ZONE 'Europe/Madrid')::DATE;

-- Foreign key indexes: PostgreSQL does not create them automatically.

CREATE INDEX idx_company_user_id ON company (user_id);
CREATE INDEX idx_job_offer_company_id ON job_offer (company_id);
CREATE INDEX idx_job_application_user_id ON job_application (user_id);
CREATE INDEX idx_job_application_company_id ON job_application (company_id);
CREATE INDEX idx_job_application_job_offer_id ON job_application (job_offer_id);
CREATE INDEX idx_interview_job_application_id ON interview (job_application_id);
CREATE INDEX idx_task_user_id ON task (user_id);
CREATE INDEX idx_task_job_application_id ON task (job_application_id);
CREATE TABLE "user" (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL
);

CREATE TABLE company (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    name VARCHAR(255) NOT NULL,
    website VARCHAR(500),
    industry VARCHAR(150),
    headquarters VARCHAR(255),
    notes TEXT,
    user_id UUID NOT NULL,

    CONSTRAINT fk_company_user
        FOREIGN KEY (user_id)
        REFERENCES "user"(id)
);

CREATE TABLE job_offer (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    location VARCHAR(255),
    work_mode VARCHAR(50),
    employment_type VARCHAR(50),
    source_url VARCHAR(500),
    published_at TIMESTAMP WITH TIME ZONE,
    expires_at TIMESTAMP WITH TIME ZONE,
    company_id UUID NOT NULL,

    CONSTRAINT fk_job_offer_company
        FOREIGN KEY (company_id)
        REFERENCES company(id)
);

CREATE TABLE job_application (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    position_title VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    applied_at TIMESTAMP WITH TIME ZONE,
    priority VARCHAR(50) NOT NULL,
    notes TEXT,
    user_id UUID NOT NULL,
    company_id UUID NOT NULL,
    job_offer_id UUID,

    CONSTRAINT fk_job_application_user
        FOREIGN KEY (user_id)
        REFERENCES "user"(id),

    CONSTRAINT fk_job_application_company
        FOREIGN KEY (company_id)
        REFERENCES company(id),

    CONSTRAINT fk_job_application_job_offer
        FOREIGN KEY (job_offer_id)
        REFERENCES job_offer(id)
);

CREATE TABLE interview (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    scheduled_at TIMESTAMP WITH TIME ZONE NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    location VARCHAR(255),
    interviewer VARCHAR(255),
    notes TEXT,
    job_application_id UUID NOT NULL,

    CONSTRAINT fk_interview_job_application
        FOREIGN KEY (job_application_id)
        REFERENCES job_application(id)
);

CREATE TABLE task (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50) NOT NULL,
    user_id UUID NOT NULL,
    job_application_id UUID,

    CONSTRAINT fk_task_user
        FOREIGN KEY (user_id)
        REFERENCES "user"(id),

    CONSTRAINT fk_task_job_application
        FOREIGN KEY (job_application_id)
        REFERENCES job_application(id)
);
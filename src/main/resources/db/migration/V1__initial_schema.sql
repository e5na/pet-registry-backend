CREATE TABLE species (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE shelters (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(50),
    email VARCHAR(255)
);

CREATE TABLE microchips (
    id BIGSERIAL PRIMARY KEY,
    microchip_number VARCHAR(15) UNIQUE NOT NULL, -- Standardne kiibi numbri pikkus
    importer VARCHAR(255),
    in_use BOOLEAN DEFAULT FALSE
);

CREATE TABLE breeds (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    species_id BIGINT REFERENCES species(id)
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    personal_code VARCHAR(50) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    phone_number VARCHAR(50),
    password VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE veterinary_profiles (
    user_id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    license_number VARCHAR(100),
    specialization VARCHAR(255)
);

CREATE TABLE owner_profiles (
    user_id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    address VARCHAR(255)
);

CREATE TABLE shelter_worker_profiles (
    user_id BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    shelter_id BIGINT NOT NULL REFERENCES shelters(id),
    job_title VARCHAR(100)
);

CREATE TABLE pets (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    microchip_id BIGINT UNIQUE REFERENCES microchips(id),
    breed_id BIGINT REFERENCES breeds(id),
    owner_id BIGINT REFERENCES users(id),
    name VARCHAR(100),
    sex CHAR(1),
    birth_date DATE,
    color VARCHAR(100),
    status VARCHAR(50) NOT NULL -- Enum: ACTIVE, LOST, FOUND, DECEASED, EXPORTED
);

CREATE TABLE shelter_admissions (
    id BIGSERIAL PRIMARY KEY,
    shelter_id BIGINT NOT NULL REFERENCES shelters(id),
    pet_id BIGINT NOT NULL REFERENCES pets(id),
    finding_location VARCHAR(255),
    finding_date DATE,
    shelter_checkin_time TIMESTAMP NOT NULL,
    shelter_health_assessment TEXT
);

CREATE TABLE veterinary_checks (
    id BIGSERIAL PRIMARY KEY,
    pet_id BIGINT NOT NULL REFERENCES pets(id),
    user_id BIGINT NOT NULL REFERENCES users(id), -- Veterinaar
    check_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    health_assessment TEXT,
    ration TEXT,
    housing_regime TEXT,
    weight DOUBLE PRECISION
);

CREATE TABLE treatments (
    id BIGSERIAL PRIMARY KEY,
    pet_id BIGINT NOT NULL REFERENCES pets(id),
    veterinary_check_id BIGINT REFERENCES veterinary_checks(id),
    description TEXT,
    medication TEXT,
    date DATE
);

CREATE TABLE pet_lifecycle_events (
    id BIGSERIAL PRIMARY KEY,
    pet_id BIGINT NOT NULL REFERENCES pets(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    user_role_id BIGINT NOT NULL REFERENCES roles(id),
    event_type VARCHAR(50) NOT NULL, -- Enum: REGISTRATION, LOST_REPORTED, DEATH, jne.
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description TEXT
);

CREATE TABLE ownership_transfers (
    id BIGSERIAL PRIMARY KEY,
    pet_id BIGINT NOT NULL REFERENCES pets(id),
    old_owner_id BIGINT NOT NULL REFERENCES users(id),
    new_owner_id BIGINT NOT NULL REFERENCES users(id),
    status VARCHAR(50) NOT NULL, -- Enum: PENDING, COMPLETED, REJECTED
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP
);

CREATE TABLE pictures (
    id BIGSERIAL PRIMARY KEY,
    pet_id BIGINT NOT NULL REFERENCES pets(id),
    file_name VARCHAR(255),
    picture BYTEA
);

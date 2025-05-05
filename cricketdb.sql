
-- Create the database
CREATE DATABASE IF NOT EXISTS cricketdb;

-- Use the database
USE cricketdb;

-- Create the 'players' table
CREATE TABLE IF NOT EXISTS players (
    name VARCHAR(100) PRIMARY KEY,
    country VARCHAR(50),
    role VARCHAR(50),
    runs INT
);

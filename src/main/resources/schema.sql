-- Create table for spaceships --
CREATE TABLE IF NOT EXISTS spaceships (
    _id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    series_movie VARCHAR(100) NOT NULL,
    ship_type VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    features TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

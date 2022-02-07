-- MS: 2/4/22 - copied SQL statements from design document and fixed syntax errors

CREATE TABLE IF NOT EXISTS User (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Name VARCHAR(30) UNIQUE,
    setting_1 INTEGER NOT NULL DEFAULT 0,
    setting_n INTEGER NOT NULL DEFAULT 0);

CREATE TABLE IF NOT EXISTS Module (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Name text);

CREATE TABLE IF NOT EXISTS CompletedModule (
    usrID INTEGER REFERENCES User(ID) ON DELETE CASCADE,
    modID INTEGER REFERENCES Module(ID),
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Rating INTEGER,
    Date TEXT NOT NULL DEFAULT CURRENT_DATE);

CREATE TABLE IF NOT EXISTS ModuleSequence (
    usrID INTEGER REFERENCES User(ID) ON DELETE CASCADE,
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Name VARCHAR(30) UNIQUE);

-- MS: 2/7/22 - fixed primary key
CREATE TABLE IF NOT EXISTS SequenceOrder (
    seqID INTEGER REFERENCES ModuleSequence(ID) ON DELETE CASCADE,
    modID INTEGER REFERENCES Module(ID) ON DELETE CASCADE,
    modOrder INTEGER NOT NULL,
    PRIMARY KEY (seqID, modOrder));
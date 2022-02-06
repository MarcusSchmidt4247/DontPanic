-- MS: 2/4/22

INSERT INTO User (Name) VALUES ("John");

INSERT INTO Module (Name) VALUES
    ("Meditation"),
    ("Haptics"),
    ("Exercises"),
    ("Reflection");

INSERT INTO ModuleSequence (usrID, Name) VALUES
    (1, "Main Sequence"),
    (1, "Test");

INSERT INTO SequenceOrder (seqID, modID, modOrder) VALUES
    (1, 2, 1),
    (1, 3, 2),
    (1, 1, 3),
    (2, 4, 1);
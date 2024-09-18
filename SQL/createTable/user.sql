CREATE TABLE "User"(
  id VARCHAR(64) NOT NULL PRIMARY KEY,
  createdAt TIMESTAMP WITH TIME ZONE NOT NULL,
  updatedAt TIMESTAMP WITH TIME ZONE NOT NULL,
  deletedAt TIMESTAMP WITH TIME ZONE NOT NULL,
  username VARCHAR(64) UNIQUE NOT NULL,
  passwordHash VARCHAR(128) NOT NULL
)

-- DROP TABLE "User"

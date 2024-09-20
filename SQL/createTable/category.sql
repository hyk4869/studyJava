CREATE TABLE "T_Category" (
  "id" VARCHAR(64) NOT NULL PRIMARY KEY,
  "createdById" VARCHAR(64) NOT NULL REFERENCES "User"(id),
  "createdByName" VARCHAR(64) NOT NULL,
  "updatedById" VARCHAR(64) NOT NULL REFERENCES "User"(id),
  "updatedByName" VARCHAR(64) NOT NULL,
  "createdAt" TIMESTAMP WITH TIME ZONE NOT NULL,
  "updatedAt" TIMESTAMP WITH TIME ZONE NOT NULL,
  "deletedAt" TIMESTAMP WITH TIME ZONE,
  name VARCHAR(64) NOT NULL
);

-- DROP TABLE "T_Category"

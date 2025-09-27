# Data Model

## Entities

- User
  - id: UUID (local)
  - displayName: string
  - frontlineAccountId: string (opaque)
  - credentialsAllowed: boolean
  - apiTokenMetadata: object { tokenType, expiresAt }

- Job
  - id: string (provider job id when available)
  - externalId: string (stable hash if provider id missing)
  - school: string
  - date: date
  - dayLength: enum { FULL, HALF, MULTI }
  - classOrGrade: string
  - teacher: string
  - subject: string
  - description: string
  - sourceHtml: text (optional cached snapshot)
  - fetchedAt: timestamp
  - status: enum { AVAILABLE, ACCEPTED, UNAVAILABLE }

- FilterPreferences
  - id: UUID
  - ownerId: User.id
  - schools: [string]
  - dateRanges: [date/date-range]
  - dayLengthOptions: [enum]
  - classes: [string]
  - teachers: [string]
  - subjects: [string]
  - pollingIntervalSeconds: integer
  - notificationPreferences: object

- AcceptedJob
  - id: UUID
  - jobRef: Job.id
  - acceptedAt: timestamp
  - acceptResult: enum { SUCCESS, FAILURE }
  - acceptMessage: string

- AuditLogEntry
 - AuditLogEntry
  - id: UUID
  - timestamp: ISO-8601 timestamp
  - actor: string (user displayName or opaque FrontlineAccountIdentifier)
  - actionType: enum { LOGIN_ATTEMPT, FETCH_JOBS, ACCEPT_ATTEMPT, ACCEPT_RESULT, EXPORT_LOG, CLEAR_LOG, ERROR }
  - jobRef: string (nullable; provider jobId or externalId/stable hash)
  - result: enum { SUCCESS, FAILURE, UNAVAILABLE, UNKNOWN }
  - message: string (short human-readable message, optional)
  - correlationId: string (nullable; used to link related events)
  - payload: json (optional; structured details)
  - createdAt: timestamp

## Indexes & Uniqueness
- Jobs: index on (externalId, fetchedAt) and unique constraint on externalId when present.
- AuditLog: time-series ordered by createdAt for efficient retention trimming.

## Storage recommendation
- Use Room for strong typing, provide migrations, and encrypt sensitive tables/fields using Android's encrypted database option or store tokens in Keystore and only store opaque metadata in DB.

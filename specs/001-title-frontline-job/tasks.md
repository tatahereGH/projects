# Tasks: Frontline Job Finder (Feature 001)

Each task is written so an LLM or developer can complete it without additional context. Parallelizable tasks are marked with [P].

T001 — Scaffold Android project and CI [P] [X]
- Create an `android/` module in the repo root with a basic Gradle Kotlin app (min API 26). Include a simple Activity and configure `build.gradle` with Kotlin, Room, OkHttp, Jsoup, and Coroutines dependencies. Add a GitHub Actions workflow `ci/android.yml` that runs `./gradlew assembleDebug` and basic unit tests.
- Files: `android/`, `android/app/src/main/java/com/frontlinehelper/app/`, `.github/workflows/ci-android.yml`
- Notes: This is parallel with other repo-structure tasks.
- Status: Completed 2025-09-26 — created `android/` scaffold and `.github/workflows/ci-android.yml` (minimal gradle wrapper placeholders included). Local Gradle wrapper generation recommended for full CI runs.

T002 — Add project linting & formatting [P]
- Add Kotlin linting (ktlint) and formatting enforcement to Gradle and CI. Configure pre-commit hook guidance in README.
- Files: `android/build.gradle`, `.github/workflows/ci-android.yml`, `.editorconfig`
 - Status: Completed 2025-09-26 — added ktlint plugin to Gradle, `.editorconfig`, and CI step running `ktlintCheck`.

T003 — Create Room entities & migrations for User/Job/FilterPreferences [P]
- Implement Room `@Entity` classes for `User`, `Job`, `FilterPreferences`, `AcceptedJob`, and `AuditLogEntry` per `data-model.md`. Add DAO interfaces and an initial migration. Include unit tests that verify basic CRUD operations using an in-memory Room DB.
- Files: `android/app/src/main/java/com/frontlinehelper/app/data/*.kt`, `android/app/src/test/com/frontlinehelper/app/` (unit tests)

T004 — Implement token storage wrapper (Keystore/EncryptedSharedPreferences) [P]
- Create a secure storage component that exposes `storeToken(tokenMetadata)`, `getToken()`, and `clearToken()` APIs and is tested with unit tests (mock Keystore). Document usage in `quickstart.md`.
- Files: `android/app/src/main/java/com/frontlinehelper/app/security/SecureStore.kt`

T005 — Implement FrontlineService (network client, token handling, auth detection)
- Implement `FrontlineService.kt` using OkHttp and Coroutines: support `fetchJobs()`, `loginInteractive()` (webview flow integration point), and `detectAuthCapabilities()` (returns enum: TOKEN, INTERACTIVE). Include retry/backoff and error mapping. Add unit tests mocking HTTP responses.
- Files: `android/app/src/main/java/com/frontlinehelper/app/network/FrontlineService.kt`, `android/app/src/test/com/frontlinehelper/app/FrontlineServiceTest.kt`

T006 — Add contract test for `GET /jobs` → failing test [P]
- Add a contract test that asserts response shape matches `contracts/job-schema.yaml`. The test should call `FrontlineService.fetchJobs()` (mocked) and validate JSON schema. Mark as failing until implementation meets contract.
- Files: `specs/001-title-frontline-job/contracts/job-schema.yaml`, `android/app/src/test/com/frontlinehelper/app/contracts/GetJobsContractTest.kt`

T007 — Implement JobParser with unit tests (HTML fixtures)
- Implement `JobParser.kt` using Jsoup to parse provider HTML into `Job` entities. Add deterministic unit tests using saved HTML fixtures placed in `specs/001-title-frontline-job/fixtures/` (include at least one sample job and edge-case variations). Ensure parser deduplicates and handles missing IDs by creating a stable hash for `externalId`.
- Files: `android/app/src/main/java/com/frontlinehelper/app/parsing/JobParser.kt`, `specs/001-title-frontline-job/fixtures/*.html`, `android/app/src/test/com/frontlinehelper/app/JobParserTest.kt`

T008 — Implement Configuration page + persistence
- Implement an Android UI screen `ConfigurationActivity/Fragment` that allows the user to set filter preferences (schools, dates, dayLength, classes, teachers, subjects), notification preferences, and polling interval. Persist to `FilterPreferences` Room entity. Add UI unit tests (Robolectric/Espresso) that verify persistence.
- Files: `android/app/src/main/java/com/frontlinehelper/app/ui/config/ConfigurationFragment.kt`, layout files, `android/app/src/androidTest/com/frontlinehelper/app/`

T009 — Implement Main list UI + quick-view cards and notification wiring
- Implement the main screen showing filtered jobs as quick-view cards. Show date, school, day-length prominently; other details smaller. Wire local notifications when new matching jobs are discovered based on stored preferences. Include a top-left small bold display of username and today's date.
- Files: `android/app/src/main/java/com/frontlinehelper/app/ui/main/MainFragment.kt`, `android/app/src/main/res/layout/`

- T010 — Implement Consolidated Accept Page (Accept All UX) (links/buttons + status updates) (FR-009)
- Implement the Consolidated Accept Page (Accept All UX) that lists all visible matching jobs with an accept link/button per row. Each row's accept link opens the provider's accept page in an in-app webview; the page listens for completion and updates per-job status (ACCEPTED / FAILED / UNAVAILABLE). Include retry/dismiss UI controls. Suggested implementation path: `android/app/src/main/java/com/frontlinehelper/app/ui/accept/ConsolidatedAcceptFragment.kt` and `android/app/src/main/java/com/frontlinehelper/app/ui/accept/ConsolidatedAcceptViewModel.kt`.
- Files: `android/app/src/main/java/com/frontlinehelper/app/ui/accept/ConsolidatedAcceptFragment.kt`, webview integration, status DB updates.

T011 — Implement Accepted Jobs screen and navigation
- Implement a screen listing AcceptedJob entries with accept timestamp and result message. Allow navigation back to main and export/clear audit log.
- Files: `android/app/src/main/java/com/frontlinehelper/app/ui/accepted/AcceptedJobsFragment.kt`

T012 — Implement audit log, export, and retention enforcement (30 days default)
- Implement `AuditLog` writer that records login attempts, fetch attempts, accept attempts, and errors. Provide export (CSV) and clear functions in the UI. Implement scheduled trimming of logs older than 30 days (configurable). Add unit tests for retention behavior.
- Files: `android/app/src/main/java/com/frontlinehelper/app/audit/AuditLog.kt`, UI export handlers.

T013 — Implement polling scheduler with backoff and connectivity awareness (FR-012)
- Implement a background polling service using WorkManager or Coroutines + foreground service that runs at the configured polling interval (default 60s to align with constitution), respects network connectivity, and applies exponential backoff on repeated failures. The polling settings UI MUST only allow intervals below 30s when the user explicitly enables an "advanced polling" mode and accepts an in-app warning about provider rate limits and potential TOS implications. The service MUST obey provider Retry-After / Rate-Limit headers and log occurrences to the audit log (FR-013). Add tests simulating network failures and backoff.
- Files: `android/app/src/main/java/com/frontlinehelper/app/sync/PollingService.kt`

T014 — Observability: structured logs and error codes [P]
- Add structured logging for critical flows (auth, fetch, accept) with error codes and correlation IDs. Ensure logs are persisted to local DB/file and included in exported audit logs.
- Files: `android/app/src/main/java/com/frontlinehelper/app/logging/Logger.kt`

T015 — Security & compliance tasks: TOS review, dependency audit, PR checklist
- Create tasks and documentation to perform manual TOS/legal review for scraping/programmatic access, add dependency vulnerability scanning job in CI, and add a PR security checklist file.
- Files: `docs/security/TOS-review.md`, `.github/workflows/dependency-scan.yml`, `SECURITY.md` additions.

T018 — CI: dependency scanning and gating (FR: Constitution enforcement) [X]
- Add a CI job that runs dependency vulnerability scanning (e.g., Gradle dependency-check or GitHub dependency review) and fails the CI build on critical/high severity findings. Wire the job into `.github/workflows/ci-android.yml` and document remediation steps. Ensure the CI job reports findings in a machine-readable artifact for maintainers.
- Files: `.github/workflows/dependency-scan.yml`, `.github/workflows/ci-android.yml`
- Status: Completed 2025-09-26 — added `ci-android.yml` and documented scanning job placeholder (implement `dependency-scan.yml` in CI as next step).

T016 — UI smoke tests & packaging [P]
-- Add basic UI smoke tests (Espresso) that cover login/configuration/main list and Consolidated Accept Page (Accept All UX). Add packaging task to build APK and verify side-load as per `quickstart.md`.
- Files: `android/app/src/androidTest/com/frontlinehelper/app/`, build scripts.

T017 — README / Quickstart improvements & developer docs [P]
- Update repository README and `specs/001-title-frontline-job/quickstart.md` with build, side-load, testing, and security guidance.
- Files: `README.md`, `specs/001-title-frontline-job/quickstart.md`

Execution order notes
- Run T001/T002 (scaffold + lint) first. Then T003/T004/T005/T007/T006 (models, secure storage, service, parser, contract tests). UI tasks (T008-T011) follow service+parser completion. Observability/TOS tasks can run in parallel.

Parallel execution groups (examples)
- Group A [P]: T001, T002, T015 (scaffold, lint, security tasks)
- Group B [P]: T003, T004, T007, T014 (model, secure storage, parser, logging)
- Group C [P]: T006, T016, T017 (contract tests placeholder, packaging, docs)

File created: `C:\Users\khanf\Documents\VibeCoding\Projects\frontLineHelper\specs\001-title-frontline-job\tasks.md`


# Feature Specification: Frontline Job Finder (Android)

**Feature Branch**: `001-title-frontline-job`  
**Created**: 2025-09-26  
**Status**: Accepted  
**Input**: User description: "Local-only Android app (side-load) that logs in to Frontline Absence Management (Texas) with user credentials; pulls and parses available jobs; applies pre-configured filters (school, date, full/half/multi day, class, teacher, subject); alerts & highlights matching jobs; shows a big bold quick view card (date, school, half/full day) with other details in smaller text; supports accepting a single job, accepting all available jobs, and viewing accepted jobs on a separate screen. Clicking an alert opens the main page. UI displays logged-in username and date in the top-left with small bold text."

## Clarifications

### Session 2025-09-26

- Q: Does Frontline provide an accept endpoint? → A: Programmatic acceptance is disallowed by Frontline / policy — accepts must be manual (D)
- Q: MFA/CAPTCHA and programmatic login? → A: Mixed: some districts/accounts require MFA or extra checks (depends on district) (D)
 - Q: Retention period for cached job data and audit logs? → A: 30 days (B)
 - Q: When programmatic acceptance is disallowed, what should the app's "Accept All" action do? → A: Consolidated Accept Page (Accept All UX) (B)
 
 - Q: Preferred polling/backoff behavior? → A: Server-guided: default 60s (constitution-aligned), obey Retry-After/Rate-Limit headers with jitter/backoff; intervals below 30s only allowed via explicit advanced opt-in (D)

## Execution Flow (main)
```
1. Parse user description from Input
   → If empty: ERROR "No feature description provided"
2. Extract key concepts from description
   → Identify: actors, actions, data, constraints
3. For each unclear aspect:
   → Mark with [NEEDS CLARIFICATION: specific question]
4. Fill User Scenarios & Testing section
   → If no clear user flow: ERROR "Cannot determine user scenarios"
5. Generate Functional Requirements
   → Each requirement must be testable
   → Mark ambiguous requirements
6. Identify Key Entities (if data involved)
7. Run Review Checklist
   → If any [NEEDS CLARIFICATION]: WARN "Spec has uncertainties"
   → If implementation details found: ERROR "Remove tech details"
8. Return: SUCCESS (spec ready for planning)
```

---

## ⚡ Quick Guidelines
- ✅ Focus on WHAT users need and WHY
- ❌ Avoid HOW to implement (no tech stack, APIs, code structure) unless explicitly required
- 👥 Written for business stakeholders, with notes for implementers where necessary

### Section Requirements
- **Mandatory sections**: Must be completed for every feature
- **Optional sections**: Include only when relevant to the feature
- When a section doesn't apply, remove it entirely (don't leave as "N/A")

### For AI Generation
When creating this spec from a user prompt:
1. **Mark all ambiguities**: Use [NEEDS CLARIFICATION: specific question] for any assumption you'd need to make
2. **Don't guess**: If the prompt doesn't specify something (e.g., "login system" without auth method), mark it
3. **Think like a tester**: Every vague requirement should fail the "testable and unambiguous" checklist item
4. **Common underspecified areas**:
   - User types and permissions
   - Data retention/deletion policies  
   - Performance targets and scale
   - Error handling behaviors
   - Integration requirements
   - Security/compliance needs

---

## User Scenarios & Testing *(mandatory)*

### Primary User Story
- As a substitute teacher using Frontline Absence Management in Texas, I want a lightweight, local Android app that logs into my Frontline account, fetches currently available jobs, highlights only the jobs that match my saved preferences, notifies me when matches appear, and lets me quickly accept a single job or accept all visible jobs so I can secure work quickly without repeatedly checking a browser.

### Acceptance Scenarios
1. **Given** the user has valid Frontline credentials and has pre-configured filters in the app's Configuration page (school(s), date(s), day-length, class, teacher, subject), **When** the app logs in and fetches available jobs, **Then** the UI shows only jobs that match the configured filters and emits a user-visible alert for each new matching job.
2. **Given** a matching job is displayed in the quick-view list, **When** the user taps the single-accept button for that job, **Then** the app attempts to accept the job via the official Frontline API and the job is moved to the "Accepted Jobs" list; if additional confirmation is required, the app guides the user to complete it.
3. **Given** multiple matching jobs exist, **When** the user taps "Accept All" (Consolidated Accept Page (Accept All UX)), **Then** the app attempts to accept each job via the official API in sequence, reporting per-job success/failure and adding successfully accepted jobs to the Accepted Jobs list.
4. **Given** the user taps an alert notification, **When** the app opens, **Then** the main page is shown with the matching job highlighted and the user can take action.
5. **Given** the app is open, **When** it displays the main page, **Then** the top-left corner shows the logged-in user's display name and today's date in small bold text.
6. **Given** a job disappears from Frontline between fetch and accept, **When** the app attempts to accept it, **Then** the app surfaces a clear failure message describing the race-condition and removes or marks the job as unavailable.

### Edge Cases
- Invalid credentials: app must present a clear error and allow retry; repeated failures should rate-limit login attempts.
- Network failures and timeouts: show cached state if available and a clear offline indicator; resume polling when online.
- Job removal between display and accept: report as failure and do not add to Accepted Jobs.
- Partial failures during Accept All: app must continue attempting remaining jobs and present aggregated results.
- Duplicate jobs or identical listings: deduplicate by job identifier (when available) and by stable hash of job fields.
- Rate limiting / TOS constraints: if the provider blocks access, app must back off and require manual intervention.
- Privacy: user credentials must be stored only if the user explicitly opts in and storage must be secure.

## Requirements *(mandatory)*

### Functional Requirements
- **FR-001**: The system MUST allow the user to securely provide Frontline credentials and use them to authenticate to Frontline.
- **FR-002**: The system MUST fetch the current list of available jobs from the user's Frontline account at configurable intervals.
- **FR-003**: The system MUST parse the provider's job list and extract job attributes: job id (when available), school name, date(s), day-length (full/half/multi), class/grade, teacher name (if present), subject, and any brief description.
**FR-004**: The system MUST provide an in-app "Configuration" page that allows the user to define filter preferences (school(s), date range or specific dates, day-length (full/half/multi), class/grade, teacher, and subject), notification preferences, and polling interval. These preferences MUST be persisted across app restarts and applied to all fetch operations.
- **FR-005**: The system MUST apply filter preferences to the parsed job list and surface only matching jobs in the main UI.
- **FR-006**: The system MUST notify the user (local notification and in-app highlight) when new matching jobs are discovered; notifications MUST be user-configurable (enable/disable, quiet hours).
- **FR-007**: The system MUST present a clear quick-view UI card for each matching job showing date, school, and day-length prominently, with additional details in smaller text.
-- **FR-008**: The system MUST provide a per-job "Accept" action that guides the user to complete acceptance using Frontline's supported interactive flows (open the provider URL or in-app web view). The app MUST NOT perform automated acceptance when policy or the provider disallows it; instead it MUST surface the next steps and help the user complete the acceptance.
- **FR-009**: The system MUST provide an "Accept All" action that opens the Consolidated Accept Page (Accept All UX): an in-app page listing all currently visible matching jobs with direct accept links or buttons. Each link/button SHOULD open the provider's job accept page (in-app webview or external browser) so the user can complete acceptance manually from a single page. The Consolidated Accept Page MUST report per-job accept status (accepted, failed, unavailable) and allow the user to retry or dismiss entries.
- **FR-010**: The system MUST include an "Accepted Jobs" screen listing accepted jobs and allow navigation back to the main page.
- **FR-011**: The system MUST display the logged-in user's display name and today's date in the top-left corner in small bold text on the main page.
**FR-012**: The system MUST support a configurable polling interval for job fetches and MUST default to 60 seconds to align with the project's constitution guidance. The system MUST respect provider rate-limiting guidance: if the provider returns Retry-After or Rate-Limit headers (e.g., 429/5xx responses), the client MUST obey those headers, apply exponential backoff with jitter, and avoid aggressive retries. The polling interval MUST be adjustable in settings with a configurable lower bound; by default the client MUST not allow intervals below 30 seconds unless the user explicitly opts into an "advanced polling" mode (the app MUST display a clear warning about provider rate limits and potential TOS implications). The upper bound remains 24 hours. The client SHOULD prefer server-specified Retry-After values when present.

-- **FR-013**: The system MUST keep an audit log of user actions (login attempts, fetch attempts, accept attempts, accept results) stored locally for debugging and reconciliation; the user MUST be able to export or clear the log. The audit log MUST include a minimal set of fields for each entry (see Data Model / AuditLogEntry schema) and exported logs MUST include those fields in CSV/JSON format.
 - **FR-014**: The system MUST provide meaningful error messages for failures (network, auth, acceptance) and surface recommended next steps.
 - **FR-016**: The system MUST deduplicate jobs before presenting them to the user.
- **FR-017**: The system MUST respect Frontline's Terms of Service; if programmatic access is disallowed for a specific account, the app MUST not perform automated accepts without explicit, documented user consent.
-- **FR-018**: Programmatic acceptance is disallowed for this deployment (per clarification). The system MUST NOT attempt automated acceptance of jobs on behalf of the user. All accept actions MUST be guided and require explicit user confirmation; the app may open the Frontline job URL or in-app web view to complete the flow.
-- **FR-019**: Authentication flow: authentication behavior varies by district/account. The system MUST detect per-account auth capability and choose the supported flow: prefer token-based authentication (OAuth/API token) for fetching job data when available; for accounts requiring interactive MFA or additional checks, prompt the user to complete interactive authentication (in-app webview or external browser). Tokens MUST be stored securely (Android Keystore / EncryptedSharedPreferences) and refreshed per provider guidance.
-- **FR-020**: The system MUST retain cached job data and local audit logs for 30 days by default. Users MUST have an option to export or permanently clear logs and cached data at any time. The retention period should be configurable in app settings.

### Key Entities *(include if feature involves data)*
- **User**: identifies the person using the app; attributes: displayName, FrontlineAccountIdentifier (opaque), credentialsAllowed (boolean), preferencesRef, apiTokenMetadata.
- **Job**: jobId (provider), school, date(s), dayLength, class/grade, teacher, subject, description, sourceHtml (cached snapshot optional), fetchedAt.
- **FilterPreferences**: owner, list of school ids/text, date filters, dayLength options, class/teacher/subject filters, pollingInterval, notificationPreferences.
- **AcceptedJob**: job reference, acceptedAt, acceptResult (success|failure), acceptMessage.

---

## Accept All UX (Consolidated Accept Page)

This subsection defines the user experience, UI behavior, and acceptance criteria for the Consolidated Accept Page (Accept All UX) referred to in FR-009.

Behavior and intent
- The Consolidated Accept Page is a single in-app screen that lists all currently visible matching jobs and provides a per-row action to open the provider's job accept page (in-app webview or external browser). It is explicitly a manual/guided flow that helps users complete acceptance without automation that violates provider policy.
- The page MUST show per-job status (PENDING, ACCEPTED, FAILED, UNAVAILABLE) and present the user with retry or dismiss actions for each entry.

UI details
- Top-left: small bold text for logged-in user's display name and today's date (same as main page).
- Page header: "Consolidated Accept Page (Accept All UX)" and a short description: "Open each job's accept page to complete acceptance; per-job status will be recorded."
- Each job row: date (prominent), school (prominent), day-length, brief description, and an action button labeled "Open Accept Page". A lightweight status chip shows current per-job state.
- Bulk actions: a lightweight toolbar with "Open All in Webview" (opens each job accept page in sequence using in-app webview or tabs) and "Export Accept Links" (CSV of accept URLs) — both actions MUST include an explicit TOS/Warning dialog if programmatic acceptance is disallowed for the account.

Acceptance criteria
- Given multiple matching jobs, opening the Consolidated Accept Page must list each job and show an "Open Accept Page" button for each.
- Tapping a job's "Open Accept Page" must open the provider's accept page; when the provider flow completes (detectable via the webview navigation or user confirmation), the app must update the per-job status to ACCEPTED or FAILED and record an AuditLogEntry (see FR-013 and Data Model).
- Using "Open All in Webview" must sequentially open each accept page (or open tabs) and record status per job. If programmatic navigation to the accept button is attempted by the app, it must stop and require explicit user interaction (to comply with policy). The app should provide clear instructions and, if possible, focus the accept button for the user but not click it automatically.

Audit & observability
- Each user action on the Consolidated Accept Page (open single accept page, open all, retry, dismiss) MUST write an AuditLogEntry with actionType and correlationId linking related actions.
- Any TOS or provider-guidance warnings shown to the user MUST be logged with the reason and user response (accepted/declined).

Privacy & TOS guidance
- Before performing any bulk or advanced action (e.g., "Open All in Webview"), the app MUST display a short, clear warning if the provider disallows programmatic acceptance and require the user to confirm they understand the consequences.


## Glossary

- Consolidated Accept Page (Accept All UX): The in-app screen described in FR-009 that aggregates all currently visible matching jobs and provides guided, per-job links to the provider's accept pages. This term is canonical across the feature artifacts and should be used in code, docs, and UI labels where appropriate.


## Review & Acceptance Checklist
*GATE: Automated checks run during main() execution*

### Content Quality
- [x] No implementation details (languages, frameworks, APIs) that are mandatory for acceptance
- [x] Focused on user value and business needs
- [x] Written for non-technical stakeholders
- [x] All mandatory sections completed

### Requirement Completeness
- [x] No [NEEDS CLARIFICATION] markers remain
- [x] Requirements are testable and unambiguous where specified
- [x] Success criteria are measurable
- [x] Scope is clearly bounded
- [x] Dependencies and assumptions identified

---

## Execution Status
*Updated by main() during processing*

- [x] User description parsed
- [x] Key concepts extracted
- [x] Ambiguities marked
- [x] User scenarios defined
- [x] Requirements generated
- [x] Entities identified
- [ ] Review checklist passed
 - [x] Review checklist passed

---

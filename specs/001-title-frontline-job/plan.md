
# Implementation Plan: [FEATURE]

**Branch**: `[###-feature-name]` | **Date**: [DATE] | **Spec**: [link]
**Input**: Feature specification from `/specs/[###-feature-name]/spec.md`

## Execution Flow (/plan command scope)
```
1. Load feature spec from Input path
   → If not found: ERROR "No feature spec at {path}"
2. Fill Technical Context (scan for NEEDS CLARIFICATION)
   → Detect Project Type from file system structure or context (web=frontend+backend, mobile=app+api)
   → Set Structure Decision based on project type
3. Fill the Constitution Check section based on the content of the constitution document.
4. Evaluate Constitution Check section below
   → If violations exist: Document in Complexity Tracking
   → If no justification possible: ERROR "Simplify approach first"
   → Update Progress Tracking: Initial Constitution Check
5. Execute Phase 0 → research.md
   → If NEEDS CLARIFICATION remain: ERROR "Resolve unknowns"
6. Execute Phase 1 → contracts, data-model.md, quickstart.md, agent-specific template file (e.g., `CLAUDE.md` for Claude Code, `.github/copilot-instructions.md` for GitHub Copilot, `GEMINI.md` for Gemini CLI, `QWEN.md` for Qwen Code or `AGENTS.md` for opencode).
7. Re-evaluate Constitution Check section
   → If new violations: Refactor design, return to Phase 1
   → Update Progress Tracking: Post-Design Constitution Check
8. Plan Phase 2 → Describe task generation approach (DO NOT create tasks.md)
9. STOP - Ready for /tasks command
```

**IMPORTANT**: The /plan command STOPS at step 7. Phases 2-4 are executed by other commands:
- Phase 2: /tasks command creates tasks.md
- Phase 3-4: Implementation execution (manual or via tools)

## Summary
[Extract from feature spec: primary requirement + technical approach from research]

 Primary requirement: local Android app that logs into Frontline, fetches/parses available jobs, applies user-configured filters, alerts the user, and provides a Consolidated Accept Page (Accept All UX) for manual acceptance flows.

Technical approach: Kotlin Android app using OkHttp for networking, Jsoup for parsing, Room for persistence, and Android Keystore / EncryptedSharedPreferences for token storage. UI implemented with Jetpack Compose or standard Views (implementer choice).

## Technical Context
**Language/Version**: Kotlin (JVM target appropriate for Android API 26+)  
**Primary Dependencies**: OkHttp, Jsoup, Room, Kotlinx Coroutines, Jetpack (Compose or ViewModel/UI)  
**Storage**: Room local DB + Keystore/EncryptedSharedPreferences for tokens  
**Testing**: JUnit + Robolectric for unit tests; Espresso for UI smoke tests; deterministic parsing tests using HTML fixtures  
**Target Platform**: Android (API 26+)  
**Project Type**: Mobile app (Android)  
**Performance Goals**: Low-latency background polling; default polling every 60 seconds (configurable) with backoff on failures  
**Constraints**: Must respect constitution security rules (no cleartext credential storage, TLS only); polling bounded to avoid provider rate limiting  
**Scale/Scope**: Single-user local app; small data volumes (tens-to-hundreds of jobs per fetch)

## Constitution Check
*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

Constitution gate summary:

- Security-First: PASS if tokens/credentials are stored in Keystore/EncryptedSharedPreferences and network calls validate TLS. (Research recommends this approach.)
- Privacy & Compliance: PARTIAL — programmatic acceptance is disallowed for many accounts; research recommends manual flows and TOS review before enabling any scraping or programmatic auth.
- Test-First: PASS — plan requires deterministic parsing tests using HTML fixtures.
- Observability: PASS — research recommended audit logs, structured logging, and reconciliation.
- Simplicity & User Focus: PARTIAL — earlier drafts requested an aggressive polling interval; plan documents a constitution-aligned default (60s), configurable polling, and backoff to reconcile usability vs rate-limiting concerns.

Complexity note: The polling default should align with the constitution (60s). If lower intervals are required, they must be behind an explicit "advanced polling" opt-in that warns about provider rate limits and TOS implications. This is documented in research.md and the plan recommends backoff and rate-limit protections.

## Project Structure

### Documentation (this feature)
```
specs/[###-feature]/
├── plan.md              # This file (/plan command output)
├── research.md          # Phase 0 output (/plan command)
├── data-model.md        # Phase 1 output (/plan command)
├── quickstart.md        # Phase 1 output (/plan command)
├── contracts/           # Phase 1 output (/plan command)
└── tasks.md             # Phase 2 output (/tasks command - NOT created by /plan)
```

### Source Code (repository root)
<!--
  ACTION REQUIRED: Replace the placeholder tree below with the concrete layout
  for this feature. Delete unused options and expand the chosen structure with
  real paths (e.g., apps/admin, packages/something). The delivered plan must
  not include Option labels.
-->
```
# [REMOVE IF UNUSED] Option 1: Single project (DEFAULT)
src/
├── models/
├── services/
├── cli/
└── lib/

tests/
├── contract/
├── integration/
└── unit/

# [REMOVE IF UNUSED] Option 2: Web application (when "frontend" + "backend" detected)
backend/
├── src/
│   ├── models/
│   ├── services/
│   └── api/
└── tests/

frontend/
├── src/
│   ├── components/
│   ├── pages/
│   └── services/
└── tests/

# [REMOVE IF UNUSED] Option 3: Mobile + API (when "iOS/Android" detected)
api/
└── [same as backend above]

ios/ or android/
└── [platform-specific structure: feature modules, UI flows, platform tests]
```

**Structure Decision**: [Document the selected structure and reference the real
directories captured above]

## Phase 0: Outline & Research
1. **Extract unknowns from Technical Context** above:
   - For each NEEDS CLARIFICATION → research task
   - For each dependency → best practices task
   - For each integration → patterns task

2. **Generate and dispatch research agents**:
   ```
   For each unknown in Technical Context:
     Task: "Research {unknown} for {feature context}"
   For each technology choice:
     Task: "Find best practices for {tech} in {domain}"
   ```

3. **Consolidate findings** in `research.md` using format:
   - Decision: [what was chosen]
   - Rationale: [why chosen]
   - Alternatives considered: [what else evaluated]

**Output**: research.md (created) with guidance on auth flows, accept behavior, and polling rationale. See `research.md`.

## Phase 1: Design & Contracts
*Prerequisites: research.md complete*

1. **Extract entities from feature spec** → `data-model.md`:
   - Entity name, fields, relationships
   - Validation rules from requirements
   - State transitions if applicable

2. **Generate API contracts** from functional requirements:
   - For each user action → endpoint
   - Use standard REST/GraphQL patterns
   - Output OpenAPI/GraphQL schema to `/contracts/`

3. **Generate contract tests** from contracts:
   - One test file per endpoint
   - Assert request/response schemas
   - Tests must fail (no implementation yet)

4. **Extract test scenarios** from user stories:
   - Each story → integration test scenario
   - Quickstart test = story validation steps

5. **Update agent file incrementally** (O(1) operation):
   - Run `.specify/scripts/powershell/update-agent-context.ps1 -AgentType copilot`
     **IMPORTANT**: Execute it exactly as specified above. Do not add or remove any arguments.
   - If exists: Add only NEW tech from current plan
   - Preserve manual additions between markers
   - Update recent changes (keep last 3)
   - Keep under 150 lines for token efficiency
   - Output to repository root

**Output**: `data-model.md`, `contracts/job-schema.yaml`, `quickstart.md` (all created). Contract tests and agent file update are recommended next steps.

## Phase 2: Task Planning Approach
*This section describes what the `/tasks` command will do - DO NOT execute during `/plan`.*

Concrete Task Generation Strategy for Frontline Job Finder
- Base template: use `.specify/templates/tasks-template.md` as the skeleton.
- Source artifacts: consume `data-model.md`, `contracts/job-schema.yaml`, `quickstart.md`, and `research.md` to synthesize tasks.
   - Task types to generate:
   - Project scaffolding tasks (android module, CI, basic build) [P]
   - Model & persistence tasks (Room entities, migrations, encryption hooks)
   - Networking & auth tasks (FrontlineService, token storage, auth-detection flows)
   - Parsing tasks (JobParser, parsing unit tests using HTML fixtures)
   - UI tasks (Configuration page, Main list view, Quick-view cards, Consolidated Accept Page (Accept All UX), Accepted Jobs screen)
   - Acceptance flow tasks (webview integration, per-job status reporting)
   - Observability & audit tasks (structured logs, audit export, retention enforcement)
   - Testing tasks (unit tests, contract tests for `/jobs`, integration smoke tests, UI smoke tests)
   - Security & compliance tasks (security PR checklist, TOS review for scraping, token-handling audit)
   - Packaging & quickstart tasks (generate APK, README, side-load instructions)

Task creation rules
- One task per atomic deliverable (e.g., "Implement JobParser.parse(html) with unit tests").
- For each contract endpoint (e.g., `GET /jobs`) create a failing contract test task [P].
- For each entity in `data-model.md` create a model task and at least one persistence test.
   - Group related UI tasks into a single Feature task with subtasks (e.g., Consolidated Accept Page (Accept All UX) — list view + per-row accept link + status updates).

Ordering and parallelism
- Ordering: follow dependency order — models → services/parsers → business logic → UI → integration tests → packaging.
- Mark [P] for tasks that can safely run in parallel (scaffold, model files, isolated parser tests).
- Use TDD: generate tests (unit/contract) as the first step for each feature, then implement to make them pass.

Estimated output
- Expected tasks: 18-28 tasks covering scaffolding, core features, tests, security checks, and release steps. The `/tasks` generator should number and order them and annotate parallelizable items with `[P]`.

Mapping examples (these will become individual tasks):
- T-01 Scaffold Android project and CI integration [P]
- T-02 Implement Room entities & migrations for User/Job/FilterPreferences [P]
- T-03 Implement FrontlineService: network client, token handling, per-account auth detection
- T-04 Implement JobParser with unit tests and HTML fixtures
- T-05 Implement Configuration page + persistence UI
- T-06 Implement Main list UI + quick-view cards and notification wiring
- T-07 Implement Consolidated Accept Page (Accept All UX) (links/buttons + status updates)
- T-08 Implement Accepted Jobs screen and navigation
- T-09 Implement audit log, export, and retention enforcement (30 days default)
- T-10 Add contract test for `GET /jobs` using `contracts/job-schema.yaml`
- T-11 Add security review checklist and automated static analysis job in CI
- T-12 Packaging: build APK, update `quickstart.md`, and verify side-load process

Note: `/tasks` will expand these mappings into numbered tasks with estimates and file paths.

## Phase 3+: Future Implementation
*These phases are beyond the scope of the /plan command*

**Phase 3**: Task execution (/tasks command creates tasks.md)  
**Phase 4**: Implementation (execute tasks.md following constitutional principles)  
**Phase 5**: Validation (run tests, execute quickstart.md, performance validation)

## Complexity Tracking
*Fill ONLY if Constitution Check has violations that must be justified*

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| [e.g., 4th project] | [current need] | [why 3 projects insufficient] |
| [e.g., Repository pattern] | [specific problem] | [why direct DB access insufficient] |


## Progress Tracking
*This checklist is updated during execution flow*

**Phase Status**:
- [x] Phase 0: Research complete (/plan command)
- [x] Phase 1: Design complete (/plan command)
- [ ] Phase 2: Task planning complete (/plan command - describe approach only)
- [ ] Phase 3: Tasks generated (/tasks command)
- [ ] Phase 4: Implementation complete
- [ ] Phase 5: Validation passed

**Gate Status**:
- [ ] Initial Constitution Check: PASS
- [ ] Post-Design Constitution Check: PASS
- [ ] All NEEDS CLARIFICATION resolved
- [ ] Complexity deviations documented
 - [x] Initial Constitution Check: PASS
 - [ ] Post-Design Constitution Check: PASS
 - [x] All NEEDS CLARIFICATION resolved (per clarifications present in spec)
 - [x] Complexity deviations documented

---
*Based on Constitution v2.1.1 - See `/memory/constitution.md`*

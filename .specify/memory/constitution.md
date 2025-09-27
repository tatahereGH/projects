<!--
Sync Impact Report

Version change: template -> 1.0.0
Modified principles: (added)
	- Library/Platform neutrality → Security-First (new focused principle)
	- Test-First retained and clarified
	- Observability clarified to Reconciliation & Auditing
Added sections: Additional Constraints (security & runtime), Development Workflow
Removed sections: placeholder tokens replaced; no removals beyond template placeholders
Templates requiring updates:
	- .specify/templates/plan-template.md ⚠ pending
	- .specify/templates/spec-template.md ⚠ pending
	- .specify/templates/tasks-template.md ⚠ pending
	- .github/prompts/constitution.prompt.md ✅ reviewed (no change required)

Follow-up TODOs:
	- Update templates listed above to reference the Security-First and Reconciliation principles.
	- Add CI checks enforcing constitution checks (linting, test coverage, security scanning).
	- Run legal/TOS review for automated Frontline access and ensure credential handling complies with policy.
	- Implement secret storage guidance (Android Keystore) in README and templates.
-->

# FrontLineHelper Constitution

## Core Principles

### I. Security-First (NON-NEGOTIABLE)
All handling of user credentials, authentication tokens, and personal data MUST use platform-secure storage
(Android Keystore for private keys / encrypted SharedPreferences for tokens). No credentials are to be
committed to source control or embedded in binaries. Network calls that include sensitive information
MUST use TLS and validate certificates. Any new dependency that affects security posture MUST include
an explicit security rationale in its pull request.

Rationale: This project interacts with third-party systems on behalf of users. Protecting credentials and
user data is the highest priority; operational compromises are unacceptable.

### II. Privacy & Compliance
Automated access to third-party systems MUST comply with the target service's Terms of Service and
applicable law. The project MUST include a documented manual review for any scraping or programmatic
login behavior. Personal data collection MUST be minimized and stored only when necessary; retention
policies and deletion procedures MUST be documented.

Rationale: Respecting user privacy and third-party contracts reduces legal risk and preserves service
availability for all users.

### III. Test-First (REQUIRED)
All new features MUST have automated tests covering the core behavior (unit + small integration). New
network parsing logic (job scraping/parsing) MUST include deterministic tests using saved HTML fixtures
and edge-case inputs. Pull requests without tests are not accepted except for trivial documentation fixes.

Rationale: Parsing remote pages is brittle; tests prevent regressions and make maintenance safer.

### IV. Observability, Reconciliation & Auditing
Critical flows (authentication, job fetch, alert delivery) MUST emit structured logs and include error
codes. Where the app performs stateful operations (e.g., marking a job as applied or dismissed locally),
the implementation MUST support reconciliation and export of an audit trail for debugging.

Rationale: When remote APIs or pages change, observability and reconciliation enable fast diagnosis and
repair.

### V. Simplicity & User Focus
User experience has priority over feature breadth. The default configuration MUST be conservative
(non-spammy polling, clear opt-ins). UI patterns should favor clarity: quick-view cards for matched jobs,
prominent action affordances, and accessible text sizes. Complex features must be justified by clear value.

Rationale: A small, reliable, easy-to-side-load app is the project goal; complexity increases support cost.

## Additional Constraints (Security & Runtime)

- Credentials: Use Android Keystore / EncryptedSharedPreferences. Provide guidance in README.
- Network: Respect rate limits; default polling interval SHOULD be no more frequent than once per 60
	seconds and MUST be configurable. If Frontline provides an API or webhook, prefer it over scraping.
- Offline: Avoid storing unnecessary personal data; cache only what improves UX and obey retention rules.
- Dependencies: Prefer well-maintained libraries with security patching histories.

## Development Workflow, Review Process & Quality Gates

- Pull requests MUST include a short description, linked issue (if applicable), tests, and a security
	impact statement when the change affects privacy/auth.
- CI MUST run linting, unit tests, and safety scanners. PRs MUST pass CI before merging.
- Code reviews require one approving reviewer for minor changes and two for security- or privacy-impacting
	changes.
- Code should be written that it can be easily ported to other AI IDEs.
- Code should be such that if required it can easily converted for iOS app.

## Documentation

- Make sure there is enough documentation for the code keeping in mind that beginner or intermediate developers can understand the structure of the code.
- Written code should be beginner and intermediate friendly.
 

## Governance

Amendments to this constitution require a documented PR that: (1) describes the change, (2) includes a
migration plan if it affects runtime behavior, and (3) is approved by two maintainers. Versioning follows
semantic versioning for governance changes: MAJOR for incompatible governance changes, MINOR for new
principles or sections, PATCH for clarifications.

**Version**: 1.0.0 | **Ratified**: 2025-09-26 | **Last Amended**: 2025-09-26

## Phase 0 — Research: Frontline Job Finder

Decision: Mobile-first Kotlin Android app, side-load distribution.

Rationale:
- User requested a local-only Android side-load app. Kotlin/Android Studio is the standard path for Android apps and supports secure storage (Android Keystore) and modern libraries (OkHttp, Coroutines, Jetpack).
- Choosing Kotlin reduces portability friction to future iOS ports when using shared patterns.

Unknowns addressed:
- Authentication variability: Frontline accounts vary by district; research recommends a dual-path approach: prefer token/API fetch when available; otherwise use interactive webview login with cookie/session capture for parsing.
- Acceptance behavior: Programmatic acceptance is disallowed in many accounts. The app will implement guided/manual accept flows using the Consolidated Accept Page (Accept All UX) and avoid automated accepts unless explicit, documented user consent and supported API exists.
-- Polling interval vs. constitution: The spec default must align with the constitution-recommended conservative default (60s). If lower intervals are required, they must be behind an explicit "advanced polling" opt-in with a clear warning about provider rate limits and potential TOS implications. Exponential backoff and server-guided Retry-After handling are recommended.

Security & Compliance research notes:
- Use Android Keystore / EncryptedSharedPreferences for token/credential storage. Do not persist raw passwords unless the user explicitly opts in and understands implications.
- All network traffic must use TLS with certificate validation. Any scraping approach must be reviewed for TOS compliance and documented in the audit trail.

Implementation suggestions (non-mandatory):
- Networking: OkHttp + Retrofit (or plain OkHttp) + Kotlin Coroutines for background polling.
- Parsing: Jsoup for HTML parsing with deterministic unit tests using saved HTML fixtures.
- Persistence: Room for structured data (jobs, preferences, accepted jobs, audit log) with encrypted database option if storing sensitive artifacts.

Output: research findings consolidated for Phase 1 design.

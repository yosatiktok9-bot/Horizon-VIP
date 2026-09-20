# HORIZON VIP

On-device Android network reconnaissance platform. Kotlin shell, Python scan engine (Chaquopy), C++ hot paths for packet parsing and fast port loops. No backend, no telemetry, no ads, no login.

Operators run scans against their own networks and authorized targets. Results stay local in Room, export as JSON/CSV/HTML, share via the system sheet.

## Architecture

```
Compose UI → ViewModel → Chaquopy bridge → Python engine
                ↓                              ↓
            Room DB ←──────── results ────── C++ (JNI)
```

- **Kotlin**: UI (Compose), navigation, Room, DataStore, Chaquopy bridge, JNI, exporters
- **Python**: DNS, passive subdomain sources, brute, async port scan, HTTP/TLS probe, tunable checker
- **C++**: IP header parse, checksum, non-blocking connect port loop

## Features

- Network (CIDR) host discovery
- Passive subdomain enum (crt.sh, HackerTarget, AlienVault, urlscan, RapidDNS, BufferOver, CertSpotter)
- Optional brute with wildcard detection
- Async TCP port scan + banner grab
- HTTP probe (status, title, server, security headers)
- TLS cert extraction
- Tunable / CDN origin detection
- Scan history, JSON/CSV/HTML export
- Hacker terminal UI — deep navy, selectable accent, scan line, glitch logo, log console

## Build

Requirements:

- JDK 17
- Python 3.8–3.11 on the build host (Chaquopy)
- Android SDK / NDK, CMake 3.22+
- AGP 8.5+, Kotlin 2.0.20

```bash
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

Chaquopy pulls aiohttp, dnspython, idna at build time. First build is slower while the Python runtime is packaged.

## CI

GitHub Actions runs lint, unit tests, and assembleDebug on push/PR. Release workflow builds on version tags.

## Privacy

No accounts. No analytics. No telemetry. All processing is on-device.

## License

MIT — see LICENSE.

## Contributing

Open issues or PRs against the main branch. Keep the stack multi-language and CI green.

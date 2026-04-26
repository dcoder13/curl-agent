plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "curl-agent"

include(
    "agent-core",
    "extractor",
    "curl-builder",
    "redaction",
    "dedupe",
    "output",
    "instrumentation:okhttp",
    "instrumentation:apache",
    "instrumentation:jdkhttp",
    "instrumentation:huc",
    "tests"
)

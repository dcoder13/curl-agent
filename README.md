# JVM HTTP to cURL Agent

Local debugging tool for JVM services that captures outbound HTTP requests and emits sanitized, copyable cURL commands via a Java agent.

## What this repository contains

- Multi-module Gradle Kotlin DSL Java agent project for outbound HTTP capture.

## Supported HTTP clients (v1)

The agent is designed to capture outbound calls from:

- OkHttp (`okhttp3.RealCall.execute`, `okhttp3.RealCall.enqueue`)
- Apache HttpClient (`org.apache.http.impl.client.InternalHttpClient.doExecute`)
- Java 11+ HttpClient (`java.net.http.HttpClient.send`, `sendAsync`)
- HttpURLConnection fallback (`connect`, `getOutputStream`) for legacy paths

## Prerequisites

- Java 17+
- Network access to download Gradle distribution (or preinstalled local distribution)

## Build and test

From repository root:

```bash
./gradlew test
```

Build agent jar:

```bash
./gradlew :agent-core:jar
```

## Run with Java agent

```bash
java \
  -javaagent:agent-core/build/libs/agent-core-0.1.0-SNAPSHOT.jar \
  -Dcurl.agent.enabled=true \
  -jar app.jar
```

You can also set in IntelliJ Run Configuration VM options:

```text
-javaagent:/absolute/path/to/curl-agent/agent-core/build/libs/agent-core-0.1.0-SNAPSHOT.jar -Dcurl.agent.enabled=true
```

## Output

- stdout logging of generated cURL
- file output at `.logs/generated-curl.log`

Redaction is applied to sensitive values such as `Authorization`, `Cookie`, `Set-Cookie`, and `X-API-Key`.

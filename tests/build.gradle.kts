plugins {
    java
}

dependencies {
    testImplementation(project(":agent-core"))
    testImplementation(project(":extractor"))
    testImplementation(project(":redaction"))
    testImplementation(project(":dedupe"))
    testImplementation(project(":curl-builder"))
    testImplementation(project(":output"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("com.squareup.okhttp3:okhttp:4.12.0")
    testImplementation("org.apache.httpcomponents:httpclient:4.5.14")
}

tasks.test {
    useJUnitPlatform()
}

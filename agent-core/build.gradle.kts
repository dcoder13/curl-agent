plugins {
    `java-library`
}

dependencies {
    api(project(":extractor"))
    implementation(project(":redaction"))
    implementation(project(":dedupe"))
    implementation(project(":curl-builder"))
    implementation(project(":output"))
    implementation(project(":instrumentation:okhttp"))
    implementation(project(":instrumentation:apache"))
    implementation(project(":instrumentation:jdkhttp"))
    implementation(project(":instrumentation:huc"))

    implementation("net.bytebuddy:byte-buddy:1.14.18")
    implementation("net.bytebuddy:byte-buddy-agent:1.14.18")
    implementation("org.slf4j:slf4j-api:2.0.13")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Premain-Class" to "com.curlagent.agent.AgentBootstrap",
                "Can-Redefine-Classes" to "true",
                "Can-Retransform-Classes" to "true"
            )
        )
    }
}

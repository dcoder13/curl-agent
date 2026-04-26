plugins {
    `java-library`
}

dependencies {
    api(project(":extractor"))
    implementation("net.bytebuddy:byte-buddy:1.14.18")
    implementation("org.slf4j:slf4j-api:2.0.13")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

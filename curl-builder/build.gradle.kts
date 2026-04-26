plugins {
    `java-library`
}

dependencies {
    api(project(":extractor"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

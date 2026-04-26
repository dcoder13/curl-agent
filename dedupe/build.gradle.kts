plugins {
    `java-library`
}

dependencies {
    api(project(":extractor"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

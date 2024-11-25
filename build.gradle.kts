plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

// https://mvnrepository.com/artifact/org.assertj/assertj-core
    testImplementation("org.assertj:assertj-core:3.26.3")


}

tasks.test {
    useJUnitPlatform()
}

// Java 컴파일 시 UTF-8 인코딩 설정
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

// 테스트 실행 시 UTF-8 인코딩 설정
tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
}

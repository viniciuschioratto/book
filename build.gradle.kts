plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
	jacoco
}

group = "com.bookstore"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.liquibase:liquibase-core")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0")
	implementation("org.mapstruct:mapstruct:1.6.3")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.3")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy("jacocoTestReport")
	doLast {
		println("View code coverage at:")
		println("file://$buildDir/reports/jacoco/test/html/index.html")
	}
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.9.23"
}

group = "com.alpha"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
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
	// jwts
	implementation ("io.jsonwebtoken:jjwt-api:0.12.3")
	runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.12.3")
	runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.12.3")
	// oauth2 + spring security
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	// data jpa
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	//swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	//s3
	implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	// stomp
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.webjars:webjars-locator-core")
	implementation("org.webjars:sockjs-client:1.0.2")
	implementation("org.webjars:stomp-websocket:2.3.3")
	// spring-kafka
	implementation("org.springframework.kafka:spring-kafka")
	// jackson
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	//swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

	//s3
	implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")

	implementation("org.json:json:20210307")
	implementation("commons-io:commons-io:2.9.0")

	//crawling
	implementation("org.jsoup:jsoup:1.17.2")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

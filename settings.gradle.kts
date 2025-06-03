rootProject.name = "otusJava"
include(
    "hw01-gradle",
    "hw03-generics",
    "hw06-annotations",
    "hw08-gc",
    "L10-byteCodes",
    "L12-solid",
    "L15-structuralPatterns:demo",
    "L16-io:homework",
    "L18-jdbc:demo",
    "L18-jdbc:homework",
    "L21-jpql:class-demo",
    "L21-jpql:homework-template",
    "L22-cache",
    "L25-di:class-demo",
    "L25-di:homework",
    "L31-executors",
    "L32-concurrentCollections:ConcurrentCollections",
    "L32-concurrentCollections:QueueDemo",
    "L38-webflux-chat:client-service",
    "L38-webflux-chat:datastore-service",
)


pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val protobufVer: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
        id("com.google.protobuf") version protobufVer
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}
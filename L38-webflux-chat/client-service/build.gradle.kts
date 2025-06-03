dependencies {
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation ("com.google.code.findbugs:jsr305")

    implementation("org.webjars:webjars-locator-core")
    implementation("org.webjars:sockjs-client")
    implementation("org.webjars:stomp-websocket")
    implementation("org.webjars:bootstrap")

    runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.78.Final:osx-aarch_64")
}



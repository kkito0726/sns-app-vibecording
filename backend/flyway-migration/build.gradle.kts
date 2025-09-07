plugins {
    id("org.flywaydb.flyway") version "10.10.0"
}


dependencies {
    implementation("org.flywaydb:flyway-mysql")
    implementation("mysql:mysql-connector-java:8.0.33") // MySQL 8.0.33 に対応するバージョン
    implementation("org.slf4j:slf4j-simple:1.7.36") // ロギング用
}

flyway {
    url = System.getenv("JDBC_URL") ?: "jdbc:mysql://localhost:3306/flick_db"
    user = System.getenv("DB_USER") ?: "root"
    password = System.getenv("DB_PASSWORD") ?: "password"
    locations = arrayOf(System.getenv("FLYWAY_LOCATIONS") ?: "filesystem:src/main/resources/db/migration")
    loggers = arrayOf("slf4j")
}
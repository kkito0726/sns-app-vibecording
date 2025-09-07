buildscript {
    val jooqVersion = "3.19.10"
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jooq:jooq-codegen:${jooqVersion}")
    }
}

import nu.studer.gradle.jooq.JooqEdition

plugins {
    id("nu.studer.jooq")
}

val jooqVersion = "3.19.10"

dependencies {
    jooqGenerator("org.jooq:jooq-codegen")
    jooqGenerator("org.flywaydb:flyway-mysql") // For parsing the schema
    jooqGenerator("com.mysql:mysql-connector-j") // Added for jOOQ generator classpath
}

jooq {
    version.set(jooqVersion)
    edition.set(JooqEdition.OSS)
    configurations {
        create("main") {
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "com.mysql.cj.jdbc.Driver"
                    url = "jdbc:mysql://localhost:3306/flick_db"
                    user = "root"
                    password = "password"
                }
                generator.apply {
                    database.apply {
                        name = "org.jooq.meta.mysql.MySQLDatabase"
                        inputSchema = "flick_db"
                        includes = ".*"
                        excludes = "flyway_schema_history"
                    }
                    target.apply {
                        packageName = "com.example.flick.gen.jooq"
                        directory = layout.buildDirectory.dir("generated-sources/jooq-kotlin").get().asFile.absolutePath
                    }

                }
            }
        }
    }
}

// Share the generated code as a source set
val generatedSources = tasks.named("generateJooq") {
    outputs.dir(provider { jooq.configurations.getByName("main").jooqConfiguration.generator.target.directory })
}

sourceSets {
    main {
        java {
            srcDir(generatedSources)
        }
    }
}

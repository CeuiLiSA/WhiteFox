import java.nio.file.Files
import java.nio.file.Paths


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {
    namespace = "com.white.fox"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.white.fox"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField("boolean", "IS_DEBUG_MODE", "true")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            buildConfigField("boolean", "IS_DEBUG_MODE", "false")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

dependencies {

    implementation(project(":hermes"))
    implementation(project(":models"))


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}

tasks.register("checkChineseHardcode") {
    group = "verification"
    description = "检查 Kotlin 源码中是否存在硬编码中文"

    doLast {
        val regex = Regex("[\\u4e00-\\u9fa5]+") // 中文字符匹配
        val srcDirs = listOf("src/main/java", "src/main/kotlin")

        val violations = mutableListOf<String>()

        srcDirs.forEach { dir ->
            val path = Paths.get(project.projectDir.path, dir)
            if (Files.exists(path)) {
                Files.walk(path).use { paths ->
                    paths.filter { it.toString().endsWith(".kt") }.forEach { file ->
                        val lines = Files.readAllLines(file)
                        lines.forEachIndexed { index, line ->
                            if (regex.containsMatchIn(line)) {
                                violations.add("${file.toAbsolutePath()}:${index + 1}: $line")
                            }
                        }
                    }
                }
            }
        }

        if (violations.isNotEmpty()) {
            println("❌ 检测到硬编码中文字符串：")
            violations.forEach { println(it) }
            throw GradleException("检测失败：存在中文硬编码，请使用资源文件或字符串常量。")
        } else {
            println("✅ 未发现中文硬编码，检查通过。")
        }
    }
}
//
//tasks.named("preBuild") {
//    dependsOn("checkChineseHardcode")
//}
/*
 * Copyright (c) 2021
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "zachtronics-leaderboard-bot"
rootDir.listFiles()?.forEach {
    if(it.isDirectory && it.list()?.contains("build.gradle.kts") == true) {
        include(it.name)
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral() //required for ksp
        maven { url = uri("https://repo.nokee.dev/release") }
        maven { url = uri("https://repo.nokee.dev/snapshot") }
    }
}

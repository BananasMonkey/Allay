plugins {
    id("buildlogic.common")
}

group = "org.allaymc"
description = "codegen"
version = "1.0.0"

dependencies {
    implementation(libs.javapoet)
    implementation(libs.nbt)
    implementation(libs.gson)
    implementation(libs.fastutil)
    implementation(libs.annotations)
}
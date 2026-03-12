-keep class com.devx.data.di.** { *; }
-keep class com.devx.data.core.** { *; }
-keep class com.devx.data.repository.** { *; }
-keep class com.devx.data.local.datasource.** { *; }
-keep class com.devx.data.remote.datasource.** { *; }
-keep class com.devx.data.local.dao.** { *; }
-keep class com.devx.data.local.entity.** { *; }
-keep class com.devx.data.local.mapper.** { *; }
-keep interface com.devx.data.remote.JikanApiService { *; }
-keep @kotlinx.serialization.Serializable class * { *; }
-keepclassmembers @kotlinx.serialization.Serializable class * { *; }
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.** {
    kotlinx.serialization.KSerializer serializer(...);
}

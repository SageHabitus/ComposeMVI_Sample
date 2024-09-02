-keep public class * {
    public protected *;
}

# Kotlin의 내부 클래스를 유지
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# JNI 메서드를 유지
-keepclasseswithmembers class * {
    native <methods>;
}

# 리플렉션을 통해 접근되는 클래스 유지
-keep class * extends java.lang.reflect.Member

# Gson 관련 ProGuard 규칙
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.** { *; }

# 테스트에서만 사용하는 ProGuard 규칙
-dontobfuscate
-keep class * extends junit.framework.TestCase

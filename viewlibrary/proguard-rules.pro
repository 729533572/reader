# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/luhe/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizationpasses 5
 #包名不使用大小写混合 aA Aa
-dontusemixedcaseclassnames
# 不混淆第三方引用的库
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#
#-dontoptimize
#-keepattributes *Annotation*
#
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
#-keepclassmembers class * extends android.app.Activity {
#   public void *(android.view.View);
#}
#
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
##
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
##如果我们自定了ListView,ScrollView,Gallery这些组件的时候，我们就不能混淆这些自定义的类了，因为在layout里面我们已经引用这个了类，而且已经写死了。同样四大组件也是不能打包混淆的
##四大组件不能混淆
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application {*;}
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
##自定义控件不要混淆
#-keep public class * extends android.view.View {*;}
#
##adapter也不能混淆
#-keep public class * extends android.widget.Adapter {*;}
#
##同样如果你觉得麻烦，就直接将BaseAdpater换成Adapter
##数据模型不要混淆
#-keepnames class * implements java.io.Serializable     #比如我们要向activity传递对象使用了Serializable接口的时候，这时候这个类及类里面#的所有内容都不能混淆
#-keepclassmembers class * implements java.io.Serializable {*;}
#
##不混淆资源类
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}
#
#-dontwarn java.**
##-keep class java.**{*;}
#-dontwarn android.**
##-keep class android.**{*;}
#-dontwarn com.example.luhe.unitlibrary.**
#-keep class com.example.luhe.unitlibrary.**{*;}
#
#-keepclasseswithmembernames class * {
#    @butterknife.* <fields>;
#}
#-keepclasseswithmembernames class * {
#    @butterknife.* <methods>;
#}
#
#-dontwarn javax.**

#-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

#这个很关键，制定某些类保留public方法和属性
-dontwarn com.hysoso.www.**
-keep public class com.hysoso.www.**{public *;}

-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

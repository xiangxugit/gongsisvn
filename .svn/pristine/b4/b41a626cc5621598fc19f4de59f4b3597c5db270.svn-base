# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

# 参考资料：https://blog.csdn.net/guolipeng_network/article/details/74551968

##############################-------- Android基本配置 start ----------##############################

-ignorewarnings                     # 忽略警告，避免打包时某些警告出现
-optimizationpasses 5               # 指定代码的压缩级别
-dontusemixedcaseclassnames         # 是否使用大小写混合包名 混淆时不会产生形形色色的类名
-dontskipnonpubliclibraryclasses    # 是否混淆第三方jar（非公共的库类）
-dontskipnonpubliclibraryclassmembers
-dontpreverify                      # 混淆时是否做预校验
-verbose                            # 混淆时是否记录日志
-dontoptimize                       # 不优化输入的类文件

-keepattributes *Annotation*, SourceFile, InnerClasses, LineNumberTable, Signature, EnclosingMethod
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    #优化 混淆时采用的算法

-keep public class * extends android.app.Activity    # 未指定成员，仅仅保持类名不被混淆
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.View
-keep public class * extends android.app.IntentService
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.hardware.display.DisplayManager
-keep public class * extends android.os.UserManager
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.app.Fragment

-keep public class * extends android.support.v4.widget
-keep public class * extends android.support.v4.**    #  *匹配任意字符不包括.  **匹配任意字符
-keep interface android.support.v4.app.** { *; }    #{ *;}    表示一个类中的所有的东西
-keep class android.support.v4.** { *; }        # 保持一个完整的包不被混淆
-keep class android.os.**{*;}

-keep class **.R$* { *; }
-keep class **.R{ *; }

#实现了android.os.Parcelable接口类的任何类，以及其内部定义的Creator内部类类型的public final静态成员变量，都不能被混淆和删除
-keep class * implements android.os.Parcelable {    # 保持Parcelable不被混淆
  public static final android.os.Parcelable$Creator *;
}

-keepclasseswithmembernames class * {     # 保持 native 方法不被混淆
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {        # 保持自定义控件类不被混淆
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(***);
}

-keepclasseswithmembers class * {        # 保持自定义控件类不被混淆
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {        # 保持自定义控件类不被混淆
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers class * extends android.app.Activity {        # 保持自定义控件类不被混淆
    public void *(android.view.View);
}

-keepclassmembers class * extends android.app.Activity { #保持类成员
   public void *(android.view.View);
}

-keepclassmembers class * extends android.content.Context {
  public void *(android.view.View);
  public void *(android.view.MenuItem);
}

-keepclassmembers enum * {                  # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
  static final long serialVersionUID;
  private static final java.io.ObjectStreamField[] serialPersistentFields;
  private void writeObject(java.io.ObjectOutputStream);
  private void readObject(java.io.ObjectInputStream);
  java.lang.Object writeReplace();
  java.lang.Object readResolve();
}

-dontwarn android.support.**
-dontwarn android.os.**

###############################-------- Android基本配置 end ----------##############################


###############################-------- 第三方依赖配置 start ----------##############################

## for sharedSDK
-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class m.framework.**{*;}


## for Retrofit
-dontnote retrofit2.Platform  # Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontwarn retrofit2.Platform$Java8  # Platform used when running on Java 8 VMs. Will not be used at runtime.
-keepattributes Signature  # Retain generic type information for use by reflection by converters and adapters.
-keepattributes Exceptions  # Retain declared checked exceptions for use by a Proxy instance.
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

## for fastjson
-dontwarn com.alibaba.fastjson.**
#-libraryjars libs/fastjson.jar
-keep class com.alibaba.fastjson.** { *; }
-keepclassmembers class * {
    public <methods>;
}

## for zxing
-keep class com.google.zxing.** {*;}
-dontwarn com.google.zxing.**

## for xutils
-keep class com.lidroid.** { *; }

## for EventBus
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

## for OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**

## for RxJava
-dontwarn javax.annotation.**
-dontwarn javax.inject.**

## for RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# for Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod
-keep class newwater.com.newwater.beans.**{*;}  ## 实体类不混淆（因为Gson中用到了反射）

###############################-------- 第三方依赖配置 end ----------##############################

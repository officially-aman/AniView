#include <jni.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_aman_aniview_ApiConfig_getBaseUrlFromNative(JNIEnv* env, jobject /* this */) {
    // Return the base URL securely
    return env->NewStringUTF("https://api.jikan.moe/v4/");
}

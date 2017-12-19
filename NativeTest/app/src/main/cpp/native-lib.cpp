#include <jni.h>
#include <string>

extern "C"
jstring
Java_com_semaphore_1soft_apps_nativetest_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
jint
Java_com_semaphore_1soft_apps_nativetest_MainActivity_intFromJNI(JNIEnv *env, jobject *obj) {
    int x;
    x = 4;
    return x;
}
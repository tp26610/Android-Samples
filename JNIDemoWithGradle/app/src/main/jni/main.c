#include "brian_com_jnidemowithgradle_Jni.h"

JNIEXPORT jstring JNICALL Java_brian_com_jnidemowithgradle_Jni_getStringFromNative
  (JNIEnv *env, jobject jobj)
  {

    return (*env)->NewStringUTF(env, "Hello From JNI !");
  }
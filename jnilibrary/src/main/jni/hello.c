#include<stdio.h>
#include<stdlib.h>
#include "tools_zhang_com_jnilibrary_NdkUtils.h"

JNIEXPORT jstring JNICALL Java_tools_zhang_com_jnilibrary_NdkUtils_fromJni
        (JNIEnv * env, jclass jc) {
    return (*env)->NewStringUTF(env, "from JNI_fromJni!");
}

JNIEXPORT jstring JNICALL Java_tools_zhang_com_jnilibrary_NdkUtils_getNormalJni
        (JNIEnv * env, jclass jc) {
    return (*env)->NewStringUTF(env, "from JNI_getNormalJni!");
}
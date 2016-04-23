/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */
void
Java_com_example_hellojni_HelloJni_stringFromJNIA( JNIEnv* env,
                                                  jobject thiz, jbyteArray byteArray)
{
#if defined(__arm__)
  #if defined(__ARM_ARCH_7A__)
    #if defined(__ARM_NEON__)
      #if defined(__ARM_PCS_VFP)
        #define ABI "armeabi-v7a/NEON (hard-float)"
      #else
        #define ABI "armeabi-v7a/NEON"
      #endif
    #else
      #if defined(__ARM_PCS_VFP)
        #define ABI "armeabi-v7a (hard-float)"
      #else
        #define ABI "armeabi-v7a"
      #endif
    #endif
  #else
   #define ABI "armeabi"
  #endif
#elif defined(__i386__)
   #define ABI "x86"
#elif defined(__x86_64__)
   #define ABI "x86_64"
#elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
   #define ABI "mips64"
#elif defined(__mips__)
   #define ABI "mips"
#elif defined(__aarch64__)
   #define ABI "arm64-v8a"
#else
   #define ABI "unknown"
#endif

   jbyte *targetImg = (*env)->GetByteArrayElements(env, byteArray, 0);
   jint  targetSize = (*env)->GetArrayLength(env, byteArray);

   int i = 0;
   for (i=0; i< targetSize; i++) {
      targetImg[i] /= 4;
   }
   (*env)->ReleaseByteArrayElements(env, byteArray, targetImg, 0);

//    return (*env)->NewStringUTF(env, "Hello from JNI !  Compiled with ABI " ABI ".");
}

void Java_com_example_hellojni_HelloJni_print(JNIEnv* env, jobject thiz, jbyteArray byteArray)
{

}

// yuv→argb 変換
void Java_com_example_hellojni_HelloJni_yuvtoargb(JNIEnv* env, jobject thiz, jbyteArray yuvArray, jintArray rgbArray, int width, int height) {
   jbyte *yuvImg = (*env)->GetByteArrayElements(env, yuvArray, 0);
   jint *rgbImg = (*env)->GetIntArrayElements(env, rgbArray, 0);

   int total = width * height;
   int x = 0;
   int y = 0;

   for (x=0; x < width; x++) {
      for (y=0; y < height; y++) {
         int y_, u, v;
         int rgb[3];

         y_ = yuvImg[y * width + x];
         u = yuvImg[(y / 2) * (width / 2) + (x / 2) + total];
         v = yuvImg[(y / 2) * (width / 2) + (x / 2) + total + (total / 4)];

         rgb[0] = y_ + (int)(1.772*v);
         rgb[1] = y_ - (int)(0.344*v + 0.714*u);
         rgb[2] = y_ + (int)(1.402*u);
         rgb[0] = rgb[0]>255? 255 : rgb[0]<0 ? 0 : rgb[0];
         rgb[1] = rgb[1]>255? 255 : rgb[1]<0 ? 0 : rgb[1];
         rgb[2] = rgb[2]>255? 255 : rgb[2]<0 ? 0 : rgb[2];

         rgbImg[y * width + x] = 0xFF000000 | (rgb[0] << 16) | (rgb[1] << 8) | (rgb[2]);
      }
   }

   (*env)->ReleaseByteArrayElements(env, yuvArray, yuvImg, 0);
   (*env)->ReleaseIntArrayElements(env, rgbArray, rgbImg, 0);
}

#include <jni.h>
#include <stdlib.h>

static const int MAX_FRAME_BUFFER = 8;
static unsigned char* g_FrameBuffer[MAX_FRAME_BUFFER];

// yuv→argb 変換
extern "C" void Java_com_example_hellojni_HelloJni_yuvtoargb(JNIEnv* env, jobject obj, jbyteArray yuvArray, jintArray rgbArray, int width, int height)
{

}

extern "C" void Java_com_example_hellojni_HelloJni_initiaizeGraphics(JNIEnv* env, jobject obj, int width, int height)
{
    for (int i = 0; i < MAX_FRAME_BUFFER; ++i)
    {
        g_FrameBuffer[i] = new unsigned char[width*height*2]; // YUV形式で保存
    }
}

extern "C" void Java_com_example_hellojni_HelloJni_releaseGraphics(JNIEnv* env, jobject obj)
{
    for (int i = 0; i < MAX_FRAME_BUFFER; ++i)
    {
        delete[] g_FrameBuffer[i];
        g_FrameBuffer[i] = NULL;
    }
}

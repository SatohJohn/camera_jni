#include <jni.h>
#include <stdlib.h>

static const int MAX_FRAME_BUFFER = 8;
static const int MAX_FRAME_MATCH = 32;
static unsigned int*   g_FrameBuffer[MAX_FRAME_BUFFER];
static unsigned char*  g_YFrameBuffer[MAX_FRAME_BUFFER];
static unsigned char*  g_FixedYFrame;
static int g_FrameIndex  = 0;
static int g_ImageLength = 0;
static int g_ImageWidth  = 0;
static int g_ImageHeight = 0;

struct BGRA
{
    unsigned char B, G, R, A;
};
// yuv→argb 変換
extern "C" void Java_com_example_hellojni_HelloJni_yuvtoargb(JNIEnv* env, jobject obj, jintArray rgbArray, jbyteArray yuvArray, int width, int height)
{
    jbyte *yuvImg = (env)->GetByteArrayElements(yuvArray, 0);
    jint *rgbImg = (env)->GetIntArrayElements(rgbArray, 0);

    const int total = width * height;
    const int inc   = width * 2;
    int x = 0;
    int y = 0;

    jbyte* uv = &yuvImg[width * height];
    jbyte* uv_y = uv;
    jbyte* uv_x = NULL;
    for (int i = 0; i < total; i++) {
        if ((i % width) == 0) {
            uv_x = uv_y;
        }
        int y_, u, v;
        int rgb[3];

        y_ = yuvImg[i];
        y_ = (unsigned char) y_;
        u = uv_x[0];
        v = uv_x[1];
        u = (unsigned char) u;
        v = (unsigned char) v;
        u -= 128;
        v -= 128;
        //rgb[2] = (int)(y_ + 1.402*v);
        // rgb[1] = (int)(y_ - 0.344*u - 0.714*v);
        //rgb[0] = (int)(y_ + 1.772*u);
//            rgb[2] = (int)(y_ + (1402*v / 1000));
//            rgb[1] = (int)(y_ - (344*u / 1000) - (714*v / 1000));
//            rgb[0] = (int)(y_ + 1772*u / 1000);
        rgb[2] = (int) (y_ + (1435 * v >> 10));
        rgb[1] = (int) (y_ - (352 * u >> 10) - (731 * v >> 10));
        rgb[0] = (int) (y_ + (1814 * u >> 10));

        rgb[0] = rgb[0] > 255 ? 255 : rgb[0] < 0 ? 0 : rgb[0];
        rgb[1] = rgb[1] > 255 ? 255 : rgb[1] < 0 ? 0 : rgb[1];
        rgb[2] = rgb[2] > 255 ? 255 : rgb[2] < 0 ? 0 : rgb[2];

        if (y_ < 0) y_ = 0;
        else if (y_ > 255) y_ = 255;
        rgbImg[i] = 0xFF000000 | (rgb[0] << 16) | (rgb[1] << 8) | (rgb[2]);

        if ((i + 1) % 2 == 0) {
            uv_x += 2;
        }
        if (i % inc == 0) {
            uv_y += width;
        }
    }

    (env)->ReleaseByteArrayElements(yuvArray, yuvImg, 0);
    (env)->ReleaseIntArrayElements(rgbArray, rgbImg, 0);
}

extern "C" void Java_com_example_hellojni_HelloJni_initiaizeGraphics(JNIEnv* env, jobject obj, int width, int height)
{
    g_ImageLength = width*height;
    g_ImageWidth = width;
    g_ImageHeight = height;
    for (int i = 0; i < MAX_FRAME_BUFFER; ++i)
    {
        g_FrameBuffer[i] = new unsigned int[width*height];
        g_YFrameBuffer[i] = new unsigned char[width*height];
    }
}

extern "C" void Java_com_example_hellojni_HelloJni_releaseGraphics(JNIEnv* env, jobject obj)
{
    for (int i = 0; i < MAX_FRAME_BUFFER; ++i)
    {
        delete[] g_FrameBuffer[i];
        g_FrameBuffer[i] = NULL;

        delete[] g_YFrameBuffer[i];
        g_YFrameBuffer[i] = NULL;
    }
    delete[] g_FixedYFrame;
}

extern "C" void Java_com_example_hellojni_HelloJni_updateFrame(JNIEnv* env, jobject obj, jintArray rgbArray, jbyteArray yuvArray) {

    jint *rgbImg = env->GetIntArrayElements(rgbArray, 0);
    jbyte *yuvImg = env->GetByteArrayElements(yuvArray, 0);

    memcpy(g_FrameBuffer[g_FrameIndex],rgbImg,g_ImageLength*sizeof(unsigned int));
    memcpy(g_YFrameBuffer[g_FrameIndex],yuvImg,g_ImageLength*sizeof(unsigned char));

    for( int i=0; i<g_ImageLength; ++i) {
        int match[MAX_FRAME_MATCH] = {};
        int max = 0;
        int max_index = g_FrameIndex;

        for (int j = 0; j < MAX_FRAME_BUFFER; ++j) {
            int value = g_YFrameBuffer[j][i] >> 3;
            match[value]++;

            if (match[value] > max) {
                max = match[value];
                max_index = j;
            }
        }
        rgbImg[i] = g_FrameBuffer[max_index][i];
/*
        if( max_index != g_FrameIndex )
        {
            int prev_index = max_index-1;
            int next_index = max_index+1;
            if( prev_index < 0) prev_index = MAX_FRAME_BUFFER-1;
            if( next_index >= MAX_FRAME_BUFFER ) next_index = 0;

            int color = 0;
            BGRA* cc;
            int rgb[3] = {};
            cc = (BGRA*)&g_FrameBuffer[prev_index][i];
            rgb[0] += cc->R;
            rgb[1] += cc->G;
            rgb[2] += cc->B;

            cc = (BGRA*)&g_FrameBuffer[max_index][i];
            rgb[0] += cc->R*2;
            rgb[1] += cc->G*2;
            rgb[2] += cc->B*2;

            cc = (BGRA*)&g_FrameBuffer[next_index][i];
            rgb[0] += cc->R;
            rgb[1] += cc->G;
            rgb[2] += cc->B;

            rgb[0] /= 4;
            rgb[1] /= 4;
            rgb[2] /= 4;
            BGRA* a=(BGRA*)&rgbImg[i];
            a->R = rgb[0];
            a->G = rgb[1];
            a->B = rgb[2];
        }
 */
    }
/*
    for( int i=0; i<g_ImageLength; ++i) {
        int color = 0;
        BGRA* cc;
        int rgb[3] = {};
        for (int j = 0; j < MAX_FRAME_BUFFER; ++j)
        {
            cc = (BGRA*)&g_FrameBuffer[j][i];
            rgb[0] += cc->R;
            rgb[1] += cc->G;
            rgb[2] += cc->B;
        }
        rgb[0] /= MAX_FRAME_BUFFER;
        rgb[1] /= MAX_FRAME_BUFFER;
        rgb[2] /= MAX_FRAME_BUFFER;
        BGRA* a=(BGRA*)&rgbImg[i];
        a->R = rgb[0];
        a->G = rgb[1];
        a->B = rgb[2];
    }
*/

    if (++g_FrameIndex >= MAX_FRAME_BUFFER) {
        g_FrameIndex = 0;
    }
    env->ReleaseIntArrayElements(rgbArray, rgbImg, 0);
    env->ReleaseByteArrayElements(yuvArray, yuvImg, 0);
}
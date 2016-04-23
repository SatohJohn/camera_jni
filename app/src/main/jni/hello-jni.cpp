#include <jni.h>
#include <stdlib.h>

static const int MAX_FRAME_BUFFER = 8;
static unsigned char* g_FrameBuffer[MAX_FRAME_BUFFER];

// yuv→argb 変換
extern "C" void Java_com_example_hellojni_HelloJni_yuvtoargb(JNIEnv* env, jobject obj, jbyteArray yuvArray, jintArray rgbArray, int width, int height)
{
    jbyte *yuvImg = (env)->GetByteArrayElements(yuvArray, 0);
    jint *rgbImg = (env)->GetIntArrayElements(rgbArray, 0);

    int total = width * height;
    int x = 0;
    int y = 0;

    jbyte* uv = &yuvImg[width * height];
    jbyte* uv_y = uv;
    jbyte* uv_x = NULL;
    for (y=0; y < height; y++) {
        uv_x = uv_y;
        for (x=0; x < width; x++) {
            int y_, u, v;
            int rgb[3];

            y_ = yuvImg[y * width + x];
            y_ = (unsigned char)y_;
            u = uv_x[0];
            v = uv_x[1];
            u = (unsigned char)u;
            v = (unsigned char)v;
            u -= 128;
            v -=128;
            rgb[2] = (int)(y_ + 1.402*v);
            rgb[1] = (int)(y_ - 0.344*u - 0.714*v);
            rgb[0] = (int)(y_ + 1.772*u);
            rgb[0] = rgb[0]>255? 255 : rgb[0]<0 ? 0 : rgb[0];
            rgb[1] = rgb[1]>255? 255 : rgb[1]<0 ? 0 : rgb[1];
            rgb[2] = rgb[2]>255? 255 : rgb[2]<0 ? 0 : rgb[2];

            if( y_ < 0) y_ = 0;
            else if( y_ > 255) y_=255;
            rgbImg[y * width + x] = 0xFF000000 | (rgb[0] << 16) | (rgb[1] << 8) | (rgb[2]);

            if ((x + 1) % 2 == 0) {
                uv_x+=2;
            }
        }
        if ((y + 1) % 2 == 0) {
            uv_y+=width;
        }
    }

    (env)->ReleaseByteArrayElements(yuvArray, yuvImg, 0);
    (env)->ReleaseIntArrayElements(rgbArray, rgbImg, 0);
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

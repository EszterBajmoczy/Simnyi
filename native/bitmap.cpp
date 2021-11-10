#include "Caff.h"
#include <fstream>


using namespace std;

typedef struct
{
    unsigned int   bfSize;
    unsigned short bfReserved1 = 0;
    unsigned short bfReserved2 = 0;
    unsigned int   bfOffBits = 0x36;
} BITMAPFILEHEADER;

typedef struct
{
    unsigned int   biSize;
    int            biWidth;
    int            biHeight;
    unsigned short biPlanes = 1;
    unsigned short biBitCount = 24;
    unsigned int   biCompression = 0;
    unsigned int   biSizeImage = 0;
    int            biXPelsPerMeter = 5000;
    int            biYPelsPerMeter = 5000;
    unsigned int   biClrUsed = 0;
    unsigned int   biClrImportant = 0;
} BITMAPINFOHEADER;

void bitmap(const Ciff& ciff, const string& outputFileName) {
    if (ciff.pixels.size() == ciff.content_size) {
        BITMAPFILEHEADER bfh;
        BITMAPINFOHEADER bih;
        bih.biSize = sizeof(BITMAPINFOHEADER);
        bfh.bfSize = 2 + sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER) + ciff.content_size;
        bih.biWidth = (int)ciff.width;
        bih.biHeight = (int)ciff.height;
        string fileName = outputFileName + ".bmp";

        ofstream file(fileName, ios::out | ios::binary);
        if (!file)
        {
            printf("Unable to write file\n");
            throw;
        }

        unsigned short bfType = 0x4d42;
        file.write((char*) &bfType, sizeof(bfType));
        file.write((char*) &bfh, sizeof(bfh));
        file.write((char*) &bih, sizeof(bih));

        for (int i = (int)ciff.content_size - 1; i >= 0; i-=3) {
            unsigned int r = ciff.pixels[i];
            unsigned int g = ciff.pixels[i - 1];
            unsigned int b = ciff.pixels[i - 2];
            file.write((char*)&r, 1);
            file.write((char*)&g, 1);
            file.write((char*)&b, 1);
        }
        file.close();
    }
}
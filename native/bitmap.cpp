#include "bitmap.h"
#include <vector>
#include <fstream>

using namespace std;

void bitmap(vector<unsigned int> rgbVector, int width, int height, const string& outputFileName) {
    size_t pixelCount = width * height * 3;

    if (rgbVector.size() == pixelCount) {
        BITMAPFILEHEADER bfh;
        BITMAPINFOHEADER bih;

        /* Magic number for file. It does not fit in the header structure due to alignment requirements, so put it outside */
        unsigned short bfType = 0x4d42;

        bfh.bfReserved1 = 0;
        bfh.bfReserved2 = 0;
        bfh.bfOffBits = 0x36;

        bih.biSize = sizeof(BITMAPINFOHEADER);
        bih.biPlanes = 1;
        bih.biBitCount = 24;
        bih.biCompression = 0;
        bih.biSizeImage = 0;
        bih.biXPelsPerMeter = 5000;
        bih.biYPelsPerMeter = 5000;
        bih.biClrUsed = 0;
        bih.biClrImportant = 0;


        bfh.bfSize = 2 + sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER) + width * height * 3;
        bih.biWidth = width;
        bih.biHeight = height;

        string fileName = outputFileName + ".bmp";

        ofstream file(fileName, ios::out | ios::binary);
        if (!file)
        {
            printf("Unable to write file\n");
            throw;
        }

        /*Write headers*/
        file.write((char*) &bfType, sizeof(bfType));
        file.write((char*)&bfh, sizeof(bfh));
        file.write((char*)&bih, sizeof(bih));

        /*Write bitmap*/
        for (int i = (height * width * 3) - 1; i >= 0; i = i - 3) {
            unsigned int r = rgbVector[i];
            unsigned int g = rgbVector[i - 1];
            unsigned int b = rgbVector[i - 2];
            file.write((char*)&r, 1);
            file.write((char*)&g, 1);
            file.write((char*)&b, 1);
        }
        file.close();
    }
}
#include "bitmap.h"
#include "Caff.h"
#include <vector>
#include <fstream>


using namespace std;

void bitmap(const Ciff& ciff, const string& outputFileName) {
    if (ciff.pixels.size() == ciff.content_size) {
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

        /*Write headers*/
        file.write((char*) &bfType, sizeof(bfType));
        file.write((char*)&bfh, sizeof(bfh));
        file.write((char*)&bih, sizeof(bih));

        /*Write bitmap*/
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
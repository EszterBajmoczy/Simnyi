#ifndef NATIVE_CIFF_H
#define NATIVE_CIFF_H

#include <string>

using namespace std;

namespace native {
    struct CiffHeader
    {
    private:
        char magic[4];
        unsigned long int header_size;
        unsigned long int content_size;
        unsigned long int width;
        unsigned long int height;
        string caption;
        string tags;
    public:
        CiffHeader();
        ~CiffHeader();

        void setMagic(char* magic);
        char* getMagic();

        void setHeaderSize(unsigned long int header_size);
        unsigned long int getHeaderSize();

        void setContentSize(unsigned long int content_size);
        unsigned long int getContentSize();

        void setWidth(unsigned long int width);
        unsigned long int getWidth();

        void setHeight(unsigned long int height);
        unsigned long int getHeight();

        void setCaption(string caption);
        string getCaption();

        void setTags(string tags);
        string getTags();
    };

    struct CiffContent
    {
    private:
        vector<unsigned int> pixels;
    public:
        CiffContent();
        ~CiffContent();

        void setPixels(const vector<unsigned int>& pixels);
        vector<unsigned int> getPixels();
    };

    struct Ciff {
    private:
        CiffHeader ciff_header;
        CiffContent ciff_content;
    public:
        Ciff();
        ~Ciff();

        CiffHeader getCiffHeader();
        CiffContent getCiffContent();
    };
}

#endif //NATIVE_CIFF_H

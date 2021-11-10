#ifndef NATIVE_CIFF_H
#define NATIVE_CIFF_H

#include <string>

using namespace std;

struct CiffHeader {
    char magic[4];
    uint64_t header_size;
    uint64_t content_size;
    uint64_t width;
    uint64_t height;
    string caption;
    string tags;
};

struct CiffContent {
    vector<uint8_t> pixels;
};

struct Ciff {
    CiffHeader ciff_header;
    CiffContent ciff_content;
};

#endif //NATIVE_CIFF_H

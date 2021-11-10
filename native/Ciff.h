#ifndef NATIVE_CIFF_H
#define NATIVE_CIFF_H

#include <vector>

struct Ciff {
    char magic[4];
    uint64_t header_size;
    uint64_t content_size;
    uint64_t width;
    uint64_t height;
    string caption;
    string tags;
    vector<uint8_t> pixels;
};

#endif //NATIVE_CIFF_H

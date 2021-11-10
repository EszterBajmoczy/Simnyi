#ifndef NATIVE_CAFF_H
#define NATIVE_CAFF_H

#include <vector>
#include "Ciff.h"

using namespace std;

class CaffHeader {
public:
    char magic[4];
    uint64_t header_size = 0;
    uint64_t num_anim = 0;
};

class CaffCredits {
public:
    uint16_t year;
    int8_t month;
    int8_t day;
    int8_t hour;
    int8_t minute;
    uint64_t creator_len;
    string creator;
};

struct CaffAnimation {
    uint64_t duration;
    Ciff *ciff;
};

template<typename T>
struct CaffBlock {
    uint8_t id = '0';
    uint64_t length = 0;
    T data;
};

class Caff {
public:
    CaffBlock<CaffHeader> caff_header;
    CaffBlock<CaffCredits> caff_credits;
    vector<CaffBlock<CaffAnimation>> caff_animations;
};

#endif //NATIVE_CAFF_H

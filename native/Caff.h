#ifndef NATIVE_CAFF_H
#define NATIVE_CAFF_H

#include <fstream>
#include <vector>

#include "Ciff.h"

using namespace std;
using namespace native;

class CaffHeader {
public:
    char magic[4];
    uint64_t header_size = 0;
    uint64_t num_anim = 0;
};

class CaffCredits {
public:
    unsigned char year[2];
    unsigned char month;
    unsigned char day;
    unsigned char hour;
    unsigned char minute;
    uint64_t creator_len;
    string creator;

};

struct CaffAnimation {
public:
    uint64_t duration;
    Ciff *ciff;

    CaffAnimation();

    ~CaffAnimation();

    void setDuration(uint64_t duration);

    uint64_t getDuration();

    void setCiff(Ciff *ciff);

    Ciff *getCiff();
};

template <typename T>
struct CaffBlock {
    unsigned char id = '0';
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

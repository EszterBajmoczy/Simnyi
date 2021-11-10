#ifndef NATIVE_CAFF_H
#define NATIVE_CAFF_H

#include <fstream>
#include <vector>

#include "Ciff.h"

using namespace std;

namespace native {
    class CaffHeader {
    public:
        char magic[4];
        uint8_t header_size;
        uint8_t num_anim;

        CaffHeader();

        ~CaffHeader();

    };

    struct CaffCredits {
    public:
        uint8_t year[2];
        uint8_t month;
        uint8_t day;
        uint8_t hour;
        uint8_t minute;
        uint64_t creator_len;
        char *creator;

        ~CaffCredits();

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

    struct CaffBlock {
    public:
        char id;
        uint64_t length;
        char *data;

        CaffBlock();

        ~CaffBlock();

        void setId(char id);

        char getId();

        void setLength(uint64_t length);

        uint64_t getLength();

        void setData(char *data);

        char *getData();
    };

    struct Caff {
        CaffHeader caff_header;
        CaffCredits caff_credits;
        vector<CaffAnimation> caff_animations;
        Caff();
    };
}

#endif //NATIVE_CAFF_H

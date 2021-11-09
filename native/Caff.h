#ifndef NATIVE_CAFF_H
#define NATIVE_CAFF_H

#include <fstream>
#include <vector>

#include "Ciff.h"

using namespace std;

namespace native {
    struct CaffHeader {
    public:
        char magic[4];
        uint8_t header_size;
        uint8_t num_anim;

        CaffHeader();
        ~CaffHeader();

        void setMagic(char* m);
        char* getMagic();

        void setHeaderSize(uint64_t header_size);
        uint64_t getHeaderSize();

        void setNumAnim(uint64_t num_anim);
        uint64_t getNumAnim();
    };

    struct CaffCredits {
    public:
        uint8_t year[2];
        uint8_t month;
        uint8_t day;
        uint8_t hour;
        uint8_t minute;
        unsigned long int creator_len;
        char *creator;

        CaffCredits();
        ~CaffCredits();

        void setCreationYear(uint8_t *year);
        uint8_t *getCreationYear();

        void setCreationMonth(uint8_t month);
        uint8_t getCreationMonth();

        void setCreationDay(uint8_t day);
        uint8_t getCreationDay();

        void setCreationHour(uint8_t hour);
        uint8_t getCreationHour();

        void setCreationMinute(uint8_t minute);
        uint8_t getCreationMinute();

        void setCreatorLen(uint64_t creatorLenght);
        uint64_t getCreatorLen();

        void setCreator(char *creator);
        char* getCreator();
    };

    struct CaffAnimation {
    public:
        uint64_t duration;
        Ciff* ciff;

        CaffAnimation();
        ~CaffAnimation();

        void setDuration(uint64_t duration);
        uint64_t getDuration();

        void setCiff(Ciff* ciff);
        Ciff* getCiff();
    };

    struct CaffBlock {
    public:
        char id;
        uint64_t length;
        char* data;

        CaffBlock();
        ~CaffBlock();

        void setId(char id);
        char getId();

        void setLength(uint64_t length);
        uint64_t getLength();

        void setData(char* data);
        char* getData();
    };

    struct Caff {
    private:
        CaffHeader caff_header;
        CaffCredits caff_credits;
        vector<CaffAnimation> caff_animations;
    };
}

#endif //NATIVE_CAFF_H

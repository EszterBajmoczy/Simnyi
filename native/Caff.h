#ifndef NATIVE_CAFF_H
#define NATIVE_CAFF_H

#include <fstream>
#include <vector>

#include "Ciff.h"

using namespace std;

namespace native {
    struct CaffHeader {
    private:
        char magic[4];
        uint8_t header_size;
        uint8_t num_anim;
    public:
        CaffHeader();
        ~CaffHeader();

        void setMagic(char* m);
        char* getMagic();

        void setHeaderSize(unsigned long int header_size);
        unsigned long int getHeaderSize();

        void setNumAnim(unsigned long int num_anim);
        unsigned long int getNumAnim();
    };

    struct CaffCredits {
    private:
        uint8_t year[2];
        uint8_t month;
        uint8_t day;
        uint8_t hour;
        uint8_t minute;
        unsigned long int creator_len;
        char *creator;
    public:
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

        void setCreatorLen(unsigned long int creatorLenght);
        unsigned long int getCreatorLen();

        void setCreator(char *creator);
        char* getCreator();
    };

    struct CaffAnimation {
    private:
        uint64_t duration;
        Ciff* ciff;
    public:
        CaffAnimation();
        ~CaffAnimation();

        void setDuration(uint64_t duration);
        uint64_t getDuration();

        void setCiff(Ciff* ciff);
        Ciff* getCiff();
    };

    struct Caff {
    private:
        CaffHeader caff_header;
        CaffCredits caff_credits;
        vector<CaffAnimation> caff_animations;
    };
}

#endif //NATIVE_CAFF_H

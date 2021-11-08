#include <cstring>
#include "Caff.h"

using namespace std;

namespace native {
    // Caff header
    native::CaffHeader::CaffHeader() {
        magic[0] = magic[1] = magic[2] = magic[3] = '$';
        header_size = 0;
        num_anim = 0;
    }

    native::CaffHeader::~CaffHeader() {}

    void native::CaffHeader::setMagic(char *magic) {
        strncpy(this->magic, magic, 4);
    }

    char *native::CaffHeader::getMagic() {
        return magic;
    }

    void native::CaffHeader::setHeaderSize(uint64_t header_size) {
        this->header_size = header_size;
    }

    uint64_t native::CaffHeader::getHeaderSize() {
        return header_size;
    }

    void native::CaffHeader::setNumAnim(uint64_t num_anim) {
        this->num_anim = num_anim;
    }

    uint64_t native::CaffHeader::getNumAnim() {
        return num_anim;
    }

    // Caff credits
    native::CaffCredits::CaffCredits() {
        year[0] = year[1] = 0;
        month = 0;
        day = 0;
        hour = 0;
        minute = 0;
        creator_len = 0;
        //TODO check how to set creator
    }

    native::CaffCredits::~CaffCredits() {}

    void native::CaffCredits::setCreationYear(uint8_t* year) {
        this->year[0] = year[0];
        this->year[1] = year[1];
    }

    uint8_t *native::CaffCredits::getCreationYear() {
        return year;
    }

    void native::CaffCredits::setCreationMonth(uint8_t month) {
        this->month = month;
    }

    uint8_t native::CaffCredits::getCreationMonth() {
        return month;
    }

    void native::CaffCredits::setCreationDay(uint8_t day) {
        this->day = day;
    }

    uint8_t native::CaffCredits::getCreationDay() {
        return day;
    }

    void native::CaffCredits::setCreationHour(uint8_t hour) {
        this->hour = hour;
    }

    uint8_t native::CaffCredits::getCreationHour() {
        return hour;
    }

    void native::CaffCredits::setCreationMinute(uint8_t minute) {
        this->minute = minute;
    }

    uint8_t native::CaffCredits::getCreationMinute() {
        return minute;
    }

    void native::CaffCredits::setCreatorLen(uint64_t creator_len) {
        this->creator_len = creator_len;
    }

    uint64_t native::CaffCredits::getCreatorLen() {
        return creator_len;
    }

    //TODO check how to set char*
    void native::CaffCredits::setCreator(char *creator) {
        this->creator = creator;
    }

    char* native::CaffCredits::getCreator() {
        return creator;
    }

    //Caff animation
    native::CaffAnimation::CaffAnimation() {
        duration = 0;
        ciff = nullptr;
    }

    native::CaffAnimation::~CaffAnimation() {}

    void native::CaffAnimation::setDuration(uint64_t duration) {
        this->duration = duration;
    }

    uint64_t native::CaffAnimation::getDuration() {
        return duration;
    }

    void native::CaffAnimation::setCiff(Ciff *ciff) {
        this->ciff = ciff;
    }

    Ciff *native::CaffAnimation::getCiff() {
        return ciff;
    }
}
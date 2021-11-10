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


    native::CaffCredits::~CaffCredits() {
        delete[] creator;
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

    //Caff block
    native::CaffBlock::CaffBlock() {
        id = 0;
        length = 8;
        data = "";
    }

    native::CaffBlock::~CaffBlock() {}

    void native::CaffBlock::setId(char id) {
        this->id = id;
    }
    char native::CaffBlock::getId(){
        return id;
    }

    void native::CaffBlock::setLength(uint64_t length) {
        this->length = length;
    }

    uint64_t native::CaffBlock::getLength() {
        return length;
    }

    void native::CaffBlock::setData(char* data) {
        this->data = data;
    }

    char* native::CaffBlock::getData() {
        return data;
    }

    native::Caff::Caff(){

    }

}
#include <cstring>
#include "Caff.h"

using namespace std;
using namespace native;

//Caff animation
CaffAnimation::CaffAnimation() {
    duration = 0;
    ciff = nullptr;
}

CaffAnimation::~CaffAnimation() {}

void CaffAnimation::setDuration(uint64_t duration) {
    this->duration = duration;
}

uint64_t CaffAnimation::getDuration() {
    return duration;
}

void CaffAnimation::setCiff(Ciff *ciff) {
    this->ciff = ciff;
}

Ciff *CaffAnimation::getCiff() {
    return ciff;
}



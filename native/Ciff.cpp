#include <vector>
#include <cstring>
#include "Ciff.h"

// Ciff header
native::CiffHeader::CiffHeader() :caption(string("")), tags(string("")) {
    magic[0] = magic[1] = magic[2] = magic[3] = '$';
    header_size = 0;
    content_size = 0;
    width = 0;
    height = 0;
}

native::CiffHeader::~CiffHeader() {}

void native::CiffHeader::setMagic(char *magic) {
    strncpy(this->magic, magic, 4);
}

char *native::CiffHeader::getMagic() {
    return magic;
}

void native::CiffHeader::setHeaderSize(unsigned long int header_size) {
    this->header_size = header_size;
}

unsigned long int native::CiffHeader::getHeaderSize() {
    return header_size;
}

void native::CiffHeader::setContentSize(unsigned long int content_size) {
    this->content_size = content_size;
}

unsigned long int native::CiffHeader::getContentSize() {
    return content_size;
}

void native::CiffHeader::setWidth(unsigned long int width) {
    this->width = width;
}

unsigned long int native::CiffHeader::getWidth() {
    return width;
}

void native::CiffHeader::setHeight(unsigned long int height) {
    this->height = height;
}

unsigned long int native::CiffHeader::getHeight() {
    return height;
}

void native::CiffHeader::setCaption(string caption) {
    this->caption = caption;
}

string native::CiffHeader::getCaption() {
    return caption;
}

void native::CiffHeader::setTags(string tags) {
    this->tags = tags;
}

string native::CiffHeader::getTags() {
    return tags;
}

// Ciff content
native::CiffContent::CiffContent() : pixels(vector<unsigned int>(0)) {}

native::CiffContent::~CiffContent() {}

void native::CiffContent::setPixels(const vector<unsigned int> &pixels) {
    this->pixels = pixels;
}

vector<unsigned int> native::CiffContent::getPixels() {
    return pixels;
}

// Ciff
native::Ciff::Ciff() : ciff_header(CiffHeader()), ciff_content(CiffContent()) {}

native::Ciff::~Ciff() {}

native::CiffHeader native::Ciff::getCiffHeader() {
    return ciff_header;
}

native::CiffContent native::Ciff::getCiffContent() {
    return ciff_content;
}

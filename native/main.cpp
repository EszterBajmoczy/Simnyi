#include <iostream>
#include "bitmap.cpp"
#include <iterator>
#include <algorithm>
#include "Caff.h"

using namespace std;

ifstream myfile;

template<typename T>
void readId(CaffBlock<T> &block) {
    myfile.read((char *) &block.id, 1);
    myfile.read((char *) &block.length, 8);
}

CaffBlock<CaffHeader> readCaffHeader() {
    //    caff header always 30 byte;
    CaffBlock<CaffHeader> result = CaffBlock<CaffHeader>();
    readId(result);
    myfile.read((char *) &result.data.magic, 4);
    myfile.read((char *) &result.data.header_size, 8);
    myfile.read((char *) &result.data.num_anim, 8);
    return result;
}

CaffBlock<CaffCredits> readCaffCredits() {
    CaffBlock<CaffCredits> result = CaffBlock<CaffCredits>();
    readId(result);
    myfile.read((char *) &result.data.year, 2);
    myfile.read((char *) &result.data.month, 1);
    myfile.read((char *) &result.data.day, 1);
    myfile.read((char *) &result.data.hour, 1);
    myfile.read((char *) &result.data.minute, 1);
    myfile.read((char *) &result.data.creator_len, 8);
    result.data.creator = std::string(result.data.creator_len, ' ');
    myfile.read(&result.data.creator[0], result.data.creator_len);
    return result;
}

Ciff readCiff() {
    Ciff ciff = Ciff();
    myfile.read(ciff.magic, 4);
    myfile.read((char *) &ciff.header_size, 8);
    myfile.read((char *) &ciff.content_size, 8);
    myfile.read((char *) &ciff.width, 8);
    myfile.read((char *) &ciff.height, 8);
    // caption
    char c = (char) myfile.get();
    while (c != '\n') {
        ciff.caption += c;
        c = (char) myfile.get();
    }
    // tags
    uint64_t byteOfTags = ciff.header_size - uint64_t((4 + 8 + 8 + 8 + 8 + ciff.caption.length()));
    for (int i = 0; i < byteOfTags; i++) {
        ciff.tags += (char) myfile.get();
    }
    char tmp;
    // pixels
    for (int i = 0; i < ciff.content_size; i++) {
        myfile.read(&tmp, 1);
        ciff.pixels.push_back(uint8_t(tmp));
    }
    return ciff;
}

CaffBlock<CaffAnimation> readCaffAnimation() {
    CaffBlock<CaffAnimation> result = CaffBlock<CaffAnimation>();
    readId(result);
    myfile.read((char *) &result.data.duration, 8);
    result.data.ciff = readCiff();
    return result;
}

int main(int argc, char *argv[]) {

    if (argv[1] == nullptr) {
        cerr << "no input file name";
        return 1;
    }

    if (argv[2] == nullptr) {
        cerr << "no output file name";
        return 2;
    }

    if (argc != 3) {
        cerr << "unknown error";
        return 3;
    }

    myfile.open(argv[1], ios::in | ios::binary);
    Caff caff;
    caff.caff_header = readCaffHeader();
    caff.caff_credits = readCaffCredits();
    caff.caff_animations = readCaffAnimation();

    bitmap(caff, argv[2]);

    cout << caff.caff_credits.data.creator;
    return 0;

}
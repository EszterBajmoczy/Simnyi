#include <iostream>
#include "bitmap.cpp"
#include <iterator>
#include <algorithm>
#include "exception"

using namespace std;

ifstream inputFile;

template<typename T>
void readId(CaffBlock<T> &block) {
    inputFile.read((char *) &block.id, 1);
    inputFile.read((char *) &block.length, 8);
}

CaffBlock<CaffHeader> readCaffHeader() {
    //    caff header always 20 byte;
    CaffBlock<CaffHeader> result = CaffBlock<CaffHeader>();
    readId(result);
    inputFile.read((char *) &result.data.magic, 4);
    if (
            result.data.magic[0] != 'C' ||
            result.data.magic[1] != 'A' ||
            result.data.magic[2] != 'F' ||
            result.data.magic[3] != 'F')
        throw exception();
    inputFile.read((char *) &result.data.header_size, 8);
    inputFile.read((char *) &result.data.num_anim, 8);
    return result;
}

CaffBlock<CaffCredits> readCaffCredits() {
    CaffBlock<CaffCredits> result = CaffBlock<CaffCredits>();
    readId(result);
    inputFile.read((char *) &result.data.year, 2);
    inputFile.read((char *) &result.data.month, 1);
    inputFile.read((char *) &result.data.day, 1);
    inputFile.read((char *) &result.data.hour, 1);
    inputFile.read((char *) &result.data.minute, 1);
    inputFile.read((char *) &result.data.creator_len, 8);
    result.data.creator = std::string(result.data.creator_len, ' ');
    inputFile.read(&result.data.creator[0], result.data.creator_len);
    return result;
}

Ciff readCiff() {
    Ciff ciff = Ciff();
    inputFile.read(ciff.magic, 4);
    string magicString;
    if (
            ciff.magic[0] != 'C' ||
            ciff.magic[1] != 'I' ||
            ciff.magic[2] != 'F' ||
            ciff.magic[3] != 'F')
        throw exception();
    inputFile.read((char *) &ciff.header_size, 8);
    inputFile.read((char *) &ciff.content_size, 8);
    inputFile.read((char *) &ciff.width, 8);
    inputFile.read((char *) &ciff.height, 8);
    // caption
    char c;
    while (!inputFile.get(c).eof()) {
        ciff.caption += c;
        if (c == '\n') {
            break;
        }
    }
    // tags
    uint64_t byteOfTags = ciff.header_size - uint64_t((4 + 8 + 8 + 8 + 8 + ciff.caption.length()));
    for (uint64_t i = 0; i < byteOfTags; i++) {
        ciff.tags += (char) inputFile.get();
    }
    char tmp;
    // pixels
    for (uint64_t i = 0; i < ciff.content_size; i++) {
        inputFile.read(&tmp, 1);
        ciff.pixels.push_back(uint8_t(tmp));
    }
    return ciff;
}

CaffBlock<CaffAnimation> readCaffAnimation() {
    CaffBlock<CaffAnimation> result = CaffBlock<CaffAnimation>();
    readId(result);
    inputFile.read((char *) &result.data.duration, 8);
    result.data.ciff = readCiff();
    return result;
}

int main(int argc, char *argv[]) {
    cout << "running" << endl;
    if (argv[1] == nullptr) {
        cerr << "no input file name" << endl;
        return 1;
    }

    if (argv[2] == nullptr) {
        cerr << "no output file name" << endl;
        return 2;
    }

    if (argc != 3) {
        cerr << "Too much argument" << endl;
        return 3;
    }

    inputFile.open(argv[1], ios::in | ios::binary);
    if (inputFile.fail()) {
        cerr << "No input file" << endl;
        return 4;
    }

    cout << "Reading file started." << endl;

    Caff caff;
    try {
        caff.caff_header = readCaffHeader();
        caff.caff_credits = readCaffCredits();
        caff.caff_animations = readCaffAnimation();
    } catch (exception e) {
        cerr << "The file is in a wrong format" << endl;
        return 5;
    }
    inputFile.close();
    cout << "Caff read" << endl;

    bitmap(caff.caff_animations.data.ciff, argv[2]);
    cout << "bitmap made" << endl;
    return 0;

}
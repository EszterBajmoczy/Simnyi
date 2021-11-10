#include <iostream>
#include "bitmap.cpp"
#include <iterator>
#include <algorithm>
#include "Caff.h"

using namespace std;

ifstream myfile;

CaffBlock<CaffHeader> readCaffHeader() {
    //    caff header always 30 byte;
    CaffBlock<CaffHeader> result = CaffBlock<CaffHeader>();
    myfile.read((char *)&result.id, 1);
    if(result.id != uint8_t(1)){
        cerr << "header error";
        abort();
    }
    myfile.read((char *) &result.length, 8);
    myfile.read((char *) &result.data.magic, 4);
    myfile.read((char *) &result.data.header_size, 8);
    myfile.read((char *) &result.data.num_anim, 8);
    return result;
}

CaffBlock<CaffCredits> readCaffCredits() {
    CaffBlock<CaffCredits> result = CaffBlock<CaffCredits>();
    myfile.read((char *) &result.id, 1);
    if(result.id != uint8_t(2)){
        cerr << "header error";
        abort();
    }
    myfile.read((char *) &result.length, 8);
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


    return 0;

}
#include <iostream>
#include "bitmap.cpp"

#include "Caff.h"
#include "Ciff.h"

int main(int argc, char *argv[]) {

    if(argv[1] == nullptr){
        cerr << "no input file name";
        return 1;
    }

    if(argv[2] == nullptr){
        cerr << "no output file name";
        return 2;
    }

    if(argc != 3) {
        cerr << "unknown error";
        return 3;
    }

    streampos size;
    char * memblock;

    ifstream file ("example.bin", ios::in|ios::binary|ios::ate);
    if (file.is_open())
    {
        size = file.tellg();
        memblock = new char [size];
        file.seekg (0, ios::beg);
        file.read (memblock, size);
        file.close();

        cout << "the entire file content is in memory";

        delete[] memblock;
    }
    else cout << "Unable to open file";
    return 0;

}

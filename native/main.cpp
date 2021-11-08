#include <iostream>
#include <vector>
#include <fstream>

using namespace std;


// unsigned long int ==> 8 byte unsigned integer

struct CiffHeader
{
    char magic[4];
    unsigned long int header_size;
    unsigned long int content_size;
    unsigned long int width;
    unsigned long int height;
    string caption;
    string tags;
};

struct CiffContent
{
    vector<unsigned int> pixels;
};

struct Ciff {
    CiffHeader ciff_header;
    CiffContent ciff_content;
};

struct CaffBlock
{
    char id;
    unsigned long int length;
    char* data;
};

struct CaffHeader
{
    char magic[4];
    unsigned long int header_size;
    unsigned long int num_anim;
};

struct CaffCredits
{
    char year[2];
    char month;
    char day;
    char hour;
    char minute;
    unsigned long int creator_len;
    string creator;
};

struct CaffAnimation
{
    unsigned long int duration;
    Ciff ciff;
};


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

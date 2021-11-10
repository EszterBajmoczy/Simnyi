#include <iostream>
#include "bitmap.cpp"
#include <iterator>
#include <cstring>

#include "Caff.h"

using namespace std;
using namespace native;

vector<unsigned char> memblock;
int position = 0;


CaffBlock<CaffHeader> readCaffHeader(){
    //    caff header allways 30 byte;
    CaffBlock<CaffHeader> result = CaffBlock<CaffHeader>();
    memcpy(&result.id, &memblock[position], 1);
    position++;
    memcpy(&result.length, &memblock[position], 8);
    position +=8;
    memcpy(&result.data.magic, &memblock[position], 4);
    position += 4;
    memcpy(&result.data.header_size, &memblock[position], 8);
    position += 8;
    memcpy(&result.data.num_anim, &memblock[position], 8);
    position += 8;
    if (position != 29) {
        cerr << "error caff header read";
        abort();
    }
    return result;
}

CaffBlock<CaffCredits> readCaffCredits(){
    if (position != 29) {
        cerr << "error caff credits read";
        abort();
    }

    CaffBlock<CaffCredits> result = CaffBlock<CaffCredits>();
    std::memcpy(&result.id, &memblock[position], 1);
    position++;
    if(result.id != 2){
        cerr << "caff credits id != 2";
        abort();
    }
    std::memcpy(&result.length, &memblock[position], 8);
    position += 8;

    if(position != 38){
        cerr << "error caff credits read";
        abort();
    }

    std::memcpy(&result.data.year, &memblock[position], 2);
    position += 2;
    std::memcpy(&result.data.month, &memblock[position], 1);
    position++;
    std::memcpy(&result.data.day, &memblock[position], 1);
    position++;
    std::memcpy(&result.data.hour, &memblock[position], 1);
    position++;
    std::memcpy(&result.data.minute, &memblock[position], 1);
    position++;
    std::memcpy(&result.data.creator_len, &memblock[position], 8);
    position += 8;

    uint64_t check = 1+8+2+1+1+1+1+result.data.creator_len;
    if(result.length == check){
        std::memcpy(&result.data.creator[0], &memblock[position], result.data.creator_len);
        position += (int)result.data.creator_len;
    }
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

    ifstream file(argv[1], ios::in | ios::binary);
    if (file.is_open()) {
        cout << "file open, and read to memory." << endl;
        std::copy(
                std::istream_iterator<unsigned char>(file),
                std::istream_iterator<unsigned char>(),
                std::back_inserter(memblock)
        );
        file.close();
        cout << "file is closed!" << endl;
    }

    Caff caff;
    caff.caff_header = readCaffHeader();
    caff.caff_credits = readCaffCredits();









//        for (int i = 0; i < caffHeader.getNumAnim() + 1; i++) {
//            CaffBlock innerCaffBlock;
//            file.read((char *) &innerCaffBlock.id, sizeof(innerCaffBlock.id));
//            file.read((char *) &innerCaffBlock.length, sizeof(innerCaffBlock.length));
//            if (innerCaffBlock.id == '\x02') {
//                CaffCredits caffCredits;
//                file.read((char *) &caffCredits.year, 2);
//                file.read((char *) &caffCredits.month, 1);
//                file.read((char *) &caffCredits.day, 1);
//                file.read((char *) &caffCredits.hour, 1);
//                file.read((char *) &caffCredits.minute, 1);
//                file.read((char *) &caffCredits.creator_len, 8);
//
//                if (caffCredits.getCreatorLen() < innerCaffBlock.length) {
//                    file.read((char *) &caffCredits.creator[0], caffCredits.creator_len);
//                }
//            }
//            cout << innerCaffBlock.id;
//            if (innerCaffBlock.id == '\x03') {
//                CaffAnimation ciffAnimation;
//                file.read((char *) &ciffAnimation.duration, 8);
//                file.read((char *) &ciffAnimation.ciff->ciff_header.magic, 4);
//                file.read((char *) &ciffAnimation.ciff->ciff_header.header_size, 8);
//                file.read((char *) &ciffAnimation.ciff->ciff_header.content_size, 8);
//                file.read((char *) &ciffAnimation.ciff->ciff_header.width, 8);
//                file.read((char *) &ciffAnimation.ciff->ciff_header.height, 8);
//
//                // Read caption
//                char currentCaptionCharacter = '0';
//                int captionSize = 0;
//                while (currentCaptionCharacter != '\n') {
//                    file.read((char *) &currentCaptionCharacter, 1);
//                    ciffAnimation.getCiff()->getCiffHeader().setCaption(
//                            ciffAnimation.getCiff()->getCiffHeader().getCaption() + currentCaptionCharacter);
//                    captionSize++;
//                }
//
//                // Read tags
//                char currentTagCharacter = '0';
//                int index = 0;
//                while (index < ciffAnimation.getCiff()->getCiffHeader().getHeaderSize() - captionSize - 36) {
//                    file.read((char *) &currentTagCharacter, 1);
//                    ciffAnimation.getCiff()->getCiffHeader().setTags(
//                            ciffAnimation.getCiff()->getCiffHeader().getTags() + currentTagCharacter);
//                    index++;
//                }
//
//                // Read pixels
//                uint8_t currentPixel = 0;
//
//                for (int i = 0; i < ciffAnimation.getCiff()->getCiffHeader().getContentSize(); i++) {
//                    file.read((char *) &currentPixel, sizeof(uint8_t));
//                    ciffAnimation.getCiff()->getCiffContent().getPixels().push_back(currentPixel);
//                }
//
//                // Generate image
//                bitmap(ciffAnimation.getCiff()->getCiffContent().getPixels(),
//                       ciffAnimation.getCiff()->getCiffHeader().getWidth(),
//                       ciffAnimation.getCiff()->getCiffHeader().getHeight(),
//                       argv[2]);
//                break;
//            }
//        }
//        file.close();

    return 0;

}
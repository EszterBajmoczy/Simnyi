#include <iostream>
#include "bitmap.cpp"
#include <fstream>
#include <iterator>
#include <cstring>

#include "Caff.h"

using namespace std;
using namespace native;

vector<char> memblock;
int position = 0;

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
                std::istream_iterator<char>(file),
                std::istream_iterator<char>(),
                std::back_inserter(memblock)
        );
        file.close();
        cout << "file is closed!" << endl;
    }


    Caff caff;
    CaffBlock caffBlock;
    caffBlock.id = memblock.front();
    std::memcpy(&caffBlock.length, &memblock[1], 8);
    position = 9;
    std::memcpy(&caff.caff_header.magic, &memblock[position], 4);
    position += 4;
    std::memcpy(&caff.caff_header.header_size, &memblock[position], 8);
    position += 8;
    std::memcpy(&caff.caff_header.num_anim, &memblock[position], 8);
    position += 8;
    if (position != 29) {
        cerr << "error";
        return position;
    }

    char caffCreditsId;
    std::memcpy(&caffCreditsId, &memblock[position], 1);
    position++;
    if(caffCreditsId != 2){
        cerr << "caff credits id != 2";
        return position;
    }
    uint64_t caffCreditsLength;
    std::memcpy(&caffCreditsLength, &memblock[position], 8);
    position += 8;

    std::memcpy(&caff.caff_credits.year[0], &memblock[position], 1);
    position++;
    std::memcpy(&caff.caff_credits.year[1], &memblock[position], 1);
    position++;
    std::memcpy(&caff.caff_credits.month, &memblock[position++], 1);
    std::memcpy(&caff.caff_credits.day, &memblock[position++], 1);
    std::memcpy(&caff.caff_credits.hour, &memblock[position++], 1);
    std::memcpy(&caff.caff_credits.minute, &memblock[position++], 1);
    std::memcpy(&caff.caff_credits.creator_len, &memblock[position], 8);
    position += 8;
    caff.caff_credits.creator = new char[caff.caff_credits.creator_len]();
    std::strncpy(caff.caff_credits.creator, &memblock[position], caff.caff_credits.creator_len);
    position += (int)caff.caff_credits.creator_len;
    cout << "cica";








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
#include <iostream>
#include "bitmap.cpp"
#include <iterator>
#include <cstring>
#include <algorithm>



#include "Caff.h"

using namespace std;
using namespace native;

ifstream myfile;

CaffBlock<CaffHeader> readCaffHeader(){
    //    caff header allways 30 byte;
    CaffBlock<CaffHeader> result = CaffBlock<CaffHeader>();
    myfile.read((char*)&result.id, 1);
    myfile.read((char*)&result.length, 8);
    myfile.read((char*)&result.data.magic, 4);
    myfile.read((char*)&result.data.header_size, 8);
    myfile.read((char*)&result.data.num_anim, 8);
    return result;
}

CaffBlock<CaffCredits> readCaffCredits(){

    CaffBlock<CaffCredits> result = CaffBlock<CaffCredits>();
    myfile.read((char*)&result.id, 1);
    myfile.read((char*)&result.length, 8);

    myfile.read((char*)&result.data.year, 2);
    myfile.read((char*)&result.data.month, 1);
    myfile.read((char*)&result.data.day, 1);
    myfile.read((char*)&result.data.hour, 1);
    myfile.read((char*)&result.data.minute, 1);
    myfile.read((char*)&result.data.creator_len, 8);

    if (result.data.creator_len < result.length) {
        result.data.creator = std::string(result.data.creator_len, ' ');
        myfile.read(&result.data.creator[0], result.data.creator_len);
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

    myfile.open(argv[1], ios::in | ios::binary);



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
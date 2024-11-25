package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WiseSayingService {
    private final FileWiseSayingRepository fileWiseSayingRepository;

    public WiseSayingService(FileWiseSayingRepository fileWiseSayingRepository) {
        this.fileWiseSayingRepository = fileWiseSayingRepository;
    }

    // 명언 조회
    public WiseSaying getWiseSaying(int id){
        return fileWiseSayingRepository.findById(id);
    }

    // 명언 추가
    public int addWiseSaying(String author, String wiseSaying){
        return fileWiseSayingRepository.save(new WiseSaying(fileWiseSayingRepository.getId(), author, wiseSaying));
    }

    // 명언 수정
    public WiseSaying updateWiseSaying(WiseSaying wiseSaying, String newWiseSaying, String newAuthor){
        wiseSaying.setWiseSaying(newWiseSaying);
        wiseSaying.setAuthor(newAuthor);
        return fileWiseSayingRepository.update(wiseSaying);
    }
    // 명언 삭제
    public int deleteWiseSaying(String cmd){
        String tmp = cmd.split("=")[1];
        return fileWiseSayingRepository.deleteById(Integer.parseInt(tmp));
    }


    // 전체 목록 조회
    public List<WiseSaying> getSayingList(){
        return new ArrayList<>(fileWiseSayingRepository
                .findAll()
                .values());
    }

    public void build() throws IOException {
        fileWiseSayingRepository.build();
    }

    public List<WiseSaying> search(String keywordType, String keyword) {
        return fileWiseSayingRepository.search(keywordType, keyword);
    }
}

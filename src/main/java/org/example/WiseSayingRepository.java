package org.example;


import java.io.IOException;
import java.util.LinkedHashMap;

public interface WiseSayingRepository {
    // 성공 시 아이디값 반환
    int save(WiseSaying wiseSaying);
    WiseSaying update(WiseSaying wiseSaying);
    int deleteById(int id);

    LinkedHashMap<Integer, WiseSaying> findAll();
    WiseSaying findById(int id);
}

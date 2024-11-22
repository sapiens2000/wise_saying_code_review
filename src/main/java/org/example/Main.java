package org.example;

import java.io.*;
import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        LinkedHashMap<Integer, WiseSaying> wiseSayingMap = new LinkedHashMap<>();

        FileWiseSayingRepository fileWiseSayingRepository = new FileWiseSayingRepository(wiseSayingMap);
        WiseSayingService wiseSayingService = new WiseSayingService(fileWiseSayingRepository);
        WiseSayingController wiseSayingController = new WiseSayingController(wiseSayingService);
        wiseSayingController.run();

    }
}

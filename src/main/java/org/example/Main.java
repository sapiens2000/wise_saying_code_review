package org.example;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        App app = new App();
        app.run(new BufferedReader(new InputStreamReader(System.in)), 0);
    }
}

class App {

    public void run(BufferedReader br, int mode) {
        // 1 = test
        FileWiseSayingRepository fileWiseSayingRepository = new FileWiseSayingRepository(mode);

        WiseSayingService wiseSayingService = new WiseSayingService(fileWiseSayingRepository);
        WiseSayingController wiseSayingController = new WiseSayingController(wiseSayingService, br);
        try {
            wiseSayingController.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

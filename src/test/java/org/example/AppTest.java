package org.example;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Scanner;

public class AppTest {

    public static String run(String cmd){
        // 리디렉션
        BufferedReader br = TestUtil.genScanner(cmd);
        ByteArrayOutputStream byteArrayOutputStream = TestUtil.setOutToByteArray();

        // 실행
        App app = new App();

        //test mode
        app.run(br, 1);

        // 리디렉션 초기화
        TestUtil.clearSetOutToByteArray(byteArrayOutputStream);
        
        
        return byteArrayOutputStream.toString();
    }

    public static void clear(){
        // 기존 데이터 삭제
        File file = new File("db\\test\\wiseSaying\\");

        while(file.exists()){
            File[] fileList = file.listFiles();

            for(int i=0;i<fileList.length;i++){
                fileList[i].delete();
            }

            if(fileList.length == 0 && file.isDirectory()){
                file.delete();
            }

        }


        File directory = new File("db\\test\\wiseSaying");
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.err.println("생성 실패: " + directory.getAbsolutePath());
            }
        }

    }
}

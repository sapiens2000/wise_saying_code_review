package org.example;

import java.io.*;
import java.util.List;

public class WiseSayingController {
    private WiseSayingService wiseSayingService;
    private BufferedReader br;

    public WiseSayingController(WiseSayingService wiseSayingService) {
        this.wiseSayingService = wiseSayingService;
        this.br = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() throws IOException {
        boolean status = true;
        while (status) {
            System.out.print("명령) ");
            String command = br.readLine();

            if (command.equals("종료")) {
                status = false;
                break;
            } else if (command.equals("빌드")) {
                wiseSayingService.build();
                System.out.println("data.json 파일의 내용이 갱신되었습니다.");
            } else if (command.equals("등록")) {
                System.out.print("명언 : ");
                String wiseSaying = br.readLine();
                System.out.print("작가 : ");
                String author = br.readLine();

                int id = wiseSayingService.addWiseSaying(author, wiseSaying);
                System.out.println(id + "번 명언이 등록되었습니다.");
            } else if (command.equals("목록")) {
                System.out.println("번호 / 작가 / 명언");
                System.out.println("----------------------");
                for (WiseSaying cur : wiseSayingService.getSayingList().reversed()) {
                    System.out.printf("%d / %s / %s\n", cur.getId(), cur.getAuthor(), cur.getWiseSaying());
                }
            } else if (command.startsWith("삭제?id=")) {
                int result = wiseSayingService.deleteWiseSaying(command);
                if(result == -1){
                    System.out.println(result + "번 명언은 존재하지 않습니다");
                }else{
                    System.out.println(result + "번 명언이 삭제되었습니다.");
                }
            } else if (command.startsWith("수정?id=")) {
                // 입력 값 오류 처리 필요
                int targetId = Integer.parseInt(command.split("=")[1]);
                WiseSaying oldWiseSaying = wiseSayingService.getWiseSaying(targetId);

                if(oldWiseSaying == null){
                    System.out.println(targetId + "번 명언은 존재하지 않습니다.");
                    continue;
                }
                System.out.println("명언(기존) : " + oldWiseSaying.getWiseSaying());
                System.out.print("명언 : ");
                String newWiseSaying = br.readLine();

                System.out.println("작가(기존) : " + oldWiseSaying.getAuthor());
                System.out.print("작가 : ");
                String newAuthor = br.readLine();

                WiseSaying updated = wiseSayingService.updateWiseSaying(oldWiseSaying, newWiseSaying, newAuthor);
                System.out.printf("%d번 명언이 수정되었습니다.\n", updated.getId());

            }
        }
    }
}

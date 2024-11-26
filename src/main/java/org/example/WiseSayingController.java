package org.example;

import java.io.*;
import java.util.List;


public class WiseSayingController {
    private final WiseSayingService wiseSayingService;
    private BufferedReader br;

    public WiseSayingController(WiseSayingService wiseSayingService, BufferedReader br) {
        this.wiseSayingService = wiseSayingService;
        this.br = br;
    }

    public void run() throws IOException {
        while (true) {
            System.out.print("명령) ");
            String cmd = br.readLine();

            if (cmd.equals("종료")) break;
            else if (cmd.equals("빌드")) build();
            else if (cmd.equals("등록")) register();
            // 목록 커맨드 리팩토링 좀 해야할듯..
            else if (cmd.equals("목록")) list(cmd);
            else if (cmd.contains("목록?page")) listWithPage(cmd);
            else if (cmd.startsWith("목록?")) search(cmd);
            else if (cmd.startsWith("삭제?id=")) delete(cmd);
            else if (cmd.startsWith("수정?id=")) update(cmd);
        }

    }

    private void build() throws IOException {
        wiseSayingService.build();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }

    private void register() throws IOException {
        System.out.print("명언 : ");
        String wiseSaying = br.readLine();
        System.out.print("작가 : ");
        String author = br.readLine();

        int id = wiseSayingService.addWiseSaying(author, wiseSaying);
        System.out.println(id + "번 명언이 등록되었습니다.");
    }

    private void delete(String cmd){
        int result = wiseSayingService.deleteWiseSaying(cmd);
        if(result == -1){
            System.out.println(result + "번 명언은 존재하지 않습니다.");
        }else{
            System.out.println(result + "번 명언이 삭제되었습니다.");
        }
    }

    private void update(String cmd) throws IOException {
        int targetId = Integer.parseInt(cmd.split("=")[1]);
        WiseSaying oldWiseSaying = wiseSayingService.getWiseSaying(targetId);

        if(oldWiseSaying == null){
            System.out.println(targetId + "번 명언은 존재하지 않습니다.");
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

    // 페이지 명시 O 목록 조회
    private void listWithPage(String cmd) {
        String[] tokens = cmd.split("=");
        System.out.println(tokens[1]);
        // default : 1
        int curPage = Integer.parseInt(tokens[1]);

        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        List<WiseSaying> wiseSayingList = wiseSayingService.getSayingList().reversed();

        // 총 개수
        int totalSize = wiseSayingList.size();
        int pageSize = 5;

        // 총 페이지 개수
        int totalPage = ((totalSize - 1) / pageSize) + 1;

        int startIdx = (curPage - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, totalSize);

        wiseSayingList.subList(startIdx, endIdx)
                .forEach(e -> System.out.printf("%d / %s / %s\n", e.getId(), e.getAuthor(), e.getWiseSaying()));

        System.out.printf("페이지 : %d / [%d]\n", curPage, totalPage);
    }
    // 페이지 명시 X (1번 페이지) 조회
    private void list(String cmd){
        String[] tokens = cmd.split("=");

        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        List<WiseSaying> wiseSayingList = wiseSayingService.getSayingList().reversed();

        // default : 1
        int curPage = 1;
        // 총 개수
        int totalSize = wiseSayingList.size();
        int pageSize = 5;

        // 총 페이지 개수
        int totalPage = ((totalSize - 1) / pageSize) + 1;

        int startIdx = (curPage - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, totalSize);

        wiseSayingList.subList(startIdx, endIdx)
                        .forEach(e -> System.out.printf("%d / %s / %s\n", e.getId(), e.getAuthor(), e.getWiseSaying()));

        System.out.printf("페이지 : %d / [%d]\n", curPage, totalPage);
    }

    private void search(String cmd){
        String[] tokens = cmd.split("&");

        String keywordType = tokens[0].split("=")[1];
        String keyword = tokens[1].split("=")[1];

        System.out.println("----------------------");
        System.out.println("검색타입 : " + keywordType);
        System.out.println("검색어 : " + keyword);
        System.out.println("----------------------");
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        List<WiseSaying> result = wiseSayingService.search(keywordType, keyword);

        for (WiseSaying cur : result.reversed()) {
            System.out.printf("%d / %s / %s\n", cur.getId(), cur.getAuthor(), cur.getWiseSaying());
        }

    }
}

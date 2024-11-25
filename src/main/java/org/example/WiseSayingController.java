package org.example;

import java.io.*;


public class WiseSayingController {
    private final WiseSayingService wiseSayingService;
    private BufferedReader br;

    public WiseSayingController(WiseSayingService wiseSayingService, BufferedReader br) {
        this.wiseSayingService = wiseSayingService;
        this.br = br;
    }

    public void run() {
        try{
            while (true) {
                System.out.print("명령) ");
                String cmd = br.readLine();

                if (cmd.equals("종료")) break;
                else if (cmd.equals("빌드")) build();
                else if (cmd.equals("등록")) register();
                else if (cmd.equals("목록")) list();
                else if (cmd.startsWith("삭제?id=")) delete(cmd);
                else if (cmd.startsWith("수정?id=")) update(cmd);
            }
        } catch(Exception e){
            e.printStackTrace();
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

    private void list(){
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (WiseSaying cur : wiseSayingService.getSayingList().reversed()) {
            System.out.printf("%d / %s / %s\n", cur.getId(), cur.getAuthor(), cur.getWiseSaying());
        }
    }
}

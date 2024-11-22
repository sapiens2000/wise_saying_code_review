package org.example;

import java.io.*;
import java.util.LinkedHashMap;

public class FileWiseSayingRepository implements WiseSayingRepository {
    private final LinkedHashMap<Integer, WiseSaying> wiseSayingMap;
    private BufferedReader br;
    private BufferedWriter bw;
    private StringBuilder sb;

    private int id;

    private static final String idFile = "lastId.txt";
    private static final String basePath = System.getProperty("user.dir") + "\\db\\wiseSaying\\";

    public FileWiseSayingRepository(LinkedHashMap<Integer, WiseSaying> wiseSayingMap) {
        this.wiseSayingMap = wiseSayingMap;
        this.sb = new StringBuilder();
        this.load();
    }

    private void load() {
        File idFile = new File(basePath + "lastId.txt");

        // id 파일 읽기
        if (!idFile.exists()) {
            try {
                this.id = 1;
                idFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                br = new BufferedReader(new FileReader(idFile));
                this.id = Integer.parseInt(br.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 전체 데이터 읽기
        File fileList = new File(basePath);

        for (String fileName : fileList.list()) {
            // null 아닐 경우 데이터 읽기
            if (fileName != null) {
                try {
                    File inputData = new File(basePath + "\\" + fileName);
                    br = new BufferedReader(new FileReader(inputData));

                    // 데이터 파일 존재하면 거기서 읽을 것인지?
                    if(fileName.equals("data.json")){
                        //TODO
                    }else if (fileName.endsWith("json")) {
                        // 개별 파일 하나씩 읽기
                        
                        br.readLine();
                        // { 버리기
                        String data = br.readLine();
                        sb.setLength(0);
                        sb.append(data.split(" : ")[1]);
                        sb.deleteCharAt(sb.length() - 1);
                        int id = Integer.parseInt(sb.toString());

                        // content 읽기
                        data = br.readLine();
                        sb.setLength(0);
                        sb.append(data.split(" : ")[1]);
                        sb.deleteCharAt(0);
                        sb.deleteCharAt(sb.length() - 1);
                        sb.deleteCharAt(sb.length() - 1);
                        String content = sb.toString();
                        // author 읽기
                        data = br.readLine();
                        sb.setLength(0);
                        sb.append(data.split(" : ")[1]);
                        sb.deleteCharAt(0);
                        sb.deleteCharAt(sb.length() - 1);
                        String author = sb.toString();
                        wiseSayingMap.put(id, new WiseSaying(id, author, content));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int save(WiseSaying newWiseSaying) {
        wiseSayingMap.put(newWiseSaying.getId(), newWiseSaying);

        File dataFile = new File(basePath + newWiseSaying.getId() + ".json");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            bw = new BufferedWriter(new FileWriter(dataFile));

            // 데이터 json 구조로 생성 (분리 예정)

            String jsonStr = String.format("""
                    {
                    \t"id":"%d",
                    \t"content":"%s",
                    \t"author":"%s"
                    }
                    ,
                    """, newWiseSaying.getId(), newWiseSaying.getWiseSaying(), newWiseSaying.getAuthor());
            String jsonData = "{\n"
                    + "\t\"id\" : " + newWiseSaying.getId() + ",\n"
                    + "\t\"content\" : \"" + newWiseSaying.getWiseSaying() + "\",\n"
                    + "\t\"author\" : \"" + newWiseSaying.getAuthor() + "\"\n"
                    + "}";

            bw.write(jsonStr);
            bw.flush();

            File idFile = new File(FileWiseSayingRepository.idFile);
            if (!idFile.exists()) {
                try {
                    idFile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            jsonData = "" + newWiseSaying.getId();
            bw = new BufferedWriter(new FileWriter(idFile));
            bw.write(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 변경 예정
        this.id++;
        return newWiseSaying.getId();
    }

    @Override
    public WiseSaying update(WiseSaying wiseSaying) {
        File dataFile = new File(basePath + wiseSaying.getId() + ".json");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            bw = new BufferedWriter(new FileWriter(dataFile));

            // 데이터 json 구조로 생성 (분리 예정)
            String jsonData = "{\n"
                    + "\t\"id\" : " + wiseSaying.getId() + ",\n"
                    + "\t\"content\" : \"" + wiseSaying.getWiseSaying() + "\",\n"
                    + "\t\"author\" : \"" + wiseSaying.getAuthor() + "\"\n"
                    + "}";

            bw.write(jsonData);
            bw.flush();

            File idFile = new File(FileWiseSayingRepository.idFile);
            if (!idFile.exists()) {
                try {
                    idFile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            jsonData = "" + wiseSaying.getId();
            bw = new BufferedWriter(new FileWriter(idFile));
            bw.write(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wiseSaying;
    }

    @Override
    public int deleteById(int id) {
        if (wiseSayingMap.get(id) == null) {
            return -1;
        }
        wiseSayingMap.remove(id);
        return id;
    }

    @Override
    public LinkedHashMap<Integer, WiseSaying> findAll() {
        return wiseSayingMap;
    }

    @Override
    public WiseSaying findById(int id) {
        return wiseSayingMap.get(id);
    }

    // 생성을 위한 id 값을 반환
    public int getId() {
        return this.id;
    }

    public void build() {
        try {
            File jsonDataFile = new File(basePath + "data.json");
            bw = new BufferedWriter(new FileWriter(jsonDataFile));

            sb.setLength(0);
            sb.append("[\n");
            for (int key : wiseSayingMap.keySet()) {
                WiseSaying cur = wiseSayingMap.get(key);

                // json 구조로 생성
                sb.append("\t{\n")
                    .append("\t\t\"id\" : " + cur.getId() + ",\n")
                    .append("\t\t\"content\" : \"" + cur.getWiseSaying() + "\",\n")
                    .append("\t\t\"author\" : \"" + cur.getAuthor() + "\"\n")
                    .append("\t}");
                if(key < wiseSayingMap.size()){
                    sb.append(",");
                }

            }
            sb.append("\n]");
            bw.write(sb.toString());
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

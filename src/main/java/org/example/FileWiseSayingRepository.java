package org.example;

import java.io.*;
import java.util.LinkedHashMap;

public class FileWiseSayingRepository implements WiseSayingRepository {
    private final LinkedHashMap<Integer, WiseSaying> wiseSayingMap = new LinkedHashMap<>();
    private BufferedReader br;
    private BufferedWriter bw;
    private StringBuilder sb;

    private int id;

    private static final String idFile = "lastId.txt";
    private final String basePath;

    public FileWiseSayingRepository(int mode) {
        this.sb = new StringBuilder();
        basePath = mode == 1 ? System.getProperty("user.dir") + "\\db\\test\\wiseSaying\\"
                : System.getProperty("user.dir") + "\\db\\wiseSaying\\";
        this.load();
    }

    private void load() {
        File idFile = new File(basePath + "lastId.txt");

        // id 파일 읽기
        if (!idFile.exists()) {
            try {
                this.id = 1;
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
        if(fileList.list() != null){
            for (String fileName : fileList.list()) {
                try {
                    File inputData = new File(basePath + "\\" + fileName);
                    br = new BufferedReader(new FileReader(inputData));
                    if (!fileName.startsWith("data") && fileName.endsWith("json")) {
                        // 개별 파일 하나씩 읽기
                        br.readLine();
                        // { 버리기
                        String data = br.readLine();
                        sb.setLength(0);
                        sb.append(data.split(":")[1]);

                        // 문자열 끝 , 제거
                        sb.deleteCharAt(sb.length() - 1);
                        int id = Integer.parseInt(sb.toString());

                        // content 읽기
                        data = br.readLine();
                        sb.setLength(0);
                        sb.append(data.split(":")[1]);
                        sb.deleteCharAt(0);
                        sb.deleteCharAt(sb.length() - 1);
                        sb.deleteCharAt(sb.length() - 1);
                        String content = sb.toString();

                        // author 읽기
                        data = br.readLine();
                        sb.setLength(0);
                        sb.append(data.split(":")[1]);
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
            bw.write(newWiseSaying.toJson());
            bw.flush();

            File idFile = new File(FileWiseSayingRepository.idFile);
            if (!idFile.exists()) {
                try {
                    idFile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            bw = new BufferedWriter(new FileWriter(idFile));
            
            // 마지막 id 저장
            bw.write(newWiseSaying.getId());
            bw.flush();

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
            bw.write(wiseSaying.toJson());
            bw.flush();

            File idFile = new File(FileWiseSayingRepository.idFile);
            if (!idFile.exists()) {
                try {
                    idFile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            bw = new BufferedWriter(new FileWriter(idFile));
            bw.write(wiseSaying.getId());
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

    public void build() throws IOException {
        File jsonDataFile = new File(basePath + "data.json");
        bw = new BufferedWriter(new FileWriter(jsonDataFile));

        sb.setLength(0);
        sb.append("[\n");
        for (int key : wiseSayingMap.keySet()) {
            WiseSaying cur = wiseSayingMap.get(key);

            // json 구조로 생성
            sb.append(cur.toJson());
            if(key < wiseSayingMap.size()){
                sb.append(",\n");
            }

        }
        sb.append("\n]");
        bw.write(sb.toString());
        bw.flush();
    }
}

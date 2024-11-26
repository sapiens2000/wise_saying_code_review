package org.example;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WiseSayingControllerTest {
    @BeforeEach
    void beforeEach() {
        AppTest.clear();
    }

    // 테스트 데이터 생성 메서드
    private String setupTestData(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= count; i++) {
            sb.append("등록\n")
                    .append(i).append("번 메시지\n")
                    .append(i).append("번 작가\n");
        }
        return sb.toString();
    }

    @DisplayName("명언 등록 후 확인")
    @Test
    void 등록성공() {
        final String out = AppTest.run("""
                등록
                1번 메시지
                1번 작가
                종료
                """);
        assertThat(out)
                .contains("명언 :")
                .contains("작가 :")
                .contains("1번 명언이 등록되었습니다.");
    }

    @DisplayName("등록 후 수정 성공")
    @Test
    void 수정성공(){
        final String out = AppTest.run("""
                등록
                1번 메시지
                1번 작가
                수정?id=1
                수정 테스트 내용
                수정 테스트 작가
                종료
                """);
        assertThat(out)
                .contains("명언(기존) :")
                .contains("작가(기존) :");

    }

    @DisplayName("등록 후 삭제 성공")
    @Test
    void 삭제성공후조회시도(){
        final String out = AppTest.run("""
                등록
                1번 메시지
                1번 작가
                삭제?id=1
                삭제?id=1
                종료
                """);
        assertThat(out)
                .contains("1번 명언이 삭제되었습니다.")
                .contains("1번 명언은 존재하지 않습니다.");
    }

    @DisplayName("등록 후 목록 확인")
    @Test
    void 등록후목록(){
        final String out = AppTest.run("""
                등록
                1번 메시지
                1번 작가
                목록
                종료
                """);

        assertThat(out)
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains("1 / 1번 작가 / 1번 메시지");

    }

    @DisplayName("빌드 성공")
    @Test
    void 빌드성공(){
        final String out = AppTest.run("""               
                빌드
                종료
                """);
        assertThat(out)
                .contains("data.json 파일의 내용이 갱신되었습니다.");
    }
    
    @DisplayName("등록 후 검색 성공")
    @Test
    void 작가검색성공(){
        final String out = AppTest.run("""
                등록
                1번 메시지
                1번 작가
                목록?keywordType=author&keyword=작가
                종료
                """);
        
        assertThat(out)
                .contains("검색타입 : author")
                .contains("검색어 : 작가")
                .contains("----------------------")
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains("1 / 1번 작가 / 1번 메시지");
    }

    @DisplayName("검색 실패")
    @Test
    void 작가검색실패(){
        final String out = AppTest.run("""
                목록?keywordType=author&keyword=아무개
                종료
                """);

        assertThat(out)
                .contains("검색타입 : author")
                .contains("검색어 : 아무개")
                .contains("----------------------");
    }

    @DisplayName("페이징 목록 조회 조건 없이")
    @Test
    void 페이지없이페이징조회(){
        final String out = AppTest.run("""
                등록
                1번 메시지
                1번 작가
                목록
                종료
                """);

        assertThat(out)
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains("1 / 1번 작가 / 1번 메시지")
                .contains("----------------------")
                .contains("페이지 : 1 / [1]");

    }

    @DisplayName("페이징 목록 조회 페이지 조건 사용")
    @Test
    void 페이지사용페이징조회(){
        // 데이터 생성
        String testData = setupTestData(6);

        final String out = AppTest.run(testData + """              
                목록?page=2
                종료
                """);

        assertThat(out)
                .contains("번호 / 작가 / 명언")
                .contains("----------------------")
                .contains("1 / 1번 작가 / 1번 메시지")
                .contains("----------------------")
                .contains("페이지 : 2 / [2]");
    }
}
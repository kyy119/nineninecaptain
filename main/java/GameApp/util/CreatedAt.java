package GameApp.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreatedAt {
    private String create;

    public CreatedAt(String create) {
        this.create = create;
    }

    public CreatedAt() {
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    // 날짜와 시간을 현재 시각으로 생성하여 포맷된 문자열로 반환하는 메소드
    public static String createDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}
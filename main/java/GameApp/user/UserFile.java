package GameApp.user;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class UserFile {

    public final String filePath;


    public UserFile() {
        String userHome = "/Users/duck/Desktop/Test";
        filePath = Paths.get(userHome, "user_data", "users.txt").toString();
        File fileDir = new File(Paths.get(userHome, "user_data").toString());
        fileDir.mkdirs();
        File userFile = new File(filePath);
        if (!userFile.exists()) {
            try {
                userFile.createNewFile();
            } catch (IOException e) {
                System.out.println("파일 생성 중 오류가 발생했습니다: " + e.getMessage());
                System.exit(0);
            }
        }
    }

    public String getFilePath() {
        return filePath;
    }
}

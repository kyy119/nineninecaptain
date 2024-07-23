package GameApp.user;

import GameApp.rank.Rank;
import GameApp.rank.RankController;
import GameApp.rank.RankService;
import GameApp.util.CreatedAt;
import GameApp.util.DATABASE;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class UserManagementSystem {

    private RankController rankController;
    private List<User> userList;

    public UserManagementSystem() throws IOException {
        userList = new ArrayList<>();
        rankController = new RankController();
        loadUsersFromFile();
    }

    //user.txt 파일 불러오는 메소드
    private void loadUsersFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(
            new FileInputStream(DATABASE.User.getDatabase()))) {
            userList = (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다: " + e.getMessage());
            System.out.println("시스템을 종료합니다.");
            System.exit(1);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일 로드 중 오류가 발생했습니다: " + e.getMessage());
            System.out.println("시스템을 종료합니다.");
            System.exit(1);
        }
    }

    //user.txt 파일에 내용 저장하는 메소드
    private void saveUsersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream(DATABASE.User.getDatabase()))) {
            oos.writeObject(userList);
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    //user 생성 메소드
    public boolean createUser(String id, String pw, String name) {
        if (!isUserIdAvailable(id)) {
            System.out.println("오류: 이미 존재하는 사용자 아이디입니다. 다른 아이디를 선택해주세요.");
            return false;
        }
        if (!isUserIdActive(id)) {
            System.out.println("오류: 비활성된 사용자 아이디입니다. 다른 아이디를 선택해주세요.");
            return false;
        }
        if (pw.length() < 3) {
            System.out.println("오류: 비밀번호는 최소 3자 이상이어야 합니다.");
            return false;
        } else if (!pw.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            System.out.println("오류: 비밀번호에 특수 문자를 포함해야 합니다.");
            return false;
        }

        String currentTime = CreatedAt.createDate();
        User newUser = new User(id, pw, name, currentTime, User.ACTIVE);
        userList.add(newUser);
        saveUsersToFile();
        return true;

    }

    //사용중인 id 확인 메소드
    public boolean isUserIdAvailable(String id) {
        for (User user : userList) {
            if (user.getUserId().equals(id) && user.getUserStatus() == User.ACTIVE) {
                return false;  // 사용 중인 아이디가 있으면 false 반환
            }
        }
        return true;  // 사용 가능한 아이디면 true 반환
    }

    //활동 상태 확인 메소드
    public boolean isUserIdActive(String id) {
        for (User user : userList) {
            if (user.getUserId().equals(id) && user.getUserStatus() == User.INACTIVE) {
                return false;  // 사용 중인 아이디가 있거나 활동이 비활성화인 경우
            }
        }
        return true;  // 사용 가능한 아이디면 true 반환
    }

    //비활성화된 아이디 로그인 메소드
    public boolean loginInActive(String id, String pw) {
        for (User user : userList) {
            if (user.getUserId().equals(id) && user.getPassword().equals(pw)
                && user.getUserStatus() == User.INACTIVE) {
                return true;
            }
        }
        return false;
    }

    //활성화된 로그인 메소드
    public boolean loginActive(String id, String pw) {
        for (User user : userList) {
            if (user.getUserId().equals(id) && user.getPassword().equals(pw)
                && user.getUserStatus() == User.ACTIVE) {
                System.out.println("로그인에 성공하셨습니다.");
                return true;
            }
        }
        return false;
    }

    //사용자 비밀번호 변경 메소드
    public void updateUserPassword(String id, String newPw) {
        for (User user : userList) {
            if (user.getUserId().equals(id)) {
                while (newPw.length() < 3 || !newPw.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
                    System.out.println("오류: 비밀번호는 최소 3자 이상이어야 하며, 특수 문자를 포함해야 합니다.");
                    System.out.print("새로운 비밀번호 입력: ");
                    newPw = new java.util.Scanner(System.in).nextLine();
                }

                user.setPassword(newPw);
                saveUsersToFile();

                return;
            }
        }
    }

    //변경하려는 비밀번호가 전이랑 같은지 확인하는 메소드
    public void updateEqualPassword(String id, String pw) {
        for (User user : userList) {
            if (user.getUserId().equals(id)) {
                if (user.getPassword().equals(pw)) {
                    System.out.println("이전 비밀번호랑 똑같습니다.");
                    System.out.print("새로운 비밀번호 입력: ");
                    pw = new java.util.Scanner(System.in).nextLine();
                }
                updateUserPassword(id, pw);
            }
        }
    }

    //사용자 활동 상태 변경 메소드
    public void deleteUser(String id, String pw) {
        boolean found = false;
        for (User user : userList) {
            if (user.getUserId().equals(id)) {
                if (user.getPassword().equals(pw.trim())) {
                    user.setUserStatus(User.INACTIVE);
                    saveUsersToFile();
                    found = true;
                    break;
                } else {
                    System.out.println("오류: 비밀번호가 일치하지 않습니다.");
                    return;
                }
            }
        }
        if (!found) {
            System.out.println("오류: 해당 사용자를 찾을 수 없습니다.");
        }
    }

    //나의 점수와 랭크를 보여주는 메소드
    public void showScoreFromRank(String id) {
        int score = -1;
        int ranking = -1;
        try {
            int[] list = rankController.myRank(id);
            System.out.println("나의 점수 : " + list[0] + "점 입니다." + " 나의 랭크는 : " + list[1] + "등 입니다.");
        } catch (Exception e) {
            System.out.println("랭킹 없습니다. 게임을 해서 랭킹을 등록하세요!");
            e.getMessage();
        }
    }
}
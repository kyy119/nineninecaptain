package GameApp.user;

import GameApp.util.CreatedAt;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserManagementSystem {

    private List<User> userList;
    private UserFile filePath;

    public UserManagementSystem() {
        userList = new ArrayList<>();
        filePath = new UserFile();
        loadUsersFromFile();
    }
    //user 생성 메소드
    public void createUser(String id, String pw, String name) {
        if (!isUserIdAvailable(id)) {
            System.out.println("오류: 이미 존재하는 사용자 아이디입니다. 다른 아이디를 선택해주세요.");
            return;
        }
        if (!isUserIdActive(id)) {
            System.out.println("오류: 비활성된 사용자 아이디입니다. 다른 아이디를 선택해주세요.");
            return;
        }
        if (pw.length() < 3) {
            System.out.println("오류: 비밀번호는 최소 3자 이상이어야 합니다.");
            return;
        }
        String currentTime = CreatedAt.createDate();
        User newUser = new User(id, pw, name, currentTime, User.ACTIVE);
        userList.add(newUser);
        saveUsersToFile();
        System.out.println("사용자가 성공적으로 생성되었습니다.");
        for(int i = 0; i < userList.size(); i ++){
            System.out.println(userList.get(i).getUserId());
        }
    }

    //로그인 메소드
    public boolean login(String id, String pw) {
        for (User user : userList) {
            if (user.getUserId().equals(id) && user.getPassword().equals(pw)
                && user.getUserStatus() == User.ACTIVE) {
                System.out.println("로그인에 성공하셨습니다.");
                return true;
            }
        }
        System.out.println("로그인에 실패하였습니다. 아이디 또는 비밀번호를 확인하세요.");
        for(int i = 0; i < userList.size(); i ++){
            System.out.println(userList.get(i).getUserId());
        }
        return false;
    }

    public void updateUserId(String currentId, String newId) {
        for (User user : userList) {
            if (user.getUserId().equals(currentId)) {
                user.setUserId(newId);
                saveUsersToFile();
                System.out.println("아이디가 성공적으로 변경되었습니다.");
                return;
            }
        }
        System.out.println("오류: 해당 사용자를 찾을 수 없습니다.");
    }

    //user 업데이트 메소드
    public void updateUserPassword(String id, String newPw) {
        for (User user : userList) {
            if (user.getUserId().equals(id)) {
                user.setPassword(newPw);
                saveUsersToFile();
                System.out.println("비밀번호가 성공적으로 업데이트되었습니다.");
                return;
            }
        }
        System.out.println("오류: 해당 사용자를 찾을 수 없습니다.");
    }

    //user 삭제 메소드
    public void deleteUser(String id, String pw) {
        boolean found = false;
        for (User user : userList) {
            if (user.getUserId().equals(id)) {
                if (user.getPassword().equals(pw.trim())) {
                    user.setUserStatus(User.INACTIVE);
                    saveUsersToFile();
                    System.out.println("사용자가 성공적으로 비활성화되었습니다.");
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

    //비밀 번호 확인 메소드
    public boolean isUserPassword(String id, String pw) {
        for (User user : userList) {
            if (user.getUserId().equals(id) && user.getPassword().equals(pw)) {
                return true;
            }
        }
        return false;
    }
    private void loadUsersFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.getFilePath()))) {
            userList = (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다: " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일 로드 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private void saveUsersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.getFilePath()))) {
            oos.writeObject(userList);
        } catch (IOException e) {
            System.out.println("파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
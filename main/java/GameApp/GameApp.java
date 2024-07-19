package GameApp;

import GameApp.user.UserManagementSystem;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GameApp {

    private static UserManagementSystem userManagement;
    private static Scanner scanner;
    private static String loggedInId;
    private static String loggedInPw;
    private static boolean loginFailed;

    public static void main(String[] args) {
        main();
    }
    public static void main(){
        try {
            userManagement = new UserManagementSystem();
        } catch (Exception e) {
            System.out.println("파일을 불러오지 못하였습니다. 종료합니다.");
            System.exit(1);
        }
        scanner = new Scanner(System.in);
        int choice = 0;

        while (choice != 3) {
            printMainMenu();
            try {
                choice = scanner.nextInt();
                scanner.nextLine();  // 개행 문자 제거
            } catch (InputMismatchException e) {
                System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                scanner.nextLine();  // 잘못된 입력 제거
                continue;
            }

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("프로그램을 종료합니다...");
                    System.exit(0);  // 프로그램 종료
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
                    break;
            }
        }

        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n---------------");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("3. 종료");
        System.out.print("원하는 작업을 선택하세요: ");
        System.out.println("\n---------------");
    }

    private static void login() {
        System.out.println("\n----로그인----");
        System.out.print("1. 아이디 입력: ");
        String id = scanner.nextLine();
        System.out.print("2. 비밀번호 입력: ");
        String pw = scanner.nextLine();
        boolean logInActive = userManagement.loginInActive(id,pw);
        if(logInActive){
            System.out.println("비활성화된 아이디 입니다. 다른 아이디로 로그인을 해주세요.");
            loginFailed = true;
        }
        boolean loggedIn = userManagement.loginActive(id, pw);
        if (loggedIn) {
            loggedInId = id;
            loggedInPw = pw;
            loginMenu();
        } else {
            if (!loginFailed) {
                System.out.println("로그인에 실패하였습니다. 아이디 또는 비밀번호를 확인하세요.");
                loginFailed = true;
            }
        }
    }

    private static void loginMenu() {
        int choice = 0;
        while (choice != 5) {
            printUserMenu();
            try {
                choice = scanner.nextInt();
                scanner.nextLine();  // 개행 문자 제거
            } catch (InputMismatchException e) {
                System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                scanner.nextLine();  // 잘못된 입력 제거
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("Game Start!");
                    break;
                case 2:
                    changePassword();
                    break;
                case 3:
                    showMyPage();
                    break;
                case 4:
                    System.out.println("로그아웃합니다.");
                    return;
                case 5:
                    System.out.println("프로그램을 종료합니다...");
                    System.exit(0);  // 프로그램 종료
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
                    break;
            }
        }
    }

    private static void showMyPage() {
        int choice = 0;
        while (choice != 4) {
            printMyPageMenu();
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                scanner.nextLine();  // 잘못된 입력 제거
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("기능 구현 중");
                    break;
                case 2:
                    changePassword();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    loginMenu();
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
                    break;
            }
        }
    }

    private static void printMyPageMenu() {
        System.out.println("\n--------------");
        System.out.println("----마이페이지----");
        System.out.println("1. 나의 랭킹");
        System.out.println("2. 비밀번호 변경");
        System.out.println("3. 회원 탈퇴");
        System.out.println("4. 뒤로 가기");
        System.out.println("--------------");
        System.out.print("원하는 작업을 선택하세요: ");
    }

    private static void printUserMenu() {
        System.out.println("\n--------------");
        System.out.println("----정보----");
        System.out.println("1. Game Start");
        System.out.println("2. 비밀번호 변경");
        System.out.println("3. 마이페이지");
        System.out.println("4. 로그아웃");
        System.out.println("5. 프로그램 종료");
        System.out.println("--------------");
        System.out.print("원하는 작업을 선택하세요: ");
    }

    private static void changePassword() {
        System.out.println("\n----비밀번호 변경----");
        System.out.print("현재 비밀번호 입력: ");
        String currentPw = scanner.nextLine();

        if (!currentPw.equals(loggedInPw)) {
            System.out.println("현재 비밀번호가 일치하지 않습니다.");
            changePassword();
        }

        System.out.print("새로운 비밀번호 입력: ");
        String newPw = scanner.nextLine();
        userManagement.updateEqualPassword(loggedInId, newPw);
//        userManagement.updateUserPassword(loggedInId, newPw);
        System.out.println("비밀번호가 성공적으로 변경되었습니다.");
        loggedInPw = newPw;  // 변경된 비밀번호로 업데이트
        main();
        System.out.println(loggedInPw);
    }

    private static void deleteUser() {
        System.out.println("\n----아이디 삭제----");
        System.out.print("아이디 입력: ");
        String id = scanner.nextLine();
        if (id.equals(loggedInId) && !userManagement.isUserIdAvailable(id)) {
            deletePassWord(id);
        } else {
            System.out.println("잘못된 아이디 입력 다시 해주세요");
            deleteUser();
        }
    }

    private static void deletePassWord(String id) {
        System.out.print("비밀번호 입력: ");
        String pw = scanner.nextLine();
        if (pw.equals(loggedInPw)) {
            userManagement.deleteUser(id, pw);
            System.out.println("회원 탈퇴가 완료되었습니다.");
            loggedInId = null;
            loggedInPw = null;
            main();
        } else {
            System.out.println("잘못된 비밀 번호 입니다. 다시 입력해주세요.");
            deletePassWord(id);
        }
    }
    private static void register() {
        System.out.println("\n----회원가입----");
        System.out.print("아이디 입력: ");
        String id = scanner.nextLine();
        System.out.print("비밀번호 입력: ");
        String pw = scanner.nextLine();
        System.out.print("이름 입력: ");
        String name = scanner.nextLine();
        if(userManagement.createUser(id,pw,name)){
            System.out.println("회원가입이 완료되었습니다.");
            login();
        }else{
            register();
        }

    }
}
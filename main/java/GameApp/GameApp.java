package GameApp;

import GameApp.user.UserManagementSystem;
import java.util.Scanner;

public class GameApp {

    private static UserManagementSystem userManagement;
    private static Scanner scanner;
    private static String longInId;
    private static String logInPw;

    public static void main(String[] args) {
        main();
    }

    public static void main() {
        userManagement = new UserManagementSystem();
        scanner = new Scanner(System.in);
        int choice = 0;

        while (choice != 3) {
            printMainMenu();
            choice = scanner.nextInt();
            scanner.nextLine();  // 개행 문자 제거

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

        boolean loggedIn = userManagement.login(id, pw);
        if (loggedIn) {
            longInId = id;
            logInPw = pw;
            loginMenu();
        } else {
            System.out.println("로그인에 실패하였습니다. 아이디 또는 비밀번호를 확인하세요.");
        }
    }

    private static void loginMenu() {
        int choice = 0;
        while (choice != 5) {
            printUserMenu();
            choice = scanner.nextInt();
            scanner.nextLine();  // 개행 문자 제거

            switch (choice) {
                case 1:
                    System.out.println("Game Start!");
                    break;
                case 2:
                    changeId();
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
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("기능 구현 중");
                    break;
                case 2:
                    changeId();
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
        System.out.println("2. 회원 정보 수정");
        System.out.println("3. 회원 탈퇴");
        System.out.println("4. 뒤로 가기");
        System.out.println("--------------");
        System.out.print("원하는 작업을 선택하세요: ");
    }

    private static void printUserMenu() {
        System.out.println("\n--------------");
        System.out.println("----정보----");
        System.out.println("1. Game Start");
        System.out.println("2. 전체 랭킹 보기");
        System.out.println("3. 마이페이지");
        System.out.println("4. 로그아웃");
        System.out.println("5. 프로그램 종료");
        System.out.println("--------------");
        System.out.print("원하는 작업을 선택하세요: ");
    }

    private static void changeId() {
        System.out.println("\n----비밀번호 변경하기----");
        System.out.print("비밀 번호 입력 : ");
        String pw = scanner.nextLine();
        if (!userManagement.isUserPassword(longInId, pw)) {
            System.out.println("잘못된 비밀 번호 입니다. 다시 입력하세요");
            changeId();
        }
        System.out.print("새로운 비밀번호 입력: ");
        String newPw = scanner.nextLine();
        userManagement.updateUserPassword(longInId, newPw);
        main();
    }

    private static void deleteUser() {
        System.out.println("\n----아이디 삭제----");
        System.out.print("아이디 입력: ");
        String id = scanner.nextLine();
        if (id.equals(longInId) && !userManagement.isUserIdAvailable(id)) {
            deletePassWord(id);
        } else {
            System.out.println("잘못된 아이디 입력 다시 해주세요");
            deleteUser();
        }
    }

    private static void deletePassWord(String id) {
        System.out.print("비밀번호 입력 : ");
        String pw = scanner.nextLine();
        if (pw.equals(logInPw)) {
            userManagement.deleteUser(id, pw);
            main();
        } else {
            System.out.println("잘못된 비밀 번호 입니다. 다시 입력해주세요.");
            deletePassWord(id);
        }
    }

    private static void register() {
        System.out.println("\n----회원가입----");
        System.out.print("1. 아이디 입력: ");
        String id = scanner.nextLine();
        System.out.print("2. 비밀번호 입력: ");
        String pw = scanner.nextLine();
        System.out.print("3. 이름 입력: ");
        String name = scanner.nextLine();
        userManagement.createUser(id, pw, name);
    }
}
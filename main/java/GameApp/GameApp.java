package GameApp;

import GameApp.rank.RankController;
import GameApp.game.Game;
import GameApp.user.UserManagementSystem;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GameApp {

    private static UserManagementSystem userManagement;
    private static Scanner scanner;
    private static String loggedInId;
    private static String loggedInPw;
    private static boolean loginFailed;

    public static void main(String[] args) throws IOException {
        loginPage();
    }
    // [실행] 로그인 페이지
    public static void loginPage() throws IOException {
        try {
            userManagement = new UserManagementSystem();
        } catch (Exception e) {
            System.out.println("파일을 불러오지 못하였습니다. 종료합니다.");
            System.exit(1);
        }
        scanner = new Scanner(System.in);
        int choice = 0;
        while (choice != 3) {
            printLoginPage();
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
    // [출력] 로그인 페이지
    private static void printLoginPage() {
        System.out.println("\n---------------");
        System.out.println("1. 로그인");
        System.out.println("2. 회원가입");
        System.out.println("3. 종료");
        System.out.print("원하는 작업을 선택하세요: ");
        System.out.println("\n---------------");
    }
    // [실행] 로그인
    private static void login() throws IOException {
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
            mainMenu();
        } else {
            if (!loginFailed) {
                System.out.println("로그인에 실패하였습니다. 아이디 또는 비밀번호를 확인하세요.");
                loginFailed = true;
            }
        }
    }
    //[실행] 회원가입
    private static void register() throws IOException {
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
    //[실행] 메인화면
    private static void mainMenu() throws IOException {
        int choice = 0;
        while (choice != 5) {
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
                   showGameMenu();
                    break;
                case 2://랭킹보기
                    RankController rankController = new RankController();
                    rankController.top20Rank();
                    break;
                case 3:
                    showMyPage();
                    break;
                case 4:
                    System.out.println("로그아웃합니다.");
                    loggedInId = " ";
                    loggedInPw = " ";
                    loginPage();
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
    //[출력] 메인화면
    private static void printMainMenu() {
        System.out.println("\n--------------");
        System.out.println("----정보----");
        System.out.println("1. Game Start");
        System.out.println("2. 랭킹 보기");
        System.out.println("3. 마이페이지");
        System.out.println("4. 로그아웃");
        System.out.println("5. 프로그램 종료");
        System.out.println("--------------");
        System.out.print("원하는 작업을 선택하세요: ");
    }

    // [실행] 게임 난이도 선택
    private static void showGameMenu() throws IOException {
        printGameLevel();
        int choice = scanner.nextInt();
        scanner.nextLine();  // 개행 문자 제거
        Game game = new Game();
        RankController rankController = new RankController();
        switch (choice) {
            case 1:
                game.prepareGame(1);
                break;
            case 2:
                game.prepareGame(2);
                break;
            case 3:
                game.prepareGame(3);
                break;
            case 4:
                mainMenu();
                return;
            default:
                System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
                showGameMenu();
                return;
        }
        rankController.createRank(loggedInId,game.getScore());
        showEndGameMenu();
    }

    // [실행] 게임이 끝나고 앞으로 할 행위 선택
    private static void showEndGameMenu() throws IOException {
        printEndGameMenu();
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                showGameMenu();
                break;
            case 2:
                mainMenu();
                break;
            case 3:
                System.exit(0);
            default:
                System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
                showEndGameMenu();
                return;
        }
    }

    // [출력] 게임 난이도 선택
    private static void printGameLevel(){
        System.out.println("\n------------------------");
        System.out.println("----게임 난이도를 선텍하세요----");
        System.out.println("1. 1단계 (쉬움) 정답시 + 10점");
        System.out.println("2. 2단계 (보통) 정답시 + 15점");
        System.out.println("3. 3단계 (어려움) 정답시 + 20점");
        System.out.println("4. 뒤로 가기");
        System.out.println("--------------");
        System.out.print("원하는 작업을 선택하세요: ");
    }

    // [출력] 게임이 끝나고 앞으로 할 행위 선택
    private static void printEndGameMenu(){
        System.out.println("----------------------");
        System.out.println("1.재도전 2.홈 화면 3.종료");
        System.out.println("----------------------");
    }
    // [실행] 마이페이지
    private static void showMyPage() throws IOException {
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
                    userManagement.showScoreFromRank(loggedInId);
                    break;
                case 2:
                    changePassword();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    mainMenu();
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
                    break;
            }
        }
    }
    //[출력] 마이페이지
    private static void printMyPageMenu() {
        System.out.println("\n--------------");
        System.out.println("----마이페이지----");
        System.out.println("1. 나의 점수");
        System.out.println("2. 비밀번호 변경");
        System.out.println("3. 회원 탈퇴");
        System.out.println("4. 뒤로 가기");
        System.out.println("--------------");
        System.out.print("원하는 작업을 선택하세요: ");
    }
    //[실행] 마이페이지 비밀번호 변경
    private static void changePassword() throws IOException {
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
        System.out.println("비밀번호가 성공적으로 변경되었습니다.");
        loggedInPw = newPw;  // 변경된 비밀번호로 업데이트
        loginPage();
        System.out.println(loggedInPw);
    }
    //[실행] 회원탈퇴
    private static void deleteUser() throws IOException {
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
    // [실행] 회원탈퇴
    private static void deletePassWord(String id) throws IOException {
        System.out.print("비밀번호 입력: ");
        String pw = scanner.nextLine();
        if (pw.equals(loggedInPw)) {
            userManagement.deleteUser(id, pw);
            System.out.println("회원 탈퇴가 완료되었습니다.");
            loggedInId = null;
            loggedInPw = null;
            loginPage();
        } else {
            System.out.println("잘못된 비밀 번호 입니다. 다시 입력해주세요.");
            deletePassWord(id);
        }
    }

}
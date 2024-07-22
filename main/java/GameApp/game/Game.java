package GameApp.game;

import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class Game {
    Scanner scanner = new Scanner(System.in);
    Timer timer = new Timer();

    private int count = 3; //시작전 3초 카운트
    private final CountDownLatch latch = new CountDownLatch(1);
    private int life = 3; //목숨 3개
    private int score = 0; //점수
    private boolean gameRunning = true;

    //게임 시작전 타이머
    TimerTask prepareTimeTask = new TimerTask() {
        public void run() {
            if (count>0){
                System.out.println(count);
                count--;
            }
            else{
                timer.cancel();
                latch.countDown();
                System.out.println("-------------------");
                System.out.println("        시작        ");
                System.out.println("-------------------");
            }
        }
    };

    //게임 시작전 멘트
    public void prepareGame(int difficulty){
        System.out.println("------------------");
        timer.schedule(prepareTimeTask, 0, 1000);

        //latch(다른 쓰레드 작업)이 끝날때까지 기다렸다가 게임시작하도록 설정
        try {
            latch.await();
            startGameCountDown(difficulty);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //게임 시간 10초 카운트다운
    public void startGameCountDown(int difficulty){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                gameRunning = false;
                System.out.println("시간이 초과되었습니다! 게임 종료!");
                System.out.println( "최종 점수: " + score );

            }
        };
        timer.schedule(task, 10000); // 10초 후에 task 실행
        playGame(difficulty);
        timer.cancel();
    }

    //연산자 랜덤 선택  + (0), -(1), *(2), ÷ (3)
    public int makeRandomOperator(int difficulty){
        Random random = new Random();
        return switch (difficulty) {
            case 1 -> random.nextInt(2);
            case 2 -> random.nextInt(2) + 2;
            case 3 -> random.nextInt(4);
            default -> throw new IllegalArgumentException("연산자 랜덤 선택 중 유효하지 않은 난이도: " + difficulty);
        };
    }

    //랜덤 숫자 생성
    public Integer makeRandomNum(int difficulty){
        Random random = new Random();
        return switch (difficulty) {
            case 1, 2 -> random.nextInt(9) + 1; // 1부터 9까지의 숫자 생성
            case 3 -> random.nextInt(99) + 1; // 1부터 99까지의 숫자 생성
            default -> throw new IllegalArgumentException("랜덤 숫자 생성 중 유효하지 않은 난이도: " + difficulty);
        };
    }

    //연산 결과
    public int calculateNum(int num1, int num2, int operatorNum){
        return switch (operatorNum) {
            case 0 -> {
                printQuestion(num1, num2, "+");
                yield num1 + num2;
            }
            case 1 -> {
                printQuestion(num1, num2, "-");
                yield num1 - num2;
            }
            case 2 -> {
                printQuestion(num1, num2, "x");
                yield num1 * num2;
            }
            case 3 -> {
                printQuestion(num1, num2, "÷");
                yield num1 / num2;
            }
            default -> throw new IllegalArgumentException("랜덤 숫자 연산중 에러");
        };

    }

    //계산식 콘솔에 출력
    public void printQuestion(int num1, int num2, String operator){
        switch (operator){
            case "+":
                System.out.println("문제: " + num1 + " + " + num2 + " = ?");
                break;
            case "-":
                System.out.println("문제: " + num1 + " - " + num2 + " = ?");
                break;
            case "x":
                System.out.println("문제: " + num1 + " x " + num2 + " = ?");
                break;
            case "÷":
                System.out.println("문제: " + num1 + " ÷ " + num2 + " = ?");
                break;
            default:
                System.out.println("랜덤 숫자 생성시 입렵 난이도가 오류했습니다.");
        }
    }

    //게임 실행
    public void playGame(int difficulty) {
        while (life > 0 && gameRunning) {
            int operatorNum = makeRandomOperator(difficulty);
            int num1 = makeRandomNum(difficulty);
            int num2 = makeRandomNum(difficulty);
            if (operatorNum == 3){
                while (num1%num2 != 0){
                    num1 = makeRandomNum(difficulty);
                    num2 = makeRandomNum(difficulty);
                }
            }
            int result = calculateNum(num1, num2, operatorNum);

            if (!gameRunning) {
                break;
            }

            // 입력이 있을 때만 처리
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();

                if (!gameRunning) {
                    break;
                }

                if (input == result) {
                    switch(difficulty){
                        case 1:
                            score += 10;
                            break;
                        case 2:
                            score += 15;
                            break;
                        case 3:
                            score += 20;
                            break;
                    }

                    System.out.println("정답입니다!! 현재 점수: " + score);
                } else {
                    life--;
                    System.out.println("오답입니다!!");
                    System.out.println("남은 목숨은 " + life + " 개입니다.");
                }
            } else {
                if (!gameRunning) {
                    break;
                }  // 게임이 종료되면 입력 루프를 종료
            }
        }

    }
}
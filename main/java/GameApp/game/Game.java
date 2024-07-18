package GameApp.game;

import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class Game {
    Timer timer = new Timer();
    private int count = 3; //시작전 3초 카운트
    private CountDownLatch latch = new CountDownLatch(1);

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

    Timer gameTimer = new Timer();

    //게임 시작전 멘트
    public void prepareGame(){
        System.out.println("------------------");
        timer.schedule(prepareTimeTask, 0, 1000);

        //latch(다른 쓰레드 작업)이 끝날때까지 기다렸다가 게임시작하도록 설정
        try {
            latch.await();
            startGameCountDown();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //연산결과
    public int calculateNum(int num1, int num2){
        return num1+num2;
    }

    //계산식 출력
    public void printQuestion(int num1, int num2){
        System.out.println("문제: " + num1 + " + " + num2 + " = ?");
    }

    //랜덤 숫자 생성
    public Integer makeRandomNum(){
        Random random = new Random();
        return random.nextInt(10);
    }

    Scanner scanner = new Scanner(System.in);
    private int life = 3;
    private int score = 0;

    private boolean gameRunning = true;


    public void startGameCountDown(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                gameRunning = false;
                System.out.println("시간이 초과되었습니다! 게임 종료!");
            }
        };
        timer.schedule(task, 10000); // 10초 후에 task 실행
        playGame();
        timer.cancel();
    }

    //게임 실행
    public void playGame() {
        while (life > 0 && gameRunning) {
            int num1 = makeRandomNum();
            int num2 = makeRandomNum();
            int result = calculateNum(num1, num2);
            printQuestion(num1, num2);
            if (!gameRunning) {
                break;
            }
            int input = scanner.nextInt();
            if (!gameRunning) {
                break;
            }

            if (input == result) {
                score += 10;
                System.out.println("정답입니다!! 현재 점수: " + score);
            } else {
                life--;
                System.out.println("오답입니다!!");
                System.out.println("남은 목숨은 " + life + " 개입니다.");
            }
        }
        System.out.println( "최종 점수: " + score );
    }
}

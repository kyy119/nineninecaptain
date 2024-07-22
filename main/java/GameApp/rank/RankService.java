package GameApp.rank;

import GameApp.util.DATABASE;
import GameApp.util.ObjectOutputStreamCustom;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RankService {

    // 랭크 정보를 저장한다.
    public void createRank(String userId, int score){
        // 게임에서 가져온 데이터를 새로 만들어서 저장
        List<Rank> createRank = new ArrayList<>();
        createRank.add(new Rank(userId, score));
        if(deleteLowScore(userId,score)) writeRankToFrom(createRank);

//         저장된 랭크정보 가져오기
        List<Rank> rankList = readRankFromFile();
        System.out.println("여긴 모든 유저의 정보");
        for(Rank rr : rankList){
            System.out.println("유저점수 ==>> " + rr.getScore() + " | 아이디 ==>> " + rr.getUserId() + " | 생성된 날짜" + rr.getCreate());
        }
        System.out.println("유저정보 ==>>" + rankList.size() + " =======");
    }
    // 파일에서 읽어오기
    public List<Rank> readRankFromFile(){
        List<Rank> rankList = new ArrayList<>();
        try{
            FileInputStream fis = new FileInputStream(DATABASE.RANK.getDatabase());
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                try {
                    rankList.add((Rank) ois.readObject());
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (IOException e) {
            System.out.println("에러4");
        }finally {
            return rankList;
        }
    }


    public void writeRankToFrom(List<Rank> rankList){
        try{
            FileOutputStream fos = new FileOutputStream(DATABASE.RANK.getDatabase(), true);
            ObjectOutputStream oos = fos.getChannel().position() == 0 ?
                                    new ObjectOutputStream(fos) : new ObjectOutputStreamCustom(fos);
            rankList.stream()
                    .forEach(rank -> {
                        try {
                            oos.writeObject(rank);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // rankList는 전체 리스트이다.
    public boolean deleteLowScore(String userId, int score){
        // 전체 랭킹에서 받아온 유저를 뺀 나머지 유저들의 랭킹
        List<Rank> allRank = readRankFromFile();
        // 나의 랭킹 정보
        List<Rank> myRankList = allRank.stream()
                .filter(myRank -> myRank.getUserId().equals(userId))
                    .collect(Collectors.toList());
        // 나의 랭킹을뺀 나머지 유저의 정보
        allRank = allRank.stream()
                .filter(rank -> !rank.getUserId().equals(userId))
                    .collect(Collectors.toList());
        // 등록된 나의 랭킹이 100개라면 들어온 점수와 100개중 가장 낮은 점수가 들어온 점수보다 작다면 낮은점수를 삭제한다.
        int myRankCount = myRankList.size();
        if(myRankCount >= 20){
            int myRankLowScore = myRankList.stream()
                .min(Comparator.comparing(Rank :: getScore))
                .get()
                .getScore();
            if(myRankLowScore < score){
                // 나의 랭킹중 제일 낮은 점수를 삭제하고 들어온 rank정보를 myRankList에 추가한다.
                myRankList.stream()
                    .min(Comparator.comparingInt(Rank::getScore))
                    .ifPresent(myRankList::remove);
                myRankList.add(new Rank(userId,score));
                //나머지 유저와 새로운 나의 랭킹을 더 한다.
                List<Rank> newRank = Stream.concat(allRank.stream(), myRankList.stream())
                    .collect(Collectors.toList());
                try{
                    FileOutputStream fos = new FileOutputStream(DATABASE.RANK.getDatabase(), false);
                    ObjectOutputStream oos = fos.getChannel().position() == 0 ?
                        new ObjectOutputStream(fos) : new ObjectOutputStreamCustom(fos);
                    newRank.stream()
                        .forEach(rank -> {
                            try {
                                oos.writeObject(rank);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return false;
        }
        return true;
    }
    // 랭킹 20명을 보여준다. 점수 순으로 점수가 같다면 먼저 푼사람
    public void findRankTop20(){
        List<Rank> rankList = readRankFromFile();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 점수가 높은순으로 정렬하고 점수가 같다면 일찍인사람이 먼저오는 정렬
        List<Rank> top20 = rankList.stream()
            .sorted(Comparator.comparing(Rank :: getScore).reversed()
                .thenComparing(Rank -> LocalDateTime.parse(Rank.getCreate(),formatter)))
            .limit(20)
            .collect(Collectors.toList());
        // foreach안에서 raakCounter++ 을 사용할수 있게된다.
        AtomicInteger rankCounter = new AtomicInteger(1);
        top20.stream()
            .forEach(rr ->  {
                int count = rankCounter.getAndIncrement();
                System.out.println(count + "등 | 유저점수 ==>> " + rr.getScore() + "아이디 ==>> " + rr.getUserId() + "생성된 날짜" + rr.getCreate());
            });

    }
}



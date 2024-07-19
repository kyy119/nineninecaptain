package GameApp.rank;

import GameApp.util.DATABASE;
import GameApp.util.ObjectOutputStreamCustom;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RankService {

    public void createRank(String userId, int score){
        // 게임에서 가져온 데이터를 새로 만들어서 저장
        List<Rank> createRank = new ArrayList<>();
        createRank.add(new Rank(userId, score));
        writeRanktoFrom(createRank);

        // 저장된 랭크정보 가져오기
        List<Rank> rankList = readRankFromFile();
        System.out.println("여긴 모든 유저의 정보");
        for(Rank rr : rankList){
            System.out.println("유저점수 ==>> " + rr.getScore() + "아이디 ==>> " + rr.getUserId());
        }
//        deleteLowScore(rankList,userId);
//        writeRanktoFrom(rank);
        //등록할려는 유저의 점수로 정렬한다.
//        deleteLowScore(rankList, userId);
//        List<Rank> afterList = readRankFromFile().stream().filter(r -> r.getUserId().equals(userId)).collect(Collectors.toList());
//        System.out.println("여긴 삭제후 유저의 정보");
//        for(Rank rr : afterList){
//            System.out.println("유저점수 ==>> " + rr.getScore() + "아이디 ==>> " + rr.getUserId());
//        }


    }
    // 파일에서 일어오기
    public List<Rank> readRankFromFile(){
        try{
            FileInputStream fis = new FileInputStream(DATABASE.RANK.getDatabase());
            ObjectInputStream ois = new ObjectInputStream(fis);
            List<Rank> rankList = new ArrayList<>();
            while (true) {
                try {
                    rankList.add((Rank) ois.readObject());
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return rankList;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeRanktoFrom(List<Rank> rankList){
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
//            oos.writeObject(rankList);
//            oos.flush();
//            oos.close();
//            fos.close();


        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteLowScore(List<Rank> rankList, String userId){
        rankList.stream()
            .min(Comparator.comparingInt(Rank::getScore))
            .ifPresent(rankList::remove);
        System.out.println("==== 삭제전 =====");
//        writeRanktoFrom(rankList); // 추가하는 메서드
        try{
            FileOutputStream fos = new FileOutputStream(DATABASE.RANK.getDatabase(), false);
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
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }





}



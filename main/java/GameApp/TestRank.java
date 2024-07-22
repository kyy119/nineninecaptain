package GameApp;

import GameApp.rank.RankController;

import java.io.IOException;

public class TestRank {
    public static void main(String[] args) throws IOException {
        RankController rankController = new RankController();
//        for(int i = 0; i < 10; i++){
//            rankController.createRank("mok11" , 6 + i );
//        }
//                    rankController.createRank("mok11" , 12 );

        rankController.top20Rank();


    }
}

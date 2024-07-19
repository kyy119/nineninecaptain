package GameApp;

import GameApp.rank.RankController;

import java.io.IOException;

public class TestRank {
    public static void main(String[] args) throws IOException {
        RankController rankController = new RankController();
        for(int i = 0; i < 10; i++){
            rankController.createRank("mok1" , 4 + i );
        }
//                    rankController.createRank("mok1" , 4 );



    }
}

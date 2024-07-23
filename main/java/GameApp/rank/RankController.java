package GameApp.rank;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class RankController {

    private final RankService rankService;

    public RankController() throws IOException {
        this.rankService = new RankService();
    }

    public void createRank(String userId, int score) {
        rankService.createRank(userId, score);
    }

    public void top20Rank() {
        rankService.findRankTop20();
    }

    public int[] myRank(String userId) {
        return rankService.findMyRankbyUserId(userId);
    }
}

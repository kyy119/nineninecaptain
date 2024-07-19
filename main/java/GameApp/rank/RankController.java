package GameApp.rank;

import java.io.IOException;

public class RankController {
    private final RankService rankService;

    public RankController() throws IOException {
        this.rankService = new RankService();
    }

    public void createRank(String userId, int score){
        rankService.createRank(userId, score);
    }
}

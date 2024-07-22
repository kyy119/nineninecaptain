package GameApp.rank;

import GameApp.util.CreatedAt;

import java.io.Serializable;

public class Rank extends CreatedAt implements Serializable {
    private static final long serialVersionUID = -123;
    private String userId;
    private int score;

    public Rank(String userId, int score) {
        super(CreatedAt.createDate());
        this.userId = userId;
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

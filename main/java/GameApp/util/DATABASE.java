package GameApp.util;

public enum DATABASE {
    User("/Users/yoon/Desktop/Test/user.txt"),
    RANK("/Users/yoon/Desktop/Test/rank.txt");

    private String database;
    DATABASE(String database) {
        this.database = database;

    }

    public String getDatabase() {
        return database;
    }
}


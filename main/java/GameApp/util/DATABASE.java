package GameApp.util;

public enum DATABASE {
    User("/Users/duck/Desktop/Test/user.txt"),
    RANK("/Users/duck/Desktop/Test/rank.txt");

    private String database;
    DATABASE(String database) {
        this.database = database;

    }

    public String getDatabase() {
        return database;
    }
}


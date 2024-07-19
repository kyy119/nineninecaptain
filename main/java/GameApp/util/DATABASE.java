package GameApp.util;

public enum DATABASE {
    User("/Users/junghunmok/Desktop/nineninecaptain/user.txt"),
    RANK("/Users/junghunmok/Desktop/nineninecaptain/rank.txt");

    private String database;
    DATABASE(String databas) {
        this.database = databas;

    }

    public String getDatabase() {
        return database;
    }
}


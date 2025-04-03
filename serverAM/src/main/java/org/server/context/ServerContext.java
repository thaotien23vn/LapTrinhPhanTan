package org.server.context;

public class ServerContext {
    private static final ServerContext INSTANCE = new ServerContext();
    private int userId;

    private ServerContext() {
        System.out.println("Created Server App context ");
        userId = 0;
    }

    public static ServerContext getInstance() {
        return INSTANCE;
    }

    public int getUserId() {
        if (INSTANCE != null)
            return INSTANCE.userId;
        else
            return -1;
    }

    public void setUserId(int id) {
        if (INSTANCE != null)
            INSTANCE.userId = id;
    }
}

package cn.cy.base.config;

/**
 * 配置
 */
public class ServerConfig {

    private int port;

    private int backlog;

    public ServerConfig(int port, int backlog) {
        this.port = port;
        this.backlog = backlog;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public static ServerConfig defaultConfig;

    static {
        defaultConfig = new ServerConfig(8100, 0);
    }
}

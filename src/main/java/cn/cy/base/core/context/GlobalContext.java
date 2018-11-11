package cn.cy.base.core.context;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.yaml.snakeyaml.Yaml;

import cn.cy.base.threadpool.ThreadPool;
import cn.cy.base.threadpool.ThreadPoolImpl;

/**
 * 全局唯一context
 */
public class GlobalContext {

    private static Logger logger = LoggerContext.getContext()
            .getLogger(GlobalContext.class.getName());

    private static ThreadPool threadPool = ThreadPoolImpl.getInstance();

    //可能没必要创建实例
    private static final class SingleTon {
        private static final GlobalContext INSTANCE = new GlobalContext();
    }

    private GlobalContext() {
    }

    public static GlobalContext getInstance() {
        return SingleTon.INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public static boolean initialize(String propFileName) {
        Yaml yaml = new Yaml();
        URL url = GlobalContext.class.getClassLoader()
                .getResource(propFileName);
        Map<String, Object> props;

        try {
            props = yaml
                    .load(new FileInputStream(url.getFile()));
        } catch (IOException ioE) {
            logger.error("Fail to get file {}", url.getFile());
            return false;
        } catch (NullPointerException nE) {
            logger.error("File {} doesn't exist in classpath", propFileName);
            return false;
        } catch (Exception e) {
            logger.error("Exception while getting global property file");
            return false;
        }
        int corePoolSize = (Integer) props.get("corePoolSize");
        int maximumPoolSize = (Integer) props.get("maximumPoolSize");
        int keepAliveTime = (Integer) props.get("keepAliveTime");
        threadPool.initialize(corePoolSize, maximumPoolSize, keepAliveTime);
        return true;
    }

    public ThreadPool getThreadPool() {
        return threadPool;
    }
}

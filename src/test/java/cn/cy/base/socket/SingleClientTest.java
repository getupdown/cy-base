package cn.cy.base.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.google.common.base.Strings;

import cn.cy.base.socket.func.ConnectFunction;
import cn.cy.base.socket.func.ReadFunction;
import cn.cy.base.socket.func.WriteFunction;

/**
 * 单连接测试
 * 主要测试单连接的异常,逻辑等等
 * 例如错误处理, 拆粘包等等
 */
public class SingleClientTest extends BaseClientTest {

    private WriteFunction writeFunction = remoteChannel -> {
        String input = "motherFucker";

        input = Strings.repeat(input, 1000);

        try {
            remoteChannel.write(ByteBuffer.wrap(input.getBytes()));
            logger.info("client has been written, local port is : {}",
                    remoteChannel.socket().getLocalPort());
        } catch (Exception e) {
            logger.warn("port {} send failed! {}", remoteChannel.socket()
                    .getLocalPort(), e.getMessage());

        }
    };

    private ReadFunction readFunction = remoteChannel -> {
        logger.info("client can read");
    };

    private ConnectFunction connectFunction = remoteChannel -> {
        logger.info("asdf");
    };

    @Test
    public void work() throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1000);

        for (int i = 0; i < 1; i++) {
            new Thread(() -> {
                try {
                    startSingleClient(writeFunction, readFunction, connectFunction);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        countDownLatch.await();
    }

}
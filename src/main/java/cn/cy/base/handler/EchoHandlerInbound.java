package cn.cy.base.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import cn.cy.base.context.SocketContext;

/**
 * 写一个简单的echo服务器
 */
public class EchoHandlerInbound implements InboundEventHandler {

    private static Logger logger = LoggerContext.getContext().getLogger(EchoHandlerInbound.class.getName());

    @Override
    public void onRead(SocketContext context) {

        ByteBuffer readBuffer = (ByteBuffer) context.getBuffer();

        SocketChannel socketChannel = context.getSocketChannel();

        try {
            //之前因为是readOnly，所以无法将channel数据写入，报非法参数异常
            int cnt = socketChannel.read(readBuffer);
            // 客户端关闭连接
            // 这里是 <=0 参照上面读部分的2, 3条
            logger.info("{} bytes read into the buffer", cnt);
            if (cnt <= 0) {
                // 触发取消事件
                onCancel(context);
                return;
            }
            //之前会报UnsupportOperation异常
            readBuffer.flip();
            System.out.println(Charset.forName("UTF-8").decode(readBuffer).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancel(SocketContext context) {
        logger.debug("{} deregister from selector ", context.getSocketChannel().toString());
        try {
            context.getSocketChannel().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

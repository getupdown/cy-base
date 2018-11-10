package cn.cy.base.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import cn.cy.base.core.context.ConnectionContext;

/**
 * 写一个简单的echo服务器
 */
public class EchoHandlerInbound implements InboundEventHandler<String> {

    private static Logger logger = LoggerContext.getContext().getLogger(EchoHandlerInbound.class.getName());

    private OutboundEventHandler<String> outboundEventHandler;

    @Override
    public void onRead(ConnectionContext<String> context) {

        ByteBuffer readBuffer = (ByteBuffer) context.getBuffer();

        SocketChannel socketChannel = context.getChannel();

        try {
            //之前因为是readOnly，所以无法将channel数据写入，报非法参数异常
            int cnt = socketChannel.read(readBuffer);
            // 客户端关闭连接
            // 这里是 <=0 参照上面读部分的2, 3条
            logger.debug("{} bytes read into the buffer", cnt);
            if (cnt <= 0) {
                // 触发取消事件
                onCancel(context);
                return;
            }
            //之前会报UnsupportOperation异常
            readBuffer.flip();
            String x = Charset.forName("UTF-8").decode(readBuffer).toString();

            context.setData(x);

            readBuffer.clear();

            triggerOutBoundHandler(context, outboundEventHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancel(ConnectionContext<String> context) {
        logger.debug("{} deregister from selector ", context.getChannel().toString());
        try {
            context.getChannel().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void triggerOutBoundHandler(ConnectionContext<String> socketContext,
                                       OutboundEventHandler<String> outboundEventHandler) {
        // 把数据原封不动地回传回去
        outboundEventHandler.trigger(socketContext);
    }
}

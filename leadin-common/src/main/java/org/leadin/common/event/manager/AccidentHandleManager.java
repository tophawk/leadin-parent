package org.leadin.common.event.manager;

import org.leadin.common.bean.BaseBean;
import org.leadin.common.event.Event;
import org.leadin.common.event.events.ClientStatusEvent;
import org.leadin.common.event.events.ExceptionEvent;
import org.leadin.common.notify.INotify;
import org.leadin.common.queue.BlackBoxQueue;
import org.leadin.common.remoting.IClient;
import org.leadin.common.remoting.factory.RemotingFactory;
import org.leadin.common.util.LogUtil;
import org.leadin.common.util.RtUtil;
import org.leadin.common.util.StatUtil;
import org.leadin.common.util.StringUtil;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

/**
 * @ProjectName: [ leadin]
 * @Author: [xiaofei ]
 * @CreateDate: [ 2015/3/10 17:07]
 * @Update: [说明版本修改内容] BY [xiaofei ][2015/3/10]
 * @Version: [v1.0]
 */
public class AccidentHandleManager implements INotify {
    /**
     * 黑匣子
     */
    private static BlockingQueue<Event> queue = BlackBoxQueue.getEQ();
    private IClient client;

    public AccidentHandleManager() {
    }

    @Override
    public void schedule(String key) {
        statistics();
    }

    @Override
    public int getPriorityValue() {
        return Thread.NORM_PRIORITY;
    }

    @Override
    public int getIntervalTime() {
        return 1000 * 60;
    }

    public void statistics() {
        //遍历事故事件
        Iterator<Event> iterator = queue.iterator();
        while (iterator.hasNext()) {
            ExceptionEvent exceptionEvent = (ExceptionEvent) iterator.next();
            Event event = exceptionEvent.getEvent();

            //报警
            LogUtil.warn(StringUtil.join("WARN: eventno=>", event.getEventNo(), "(", event.getEventType().toString(), ") exception=>", exceptionEvent.getCause()));
            //清除
            BlackBoxQueue.getEQ().remove(exceptionEvent);
        }

        //发送notify
        if (!RtUtil.isServer()) { //client端发送状态上来
            LogUtil.info("######sending client info begin client={}#####",client);
            String clientJson = StatUtil.toJson();
            LogUtil.info("sending... ... {}", clientJson);
            BaseBean bean = new BaseBean();
            bean.setContent(clientJson);
            bean.setTomcatName("");
            bean.setServerIp("");
            Event event = new ClientStatusEvent();
            event.setEventNo("client-info");
            event.setEventObject(bean);

            if(client == null){
                client = RemotingFactory.getDefaultFactory().createClient();
            }

            client.sent(event);
            LogUtil.info("######sending client info end client={}#####",client);
        } else {
            //TODO 服务端状态收集=》wait
        }
    }
}

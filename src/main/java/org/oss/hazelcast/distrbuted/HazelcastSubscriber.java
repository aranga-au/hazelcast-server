package org.oss.hazelcast.distrbuted;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by arang on 26/06/2016.
 */

@Component
@ConditionalOnProperty("subscriber")
public class HazelcastSubscriber implements MessageListener<Map<String,Object>>
{

    private final ITopic<Map<String,Object>> topic;
    @Autowired
    public HazelcastSubscriber(HazelcastInstance instance)
    {
         topic = instance.getTopic("my-topic");
    }

    @PostConstruct
    public void initSub()
    {
        topic.addMessageListener(this);
    }

    @Override
    public void onMessage(Message<Map<String, Object>> message)
    {
        System.out.println(message.getMessageObject());
    }
}

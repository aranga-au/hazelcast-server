package org.oss.hazelcast.distrbuted;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by arang on 26/06/2016.
 */
@Component
public class HazelcastPublisher{


    private final ITopic<Map<String,Object>> topic;

    public HazelcastPublisher(HazelcastInstance instance)
    {

        this.topic = instance.getTopic("my-topic");
    }

    public void publish(Map<String,Object> message)
    {
        topic.publish(message);
    }

}
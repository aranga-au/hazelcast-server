package org.oss.hazelcast.controller;

import org.oss.hazelcast.distrbuted.HazelcastPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by arang on 26/06/2016.
 */
@RestController
public class PublishController
{

    @Value("${aws.instance.id:n/a}")
    private String instanceId;

    @Autowired
    private HazelcastPublisher publisher;

    @RequestMapping("/api/publish")
    public Map<String,Object> publish()
    {
        final Map<String,Object> message = new HashMap<String,Object>();
        message.put("time",new Date().toString());
        message.put("instance id",instanceId);
        publisher.publish(message);
        return message;
    }
}

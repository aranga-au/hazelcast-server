package org.oss.hazelcast.controller;

import com.hazelcast.core.HazelcastInstance;
import org.oss.hazelcast.distrbuted.HazelcastPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Created by arang on 26/06/2016.
 */
@RestController
public class PublishController
{

    @Value("${aws.instance.id:n/a}")
    private String instanceId;

    @Autowired
    private HazelcastInstance instance;

    @Autowired
    private HazelcastPublisher publisher;

    @RequestMapping("/api/publish")
    public Map<String,Object> publish()
    {
        final Map<String,Object> message = new HashMap<String,Object>();
        message.put("time",new Date().toString());
        message.put("instance id",instanceId);
        long ii =System.currentTimeMillis();
        Lock lock = instance.getLock("my-lock");
        try
        {
            if (lock.tryLock())
            {
                message.put("lock",true);
                message.put("lock time",(System.currentTimeMillis()-ii)+" ms");
                publisher.publish(message);
                Thread.sleep(13500);



                lock.unlock();
            }
            else
            {
                message.put("lock",false);

            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        finally {

        }
        return message;
    }
}

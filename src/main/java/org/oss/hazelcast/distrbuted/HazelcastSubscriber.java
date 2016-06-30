package org.oss.hazelcast.distrbuted;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.Message;
import com.hazelcast.core.MessageListener;

import org.oss.hazelcast.pojo.Records;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        String tmp = System.getProperty("java.io.tmpdir");
        Path wiki_path = Paths.get(tmp, "myjson.txt");
        System.out.println(tmp);
        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes=null;

        System.out.println(message.getMessageObject()+" -"+message.getPublishingMember());
        try {

            bytes =Files.readAllBytes(wiki_path);
        }
        catch(Throwable e)
        {

        }
        Records r = new Records();
        if (bytes != null){

            try
            {
                r = mapper.readValue(bytes,Records.class);
            }catch (Throwable e){
                e.printStackTrace();
            }



        }
        message.getMessageObject().put("member",message.getPublishingMember().toString());
        r.add(message.getMessageObject());
        try {

            mapper.writeValue(wiki_path.toFile(),r);
        }catch (Throwable e)
        {
            e.printStackTrace();
        }



    }
}

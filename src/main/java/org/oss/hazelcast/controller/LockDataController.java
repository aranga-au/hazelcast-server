package org.oss.hazelcast.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.oss.hazelcast.pojo.Records;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by arang on 2/07/2016.
 */
@RestController
public class LockDataController
{

    @RequestMapping("/api/lockdata")
    public Records display() throws  Exception
    {
        String tmp = System.getProperty("java.io.tmpdir");
        Path wiki_path = Paths.get(tmp, "myjson.txt");
        System.out.println(tmp);
        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes =Files.readAllBytes(wiki_path);
        Records r = mapper.readValue(bytes,Records.class);
        return r;
    }
}

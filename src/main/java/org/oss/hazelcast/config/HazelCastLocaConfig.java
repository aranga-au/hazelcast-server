package org.oss.hazelcast.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by arang on 25/06/2016.
 */
@Configuration
@ConditionalOnMissingBean(HazelCastAwsConfig.class)
public class HazelCastLocaConfig
{
    @Bean
    public Config createConig()
    {
        Config c = new Config();
        c.setInstanceName("test-hazelcast-instance-1");
        c.setNetworkConfig(new NetworkConfig());
        c.getNetworkConfig().setJoin(new JoinConfig());
        c.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(true);


        return c;
    }

    @Bean
    @Autowired
    public HazelcastInstance createInstance(Config config)
    {
        return Hazelcast.newHazelcastInstance(config);
    }


}

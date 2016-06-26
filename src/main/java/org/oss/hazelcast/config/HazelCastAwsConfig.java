package org.oss.hazelcast.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by arang on 25/06/2016.
 */
@Configuration
@ConditionalOnProperty("aws.access.key")
public class HazelCastAwsConfig {


    @Value("{aws.access.key:qqqqq}")
    private String awsAccessKey;

    @Value("{aws.secret.access.key:kkkkk}")
    private String awsSecrectKey;

    @PostConstruct
    public void  init()
    {
        System.out.println(awsAccessKey);
    }

    /*
    @Bean
    public Config createConig()
    {
        try {
            System.out.println(awsAccessKey);
            Config c = new Config();
            c.setInstanceName("test-hazelcast-instance-1");
            c.setNetworkConfig(new NetworkConfig());
            c.getNetworkConfig().setJoin(new JoinConfig());
            c.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
            c.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
            c.getNetworkConfig().getJoin().getAwsConfig().setEnabled(true);
            c.getNetworkConfig().getJoin().getAwsConfig().setAccessKey(awsAccessKey);
            c.getNetworkConfig().getJoin().getAwsConfig().setSecretKey(awsSecrectKey);
            c.getNetworkConfig().getJoin().getAwsConfig().setRegion("ap-southeast-2");
            c.getNetworkConfig().getJoin().getAwsConfig().setHostHeader("ec2.amazonaws.com");
            c.getNetworkConfig().getJoin().getAwsConfig().setSecurityGroupName("hazelcast-sg");
            c.getNetworkConfig().getJoin().getAwsConfig().setTagKey("type");
            c.getNetworkConfig().getJoin().getAwsConfig().setTagValue("hz-nodes");
            return c;
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            throw e;
        }


    }*/

}
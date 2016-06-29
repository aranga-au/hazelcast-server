package org.oss.hazelcast.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by arang on 29/06/2016.
 */
public class Records
{
    private List<Map<String,Object>> data = new ArrayList<>();

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
    public void add(Map<String, Object> map)
    {
        data.add(map);
    }
}

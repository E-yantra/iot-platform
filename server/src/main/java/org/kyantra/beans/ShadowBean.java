package org.kyantra.beans;

import java.util.HashMap;
import java.util.Map;

public class ShadowBean {

    Map<String,HashMap<String,Object>> map;
    ThingBean thingBean;

    public ShadowBean() {
        map = new HashMap<>();
        map.put("state", new HashMap<>());
    }

    public void setThingBean(ThingBean thingBean) {
        this.thingBean = thingBean;
    }

    public String getUpdateTopic() {
        return String.format("$aws/things/thing%s/shadow/update",thingBean.getId()+"");
    }

    public String getUpdateAcceptedTopic() {
        return String.format("$aws/things/thing%s/shadow/update/accepted",thingBean.getId()+"");
    }

    public String getDeltaTopic() {
        return String.format("$aws/things/thing%s/shadow/update/delta",thingBean.getId()+"");
    }

    public String getUpdateRejectedTopic() {
        return String.format("$aws/things/thing%s/shadow/update/rejected",thingBean.getId()+"");
    }


    public void clear(){
        map.get("state").clear();
    }

    public void setDesired(DeviceAttributeBean bean, String property, Object value){
        map.get("state").putIfAbsent("desired", new HashMap<>());
        HashMap<Object, Object> submap = (HashMap<Object, Object>) map.get("state").get("desired");
        property = "device"+bean.getParentDevice().getId()+"."+property;
        submap.put(property,value);
    }

    public void setReported(DeviceAttributeBean bean, String property,Object value){
        map.get("state").putIfAbsent("reported", new HashMap<>());
        HashMap<Object, Object> submap = (HashMap<Object, Object>) map.get("state").get("reported");
        property = "device"+bean.getParentDevice().getId()+"."+property;
        submap.put(property,value);
    }

    public Object getDesired(DeviceAttributeBean bean, String property){
        map.get("state").putIfAbsent("desired", new HashMap<>());
        HashMap<Object, Object> submap = (HashMap<Object, Object>) map.get("state").get("desired");
        property = "device"+bean.getParentDevice().getId()+"."+property;
        return submap.get(property);
    }

    public Object getReported(DeviceAttributeBean bean, String property){
        map.get("state").putIfAbsent("reported", new HashMap<>());
        HashMap<Object, Object> submap = (HashMap<Object, Object>) map.get("state").get("reported");
        property = "device"+bean.getParentDevice().getId()+"."+property;
        return submap.get(property);
    }

    public Object getMap(){
        return map;
    }

}

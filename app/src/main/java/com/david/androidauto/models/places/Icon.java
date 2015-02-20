
package com.david.androidauto.models.places;

import java.util.HashMap;
import java.util.Map;

public class Icon {

    private String prefix;
    private String suffix;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

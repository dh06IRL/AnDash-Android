
package com.david.androidauto.models.gas;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class Main {

    @Expose
    private String zip;
    @Expose
    private List<Item> item = new ArrayList<Item>();
    @Expose
    private Boolean error;

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

}

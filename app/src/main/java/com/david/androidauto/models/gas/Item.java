
package com.david.androidauto.models.gas;

import com.google.gson.annotations.Expose;

public class Item {

    @Expose
    private String regular;
    @Expose
    private String plus;
    @Expose
    private String premium;
    @Expose
    private String diesel;
    @Expose
    private String brand;
    @Expose
    private String img;
    @Expose
    private String address;
    @Expose
    private Integer pupdate;
    @Expose
    private String distance;

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getPlus() {
        return plus;
    }

    public void setPlus(String plus) {
        this.plus = plus;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getDiesel() {
        return diesel;
    }

    public void setDiesel(String diesel) {
        this.diesel = diesel;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPupdate() {
        return pupdate;
    }

    public void setPupdate(Integer pupdate) {
        this.pupdate = pupdate;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}

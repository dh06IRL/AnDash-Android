
package com.david.androidauto.models.weather.day;

import com.google.gson.annotations.Expose;

public class Temp {

    @Expose
    private Double day;
    @Expose
    private Double eve;
    @Expose
    private Double max;
    @Expose
    private Double min;
    @Expose
    private Double morn;
    @Expose
    private Double night;

    /**
     * 
     * @return
     *     The day
     */
    public Double getDay() {
        return day;
    }

    /**
     * 
     * @param day
     *     The day
     */
    public void setDay(Double day) {
        this.day = day;
    }

    /**
     * 
     * @return
     *     The eve
     */
    public Double getEve() {
        return eve;
    }

    /**
     * 
     * @param eve
     *     The eve
     */
    public void setEve(Double eve) {
        this.eve = eve;
    }

    /**
     * 
     * @return
     *     The max
     */
    public Double getMax() {
        return max;
    }

    /**
     * 
     * @param max
     *     The max
     */
    public void setMax(Double max) {
        this.max = max;
    }

    /**
     * 
     * @return
     *     The min
     */
    public Double getMin() {
        return min;
    }

    /**
     * 
     * @param min
     *     The min
     */
    public void setMin(Double min) {
        this.min = min;
    }

    /**
     * 
     * @return
     *     The morn
     */
    public Double getMorn() {
        return morn;
    }

    /**
     * 
     * @param morn
     *     The morn
     */
    public void setMorn(Double morn) {
        this.morn = morn;
    }

    /**
     * 
     * @return
     *     The night
     */
    public Double getNight() {
        return night;
    }

    /**
     * 
     * @param night
     *     The night
     */
    public void setNight(Double night) {
        this.night = night;
    }

}

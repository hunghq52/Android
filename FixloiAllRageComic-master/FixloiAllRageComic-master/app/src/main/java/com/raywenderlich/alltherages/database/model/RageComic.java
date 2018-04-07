package com.raywenderlich.alltherages.database.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by laptopTCC on 5/27/2017.
 */

public class RageComic extends RealmObject {
    @PrimaryKey
    private String oid;
    private String name;
    private String url;
    private String description;
    private Float old_price;
    private Float new_price;
    private Float discount_rate;
    private Boolean is_favorite;

    public RageComic(){

    }

    public RageComic(String oid, String name, String url, String description, Float old_price, Float new_price, Float discount_rate, Boolean is_favorite) {
        this.oid = oid;
        this.name = name;
        this.url = url;
        this.description = description;
        this.old_price = old_price;
        this.new_price = new_price;
        this.discount_rate = discount_rate;
        this.is_favorite = is_favorite;
    }


    public RageComic(String url,String name, String description) {
        this.name = name;
        this.url = url;
        this.description = description;
    }

    public String getOid() {
        return oid;
    }

    public void setId(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getOld_price() {
        return old_price;
    }

    public void setOld_price(Float old_price) {
        this.old_price = old_price;
    }

    public Float getNew_price() {
        return new_price;
    }

    public void setNew_price(Float new_price) {
        this.new_price = new_price;
    }

    public Float getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(Float discount_rate) {
        this.discount_rate = discount_rate;
    }

    public Boolean getIs_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(Boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    @Override
    public String toString() {
        return "RageComic{" +
                "oid='" + oid + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", old_price=" + old_price +
                ", new_price=" + new_price +
                ", discount_rate=" + discount_rate +
                ", is_favorite=" + is_favorite +
                '}';
    }
}

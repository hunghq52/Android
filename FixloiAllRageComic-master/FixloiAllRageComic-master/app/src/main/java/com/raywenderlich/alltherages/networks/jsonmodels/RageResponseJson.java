package com.raywenderlich.alltherages.networks.jsonmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laptopTCC on 6/2/2017.
 */

public class RageResponseJson {
    @SerializedName("_id")
    private ID id;
    @SerializedName("name")
    private String name;
    @SerializedName("url")
    private String url;
    @SerializedName("description")
    private String description;
    @SerializedName("old_price")
    private Float old_price;
    @SerializedName("new_price")
    private Float new_price;
    @SerializedName("discount_rate")
    private Float discount_rate;
    @SerializedName("is_favorite")
    private Boolean is_favorite;

    public static class ID{

        @SerializedName("$oid")
        private String oid;
        public ID(String oid){
            this.oid = oid;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

    }
    public RageResponseJson(ID id,String name, String url, String description, Float old_price, Float new_price, Float discount_rate, Boolean is_favorite) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.description = description;
        this.old_price = old_price;
        this.new_price = new_price;
        this.discount_rate = discount_rate;
        this.is_favorite = is_favorite;
    }

    public RageResponseJson(String name, String url, String description, Float old_price, Float new_price, Float discount_rate, Boolean is_favorite) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.old_price = old_price;
        this.new_price = new_price;
        this.discount_rate = discount_rate;
        this.is_favorite = is_favorite;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
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
        return "RageResponseJson{" +
                "id=" + id +
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

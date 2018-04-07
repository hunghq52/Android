package com.raywenderlich.alltherages.networks.jsonmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laptopTCC on 6/8/2017.
 */

public class OrderResponseJson {
    @SerializedName("_id")
    private Id id;
    @SerializedName("items")
    private SingleOrder items;
    @SerializedName("date")
    private String date;
    @SerializedName("address_order")
    private String address_order;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("customer")
    private Customer customer;
    @SerializedName("is_Success")
    private boolean is_Success;
    @SerializedName("spend")
    private float spend;
    @SerializedName("ship_money")
    private float ship_money;
    @SerializedName("code")
    private String code;
    @SerializedName("code_price")
    private float code_price;

    public OrderResponseJson(Id id, SingleOrder items, String date, String address_order, String phone_number, Customer customer, boolean is_Success, float spend, float ship_money, String code, float code_price) {
        this.id = id;
        this.items = items;
        this.date = date;
        this.address_order = address_order;
        this.phone_number = phone_number;
        this.customer = customer;
        this.is_Success = is_Success;
        this.spend = spend;
        this.ship_money = ship_money;
        this.code = code;
        this.code_price = code_price;
    }

    public OrderResponseJson(SingleOrder items, float spend) {
        this.items = items;
        this.spend = spend;
    }

    private class SingleOrder{
        private Rage rage;
        private int count;

        private class Rage{
            @SerializedName("$oid")
            private String oid;
        }
    }

    private class Id{
        @SerializedName("$oid")
        private String oid;
    }
    private class Customer{
        @SerializedName("$oid")
        private String oid;
    }
    private class Rage{
        @SerializedName("$oid")
        private String oid;
    }
}

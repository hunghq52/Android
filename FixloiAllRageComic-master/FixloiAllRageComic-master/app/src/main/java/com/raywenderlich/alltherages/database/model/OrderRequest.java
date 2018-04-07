package com.raywenderlich.alltherages.database.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laptopTCC on 6/8/2017.
 */

public class OrderRequest {
    @SerializedName("items")
    private SingleOrder items;
    @SerializedName("address_order")
    private String address_order;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("code")
    private String code;

    public OrderRequest(SingleOrder items, String address_order, String phone_number, String user_id, String code){
        this.items = items;
        this.address_order = address_order;
        this.phone_number = phone_number;
        this.user_id = user_id;
        this.code = code;
    }


    private class SingleOrder{
        @SerializedName("id")
        private String id;
        @SerializedName("count")
        private int count;

        public SingleOrder(String id, int count) {
            this.id = id;
            this.count = count;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}

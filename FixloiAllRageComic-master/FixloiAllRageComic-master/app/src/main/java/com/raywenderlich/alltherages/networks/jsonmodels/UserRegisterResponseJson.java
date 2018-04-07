package com.raywenderlich.alltherages.networks.jsonmodels;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laptopTCC on 6/2/2017.
 */

public class UserRegisterResponseJson {
    @SerializedName("id")
    private String idUser;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("token")
    private String token;
    @SerializedName("address")
    private String address;
    @SerializedName("address_order")
    private String address_order;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("total_spend")
    private String totalSpend;
    @SerializedName("urlPic")
    private String urlPic;
    @SerializedName("urlFb")
    private String urlFb;
    /*@SerializedName("rages_like")
    private RageResponseJson rages_like;*/
    /*@SerializedName("comment")
    private CommentResponseJson comment;*/


    @SerializedName("_id")
    private Id Id;

    public static class Id {

        @SerializedName("$oid")
        private String $oid;

        @Override
        public String toString() {
            return "Id{" +
                    "$oid='" + $oid + '\'' +
                    '}';
        }
        public String get$oid() {
            return $oid;
        }

    }

    public UserRegisterResponseJson(String idUser, String totalSpend, UserRegisterResponseJson.Id id) {
        this.idUser = idUser;
        this.totalSpend = totalSpend;
        this.Id = id;
    }

    public UserRegisterResponseJson(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public UserRegisterResponseJson(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserRegisterResponseJson(Uri linkUri, String token){
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(String totalSpend) {
        this.totalSpend = totalSpend;
    }

    public UserRegisterResponseJson.Id getId() {
        return Id;
    }

    public void setId(UserRegisterResponseJson.Id id) {
        Id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_order() {
        return address_order;
    }

    public void setAddress_order(String address_order) {
        this.address_order = address_order;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUrlPic() {
        return urlPic;
    }

    public void setUrlPic(String urlPic) {
        this.urlPic = urlPic;
    }

    public String getUrlFb() {
        return urlFb;
    }

    public void setUrlFb(String urlFb) {
        this.urlFb = urlFb;
    }

    /*public RageResponseJson getRages_like() {
        return rages_like;
    }

    public void setRages_like(RageResponseJson rages_like) {
        this.rages_like = rages_like;
    }*/

   /* public CommentResponseJson getComment() {
        return comment;
    }

    public void setComment(CommentResponseJson comment) {
        this.comment = comment;
    }*/

    @Override
    public String toString() {
        return "UserRegisterResponseJson{" +
                "idUser='" + idUser + '\'' +
                ", totalSpend='" + totalSpend + '\'' +
                ", Id=" + Id +
                ", token='" + token + '\'' +
                '}';
    }

}

package com.raywenderlich.alltherages.database.model;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;
import com.raywenderlich.alltherages.networks.jsonmodels.UserRegisterResponseJson;

/**
 * Created by laptopTCC on 6/8/2017.
 */

public class User {
    //@PrimaryKey
    private String idUser;
    private String username;
    private String password;
    private String token;
    private String address;
    private String address_order;
    private String phone_number;
    private String totalSpend;
    private String urlPic;
    private String urlFb;
    //private RageResponseJson rages_like;
    //private CommentResponseJson comment;
    /*@SerializedName("_id")
    private UserRegisterResponseJson.Id Id;*/

    public User(){

    }

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

    public User(String idUser, String totalSpend, UserRegisterResponseJson.Id id) {
        this.idUser = idUser;
        this.totalSpend = totalSpend;
        //this.Id = id;
    }

    public User(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(Uri linkUri, String token){
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

    /*public UserRegisterResponseJson.Id getId() {
        return Id;
    }

    public void setId(UserRegisterResponseJson.Id id) {
        Id = id;
    }*/

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

    /*public CommentResponseJson getComment() {
        return comment;
    }*/

    /*public void setComment(CommentResponseJson comment) {
        this.comment = comment;
    }*/

    @Override
    public String toString() {
        return "User {" +
                "idUser='" + idUser + '\'' +
                ", totalSpend='" + totalSpend + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}

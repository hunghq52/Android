package com.raywenderlich.alltherages.networks.jsonmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laptopTCC on 6/4/2017.
 */

public class UserUpdate {
    @SerializedName("address")
    private String address;
    @SerializedName("name")
    private String name;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("_id")
    private Id id;
    @SerializedName("urlPic")
    private String urlPic;
    @SerializedName("urlFaceBook")
    private String urlFaceBook;
    @SerializedName("password")
    private String password;

    public UserUpdate(String address, String name, String phoneNumber, Id id, String urlPic, String urlFaceBook, String password) {
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.urlPic = urlPic;
        this.urlFaceBook = urlFaceBook;
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getUrlPic() {
        return urlPic;
    }

    public void setUrlPic(String urlPic) {
        this.urlPic = urlPic;
    }

    public String getUrlFaceBook() {
        return urlFaceBook;
    }

    public void setUrlFaceBook(String urlFaceBook) {
        this.urlFaceBook = urlFaceBook;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public class Id {

        @SerializedName("$oid")
        public String $oid;
        public String get$oid() {
            return $oid;
        }

        @Override
        public String toString() {
            return "UserUpdate{" +
                    "address='" + address + '\'' +
                    ", name='" + name + '\'' +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", id=" + id +
                    ", urlPic='" + urlPic + '\'' +
                    ", urlFaceBook='" + urlFaceBook + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}

package com.raywenderlich.alltherages.database.model;

import com.raywenderlich.alltherages.utils.Utils;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by laptopTCC on 6/13/2017.
 */

public class SingleOrder extends RealmObject {
    @PrimaryKey
    private String local_id;
    private RageComic rageComic;
    private int count;

    public SingleOrder(){

    }

    public SingleOrder(RageComic rageComic, int count) {
        this.local_id = Utils.getUUID();
        this.rageComic = rageComic;
        this.count = count;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public RageComic getRageComic() {
        return rageComic;
    }

    public void setRageComic(RageComic rageComic) {
        this.rageComic = rageComic;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "SingleOrder{" +
                "local_id='" + local_id + '\'' +
                ", rageComic=" + rageComic +
                ", count=" + count +
                '}';
    }
}

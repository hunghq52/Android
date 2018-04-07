package com.raywenderlich.alltherages.database;

import android.content.Context;

import com.raywenderlich.alltherages.database.model.RageComic;
import com.raywenderlich.alltherages.database.model.SingleOrder;
import com.raywenderlich.alltherages.utils.SharedPref;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by laptopTCC on 5/27/2017.
 */

public class DBContext {
    private static final String TAG = DBContext.class.toString();
    public Realm realm;
    public static DBContext instance;
    //public List<RageComic> rageComics;

    private DBContext(Context context){
        //rageComics = new Vector<>();
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public static void setInstance(Context context) {
        instance = new DBContext(context);
    }

    public void DeleteAllRealm(){
        try {
            realm.close();
            Realm.deleteRealm(realm.getConfiguration());
            //Realm file has been deleted.
        } catch (Exception ex){
            ex.printStackTrace();
            //No Realm file to remove.
        }
    }
//===================================================RageComic=====================================================//
    //1. Get all Ragecomic
    public List<RageComic> getAllRageComic(){
        RealmResults<RageComic> allRageComics = realm.where(RageComic.class).findAll();
        //Log.d(TAG,String.format("getAllRageComic:%s" ,allRageComics.toString()));
        return allRageComics;
    }

    //5.Get a RageComic
    public RageComic findRageComic(RageComic rageComic){
        //realm.beginTransaction();
        RageComic aRageComic = realm.where(RageComic.class).equalTo("name",rageComic.getName()).findFirst();
        //realm.commitTransaction();
        return aRageComic;
    }

    //2. Delete a Ragecomic
    public void deleteRageComic(RageComic rageComic){
        realm.beginTransaction();
        RealmResults<RageComic> delRageComics = realm.where(RageComic.class).equalTo("name",rageComic.getName()).findAll();
        if(delRageComics != null){
            for(RageComic r: delRageComics){
                r.deleteFromRealm();
            }
            realm.commitTransaction();
        }
    }

    //3. Add or update a class
    public <T> void addOrUpdate(T t){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate((RealmObject) t);
        realm.commitTransaction();
    }

    //4.Add a Class
    public <T> void add(T t){
        realm.beginTransaction();
        realm.copyToRealm((RealmObject) t);
        realm.commitTransaction();
    }
    //===================================================SingleOrder=====================================================//
    //1.get all SingleOrder
    public List<SingleOrder> getAllSingleOrder(){
        RealmResults<SingleOrder> allSingleOrder = realm.where(SingleOrder.class).findAll();
        //Log.d(TAG,String.format("getAllSingleOrder:%s" ,allSingleOrder.toString()));
        return allSingleOrder;
    }

    //2. Thay doi so luong hang dat
    public void changeQuantity(SingleOrder singleOrder, int sl){
        realm.beginTransaction();
        singleOrder.setCount(sl);
        realm.copyToRealmOrUpdate(singleOrder);
        realm.commitTransaction();
    }
    //3. Detele a singleorder
    public void deleteSingleOrder(SingleOrder singleOrder){
        realm.beginTransaction();;
        RealmResults<SingleOrder> delSingleOrder = realm.where(SingleOrder.class).equalTo("local_id",singleOrder.getLocal_id()).findAll();
        for(SingleOrder s: delSingleOrder){
            s.deleteFromRealm();
        }
        realm.commitTransaction();
    }

    //4. Tim mat hang trong singleorder
    public boolean findRageComicSingleOrder(RageComic rageComic){
        List<SingleOrder> singleOrders =  DBContext.instance.getAllSingleOrder();
        boolean existSingleOrder = false;
        for(SingleOrder so: singleOrders){
            //Lưu ý: so sánh 2 object thì sử dụng equals, không sử dụng dấu ==
            if (so.getRageComic().getOid().equals(rageComic.getOid())){
                existSingleOrder = true;
                //singleOrderList.add(so);
            }
        }
        return existSingleOrder;
    }





    public void cleanCart() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        SharedPref.instance.getSharedPreferences().edit().putInt("COUNT", 0).commit();
    }
}

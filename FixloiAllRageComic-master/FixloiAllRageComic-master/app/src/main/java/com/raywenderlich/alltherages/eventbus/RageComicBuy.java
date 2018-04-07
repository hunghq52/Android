package com.raywenderlich.alltherages.eventbus;

import com.raywenderlich.alltherages.database.model.RageComic;

/**
 * Created by laptopTCC on 5/27/2017.
 */

public class RageComicBuy {
    private RageComic rageComic;

    public RageComicBuy(RageComic rageComic) {
        this.rageComic = rageComic;
    }

    public RageComic getRageComic() {
        return rageComic;
    }

    public void setRageComic(RageComic rageComic) {
        this.rageComic = rageComic;
    }

}

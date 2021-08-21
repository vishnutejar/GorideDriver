package com.tranxit.enterprise.data.network.model;

/**
 * Created by santhosh@appoets.com on 27-11-2017.
 */

public class Walkthrough {
    public String title, description;
    public int drawable;

    public Walkthrough(int drawable, String title, String description) {
        this.drawable = drawable;
        this.title = title;
        this.description = description;
    }

}

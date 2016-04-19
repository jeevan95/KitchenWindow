package com.jeevan.finediner;

import java.io.Serializable;

/**
 * Created by JEEVAN on 12/03/2016.
 */
public class Item implements Serializable {

    private String name;
    private String desc;
    private double price;
    private int quantity =1;
    private long time;
    private double timefactor=0.2;
    public Item(Item i ){
        name = i.name;
        desc = i.desc;
        price = i.price;
        quantity = i.quantity;
        time = i.time;
    }
    public Item(String n, String ds, double pr, long ti ) {
        this.name = n;
        this.desc = ds;
        this.price = pr;
        time = ti;

    }
    public void setQuantity(int q){
        quantity = q;
    }
    public String getName(){return name;}
    public String getDesc(){return desc;}
    public double getPrice(){return price;}
    public int getQuantity(){return quantity;}
    public long getTime(){
            return Math.round(time + (getQuantity()-1)*(time*timefactor));
    }

}


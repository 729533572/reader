package com.yn.reader.model.buy;

/**
 * Created by luhe on 2018/3/22.
 */

public class BuyChoice {
    /*
    "id":1,
    "name":"11",
    "price":20,
    "days":20
    "coin":1200,
　　 "extracoin":100
    * */
    private Long id;
    private String name;
    private Float price;
    private int days;
    private int coin;
    private int extracoin;
    private boolean isSelected = false;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getPrice() {
        return price;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getDays() {
        return days;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getCoin() {
        return coin;
    }

    public void setExtracoin(int extracoin) {
        this.extracoin = extracoin;
    }

    public int getExtracoin() {
        return extracoin;
    }
}

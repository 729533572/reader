package com.yn.reader.model.chapter;

import com.yn.reader.model.BaseData;
import com.yn.reader.model.dao.BookContentBean;

/**
 * 获取章节内容
 * Created by sunxy on 2018/3/14.
 */

public class BookContentGroup extends BaseData {
    /*
    *  "countchapter": "6",*后面还有多少章未购买的
      "balance": 938,*用户余额
      "chapterautobuy": 0,*是否自动购买 1,用户已选择自动购买;0,未选择
      "price": 4,本章价格
      "chapterdetails":"灵溪宗，位于东林洲内，属于通天河的下游支脉所在"
      "isbookvip": 0 这本书是否是会员书籍
    * */
    private BookContentBean data;

    private int countchapter;
    private int balance;
    private int chapterautobuy;
    private int price;
    private int isbookvip;
    private String chapterdetails;

    public BookContentBean getData() {
        data = data == null ? new BookContentBean() : data;
        return data;
    }

    public void setData(BookContentBean data) {
        this.data = data == null ? new BookContentBean() : data;
    }

    public void setCountchapter(int countchapter) {
        this.countchapter = countchapter;
    }

    public int getCountchapter() {
        return countchapter;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void setChapterautobuy(int chapterautobuy) {
        this.chapterautobuy = chapterautobuy;
    }

    public int getChapterautobuy() {
        return chapterautobuy;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setIsbookvip(int isbookvip) {
        this.isbookvip = isbookvip;
    }

    public int getIsbookvip() {
        return isbookvip;
    }

    public void setChapterdetails(String chapterdetails) {
        this.chapterdetails = chapterdetails;
    }

    public String getChapterdetails() {
        return chapterdetails;
    }
}

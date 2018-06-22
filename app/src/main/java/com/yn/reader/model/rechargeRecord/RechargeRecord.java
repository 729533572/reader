package com.yn.reader.model.rechargeRecord;

/**
 * Created by luhe on 2018/3/21.
 */

public class RechargeRecord {
    /*
    *  "id":2,
        "userid":1,
        "content":"4大萨达",
        "money":2,
        "createdate":"2018-03-08 14:16:41"
    * */
    private Long id;
    private Long userid;
    private String content;
    private Float money;
    private String createdate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Float getMoney() {
        return money;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getCreatedate() {
        return createdate;
    }
}

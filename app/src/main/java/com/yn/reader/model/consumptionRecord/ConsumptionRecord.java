package com.yn.reader.model.consumptionRecord;

/**
 * Created by luhe on 2018/3/21.
 */

public class ConsumptionRecord {
    /*
    "id":7,
    "userid":1,
    "content":"购买了wewq 13章",
    "coin":31.5,
    "createdate":"2018-03-12 10:29:32"
    * */
    private Long id;
    private Long userid;
    private String content;
    private Float coin;
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

    public void setCoin(Float coin) {
        this.coin = coin;
    }

    public Float getCoin() {
        return coin;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getCreatedate() {
        return createdate;
    }
}

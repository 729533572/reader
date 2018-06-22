package com.yn.reader.model.dao;

import com.yn.reader.model.hotSearch.HotSearch;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by luhe on 2018/3/21.
 */
@Entity
public class SearchHistoryBean {
    @Id
    private Long id;
    @Unique
    private String name;
    private int link_type = 1;
    private String link_content;

    public SearchHistoryBean(HotSearch hotSearch) {
        this.name = hotSearch.getName();
        this.link_type = hotSearch.getLink_type();
        this.link_content = hotSearch.getLink_content();
    }

    public SearchHistoryBean(String keyword) {
        this.name = keyword;
    }

    @Generated(hash = 851414975)
    public SearchHistoryBean(Long id, String name, int link_type,
            String link_content) {
        this.id = id;
        this.name = name;
        this.link_type = link_type;
        this.link_content = link_content;
    }

    @Generated(hash = 1570282321)
    public SearchHistoryBean() {
    }



    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLink_type(int link_type) {
        this.link_type = link_type;
    }

    public int getLink_type() {
        return link_type;
    }

    public void setLink_content(String link_content) {
        this.link_content = link_content;
    }

    public String getLink_content() {
        return link_content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

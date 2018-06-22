package com.yn.reader.model.adconfig;

import java.util.List;

/**
 * 广告配置
 * Created by sunxy on 2018/1/10.
 */

public class AdConfig {
    private int id;
    private int type;
    private String name;
    private int sort;
    private String key;
    private List<KeyInfo> order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<KeyInfo> getOrder() {
        return order;
    }

    public void setOrder(List<KeyInfo> order) {
        this.order = order;
    }

    public static class KeyInfo {
        private String key1;
        private String key2;
        private int type;

        public String getKey1() {
            return key1;
        }

        public void setKey1(String key1) {
            this.key1 = key1;
        }

        public String getKey2() {
            return key2;
        }

        public void setKey2(String key2) {
            this.key2 = key2;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
}

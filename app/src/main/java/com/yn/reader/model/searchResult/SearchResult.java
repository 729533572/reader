package com.yn.reader.model.searchResult;

import java.util.List;

/**
 * Created by luhe on 2018/3/21.
 */

public class SearchResult {
    private int total;
    private int pagesize;
    private int pageno;
    private int maxpageno;
    private List<SearchResultBook> heatrank;

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    public int getPageno() {
        return pageno;
    }

    public void setMaxpageno(int maxpageno) {
        this.maxpageno = maxpageno;
    }

    public int getMaxpageno() {
        return maxpageno;
    }

    public void setHeatrank(List<SearchResultBook> heatrank) {
        this.heatrank = heatrank;
    }

    public List<SearchResultBook> getHeatrank() {
        return heatrank;
    }
}

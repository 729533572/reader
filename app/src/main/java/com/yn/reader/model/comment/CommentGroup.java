package com.yn.reader.model.comment;

import java.util.List;

/**
 * 评论
 * Created by sunxy on 2018/3/16.
 */

public class CommentGroup {
    private int total;
    private int pagesize;
    private int pageno;
    private int maxpageno;
    private int type;
    private List<Comment> comments;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

    public int getMaxpageno() {
        return maxpageno;
    }

    public void setMaxpageno(int maxpageno) {
        this.maxpageno = maxpageno;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getType(){return type;}

    public void setType(int type){
        this.type = type;
    }
}

package com.hzq.base.dao;


import java.io.Serializable;

/**
 * Created by hzq on 16/9/27.
 */
public class Page implements Serializable {
    protected int per = 20; //每页数量
    protected int page = 1; //当前页
    protected long count = -1L; //总数量
    protected long pages = -1L; //总页数

    public Page() {
    }

    public Page(int per, int current, int count, int pages) {
        this.per = per;
        this.page = current;
        this.count = count;
        this.pages = pages;
    }


    public Page(int per) {
        this.per = per;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
        if (page < 1) {
            this.page = 1;
        }

    }

    public int getPer() {
        return this.per;
    }

    public void setPer(int per) {
        this.per = per;
    }

    public Page per(int thePer) {
        this.setPer(thePer);
        return this;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getPages() {
        return pages;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }
}

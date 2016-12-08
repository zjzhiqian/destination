package com.hzq.base.dao;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzq on 16/9/27.
 */
public class PageResult<T> {
    private Page page = new Page();

    private List<T> resultList = new ArrayList<>();


    public PageResult(Page page, List<T> resultList) {
        this.page = page;
        this.resultList = resultList;
    }


    public PageResult(int current, int per, int count, List<T> data) {
        page.setPage(current);
        page.setPer(per);
        page.setCount(count);
        setResultList(data);
    }

    public PageResult(int current, int per, int count, int pages, List<T> data) {
        page.setPage(current);
        page.setPer(per);
        page.setCount(count);
        page.setPages(pages);
        setResultList(data);
    }


    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }
}

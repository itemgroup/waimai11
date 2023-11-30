package com.amx.luckin.entity;

public class Category {
    private Integer id;
    private String title;
    private String desc;
    private String indexImg;

    public Category() {
    }

    public Category(Integer id, String title, String desc, String indexImg) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.indexImg = indexImg;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", indexImg='" + indexImg + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIndexImg() {
        return indexImg;
    }

    public void setIndexImg(String indexImg) {
        this.indexImg = indexImg;
    }
}

package com.amx.luckin.entity;

public class Coffee {
    private Integer id;
    private String title;
    private Integer price;
    private Integer oldprice;
    private String image;
    private Integer parentId;

    public Coffee() {
    }

    public Coffee(Integer id, String title, Integer price, Integer oldprice, String image, Integer parentId) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.oldprice = oldprice;
        this.image = image;
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Coffee{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", oldprice=" + oldprice +
                ", image='" + image + '\'' +
                ", parentId=" + parentId +
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getOldprice() {
        return oldprice;
    }

    public void setOldprice(Integer oldprice) {
        this.oldprice = oldprice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}

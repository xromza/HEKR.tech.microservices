package ru.xromza.order.interfaces;

public interface ProductInfoInterface {
    void setBrand(String brand);
    void setColor(String color);
    void setMainImageUrl(String url);
    void setSize(String size);
    void setTitle(String title);
    void setProductId(Long id);
    void setSku(String sku);
    Long getVariantId();
}

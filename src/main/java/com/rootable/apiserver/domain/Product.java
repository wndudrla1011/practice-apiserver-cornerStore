package com.rootable.apiserver.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "tbl_product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "imageList")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    private String name;

    private int price;

    private String description;

    private boolean delFlag;

    @ElementCollection
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();

    public void changeName(String name) {
        this.name = name;
    }

    public void changePrice(int price) {
        this.price = price;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeDel(boolean delFlag) {
        this.delFlag = delFlag;
    }

    //객체로 이미지 추가
    public void addImage(ProductImage image) {
        image.setOrd(imageList.size());
        imageList.add(image);
    }

    //문자열로 이미지 추가
    public void addImageString(String fileName) {

        ProductImage productImage = ProductImage.builder()
                .fileName(fileName)
                .build();

        addImage(productImage);

    }

    public void clearList() {
        this.imageList.clear();
    }

}

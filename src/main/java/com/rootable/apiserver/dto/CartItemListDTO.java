package com.rootable.apiserver.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CartItemListDTO {

    private Long cino;

    private int qty;

    private Long pno;

    private String name;

    private int price;

    private String imageFile;

    public CartItemListDTO(Long cino, int qty, Long pno, String name, int price, String imageFile) {
        this.cino = cino;
        this.qty = qty;
        this.pno = pno;
        this.name = name;
        this.price = price;
        this.imageFile = imageFile;
    }

}

package com.rootable.apiserver.repository;

import com.rootable.apiserver.domain.CartItem;
import com.rootable.apiserver.dto.CartItemListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /*
     * 사용자의 모든 CartItem 목록 == CartItemListDTO
     * input -> email
     * output -> CartItemListDTO
     * */
    @Query("select " +
            "new com.rootable.apiserver.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.name, p.price , pi.fileName) " +
            "from " +
            "CartItem ci inner join Cart c on ci.cart = c " +
            "left join Product p on ci.product = p " +
            "left join p.imageList pi " +
            "where " +
            "c.owner.email = :email and pi.ord = 0 " +
            "order by ci desc")
    List<CartItemListDTO> getItemsOfCartDTOByEmail(@Param("email") String email);

    /*
     * email, 상품 번호로 해당 상품이 장바구니에 존재하는지 확인
     * */
    @Query("select ci " +
            "from " +
            "CartItem ci inner join Cart c on ci.cart = c " +
            "where " +
            "c.owner.email = :email and ci.product.pno = :pno")
    CartItem getItemOfPno(@Param("email") String email, @Param("pno") Long pno);

    /*
     * 장바구니 아이템 번호로 장바구니 번호를 얻어오려고 하는 경우
     * */
    @Query("select c.cno " +
            "from " +
            "Cart c inner join CartItem ci on ci.cart = c " +
            "where " +
            "ci.cino = :cino")
    Long getCartFromItem(@Param("cino") Long cino);

    /*
     * 장바구니 번호로 모든 장바구니 아이템 조회
     * */
    @Query("select " +
            "new com.rootable.apiserver.dto.CartItemListDTO(ci.cino, ci.qty, p.pno, p.name, p.price, pi.fileName) " +
            "from " +
            "CartItem ci inner join Cart c on ci.cart = c " +
            "left join Product p on ci.product = p " +
            "left join p.imageList pi " +
            "where " +
            "c.cno = :cno and pi.ord = 0 " +
            "order by ci desc")
    List<CartItemListDTO> getItemsOfCartDTOByCart(@Param("cno") Long cno);

}

package com.rootable.apiserver.repository;

import com.rootable.apiserver.domain.Cart;
import com.rootable.apiserver.domain.CartItem;
import com.rootable.apiserver.domain.Member;
import com.rootable.apiserver.domain.Product;
import com.rootable.apiserver.dto.CartItemListDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class CartRepositoryTests {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @Commit
    @Test
    public void insertByProduct() throws Exception {

        log.info("test1-----------------------");

        //사용자가 전송하는 정보
        String email = "user1@aaa.com";
        Long pno = 6L;
        int qty = 2;

        //만일 기존에 사용자의 장바구니 아이템이 있었다면
        CartItem cartItem = cartItemRepository.getItemOfPno(email, pno);

        //이미 사용자의 장바구니에 담겨 있는 상품
        if (cartItem != null) {
            cartItem.changeQty(qty);
            cartItemRepository.save(cartItem);
            return;
        }

        //장바구니 아이템이 없었다면 장바구니부터 확인 필요

        //사용자가 장바구니를 만든적이 있는지 확인
        Optional<Cart> result = cartRepository.getCartOfMember(email);

        Cart cart = null;

        //사용자의 장바구니가 존재하지 않으면 장바구니 생성
        if (result.isEmpty()) {
            log.info("MemberCart is not exist!!");

            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();

            cart = cartRepository.save(tempCart);
        } else { //장바구니는 있지만 해당 상품의 장바구니 아이템은 없는 경우
            cart = result.get();
        }

        log.info(cart);

        //장바구니 아이템 생성
        Product product = Product.builder().pno(pno).build();
        cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();

        cartItemRepository.save(cartItem);

    }

    @Transactional
    @Commit
    @Test
    public void updateByCino() throws Exception {

        Long cino = 3L;
        int qty = 5;

        Optional<CartItem> result = cartItemRepository.findById(cino);

        CartItem cartItem = result.orElseThrow();

        cartItem.changeQty(qty);

        cartItemRepository.save(cartItem);

    }

    @Test
    public void listOfMember() throws Exception {

        String email = "user1@aaa.com";

        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByEmail(email);

        for (CartItemListDTO dto : cartItemList) {
            log.info(dto);
        }

    }

    @Transactional
    @Commit
    @Test
    public void deleteThenList() throws Exception {

        Long cino = 3L;

        Long cno = cartItemRepository.getCartFromItem(cino); //장바구니 조회

        cartItemRepository.deleteById(cino); //장바구니 아이템 삭제

        //장바구니 아이템 목록 조회
        List<CartItemListDTO> cartItemList = cartItemRepository.getItemsOfCartDTOByCart(cno);

        for (CartItemListDTO dto : cartItemList) {
            log.info(dto);
        }

    }
    
}

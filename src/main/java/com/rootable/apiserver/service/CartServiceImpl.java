package com.rootable.apiserver.service;

import com.rootable.apiserver.domain.Cart;
import com.rootable.apiserver.domain.CartItem;
import com.rootable.apiserver.domain.Member;
import com.rootable.apiserver.domain.Product;
import com.rootable.apiserver.dto.CartItemDTO;
import com.rootable.apiserver.dto.CartItemListDTO;
import com.rootable.apiserver.repository.CartItemRepository;
import com.rootable.apiserver.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItemListDTO> addOrModify(CartItemDTO cartItemDTO) {

        //사용자가 전송하는 정보
        String email = cartItemDTO.getEmail();
        Long pno = cartItemDTO.getPno();
        int qty = cartItemDTO.getQty();
        Long cino = cartItemDTO.getCino();

        log.info("======================================================");
        log.info(cartItemDTO.getCino() == null);

        //장바구니 아이템 번호가 있어서 수량만 변경하는 경우
        if (cino != null) {
            Optional<CartItem> cartItemResult = cartItemRepository.findById(cino);

            CartItem cartItem = cartItemResult.orElseThrow();

            cartItem.changeQty(qty);

            cartItemRepository.save(cartItem);

            return getCartItems(email);
        }

        //장바구니 아이템 번호 cino가 없는 경우

        //사용자의 카트
        Cart cart = getCart(email);

        CartItem cartItem = null;

        /*
        * 해당 상품이 장바구니 아이템에 있는지 확인
        * 이미 동일한 상품이 담긴적이 있을 수 있으므로
        * */
        cartItem = cartItemRepository.getItemOfPno(email, pno);

        if (cartItem == null) { //현재 장바구니에 없는 상품
            Product product = Product.builder().pno(pno).build();
            cartItem = CartItem.builder().product(product).cart(cart).qty(qty).build();
        } else { //이미 장바구니에 담긴 상품
            cartItem.changeQty(qty); //수량 수정
        }

        cartItemRepository.save(cartItem); //장바구니 아이템 저장

        return getCartItems(email);

    }

    //사용자의 장바구니가 없었다면 새로운 장바구니를 생성하고 반환
    private Cart getCart(String email) {

        Cart cart = null;

        Optional<Cart> result = cartRepository.getCartOfMember(email);

        if (result.isEmpty()) {
            log.info("Cart of the member is not exist!!");

            Member member = Member.builder().email(email).build();
            Cart tempCart = Cart.builder().owner(member).build();

            cart = cartRepository.save(tempCart);
        } else {
            cart = result.get();
        }

        return cart;
    }

    @Override
    public List<CartItemListDTO> getCartItems(String email) {
        return cartItemRepository.getItemsOfCartDTOByEmail(email);
    }

    @Override
    public List<CartItemListDTO> remove(Long cino) {

        Long cno = cartItemRepository.getCartFromItem(cino);

        log.info("cart no: " + cno);

        cartItemRepository.deleteById(cino);

        return cartItemRepository.getItemsOfCartDTOByCart(cno);

    }

}

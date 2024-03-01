package com.rootable.apiserver.service;

import com.rootable.apiserver.dto.PageRequestDTO;
import com.rootable.apiserver.dto.PageResponseDTO;
import com.rootable.apiserver.dto.ProductDTO;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProductService {

    PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO);

    Long register(ProductDTO productDTO);

    ProductDTO get(Long pno);

    void modify(ProductDTO productDTO);

    void remove(Long pno);

}

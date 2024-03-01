package com.rootable.apiserver.repository.search;

import com.rootable.apiserver.dto.PageRequestDTO;
import com.rootable.apiserver.dto.PageResponseDTO;
import com.rootable.apiserver.dto.ProductDTO;

public interface ProductSearch {

    PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO);

}

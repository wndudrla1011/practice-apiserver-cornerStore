package com.rootable.apiserver.service;

import com.rootable.apiserver.dto.PageRequestDTO;
import com.rootable.apiserver.dto.PageResponseDTO;
import com.rootable.apiserver.dto.ProductDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Test
    public void list() throws Exception {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        PageResponseDTO<ProductDTO> responseDTO = productService.getList(pageRequestDTO);

        log.info(responseDTO.getDtoList());

    }

    @Test
    public void register() throws Exception {

        ProductDTO productDTO = ProductDTO.builder()
                .name("New Product")
                .price(1000)
                .description("This is New Product")
                .build();

        productDTO.setUploadFileNames(List.of(UUID.randomUUID() + "_" + "Test1.jpg",
                                              UUID.randomUUID() + "_" + "Test2.jpg"));

        productService.register(productDTO);

    }
    
}

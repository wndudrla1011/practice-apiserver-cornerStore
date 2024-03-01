package com.rootable.apiserver.repository;

import com.rootable.apiserver.domain.Product;
import com.rootable.apiserver.dto.PageRequestDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void insert() throws Exception {

        for (int i = 0; i < 10; i++) {

            Product product = Product.builder()
                    .name("Test " + i)
                    .price(1000 + i * 100)
                    .description("Test Desc " + i)
                    .build();

            product.addImageString(UUID.randomUUID() + "_" + "IMAGE1.jpg");

            product.addImageString(UUID.randomUUID() + "_" + "IMAGE2.jpg");

            productRepository.save(product);

        }

    }

    @Transactional
    @Test
    @DisplayName("@Transactional 사용")
    public void read1() throws Exception {

        Long pno = 1L;

        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        log.info(product);

        log.info(product.getImageList());

    }

    @Test
    @DisplayName("@EntityGraph 사용")
    public void read2() throws Exception {

        Long pno = 1L;

        Optional<Product> result = productRepository.selectOne(pno);

        Product product = result.orElseThrow();

        log.info(product);

        log.info(product.getImageList());

    }

    @Transactional
    @Test
    public void delete() throws Exception {

        Long pno = 2L;

        productRepository.updateToDelete(pno, true);

        log.info(productRepository.selectOne(pno));

    }

    @Test
    @DisplayName("상품에 저장된 파일 변경")
    public void update() throws Exception {

        Product product = productRepository.selectOne(1L).get();

        product.changePrice(3000);

        product.clearList();

        product.addImageString(UUID.randomUUID() + "_" + "IMAGE1.jpa");
        product.addImageString(UUID.randomUUID() + "_" + "IMAGE2.jpa");
        product.addImageString(UUID.randomUUID() + "_" + "IMAGE3.jpa");

        productRepository.save(product);

    }
    
    @Test
    public void list() throws Exception {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);

        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));

    }
 
    @Test
    public void search() throws Exception {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        productRepository.searchList(pageRequestDTO);

    }
    
}

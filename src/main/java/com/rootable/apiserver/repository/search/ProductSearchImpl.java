package com.rootable.apiserver.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.rootable.apiserver.domain.Product;
import com.rootable.apiserver.domain.QProduct;
import com.rootable.apiserver.domain.QProductImage;
import com.rootable.apiserver.dto.PageRequestDTO;
import com.rootable.apiserver.dto.PageResponseDTO;
import com.rootable.apiserver.dto.ProductDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public PageResponseDTO<ProductDTO> searchList(PageRequestDTO pageRequestDTO) {

        log.info("-------------------searchList-------------------");

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());

        QProduct product = QProduct.product;
        QProductImage productImage = QProductImage.productImage;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(product.imageList, productImage); //@ElementCollection 처리

        query.where(productImage.ord.eq(0));

        Objects.requireNonNull(getQuerydsl()).applyPagination(pageable, query); //페이징

        List<Tuple> productList = query.select(product, productImage).fetch();

        long count = query.fetchCount();

        log.info("====================================");
        log.info(productList);

        return null;

    }

}

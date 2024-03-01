package com.rootable.apiserver.service;

import com.rootable.apiserver.domain.Product;
import com.rootable.apiserver.domain.ProductImage;
import com.rootable.apiserver.dto.PageRequestDTO;
import com.rootable.apiserver.dto.PageResponseDTO;
import com.rootable.apiserver.dto.ProductDTO;
import com.rootable.apiserver.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /*
    * 목록 조회
    * */
    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()); //내림차순 10개 데이터 페이징

        Page<Object[]> result = productRepository.selectList(pageable); //쿼리 실행

        //Object[] => [[product10, productImage10], [product9], [productImage9], ...]
        List<ProductDTO> dtoList = result.get().map(arr -> {

            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            String imageStr = productImage.getFileName(); //업로드 파일 이름(0번째만)

            return ProductDTO.builder()
                    .pno(product.getPno())
                    .name(product.getName())
                    .price(product.getPrice())
                    .description(product.getDescription())
                    .uploadFileNames(List.of(imageStr)) //0번째 파일 하나만
                    .build();

        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .totalCount((int) totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();

    }

    /*
    * 등록
    * */
    @Override
    public Long register(ProductDTO productDTO) {

        Product product = dtoToEntity(productDTO);

        log.info("--------------------------------");
        log.info(product);
        log.info(product.getImageList());

        return productRepository.save(product).getPno();

    }

    /*
    * 단건 조회
    * */
    @Override
    public ProductDTO get(Long pno) {

        Optional<Product> result = productRepository.findById(pno);

        Product product = result.orElseThrow();

        return entityToDTO(product);

    }

    @Override
    public void modify(ProductDTO productDTO) {

        //조회
        Product product = productRepository.findById(productDTO.getPno()).orElseThrow();

        //수정
        product.changePrice(productDTO.getPrice());
        product.changeName(productDTO.getName());
        product.changeDescription(productDTO.getDescription());
        product.changeDel(productDTO.isDelFlag());

        //final file list
        List<String> uploadFileNames = productDTO.getUploadFileNames();

        product.clearList(); //imageList 비움

        //재업로드를 했다면: imageList <- uploadFileNames
        if (uploadFileNames != null && !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(product::addImageString);
        }

        //저장
        productRepository.save(product);

    }

    @Override
    public void remove(Long pno) {
        productRepository.deleteById(pno);
    }

    /*
    * DTO -> Entity
    * 등록에서 사용
    * */
    private Product dtoToEntity(ProductDTO productDTO) {

        Product product = Product.builder()
                .pno(productDTO.getPno())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .build();

        List<String> uploadFileNames = productDTO.getUploadFileNames();

        if (uploadFileNames == null || uploadFileNames.size() == 0) {
            return product;
        }

        uploadFileNames.forEach(product::addImageString);

        return product;

    }

    /*
    * Entity -> DTO
    * 조회에서 사용
    * */
    private ProductDTO entityToDTO(Product product) {

        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .delFlag(product.isDelFlag())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if (imageList == null || imageList.isEmpty()) {
            return productDTO;
        }

        List<String> fileNameList = imageList.stream().map(ProductImage::getFileName).toList();

        productDTO.setUploadFileNames(fileNameList);

        return productDTO;

    }

}

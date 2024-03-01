package com.rootable.apiserver.controller;

import com.rootable.apiserver.dto.PageRequestDTO;
import com.rootable.apiserver.dto.PageResponseDTO;
import com.rootable.apiserver.dto.ProductDTO;
import com.rootable.apiserver.service.ProductService;
import com.rootable.apiserver.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;
    private final ProductService productService;

    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO) {

        log.info("register : " + productDTO);

        List<MultipartFile> files = productDTO.getFiles(); //파일 목록

        List<String> uploadedFileNames = fileUtil.saveFiles(files); //업로드한 파일명들

        productDTO.setUploadFileNames(uploadedFileNames);

        log.info("uploadedFileNames : " + uploadedFileNames);

        Long pno = productService.register(productDTO);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Map.of("RESULT", pno);

    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName) {
        return fileUtil.getFile(fileName);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        return productService.getList(pageRequestDTO);
    }

    @GetMapping("/{pno}")
    public ProductDTO read(@PathVariable("pno") Long pno) {
        return productService.get(pno);
    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable("pno") Long pno, ProductDTO productDTO) {

        productDTO.setPno(pno);

        //old files
        ProductDTO oldProductDTO = productService.get(pno);
        List<String> oldFileNames = oldProductDTO.getUploadFileNames();

        //new files (except for existing files)
        List<MultipartFile> newFiles = productDTO.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(newFiles);

        //existing Files
        List<String> uploadedFileNames = productDTO.getUploadFileNames();

        /*
        * final file list
        * 🌟 uploadedFileNames 를 수정하면 ProductDTO 의 필드도 수정됨 🌟
        * */
        if (currentUploadFileNames != null && !currentUploadFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        productService.modify(productDTO); //수정

        //업로드한 파일 목록에 포함되지 않은 기존 파일들 제거
        if (oldFileNames != null && !oldFileNames.isEmpty()) {

            List<String> willRemoveFiles = oldFileNames
                    .stream()
                    .filter(oldFileName -> !uploadedFileNames.contains(oldFileName))
                    .toList();

            fileUtil.deleteFiles(willRemoveFiles);

        }

        return Map.of("RESULT", "SUCCESS");

    }

    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable("pno") Long pno) {

        List<String> oldFileNames = productService.get(pno).getUploadFileNames();

        productService.remove(pno);

        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "SUCCESS");

    }

}

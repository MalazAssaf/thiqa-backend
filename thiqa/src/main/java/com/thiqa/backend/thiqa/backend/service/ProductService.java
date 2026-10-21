package com.thiqa.backend.thiqa.backend.service;

import com.thiqa.backend.thiqa.backend.dto.ProductCreateRequest;
import com.thiqa.backend.thiqa.backend.dto.ProductResponse;
import com.thiqa.backend.thiqa.backend.dto.ProductUpdateRequest;
import com.thiqa.backend.thiqa.backend.entity.Product;
import com.thiqa.backend.thiqa.backend.entity.Store;
import com.thiqa.backend.thiqa.backend.repository.ProductRepository;
import com.thiqa.backend.thiqa.backend.shared.CustomResponseException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  @Value("${app.upload-dir}")
  private String uploadDir;

  private static final List<String> ALLOWED = List.of("image/jpeg", "image/png", "image/webp");

  public static ProductResponse toResponse(Product p) {
    return new ProductResponse(p.getId(), p.getName(), p.getDescription(),
        p.getPrice(), p.getImageUrl(), p.getStock());
  }

  public List<ProductResponse> listForStore(Store store) {
    return productRepository.findByStoreOrderByNameAsc(store).stream()
        .map(ProductService::toResponse)
        .toList();
  }

  public ProductResponse create(Store store, ProductCreateRequest request) throws IOException {
    Product product = new Product();
    product.setStore(store);
    apply(product, request.name(), request.description(), request.price(), request.stock());
    product.setImageUrl(saveImage(request.image()));
    return toResponse(productRepository.save(product));
  }

  public ProductResponse update(Store store, Long id, ProductUpdateRequest request) throws IOException {
    Product product = findOwned(store, id);
    apply(product, request.name(), request.description(), request.price(), request.stock());
    if (request.image() != null && !request.image().isEmpty()) {
      product.setImageUrl(saveImage(request.image())); // no new image -> keep the current one
    }
    return toResponse(productRepository.save(product));
  }

  private void apply(Product product, String name, String description, BigDecimal price, Integer stock) {
    product.setName(name.trim());
    product.setDescription(description == null ? null : description.trim());
    product.setPrice(price);
    product.setStock(stock);
  }

  public void delete(Store store, Long id) {
    productRepository.delete(findOwned(store, id));
  }

  private String saveImage(MultipartFile file) throws IOException {
    if (!ALLOWED.contains(file.getContentType())) {
      throw CustomResponseException.badRequestException("Upload a JPG, PNG or WEBP image.");
    }
    String ext = switch (file.getContentType()) {
      case "image/png" -> ".png";
      case "image/webp" -> ".webp";
      default -> ".jpg";
    };
    String fileName = UUID.randomUUID() + ext;
    Path folder = Paths.get(uploadDir, "products");
    Files.createDirectories(folder);
    Files.copy(file.getInputStream(), folder.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
    return "/uploads/products/" + fileName;
  }

  private Product findOwned(Store store, Long id) {
    return productRepository.findByIdAndStore(id, store)
        .orElseThrow(() -> CustomResponseException.resourceNotFoundException("Product not found."));
  }
}
package com.product.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.product.api.dto.in.DtoProductImageIn;
import com.product.api.entity.ProductImage;
import com.product.api.repository.RepoProduct;
import com.product.api.repository.RepoProductImage;
import com.product.exception.ApiException;
import com.product.exception.DBAccessException;

@Service
public class SvcProductImageImp implements SvcProductImage {

    @Autowired
    RepoProductImage repo;

    @Autowired
    RepoProduct repoProduct;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.images}")
    private String uploadImages;

    @Override
    public ResponseEntity<List<ProductImage>> getProductImages(Integer productId) {
        try {
            validateProductId(productId);
            List<ProductImage> images = repo.findByProductId(productId);
            return new ResponseEntity<>(images, HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }

    @Override
    public ResponseEntity<String> createProductImage(Integer productId, DtoProductImageIn in) {
        try {
            validateProductId(productId);

            // Eliminar prefijo Base64 si existe
            String imageData = in.getImage();
            if (imageData.startsWith("data:image")) {
                int commaIndex = imageData.indexOf(",");
                if (commaIndex != -1) {
                    imageData = imageData.substring(commaIndex + 1);
                }
            }

            // Decodificar Base64 a bytes
            byte[] imageBytes = Base64.getDecoder().decode(imageData);

            // Generar nombre único y construir ruta
            String fileName = UUID.randomUUID().toString() + ".png";
            Path imagePath = Paths.get(uploadDir, uploadImages, "product", fileName);

            // Crear directorio si no existe y guardar archivo
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, imageBytes);

            // Guardar ruta en base de datos
            ProductImage productImage = new ProductImage();
            productImage.setProductId(productId);
            productImage.setImage("/product/" + fileName);
            productImage.setStatus(1);
            repo.save(productImage);

            return new ResponseEntity<>("La imagen ha sido registrada", HttpStatus.CREATED);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        } catch (IOException e) {
            throw new ApiException("Error al guardar el archivo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteProductImage(Integer productId, Integer productImageId) {
        try {
            validateProductId(productId);

            ProductImage productImage = repo.findById(productImageId)
                    .orElseThrow(() -> new ApiException(
                            "El id de imagen no existe", HttpStatus.NOT_FOUND));

            // Verificar que la imagen pertenece al producto indicado
            if (!productImage.getProductId().equals(productId)) {
                throw new ApiException(
                        "La imagen no pertenece al producto indicado", HttpStatus.BAD_REQUEST);
            }

            // Eliminar archivo del sistema de archivos
            String imageUrl = productImage.getImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                if (imageUrl.startsWith("/")) {
                    imageUrl = imageUrl.substring(1);
                }
                Path imagePath = Paths.get(uploadDir, uploadImages, imageUrl);
                Files.deleteIfExists(imagePath);
            }

            repo.delete(productImage);
            return new ResponseEntity<>("La imagen ha sido eliminada", HttpStatus.OK);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        } catch (IOException e) {
            throw new ApiException("Error al eliminar el archivo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void validateProductId(Integer id) {
        try {
            if (repoProduct.findById(id).isEmpty()) {
                throw new ApiException("El id del producto no existe", HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }
}

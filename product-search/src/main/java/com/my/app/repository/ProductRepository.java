package com.my.app.repository;

import com.my.app.model.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ProductRepository extends ElasticsearchRepository<Product, String> {
    @NonNull List<Product> findByNameLikeOrDescriptionLikeOrCategoryLikeOrStorageLikeOrRamLikeOrProcessorLike(
            String name, String description, String category, String storage, String ram, String processor);

    @NonNull List<Product> findByNameOrDescriptionOrCategoryOrStorageOrRamOrProcessor(
            String name, String description, String category, String storage, String ram, String processor);
}

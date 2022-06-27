package com.micropos.batch.repository;

import com.micropos.batch.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<Product, String> {
    @Override
    <S extends Product> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    <S extends Product> S save(S entity);

    Optional<Product> findByAsin(String s);

    boolean existsByAsin(String s);

    @Override
    Iterable<Product> findAll();

    @Override
    long count();
}

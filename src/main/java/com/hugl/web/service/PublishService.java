package com.hugl.web.service;

import com.hugl.web.service.dto.PublishDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.hugl.web.domain.Publish}.
 */
public interface PublishService {

    /**
     * Save a publish.
     *
     * @param publishDTO the entity to save.
     * @return the persisted entity.
     */
    PublishDTO save(PublishDTO publishDTO);

    /**
     * Get all the publishes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PublishDTO> findAll(Pageable pageable);


    /**
     * Get the "id" publish.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PublishDTO> findOne(Long id);

    /**
     * Delete the "id" publish.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

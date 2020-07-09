package com.hugl.web.service.impl;

import com.hugl.web.service.PublishService;
import com.hugl.web.domain.Publish;
import com.hugl.web.repository.PublishRepository;
import com.hugl.web.service.dto.PublishDTO;
import com.hugl.web.service.mapper.PublishMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Publish}.
 */
@Service
@Transactional
public class PublishServiceImpl implements PublishService {

    private final Logger log = LoggerFactory.getLogger(PublishServiceImpl.class);

    private final PublishRepository publishRepository;

    private final PublishMapper publishMapper;

    public PublishServiceImpl(PublishRepository publishRepository, PublishMapper publishMapper) {
        this.publishRepository = publishRepository;
        this.publishMapper = publishMapper;
    }

    @Override
    public PublishDTO save(PublishDTO publishDTO) {
        log.debug("Request to save Publish : {}", publishDTO);
        Publish publish = publishMapper.toEntity(publishDTO);
        publish = publishRepository.save(publish);
        return publishMapper.toDto(publish);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublishDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Publishes");
        return publishRepository.findAll(pageable)
            .map(publishMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PublishDTO> findOne(Long id) {
        log.debug("Request to get Publish : {}", id);
        return publishRepository.findById(id)
            .map(publishMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Publish : {}", id);
        publishRepository.deleteById(id);
    }
}

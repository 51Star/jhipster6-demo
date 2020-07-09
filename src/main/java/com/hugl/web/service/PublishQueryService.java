package com.hugl.web.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.hugl.web.domain.Publish;
import com.hugl.web.domain.*; // for static metamodels
import com.hugl.web.repository.PublishRepository;
import com.hugl.web.service.dto.PublishCriteria;
import com.hugl.web.service.dto.PublishDTO;
import com.hugl.web.service.mapper.PublishMapper;

/**
 * Service for executing complex queries for {@link Publish} entities in the database.
 * The main input is a {@link PublishCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PublishDTO} or a {@link Page} of {@link PublishDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PublishQueryService extends QueryService<Publish> {

    private final Logger log = LoggerFactory.getLogger(PublishQueryService.class);

    private final PublishRepository publishRepository;

    private final PublishMapper publishMapper;

    public PublishQueryService(PublishRepository publishRepository, PublishMapper publishMapper) {
        this.publishRepository = publishRepository;
        this.publishMapper = publishMapper;
    }

    /**
     * Return a {@link List} of {@link PublishDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PublishDTO> findByCriteria(PublishCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Publish> specification = createSpecification(criteria);
        return publishMapper.toDto(publishRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PublishDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PublishDTO> findByCriteria(PublishCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Publish> specification = createSpecification(criteria);
        return publishRepository.findAll(specification, page)
            .map(publishMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PublishCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Publish> specification = createSpecification(criteria);
        return publishRepository.count(specification);
    }

    /**
     * Function to convert {@link PublishCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Publish> createSpecification(PublishCriteria criteria) {
        Specification<Publish> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Publish_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Publish_.name));
            }
            if (criteria.getPicUrl1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPicUrl1(), Publish_.picUrl1));
            }
            if (criteria.getPicUrl2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPicUrl2(), Publish_.picUrl2));
            }
            if (criteria.getPicUrl3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPicUrl3(), Publish_.picUrl3));
            }
            if (criteria.getPicUrl4() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPicUrl4(), Publish_.picUrl4));
            }
            if (criteria.getMemberId() != null) {
                specification = specification.and(buildSpecification(criteria.getMemberId(),
                    root -> root.join(Publish_.member, JoinType.LEFT).get(Member_.id)));
            }
        }
        return specification;
    }
}

package com.hugl.web.web.rest;

import com.hugl.web.service.PublishService;
import com.hugl.web.web.rest.errors.BadRequestAlertException;
import com.hugl.web.service.dto.PublishDTO;
import com.hugl.web.service.dto.PublishCriteria;
import com.hugl.web.service.PublishQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.hugl.web.domain.Publish}.
 */
@RestController
@RequestMapping("/api")
public class PublishResource {

    private final Logger log = LoggerFactory.getLogger(PublishResource.class);

    private static final String ENTITY_NAME = "publishPublish";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PublishService publishService;

    private final PublishQueryService publishQueryService;

    public PublishResource(PublishService publishService, PublishQueryService publishQueryService) {
        this.publishService = publishService;
        this.publishQueryService = publishQueryService;
    }

    /**
     * {@code POST  /publishes} : Create a new publish.
     *
     * @param publishDTO the publishDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new publishDTO, or with status {@code 400 (Bad Request)} if the publish has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/publishes")
    public ResponseEntity<PublishDTO> createPublish(@RequestBody PublishDTO publishDTO) throws URISyntaxException {
        log.debug("REST request to save Publish : {}", publishDTO);
        if (publishDTO.getId() != null) {
            throw new BadRequestAlertException("A new publish cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PublishDTO result = publishService.save(publishDTO);
        return ResponseEntity.created(new URI("/api/publishes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /publishes} : Updates an existing publish.
     *
     * @param publishDTO the publishDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publishDTO,
     * or with status {@code 400 (Bad Request)} if the publishDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the publishDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/publishes")
    public ResponseEntity<PublishDTO> updatePublish(@RequestBody PublishDTO publishDTO) throws URISyntaxException {
        log.debug("REST request to update Publish : {}", publishDTO);
        if (publishDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PublishDTO result = publishService.save(publishDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, publishDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /publishes} : get all the publishes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of publishes in body.
     */
    @GetMapping("/publishes")
    public ResponseEntity<List<PublishDTO>> getAllPublishes(PublishCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Publishes by criteria: {}", criteria);
        Page<PublishDTO> page = publishQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /publishes/count} : count all the publishes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/publishes/count")
    public ResponseEntity<Long> countPublishes(PublishCriteria criteria) {
        log.debug("REST request to count Publishes by criteria: {}", criteria);
        return ResponseEntity.ok().body(publishQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /publishes/:id} : get the "id" publish.
     *
     * @param id the id of the publishDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the publishDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/publishes/{id}")
    public ResponseEntity<PublishDTO> getPublish(@PathVariable Long id) {
        log.debug("REST request to get Publish : {}", id);
        Optional<PublishDTO> publishDTO = publishService.findOne(id);
        return ResponseUtil.wrapOrNotFound(publishDTO);
    }

    /**
     * {@code DELETE  /publishes/:id} : delete the "id" publish.
     *
     * @param id the id of the publishDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/publishes/{id}")
    public ResponseEntity<Void> deletePublish(@PathVariable Long id) {
        log.debug("REST request to delete Publish : {}", id);
        publishService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

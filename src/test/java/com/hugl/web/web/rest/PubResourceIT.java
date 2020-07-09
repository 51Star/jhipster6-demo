package com.hugl.web.web.rest;

import com.hugl.web.RedisTestContainerExtension;
import com.hugl.web.PublishApp;
import com.hugl.web.domain.N;
import com.hugl.web.service.mapper.PubMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PubResource} REST controller.
 */
@SpringBootTest(classes = PublishApp.class)
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class PubResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PubRepository pubRepository;

    @Autowired
    private PubMapper pubMapper;

    @Autowired
    private PubService pubService;

    @Autowired
    private PubQueryService pubQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPubMockMvc;

    private Pub pub;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pub createEntity(EntityManager em) {
        Pub pub = new Pub()
            .name(DEFAULT_NAME);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceIT.createEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        pub.setMember(member);
        return pub;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pub createUpdatedEntity(EntityManager em) {
        Pub pub = new Pub()
            .name(UPDATED_NAME);
        // Add required entity
        Member member;
        if (TestUtil.findAll(em, Member.class).isEmpty()) {
            member = MemberResourceIT.createUpdatedEntity(em);
            em.persist(member);
            em.flush();
        } else {
            member = TestUtil.findAll(em, Member.class).get(0);
        }
        pub.setMember(member);
        return pub;
    }

    @BeforeEach
    public void initTest() {
        pub = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllPubs() throws Exception {
        // Initialize the database
        pubRepository.saveAndFlush(pub);

        // Get all the pubList
        restPubMockMvc.perform(get("/api/pubs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pub.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getPub() throws Exception {
        // Initialize the database
        pubRepository.saveAndFlush(pub);

        // Get the pub
        restPubMockMvc.perform(get("/api/pubs/{id}", pub.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pub.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }


    @Test
    @Transactional
    public void getPubsByIdFiltering() throws Exception {
        // Initialize the database
        pubRepository.saveAndFlush(pub);

        Long id = pub.getId();

        defaultPubShouldBeFound("id.equals=" + id);
        defaultPubShouldNotBeFound("id.notEquals=" + id);

        defaultPubShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPubShouldNotBeFound("id.greaterThan=" + id);

        defaultPubShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPubShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPubsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        pubRepository.saveAndFlush(pub);

        // Get all the pubList where name equals to DEFAULT_NAME
        defaultPubShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the pubList where name equals to UPDATED_NAME
        defaultPubShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPubsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pubRepository.saveAndFlush(pub);

        // Get all the pubList where name not equals to DEFAULT_NAME
        defaultPubShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the pubList where name not equals to UPDATED_NAME
        defaultPubShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPubsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        pubRepository.saveAndFlush(pub);

        // Get all the pubList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPubShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the pubList where name equals to UPDATED_NAME
        defaultPubShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPubsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        pubRepository.saveAndFlush(pub);

        // Get all the pubList where name is not null
        defaultPubShouldBeFound("name.specified=true");

        // Get all the pubList where name is null
        defaultPubShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPubsByNameContainsSomething() throws Exception {
        // Initialize the database
        pubRepository.saveAndFlush(pub);

        // Get all the pubList where name contains DEFAULT_NAME
        defaultPubShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the pubList where name contains UPDATED_NAME
        defaultPubShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPubsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        pubRepository.saveAndFlush(pub);

        // Get all the pubList where name does not contain DEFAULT_NAME
        defaultPubShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the pubList where name does not contain UPDATED_NAME
        defaultPubShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPubsByMemberIsEqualToSomething() throws Exception {
        // Get already existing entity
        Member member = pub.getMember();
        pubRepository.saveAndFlush(pub);
        Long memberId = member.getId();

        // Get all the pubList where member equals to memberId
        defaultPubShouldBeFound("memberId.equals=" + memberId);

        // Get all the pubList where member equals to memberId + 1
        defaultPubShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }


    @Test
    @Transactional
    public void getAllPubsByNIsEqualToSomething() throws Exception {
        // Initialize the database
        pubRepository.saveAndFlush(pub);
        N n = NResourceIT.createEntity(em);
        em.persist(n);
        em.flush();
        pub.addN(n);
        pubRepository.saveAndFlush(pub);
        Long nId = n.getId();

        // Get all the pubList where n equals to nId
        defaultPubShouldBeFound("nId.equals=" + nId);

        // Get all the pubList where n equals to nId + 1
        defaultPubShouldNotBeFound("nId.equals=" + (nId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPubShouldBeFound(String filter) throws Exception {
        restPubMockMvc.perform(get("/api/pubs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pub.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restPubMockMvc.perform(get("/api/pubs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPubShouldNotBeFound(String filter) throws Exception {
        restPubMockMvc.perform(get("/api/pubs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPubMockMvc.perform(get("/api/pubs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPub() throws Exception {
        // Get the pub
        restPubMockMvc.perform(get("/api/pubs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}

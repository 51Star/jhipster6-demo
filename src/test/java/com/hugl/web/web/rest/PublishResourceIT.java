package com.hugl.web.web.rest;

import com.hugl.web.RedisTestContainerExtension;
import com.hugl.web.PublishApp;
import com.hugl.web.domain.Publish;
import com.hugl.web.domain.Member;
import com.hugl.web.repository.PublishRepository;
import com.hugl.web.service.PublishService;
import com.hugl.web.service.dto.PublishDTO;
import com.hugl.web.service.mapper.PublishMapper;
import com.hugl.web.service.dto.PublishCriteria;
import com.hugl.web.service.PublishQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PublishResource} REST controller.
 */
@SpringBootTest(classes = PublishApp.class)
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class PublishResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PIC_URL_1 = "AAAAAAAAAA";
    private static final String UPDATED_PIC_URL_1 = "BBBBBBBBBB";

    private static final String DEFAULT_PIC_URL_2 = "AAAAAAAAAA";
    private static final String UPDATED_PIC_URL_2 = "BBBBBBBBBB";

    private static final String DEFAULT_PIC_URL_3 = "AAAAAAAAAA";
    private static final String UPDATED_PIC_URL_3 = "BBBBBBBBBB";

    private static final String DEFAULT_PIC_URL_4 = "AAAAAAAAAA";
    private static final String UPDATED_PIC_URL_4 = "BBBBBBBBBB";

    @Autowired
    private PublishRepository publishRepository;

    @Autowired
    private PublishMapper publishMapper;

    @Autowired
    private PublishService publishService;

    @Autowired
    private PublishQueryService publishQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPublishMockMvc;

    private Publish publish;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Publish createEntity(EntityManager em) {
        Publish publish = new Publish()
            .name(DEFAULT_NAME)
            .picUrl1(DEFAULT_PIC_URL_1)
            .picUrl2(DEFAULT_PIC_URL_2)
            .picUrl3(DEFAULT_PIC_URL_3)
            .picUrl4(DEFAULT_PIC_URL_4);
        return publish;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Publish createUpdatedEntity(EntityManager em) {
        Publish publish = new Publish()
            .name(UPDATED_NAME)
            .picUrl1(UPDATED_PIC_URL_1)
            .picUrl2(UPDATED_PIC_URL_2)
            .picUrl3(UPDATED_PIC_URL_3)
            .picUrl4(UPDATED_PIC_URL_4);
        return publish;
    }

    @BeforeEach
    public void initTest() {
        publish = createEntity(em);
    }

    @Test
    @Transactional
    public void createPublish() throws Exception {
        int databaseSizeBeforeCreate = publishRepository.findAll().size();
        // Create the Publish
        PublishDTO publishDTO = publishMapper.toDto(publish);
        restPublishMockMvc.perform(post("/api/publishes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(publishDTO)))
            .andExpect(status().isCreated());

        // Validate the Publish in the database
        List<Publish> publishList = publishRepository.findAll();
        assertThat(publishList).hasSize(databaseSizeBeforeCreate + 1);
        Publish testPublish = publishList.get(publishList.size() - 1);
        assertThat(testPublish.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPublish.getPicUrl1()).isEqualTo(DEFAULT_PIC_URL_1);
        assertThat(testPublish.getPicUrl2()).isEqualTo(DEFAULT_PIC_URL_2);
        assertThat(testPublish.getPicUrl3()).isEqualTo(DEFAULT_PIC_URL_3);
        assertThat(testPublish.getPicUrl4()).isEqualTo(DEFAULT_PIC_URL_4);
    }

    @Test
    @Transactional
    public void createPublishWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = publishRepository.findAll().size();

        // Create the Publish with an existing ID
        publish.setId(1L);
        PublishDTO publishDTO = publishMapper.toDto(publish);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublishMockMvc.perform(post("/api/publishes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(publishDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Publish in the database
        List<Publish> publishList = publishRepository.findAll();
        assertThat(publishList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPublishes() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList
        restPublishMockMvc.perform(get("/api/publishes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publish.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].picUrl1").value(hasItem(DEFAULT_PIC_URL_1)))
            .andExpect(jsonPath("$.[*].picUrl2").value(hasItem(DEFAULT_PIC_URL_2)))
            .andExpect(jsonPath("$.[*].picUrl3").value(hasItem(DEFAULT_PIC_URL_3)))
            .andExpect(jsonPath("$.[*].picUrl4").value(hasItem(DEFAULT_PIC_URL_4)));
    }
    
    @Test
    @Transactional
    public void getPublish() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get the publish
        restPublishMockMvc.perform(get("/api/publishes/{id}", publish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(publish.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.picUrl1").value(DEFAULT_PIC_URL_1))
            .andExpect(jsonPath("$.picUrl2").value(DEFAULT_PIC_URL_2))
            .andExpect(jsonPath("$.picUrl3").value(DEFAULT_PIC_URL_3))
            .andExpect(jsonPath("$.picUrl4").value(DEFAULT_PIC_URL_4));
    }


    @Test
    @Transactional
    public void getPublishesByIdFiltering() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        Long id = publish.getId();

        defaultPublishShouldBeFound("id.equals=" + id);
        defaultPublishShouldNotBeFound("id.notEquals=" + id);

        defaultPublishShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPublishShouldNotBeFound("id.greaterThan=" + id);

        defaultPublishShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPublishShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPublishesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where name equals to DEFAULT_NAME
        defaultPublishShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the publishList where name equals to UPDATED_NAME
        defaultPublishShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPublishesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where name not equals to DEFAULT_NAME
        defaultPublishShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the publishList where name not equals to UPDATED_NAME
        defaultPublishShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPublishesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPublishShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the publishList where name equals to UPDATED_NAME
        defaultPublishShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPublishesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where name is not null
        defaultPublishShouldBeFound("name.specified=true");

        // Get all the publishList where name is null
        defaultPublishShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllPublishesByNameContainsSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where name contains DEFAULT_NAME
        defaultPublishShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the publishList where name contains UPDATED_NAME
        defaultPublishShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPublishesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where name does not contain DEFAULT_NAME
        defaultPublishShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the publishList where name does not contain UPDATED_NAME
        defaultPublishShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllPublishesByPicUrl1IsEqualToSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl1 equals to DEFAULT_PIC_URL_1
        defaultPublishShouldBeFound("picUrl1.equals=" + DEFAULT_PIC_URL_1);

        // Get all the publishList where picUrl1 equals to UPDATED_PIC_URL_1
        defaultPublishShouldNotBeFound("picUrl1.equals=" + UPDATED_PIC_URL_1);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl1 not equals to DEFAULT_PIC_URL_1
        defaultPublishShouldNotBeFound("picUrl1.notEquals=" + DEFAULT_PIC_URL_1);

        // Get all the publishList where picUrl1 not equals to UPDATED_PIC_URL_1
        defaultPublishShouldBeFound("picUrl1.notEquals=" + UPDATED_PIC_URL_1);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl1IsInShouldWork() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl1 in DEFAULT_PIC_URL_1 or UPDATED_PIC_URL_1
        defaultPublishShouldBeFound("picUrl1.in=" + DEFAULT_PIC_URL_1 + "," + UPDATED_PIC_URL_1);

        // Get all the publishList where picUrl1 equals to UPDATED_PIC_URL_1
        defaultPublishShouldNotBeFound("picUrl1.in=" + UPDATED_PIC_URL_1);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl1IsNullOrNotNull() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl1 is not null
        defaultPublishShouldBeFound("picUrl1.specified=true");

        // Get all the publishList where picUrl1 is null
        defaultPublishShouldNotBeFound("picUrl1.specified=false");
    }
                @Test
    @Transactional
    public void getAllPublishesByPicUrl1ContainsSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl1 contains DEFAULT_PIC_URL_1
        defaultPublishShouldBeFound("picUrl1.contains=" + DEFAULT_PIC_URL_1);

        // Get all the publishList where picUrl1 contains UPDATED_PIC_URL_1
        defaultPublishShouldNotBeFound("picUrl1.contains=" + UPDATED_PIC_URL_1);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl1NotContainsSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl1 does not contain DEFAULT_PIC_URL_1
        defaultPublishShouldNotBeFound("picUrl1.doesNotContain=" + DEFAULT_PIC_URL_1);

        // Get all the publishList where picUrl1 does not contain UPDATED_PIC_URL_1
        defaultPublishShouldBeFound("picUrl1.doesNotContain=" + UPDATED_PIC_URL_1);
    }


    @Test
    @Transactional
    public void getAllPublishesByPicUrl2IsEqualToSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl2 equals to DEFAULT_PIC_URL_2
        defaultPublishShouldBeFound("picUrl2.equals=" + DEFAULT_PIC_URL_2);

        // Get all the publishList where picUrl2 equals to UPDATED_PIC_URL_2
        defaultPublishShouldNotBeFound("picUrl2.equals=" + UPDATED_PIC_URL_2);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl2 not equals to DEFAULT_PIC_URL_2
        defaultPublishShouldNotBeFound("picUrl2.notEquals=" + DEFAULT_PIC_URL_2);

        // Get all the publishList where picUrl2 not equals to UPDATED_PIC_URL_2
        defaultPublishShouldBeFound("picUrl2.notEquals=" + UPDATED_PIC_URL_2);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl2IsInShouldWork() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl2 in DEFAULT_PIC_URL_2 or UPDATED_PIC_URL_2
        defaultPublishShouldBeFound("picUrl2.in=" + DEFAULT_PIC_URL_2 + "," + UPDATED_PIC_URL_2);

        // Get all the publishList where picUrl2 equals to UPDATED_PIC_URL_2
        defaultPublishShouldNotBeFound("picUrl2.in=" + UPDATED_PIC_URL_2);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl2IsNullOrNotNull() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl2 is not null
        defaultPublishShouldBeFound("picUrl2.specified=true");

        // Get all the publishList where picUrl2 is null
        defaultPublishShouldNotBeFound("picUrl2.specified=false");
    }
                @Test
    @Transactional
    public void getAllPublishesByPicUrl2ContainsSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl2 contains DEFAULT_PIC_URL_2
        defaultPublishShouldBeFound("picUrl2.contains=" + DEFAULT_PIC_URL_2);

        // Get all the publishList where picUrl2 contains UPDATED_PIC_URL_2
        defaultPublishShouldNotBeFound("picUrl2.contains=" + UPDATED_PIC_URL_2);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl2NotContainsSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl2 does not contain DEFAULT_PIC_URL_2
        defaultPublishShouldNotBeFound("picUrl2.doesNotContain=" + DEFAULT_PIC_URL_2);

        // Get all the publishList where picUrl2 does not contain UPDATED_PIC_URL_2
        defaultPublishShouldBeFound("picUrl2.doesNotContain=" + UPDATED_PIC_URL_2);
    }


    @Test
    @Transactional
    public void getAllPublishesByPicUrl3IsEqualToSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl3 equals to DEFAULT_PIC_URL_3
        defaultPublishShouldBeFound("picUrl3.equals=" + DEFAULT_PIC_URL_3);

        // Get all the publishList where picUrl3 equals to UPDATED_PIC_URL_3
        defaultPublishShouldNotBeFound("picUrl3.equals=" + UPDATED_PIC_URL_3);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl3IsNotEqualToSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl3 not equals to DEFAULT_PIC_URL_3
        defaultPublishShouldNotBeFound("picUrl3.notEquals=" + DEFAULT_PIC_URL_3);

        // Get all the publishList where picUrl3 not equals to UPDATED_PIC_URL_3
        defaultPublishShouldBeFound("picUrl3.notEquals=" + UPDATED_PIC_URL_3);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl3IsInShouldWork() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl3 in DEFAULT_PIC_URL_3 or UPDATED_PIC_URL_3
        defaultPublishShouldBeFound("picUrl3.in=" + DEFAULT_PIC_URL_3 + "," + UPDATED_PIC_URL_3);

        // Get all the publishList where picUrl3 equals to UPDATED_PIC_URL_3
        defaultPublishShouldNotBeFound("picUrl3.in=" + UPDATED_PIC_URL_3);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl3IsNullOrNotNull() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl3 is not null
        defaultPublishShouldBeFound("picUrl3.specified=true");

        // Get all the publishList where picUrl3 is null
        defaultPublishShouldNotBeFound("picUrl3.specified=false");
    }
                @Test
    @Transactional
    public void getAllPublishesByPicUrl3ContainsSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl3 contains DEFAULT_PIC_URL_3
        defaultPublishShouldBeFound("picUrl3.contains=" + DEFAULT_PIC_URL_3);

        // Get all the publishList where picUrl3 contains UPDATED_PIC_URL_3
        defaultPublishShouldNotBeFound("picUrl3.contains=" + UPDATED_PIC_URL_3);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl3NotContainsSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl3 does not contain DEFAULT_PIC_URL_3
        defaultPublishShouldNotBeFound("picUrl3.doesNotContain=" + DEFAULT_PIC_URL_3);

        // Get all the publishList where picUrl3 does not contain UPDATED_PIC_URL_3
        defaultPublishShouldBeFound("picUrl3.doesNotContain=" + UPDATED_PIC_URL_3);
    }


    @Test
    @Transactional
    public void getAllPublishesByPicUrl4IsEqualToSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl4 equals to DEFAULT_PIC_URL_4
        defaultPublishShouldBeFound("picUrl4.equals=" + DEFAULT_PIC_URL_4);

        // Get all the publishList where picUrl4 equals to UPDATED_PIC_URL_4
        defaultPublishShouldNotBeFound("picUrl4.equals=" + UPDATED_PIC_URL_4);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl4IsNotEqualToSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl4 not equals to DEFAULT_PIC_URL_4
        defaultPublishShouldNotBeFound("picUrl4.notEquals=" + DEFAULT_PIC_URL_4);

        // Get all the publishList where picUrl4 not equals to UPDATED_PIC_URL_4
        defaultPublishShouldBeFound("picUrl4.notEquals=" + UPDATED_PIC_URL_4);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl4IsInShouldWork() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl4 in DEFAULT_PIC_URL_4 or UPDATED_PIC_URL_4
        defaultPublishShouldBeFound("picUrl4.in=" + DEFAULT_PIC_URL_4 + "," + UPDATED_PIC_URL_4);

        // Get all the publishList where picUrl4 equals to UPDATED_PIC_URL_4
        defaultPublishShouldNotBeFound("picUrl4.in=" + UPDATED_PIC_URL_4);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl4IsNullOrNotNull() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl4 is not null
        defaultPublishShouldBeFound("picUrl4.specified=true");

        // Get all the publishList where picUrl4 is null
        defaultPublishShouldNotBeFound("picUrl4.specified=false");
    }
                @Test
    @Transactional
    public void getAllPublishesByPicUrl4ContainsSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl4 contains DEFAULT_PIC_URL_4
        defaultPublishShouldBeFound("picUrl4.contains=" + DEFAULT_PIC_URL_4);

        // Get all the publishList where picUrl4 contains UPDATED_PIC_URL_4
        defaultPublishShouldNotBeFound("picUrl4.contains=" + UPDATED_PIC_URL_4);
    }

    @Test
    @Transactional
    public void getAllPublishesByPicUrl4NotContainsSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        // Get all the publishList where picUrl4 does not contain DEFAULT_PIC_URL_4
        defaultPublishShouldNotBeFound("picUrl4.doesNotContain=" + DEFAULT_PIC_URL_4);

        // Get all the publishList where picUrl4 does not contain UPDATED_PIC_URL_4
        defaultPublishShouldBeFound("picUrl4.doesNotContain=" + UPDATED_PIC_URL_4);
    }


    @Test
    @Transactional
    public void getAllPublishesByMemberIsEqualToSomething() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);
        Member member = MemberResourceIT.createEntity(em);
        em.persist(member);
        em.flush();
        publish.setMember(member);
        publishRepository.saveAndFlush(publish);
        Long memberId = member.getId();

        // Get all the publishList where member equals to memberId
        defaultPublishShouldBeFound("memberId.equals=" + memberId);

        // Get all the publishList where member equals to memberId + 1
        defaultPublishShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPublishShouldBeFound(String filter) throws Exception {
        restPublishMockMvc.perform(get("/api/publishes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publish.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].picUrl1").value(hasItem(DEFAULT_PIC_URL_1)))
            .andExpect(jsonPath("$.[*].picUrl2").value(hasItem(DEFAULT_PIC_URL_2)))
            .andExpect(jsonPath("$.[*].picUrl3").value(hasItem(DEFAULT_PIC_URL_3)))
            .andExpect(jsonPath("$.[*].picUrl4").value(hasItem(DEFAULT_PIC_URL_4)));

        // Check, that the count call also returns 1
        restPublishMockMvc.perform(get("/api/publishes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPublishShouldNotBeFound(String filter) throws Exception {
        restPublishMockMvc.perform(get("/api/publishes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPublishMockMvc.perform(get("/api/publishes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingPublish() throws Exception {
        // Get the publish
        restPublishMockMvc.perform(get("/api/publishes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePublish() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        int databaseSizeBeforeUpdate = publishRepository.findAll().size();

        // Update the publish
        Publish updatedPublish = publishRepository.findById(publish.getId()).get();
        // Disconnect from session so that the updates on updatedPublish are not directly saved in db
        em.detach(updatedPublish);
        updatedPublish
            .name(UPDATED_NAME)
            .picUrl1(UPDATED_PIC_URL_1)
            .picUrl2(UPDATED_PIC_URL_2)
            .picUrl3(UPDATED_PIC_URL_3)
            .picUrl4(UPDATED_PIC_URL_4);
        PublishDTO publishDTO = publishMapper.toDto(updatedPublish);

        restPublishMockMvc.perform(put("/api/publishes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(publishDTO)))
            .andExpect(status().isOk());

        // Validate the Publish in the database
        List<Publish> publishList = publishRepository.findAll();
        assertThat(publishList).hasSize(databaseSizeBeforeUpdate);
        Publish testPublish = publishList.get(publishList.size() - 1);
        assertThat(testPublish.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPublish.getPicUrl1()).isEqualTo(UPDATED_PIC_URL_1);
        assertThat(testPublish.getPicUrl2()).isEqualTo(UPDATED_PIC_URL_2);
        assertThat(testPublish.getPicUrl3()).isEqualTo(UPDATED_PIC_URL_3);
        assertThat(testPublish.getPicUrl4()).isEqualTo(UPDATED_PIC_URL_4);
    }

    @Test
    @Transactional
    public void updateNonExistingPublish() throws Exception {
        int databaseSizeBeforeUpdate = publishRepository.findAll().size();

        // Create the Publish
        PublishDTO publishDTO = publishMapper.toDto(publish);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublishMockMvc.perform(put("/api/publishes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(publishDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Publish in the database
        List<Publish> publishList = publishRepository.findAll();
        assertThat(publishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePublish() throws Exception {
        // Initialize the database
        publishRepository.saveAndFlush(publish);

        int databaseSizeBeforeDelete = publishRepository.findAll().size();

        // Delete the publish
        restPublishMockMvc.perform(delete("/api/publishes/{id}", publish.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Publish> publishList = publishRepository.findAll();
        assertThat(publishList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

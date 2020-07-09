package com.hugl.web.web.rest;

import com.hugl.web.RedisTestContainerExtension;
import com.hugl.web.PublishApp;
import com.hugl.web.domain.Member;
import com.hugl.web.repository.MemberRepository;
import com.hugl.web.service.MemberService;
import com.hugl.web.service.dto.MemberDTO;
import com.hugl.web.service.mapper.MemberMapper;
import com.hugl.web.service.dto.MemberCriteria;
import com.hugl.web.service.MemberQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MemberResource} REST controller.
 */
@SpringBootTest(classes = PublishApp.class)
@ExtendWith({ RedisTestContainerExtension.class, MockitoExtension.class })
@AutoConfigureMockMvc
@WithMockUser
public class MemberResourceIT {

    private static final String DEFAULT_NICKNAME = "AAAAAAAAAA";
    private static final String UPDATED_NICKNAME = "BBBBBBBBBB";

    private static final String DEFAULT_MPIC = "AAAAAAAAAA";
    private static final String UPDATED_MPIC = "BBBBBBBBBB";

    private static final String DEFAULT_OPENID = "AAAAAAAAAA";
    private static final String UPDATED_OPENID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberQueryService memberQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberMockMvc;

    private Member member;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createEntity(EntityManager em) {
        Member member = new Member()
            .nickname(DEFAULT_NICKNAME)
            .mpic(DEFAULT_MPIC)
            .openid(DEFAULT_OPENID)
            .createdTime(DEFAULT_CREATED_TIME)
            .updatedTime(DEFAULT_UPDATED_TIME);
        return member;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createUpdatedEntity(EntityManager em) {
        Member member = new Member()
            .nickname(UPDATED_NICKNAME)
            .mpic(UPDATED_MPIC)
            .openid(UPDATED_OPENID)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME);
        return member;
    }

    @BeforeEach
    public void initTest() {
        member = createEntity(em);
    }

    @Test
    @Transactional
    public void createMember() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();
        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);
        restMemberMockMvc.perform(post("/api/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isCreated());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate + 1);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getNickname()).isEqualTo(DEFAULT_NICKNAME);
        assertThat(testMember.getMpic()).isEqualTo(DEFAULT_MPIC);
        assertThat(testMember.getOpenid()).isEqualTo(DEFAULT_OPENID);
        assertThat(testMember.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testMember.getUpdatedTime()).isEqualTo(DEFAULT_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void createMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();

        // Create the Member with an existing ID
        member.setId(1L);
        MemberDTO memberDTO = memberMapper.toDto(member);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberMockMvc.perform(post("/api/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMembers() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList
        restMemberMockMvc.perform(get("/api/members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME)))
            .andExpect(jsonPath("$.[*].mpic").value(hasItem(DEFAULT_MPIC)))
            .andExpect(jsonPath("$.[*].openid").value(hasItem(DEFAULT_OPENID)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(member.getId().intValue()))
            .andExpect(jsonPath("$.nickname").value(DEFAULT_NICKNAME))
            .andExpect(jsonPath("$.mpic").value(DEFAULT_MPIC))
            .andExpect(jsonPath("$.openid").value(DEFAULT_OPENID))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.updatedTime").value(DEFAULT_UPDATED_TIME.toString()));
    }


    @Test
    @Transactional
    public void getMembersByIdFiltering() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        Long id = member.getId();

        defaultMemberShouldBeFound("id.equals=" + id);
        defaultMemberShouldNotBeFound("id.notEquals=" + id);

        defaultMemberShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMemberShouldNotBeFound("id.greaterThan=" + id);

        defaultMemberShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMemberShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMembersByNicknameIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where nickname equals to DEFAULT_NICKNAME
        defaultMemberShouldBeFound("nickname.equals=" + DEFAULT_NICKNAME);

        // Get all the memberList where nickname equals to UPDATED_NICKNAME
        defaultMemberShouldNotBeFound("nickname.equals=" + UPDATED_NICKNAME);
    }

    @Test
    @Transactional
    public void getAllMembersByNicknameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where nickname not equals to DEFAULT_NICKNAME
        defaultMemberShouldNotBeFound("nickname.notEquals=" + DEFAULT_NICKNAME);

        // Get all the memberList where nickname not equals to UPDATED_NICKNAME
        defaultMemberShouldBeFound("nickname.notEquals=" + UPDATED_NICKNAME);
    }

    @Test
    @Transactional
    public void getAllMembersByNicknameIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where nickname in DEFAULT_NICKNAME or UPDATED_NICKNAME
        defaultMemberShouldBeFound("nickname.in=" + DEFAULT_NICKNAME + "," + UPDATED_NICKNAME);

        // Get all the memberList where nickname equals to UPDATED_NICKNAME
        defaultMemberShouldNotBeFound("nickname.in=" + UPDATED_NICKNAME);
    }

    @Test
    @Transactional
    public void getAllMembersByNicknameIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where nickname is not null
        defaultMemberShouldBeFound("nickname.specified=true");

        // Get all the memberList where nickname is null
        defaultMemberShouldNotBeFound("nickname.specified=false");
    }
                @Test
    @Transactional
    public void getAllMembersByNicknameContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where nickname contains DEFAULT_NICKNAME
        defaultMemberShouldBeFound("nickname.contains=" + DEFAULT_NICKNAME);

        // Get all the memberList where nickname contains UPDATED_NICKNAME
        defaultMemberShouldNotBeFound("nickname.contains=" + UPDATED_NICKNAME);
    }

    @Test
    @Transactional
    public void getAllMembersByNicknameNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where nickname does not contain DEFAULT_NICKNAME
        defaultMemberShouldNotBeFound("nickname.doesNotContain=" + DEFAULT_NICKNAME);

        // Get all the memberList where nickname does not contain UPDATED_NICKNAME
        defaultMemberShouldBeFound("nickname.doesNotContain=" + UPDATED_NICKNAME);
    }


    @Test
    @Transactional
    public void getAllMembersByMpicIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where mpic equals to DEFAULT_MPIC
        defaultMemberShouldBeFound("mpic.equals=" + DEFAULT_MPIC);

        // Get all the memberList where mpic equals to UPDATED_MPIC
        defaultMemberShouldNotBeFound("mpic.equals=" + UPDATED_MPIC);
    }

    @Test
    @Transactional
    public void getAllMembersByMpicIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where mpic not equals to DEFAULT_MPIC
        defaultMemberShouldNotBeFound("mpic.notEquals=" + DEFAULT_MPIC);

        // Get all the memberList where mpic not equals to UPDATED_MPIC
        defaultMemberShouldBeFound("mpic.notEquals=" + UPDATED_MPIC);
    }

    @Test
    @Transactional
    public void getAllMembersByMpicIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where mpic in DEFAULT_MPIC or UPDATED_MPIC
        defaultMemberShouldBeFound("mpic.in=" + DEFAULT_MPIC + "," + UPDATED_MPIC);

        // Get all the memberList where mpic equals to UPDATED_MPIC
        defaultMemberShouldNotBeFound("mpic.in=" + UPDATED_MPIC);
    }

    @Test
    @Transactional
    public void getAllMembersByMpicIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where mpic is not null
        defaultMemberShouldBeFound("mpic.specified=true");

        // Get all the memberList where mpic is null
        defaultMemberShouldNotBeFound("mpic.specified=false");
    }
                @Test
    @Transactional
    public void getAllMembersByMpicContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where mpic contains DEFAULT_MPIC
        defaultMemberShouldBeFound("mpic.contains=" + DEFAULT_MPIC);

        // Get all the memberList where mpic contains UPDATED_MPIC
        defaultMemberShouldNotBeFound("mpic.contains=" + UPDATED_MPIC);
    }

    @Test
    @Transactional
    public void getAllMembersByMpicNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where mpic does not contain DEFAULT_MPIC
        defaultMemberShouldNotBeFound("mpic.doesNotContain=" + DEFAULT_MPIC);

        // Get all the memberList where mpic does not contain UPDATED_MPIC
        defaultMemberShouldBeFound("mpic.doesNotContain=" + UPDATED_MPIC);
    }


    @Test
    @Transactional
    public void getAllMembersByOpenidIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where openid equals to DEFAULT_OPENID
        defaultMemberShouldBeFound("openid.equals=" + DEFAULT_OPENID);

        // Get all the memberList where openid equals to UPDATED_OPENID
        defaultMemberShouldNotBeFound("openid.equals=" + UPDATED_OPENID);
    }

    @Test
    @Transactional
    public void getAllMembersByOpenidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where openid not equals to DEFAULT_OPENID
        defaultMemberShouldNotBeFound("openid.notEquals=" + DEFAULT_OPENID);

        // Get all the memberList where openid not equals to UPDATED_OPENID
        defaultMemberShouldBeFound("openid.notEquals=" + UPDATED_OPENID);
    }

    @Test
    @Transactional
    public void getAllMembersByOpenidIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where openid in DEFAULT_OPENID or UPDATED_OPENID
        defaultMemberShouldBeFound("openid.in=" + DEFAULT_OPENID + "," + UPDATED_OPENID);

        // Get all the memberList where openid equals to UPDATED_OPENID
        defaultMemberShouldNotBeFound("openid.in=" + UPDATED_OPENID);
    }

    @Test
    @Transactional
    public void getAllMembersByOpenidIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where openid is not null
        defaultMemberShouldBeFound("openid.specified=true");

        // Get all the memberList where openid is null
        defaultMemberShouldNotBeFound("openid.specified=false");
    }
                @Test
    @Transactional
    public void getAllMembersByOpenidContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where openid contains DEFAULT_OPENID
        defaultMemberShouldBeFound("openid.contains=" + DEFAULT_OPENID);

        // Get all the memberList where openid contains UPDATED_OPENID
        defaultMemberShouldNotBeFound("openid.contains=" + UPDATED_OPENID);
    }

    @Test
    @Transactional
    public void getAllMembersByOpenidNotContainsSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where openid does not contain DEFAULT_OPENID
        defaultMemberShouldNotBeFound("openid.doesNotContain=" + DEFAULT_OPENID);

        // Get all the memberList where openid does not contain UPDATED_OPENID
        defaultMemberShouldBeFound("openid.doesNotContain=" + UPDATED_OPENID);
    }


    @Test
    @Transactional
    public void getAllMembersByCreatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where createdTime equals to DEFAULT_CREATED_TIME
        defaultMemberShouldBeFound("createdTime.equals=" + DEFAULT_CREATED_TIME);

        // Get all the memberList where createdTime equals to UPDATED_CREATED_TIME
        defaultMemberShouldNotBeFound("createdTime.equals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllMembersByCreatedTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where createdTime not equals to DEFAULT_CREATED_TIME
        defaultMemberShouldNotBeFound("createdTime.notEquals=" + DEFAULT_CREATED_TIME);

        // Get all the memberList where createdTime not equals to UPDATED_CREATED_TIME
        defaultMemberShouldBeFound("createdTime.notEquals=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllMembersByCreatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where createdTime in DEFAULT_CREATED_TIME or UPDATED_CREATED_TIME
        defaultMemberShouldBeFound("createdTime.in=" + DEFAULT_CREATED_TIME + "," + UPDATED_CREATED_TIME);

        // Get all the memberList where createdTime equals to UPDATED_CREATED_TIME
        defaultMemberShouldNotBeFound("createdTime.in=" + UPDATED_CREATED_TIME);
    }

    @Test
    @Transactional
    public void getAllMembersByCreatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where createdTime is not null
        defaultMemberShouldBeFound("createdTime.specified=true");

        // Get all the memberList where createdTime is null
        defaultMemberShouldNotBeFound("createdTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllMembersByUpdatedTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where updatedTime equals to DEFAULT_UPDATED_TIME
        defaultMemberShouldBeFound("updatedTime.equals=" + DEFAULT_UPDATED_TIME);

        // Get all the memberList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultMemberShouldNotBeFound("updatedTime.equals=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllMembersByUpdatedTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where updatedTime not equals to DEFAULT_UPDATED_TIME
        defaultMemberShouldNotBeFound("updatedTime.notEquals=" + DEFAULT_UPDATED_TIME);

        // Get all the memberList where updatedTime not equals to UPDATED_UPDATED_TIME
        defaultMemberShouldBeFound("updatedTime.notEquals=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllMembersByUpdatedTimeIsInShouldWork() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where updatedTime in DEFAULT_UPDATED_TIME or UPDATED_UPDATED_TIME
        defaultMemberShouldBeFound("updatedTime.in=" + DEFAULT_UPDATED_TIME + "," + UPDATED_UPDATED_TIME);

        // Get all the memberList where updatedTime equals to UPDATED_UPDATED_TIME
        defaultMemberShouldNotBeFound("updatedTime.in=" + UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllMembersByUpdatedTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList where updatedTime is not null
        defaultMemberShouldBeFound("updatedTime.specified=true");

        // Get all the memberList where updatedTime is null
        defaultMemberShouldNotBeFound("updatedTime.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMemberShouldBeFound(String filter) throws Exception {
        restMemberMockMvc.perform(get("/api/members?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].nickname").value(hasItem(DEFAULT_NICKNAME)))
            .andExpect(jsonPath("$.[*].mpic").value(hasItem(DEFAULT_MPIC)))
            .andExpect(jsonPath("$.[*].openid").value(hasItem(DEFAULT_OPENID)))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].updatedTime").value(hasItem(DEFAULT_UPDATED_TIME.toString())));

        // Check, that the count call also returns 1
        restMemberMockMvc.perform(get("/api/members/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMemberShouldNotBeFound(String filter) throws Exception {
        restMemberMockMvc.perform(get("/api/members?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMemberMockMvc.perform(get("/api/members/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingMember() throws Exception {
        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member
        Member updatedMember = memberRepository.findById(member.getId()).get();
        // Disconnect from session so that the updates on updatedMember are not directly saved in db
        em.detach(updatedMember);
        updatedMember
            .nickname(UPDATED_NICKNAME)
            .mpic(UPDATED_MPIC)
            .openid(UPDATED_OPENID)
            .createdTime(UPDATED_CREATED_TIME)
            .updatedTime(UPDATED_UPDATED_TIME);
        MemberDTO memberDTO = memberMapper.toDto(updatedMember);

        restMemberMockMvc.perform(put("/api/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getNickname()).isEqualTo(UPDATED_NICKNAME);
        assertThat(testMember.getMpic()).isEqualTo(UPDATED_MPIC);
        assertThat(testMember.getOpenid()).isEqualTo(UPDATED_OPENID);
        assertThat(testMember.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testMember.getUpdatedTime()).isEqualTo(UPDATED_UPDATED_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc.perform(put("/api/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeDelete = memberRepository.findAll().size();

        // Delete the member
        restMemberMockMvc.perform(delete("/api/members/{id}", member.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

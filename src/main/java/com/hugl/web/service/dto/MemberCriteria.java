package com.hugl.web.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.hugl.web.domain.Member} entity. This class is used
 * in {@link com.hugl.web.web.rest.MemberResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /members?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MemberCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nickname;

    private StringFilter mpic;

    private StringFilter openid;

    private InstantFilter createdTime;

    private InstantFilter updatedTime;

    public MemberCriteria() {
    }

    public MemberCriteria(MemberCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nickname = other.nickname == null ? null : other.nickname.copy();
        this.mpic = other.mpic == null ? null : other.mpic.copy();
        this.openid = other.openid == null ? null : other.openid.copy();
        this.createdTime = other.createdTime == null ? null : other.createdTime.copy();
        this.updatedTime = other.updatedTime == null ? null : other.updatedTime.copy();
    }

    @Override
    public MemberCriteria copy() {
        return new MemberCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNickname() {
        return nickname;
    }

    public void setNickname(StringFilter nickname) {
        this.nickname = nickname;
    }

    public StringFilter getMpic() {
        return mpic;
    }

    public void setMpic(StringFilter mpic) {
        this.mpic = mpic;
    }

    public StringFilter getOpenid() {
        return openid;
    }

    public void setOpenid(StringFilter openid) {
        this.openid = openid;
    }

    public InstantFilter getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(InstantFilter createdTime) {
        this.createdTime = createdTime;
    }

    public InstantFilter getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(InstantFilter updatedTime) {
        this.updatedTime = updatedTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MemberCriteria that = (MemberCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nickname, that.nickname) &&
            Objects.equals(mpic, that.mpic) &&
            Objects.equals(openid, that.openid) &&
            Objects.equals(createdTime, that.createdTime) &&
            Objects.equals(updatedTime, that.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nickname,
        mpic,
        openid,
        createdTime,
        updatedTime
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nickname != null ? "nickname=" + nickname + ", " : "") +
                (mpic != null ? "mpic=" + mpic + ", " : "") +
                (openid != null ? "openid=" + openid + ", " : "") +
                (createdTime != null ? "createdTime=" + createdTime + ", " : "") +
                (updatedTime != null ? "updatedTime=" + updatedTime + ", " : "") +
            "}";
    }

}

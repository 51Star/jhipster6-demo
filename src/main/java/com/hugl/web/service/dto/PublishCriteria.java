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

/**
 * Criteria class for the {@link com.hugl.web.domain.Publish} entity. This class is used
 * in {@link com.hugl.web.web.rest.PublishResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /publishes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PublishCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter picUrl1;

    private StringFilter picUrl2;

    private StringFilter picUrl3;

    private StringFilter picUrl4;

    private LongFilter memberId;

    public PublishCriteria() {
    }

    public PublishCriteria(PublishCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.picUrl1 = other.picUrl1 == null ? null : other.picUrl1.copy();
        this.picUrl2 = other.picUrl2 == null ? null : other.picUrl2.copy();
        this.picUrl3 = other.picUrl3 == null ? null : other.picUrl3.copy();
        this.picUrl4 = other.picUrl4 == null ? null : other.picUrl4.copy();
        this.memberId = other.memberId == null ? null : other.memberId.copy();
    }

    @Override
    public PublishCriteria copy() {
        return new PublishCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getPicUrl1() {
        return picUrl1;
    }

    public void setPicUrl1(StringFilter picUrl1) {
        this.picUrl1 = picUrl1;
    }

    public StringFilter getPicUrl2() {
        return picUrl2;
    }

    public void setPicUrl2(StringFilter picUrl2) {
        this.picUrl2 = picUrl2;
    }

    public StringFilter getPicUrl3() {
        return picUrl3;
    }

    public void setPicUrl3(StringFilter picUrl3) {
        this.picUrl3 = picUrl3;
    }

    public StringFilter getPicUrl4() {
        return picUrl4;
    }

    public void setPicUrl4(StringFilter picUrl4) {
        this.picUrl4 = picUrl4;
    }

    public LongFilter getMemberId() {
        return memberId;
    }

    public void setMemberId(LongFilter memberId) {
        this.memberId = memberId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PublishCriteria that = (PublishCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(picUrl1, that.picUrl1) &&
            Objects.equals(picUrl2, that.picUrl2) &&
            Objects.equals(picUrl3, that.picUrl3) &&
            Objects.equals(picUrl4, that.picUrl4) &&
            Objects.equals(memberId, that.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        picUrl1,
        picUrl2,
        picUrl3,
        picUrl4,
        memberId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PublishCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (picUrl1 != null ? "picUrl1=" + picUrl1 + ", " : "") +
                (picUrl2 != null ? "picUrl2=" + picUrl2 + ", " : "") +
                (picUrl3 != null ? "picUrl3=" + picUrl3 + ", " : "") +
                (picUrl4 != null ? "picUrl4=" + picUrl4 + ", " : "") +
                (memberId != null ? "memberId=" + memberId + ", " : "") +
            "}";
    }

}

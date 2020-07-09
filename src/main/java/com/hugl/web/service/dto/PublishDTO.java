package com.hugl.web.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.hugl.web.domain.Publish} entity.
 */
public class PublishDTO implements Serializable {
    
    private Long id;

    private String name;

    private String picUrl1;

    private String picUrl2;

    private String picUrl3;

    private String picUrl4;


    private Long memberId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl1() {
        return picUrl1;
    }

    public void setPicUrl1(String picUrl1) {
        this.picUrl1 = picUrl1;
    }

    public String getPicUrl2() {
        return picUrl2;
    }

    public void setPicUrl2(String picUrl2) {
        this.picUrl2 = picUrl2;
    }

    public String getPicUrl3() {
        return picUrl3;
    }

    public void setPicUrl3(String picUrl3) {
        this.picUrl3 = picUrl3;
    }

    public String getPicUrl4() {
        return picUrl4;
    }

    public void setPicUrl4(String picUrl4) {
        this.picUrl4 = picUrl4;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PublishDTO)) {
            return false;
        }

        return id != null && id.equals(((PublishDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PublishDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", picUrl1='" + getPicUrl1() + "'" +
            ", picUrl2='" + getPicUrl2() + "'" +
            ", picUrl3='" + getPicUrl3() + "'" +
            ", picUrl4='" + getPicUrl4() + "'" +
            ", memberId=" + getMemberId() +
            "}";
    }
}

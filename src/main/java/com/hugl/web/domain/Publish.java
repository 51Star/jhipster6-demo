package com.hugl.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Publish.
 */
@Entity
@Table(name = "publish")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Publish implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "pic_url_1")
    private String picUrl1;

    @Column(name = "pic_url_2")
    private String picUrl2;

    @Column(name = "pic_url_3")
    private String picUrl3;

    @Column(name = "pic_url_4")
    private String picUrl4;

    @ManyToOne
    @JsonIgnoreProperties(value = "publishes", allowSetters = true)
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Publish name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl1() {
        return picUrl1;
    }

    public Publish picUrl1(String picUrl1) {
        this.picUrl1 = picUrl1;
        return this;
    }

    public void setPicUrl1(String picUrl1) {
        this.picUrl1 = picUrl1;
    }

    public String getPicUrl2() {
        return picUrl2;
    }

    public Publish picUrl2(String picUrl2) {
        this.picUrl2 = picUrl2;
        return this;
    }

    public void setPicUrl2(String picUrl2) {
        this.picUrl2 = picUrl2;
    }

    public String getPicUrl3() {
        return picUrl3;
    }

    public Publish picUrl3(String picUrl3) {
        this.picUrl3 = picUrl3;
        return this;
    }

    public void setPicUrl3(String picUrl3) {
        this.picUrl3 = picUrl3;
    }

    public String getPicUrl4() {
        return picUrl4;
    }

    public Publish picUrl4(String picUrl4) {
        this.picUrl4 = picUrl4;
        return this;
    }

    public void setPicUrl4(String picUrl4) {
        this.picUrl4 = picUrl4;
    }

    public Member getMember() {
        return member;
    }

    public Publish member(Member member) {
        this.member = member;
        return this;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Publish)) {
            return false;
        }
        return id != null && id.equals(((Publish) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Publish{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", picUrl1='" + getPicUrl1() + "'" +
            ", picUrl2='" + getPicUrl2() + "'" +
            ", picUrl3='" + getPicUrl3() + "'" +
            ", picUrl4='" + getPicUrl4() + "'" +
            "}";
    }
}

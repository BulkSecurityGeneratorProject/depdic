package com.skcc.lucy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.skcc.lucy.domain.enumeration.ColTyp;

/**
 * A DictVMeta.
 */
@Entity
@Table(name = "dict_v_meta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "dictvmeta")
public class DictVMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "col_typ")
    private ColTyp colTyp;

    @Column(name = "col_kr")
    private String colKr;

    @Column(name = "col_en")
    private String colEn;

    @Column(name = "json_desc")
    private String jsonDesc;

    @Column(name = "memo")
    private String memo;

    @Column(name = "dtm")
    private Instant dtm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ColTyp getColTyp() {
        return colTyp;
    }

    public DictVMeta colTyp(ColTyp colTyp) {
        this.colTyp = colTyp;
        return this;
    }

    public void setColTyp(ColTyp colTyp) {
        this.colTyp = colTyp;
    }

    public String getColKr() {
        return colKr;
    }

    public DictVMeta colKr(String colKr) {
        this.colKr = colKr;
        return this;
    }

    public void setColKr(String colKr) {
        this.colKr = colKr;
    }

    public String getColEn() {
        return colEn;
    }

    public DictVMeta colEn(String colEn) {
        this.colEn = colEn;
        return this;
    }

    public void setColEn(String colEn) {
        this.colEn = colEn;
    }

    public String getJsonDesc() {
        return jsonDesc;
    }

    public DictVMeta jsonDesc(String jsonDesc) {
        this.jsonDesc = jsonDesc;
        return this;
    }

    public void setJsonDesc(String jsonDesc) {
        this.jsonDesc = jsonDesc;
    }

    public String getMemo() {
        return memo;
    }

    public DictVMeta memo(String memo) {
        this.memo = memo;
        return this;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Instant getDtm() {
        return dtm;
    }

    public DictVMeta dtm(Instant dtm) {
        this.dtm = dtm;
        return this;
    }

    public void setDtm(Instant dtm) {
        this.dtm = dtm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DictVMeta dictVMeta = (DictVMeta) o;
        if (dictVMeta.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dictVMeta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DictVMeta{" +
            "id=" + getId() +
            ", colTyp='" + getColTyp() + "'" +
            ", colKr='" + getColKr() + "'" +
            ", colEn='" + getColEn() + "'" +
            ", jsonDesc='" + getJsonDesc() + "'" +
            ", memo='" + getMemo() + "'" +
            ", dtm='" + getDtm() + "'" +
            "}";
    }
}

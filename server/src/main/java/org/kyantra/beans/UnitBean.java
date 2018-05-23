package org.kyantra.beans;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.kyantra.dao.UnitDAO;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

/**
 * A unit is a business unit. A unit has more units as its children or things.
 * For example College X is a unit which will have Farm 1 and Farm 2 as its sub units.
 */
@Entity
@Table(name = "units")
@Transactional
public class UnitBean {

    @Id
    @Expose
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "unit_name")
    @Expose
    String unitName;

    @Column(name = "description")
    @Expose
    String description;

    @Column(name = "photo")
    @Expose
    String photo;

    @ManyToOne
    private UnitBean parent;

    // Remove the entire subtree
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<UnitBean> subunits;

    // Remove also the things while removing the sub-tree
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "parentUnit", orphanRemoval = true, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<ThingBean> things;

    public List<UnitBean> getSubunits() {
        return subunits;
    }

    public void setSubunits(List<UnitBean> subunits) {
        this.subunits = subunits;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public UnitBean getParent() {
        return parent;
    }

    public void setParent(UnitBean parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<ThingBean> getThings() {
        return things;
    }

    public void setThings(List<ThingBean> things) {
        this.things = things;
    }

    public static UnitBean valueOf(String value) {
        return UnitDAO.getInstance().get(Integer.parseInt(value));
    }

    /* Utilities for deleting and persisting child Thing */
    public ThingBean addThing(ThingBean thingBean) {
        this.things.add(thingBean);
        thingBean.setParentUnit(this);
        return thingBean;
    }

    public void removeThing(ThingBean thingBean) {
        this.things.remove(thingBean);
        thingBean.setParentUnit(null);
    }

    // required for checking if two units are same in checkAccess method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnitBean unitBean = (UnitBean) o;
        return Objects.equals(id, unitBean.id) &&
                Objects.equals(unitName, unitBean.unitName) &&
                Objects.equals(description, unitBean.description) &&
                Objects.equals(photo, unitBean.photo) &&
                Objects.equals(parent, unitBean.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unitName, description, photo, parent);
    }
}

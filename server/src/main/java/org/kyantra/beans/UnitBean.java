package org.kyantra.beans;

import com.google.gson.annotations.Expose;
import org.kyantra.dao.UnitDAO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * A unit is a business unit. A unit has more units as its children or things.
 * For example College X is a unit which will have Farm 1 and Farm 2 as its sub units.
 */
@Entity
@Table(name = "units")
public class UnitBean {

    @Id
    @Expose
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;

    @Column(name = "unit_name", unique = true)
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

    @OneToMany
    private List<UnitBean> subunits;

    @OneToMany
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

    public static UnitBean valueOf(String value){
        return UnitDAO.getInstance().get(Integer.parseInt(value));
    }
}

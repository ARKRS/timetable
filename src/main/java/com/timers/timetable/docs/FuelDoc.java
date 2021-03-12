package com.timers.timetable.docs;

import com.timers.timetable.deptsmanagement.Department;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "fuel_doc",uniqueConstraints = {@UniqueConstraint(columnNames = "Fuel_doc_id")})
public class FuelDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Fuel_doc_id",nullable = false)
    private Long docid;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id", referencedColumnName = "id",nullable = false, foreignKey = @ForeignKey(name = "FuelDoc_department_id_FK"))
    private Department department;
    private Date workmonth;
    private Boolean docUploaded;
    private String doc_UUID;

    @ElementCollection
    @CollectionTable(name = "fuel_doc_details",joinColumns = @JoinColumn(name = "Fuel_doc_id"))
    private List<FuelDocDetails> fuelDocDetails;


    public Department getDepartment() {        return department;    }

    public void setDepartment(Department department) {        this.department = department;    }

    public Date getWorkmonth() {        return workmonth;    }

    public void setWorkmonth(Date workmonth) {        this.workmonth = workmonth;    }

    public Boolean getDocUploaded() {        return docUploaded;    }

    public void setDocUploaded(Boolean docUploaded) {        this.docUploaded = docUploaded;    }

    public String getDoc_UUID() {        return doc_UUID;    }

    public void setDoc_UUID(String doc_UUID) {        this.doc_UUID = doc_UUID;    }

    public List<FuelDocDetails> getFuelDocDetails() {
        return fuelDocDetails;
    }

    public void setFuelDocDetails(List<FuelDocDetails> fuelDocDetails) {
        this.fuelDocDetails = fuelDocDetails;
    }
}

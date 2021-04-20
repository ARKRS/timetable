package com.timers.timetable.docs;

import com.timers.timetable.employees.Employee;

import javax.persistence.*;
import java.util.Date;

@Embeddable
public class FuelDocDetails {

 /*   @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fuelDoc_id_fk"))
    private FuelDoc fuelDoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Employee_id", nullable = false, //
            foreignKey = @ForeignKey(name = "fd_detail_emp_id"))
    private Employee employee;

    @Column(name = "DateOfMonth", nullable = false)
    private Date dateOfMonth;

    @Column(name = "Amount", nullable = false)
    private float amount;

    public Date getDateOfMonth() {
        return dateOfMonth;
    }

    public void setDateOfMonth(Date dateOfMonth) {
        this.dateOfMonth = dateOfMonth;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public FuelDocDetails() {
    }

    public FuelDoc getFuelDoc() {
        return fuelDoc;
    }

    public void setFuelDoc(FuelDoc fuelDoc) {
        this.fuelDoc = fuelDoc;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public FuelDocDetails(FuelDoc fuelDoc, Employee employee, Date dateOfMonth, Float amount) {
        this.fuelDoc = fuelDoc;
        this.employee = employee;
        this.dateOfMonth = dateOfMonth;
        this.amount = amount;
    }
}

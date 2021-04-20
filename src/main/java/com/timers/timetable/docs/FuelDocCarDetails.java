package com.timers.timetable.docs;

import com.timers.timetable.employees.Employee;

import javax.persistence.*;

@Embeddable
public class FuelDocCarDetails {

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



    @Column(name = "StartOdometerData", nullable = false)
    private int startOdometerData;

    @Column(name = "EndOdometerData", nullable = false)
    private int endOdometerData;

    @Column(name="tankreststart", nullable = false)
    private int tankreststart;

    @Column(name="tankrestend", nullable = false)
    private int tankrestend;


    @Column(name="fueltype")
    private FuelType fuelType;

    public FuelDocCarDetails(FuelDoc fuelDoc, Employee employee, int startOdometerData, int endOdometerData, int tankreststart, int tankrestend,FuelType fuelType) {
        this.fuelDoc = fuelDoc;
        this.employee = employee;
        this.startOdometerData = startOdometerData;
        this.endOdometerData = endOdometerData;
        this.tankreststart = tankreststart;
        this.tankrestend = tankrestend;
        this.fuelType = fuelType;
    }
    public FuelDocCarDetails() {

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

    public int getStartOdometerData() {
        return startOdometerData;
    }

    public void setStartOdometerData(int startOdometerData) {
        this.startOdometerData = startOdometerData;
    }

    public int getEndOdometerData() {
        return endOdometerData;
    }

    public void setEndOdometerData(int endOdometerData) {
        this.endOdometerData = endOdometerData;
    }

    public int getTankreststart() {       return tankreststart;    }

    public void setTankreststart(int tankreststart) {        this.tankreststart = tankreststart;    }

    public int getTankrestend() {        return tankrestend;    }

    public void setTankrestend(int tankrestend) {        this.tankrestend = tankrestend;    }

    public FuelType getFuelType() {return fuelType;    }

    public void setFuelType(FuelType fuelType) {        this.fuelType = fuelType;    }

}

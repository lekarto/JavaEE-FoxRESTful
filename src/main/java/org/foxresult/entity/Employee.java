package org.foxresult.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {
    private static final long serialVersionID = 8201337883209943936L;
    
    @Id
    @SequenceGenerator(name = "employee_id_generator", sequenceName = "employee_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "employee_id_generator")
    private Integer id;
    @Column(name = "first_name", length = 100)
    private String firstName;
    @Column(name = "last_name", length = 130)
    private String lastName;
    @Column(name = "sex")
    private boolean sex;
    @Column(name = "salary")
    private Float salary;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department; 

    public static boolean FEMALE = false;
    public static boolean MALE = true;

    public Employee() {}
    
    public Employee(String firstName, String lastName,
                    boolean sex, Float salary, Department department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.salary = salary;
        this.department = department;
    }

    public Employee(String firstName, String lastName,
                    boolean sex, Float salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.salary = salary;
    }
    
    public Employee(Integer id, String firstName, String lastName,
                    boolean sex, Float salary, Department department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.salary = salary;
        this.department = department;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean getSex() {
        return sex;
    }

    public boolean Male() {
        return (sex == MALE);
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}

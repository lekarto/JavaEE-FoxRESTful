package org.foxresult.entity;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Department class describes a specific branch of the company
 */
@Entity
@Table(name = "departments")
public class Department {
    @Id
    @SequenceGenerator(name = "department_id_generator", sequenceName = "department_id_seq")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "department_id_generator")
    private Integer id;
    @Column(name = "name", length = 400)
    private String name;
    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Employee> employees;
    
    public Department() {}

    /**
     *
     * @param id      Set Department ID
     * @param name    Set Department name
     */
    public Department(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Department(String name) {
        this.name = name;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @JsonIgnore
    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}

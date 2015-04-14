package org.foxresult.entity.filter;

import org.foxresult.entity.Employee;

/**
 * Employee filter class for DB requests. Can be used next filers:
 * - by gender
 * - by department
 * - by min, max salary and those combination
 * @see org.foxresult.entity.Department
 * @see org.foxresult.entity.Employee
 */
public class EmployeeFilter {
    public static String DEPARTMENT = "dep";
    public String department = null;
    public static String SEX = "sex";
    public Boolean sex        = null;
    public static String MIN_SALARY = "minSalary";
    public Float minSalary    = null;
    public static String MAX_SALARY = "maxSalary";
    public Float maxSalary    = null;
    
    public EmployeeFilter() {}
    
    public String toString() {
        return department+" - " + sex + " - " + minSalary+"|"+maxSalary;
    }
    
    public boolean parseDepartment(String dep) {
        department = dep;
        System.out.println("filter dep: "+dep);
        return true;
    }
    
    public boolean parseSex(String sex) {
        if (sex != null)
            if (sex.toLowerCase().equals("m")) {
                this.sex = Employee.MALE;
                return true;
            } else if (sex.toLowerCase().equals("f")) {
                this.sex = Employee.FEMALE;
                return true;
            } else {
                return false;
            }
        else return true;
    }
    
    public boolean parseMinSalary(String salary) {
        if (salary != null) {
            if (salary.matches("[\\d]{1,38}")) {
                minSalary = new Float(salary);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }        
    }

    public boolean parseMaxSalary(String salary) {
        if (salary != null) {
            if (salary.matches("[\\d]{1,38}")) {
                maxSalary = new Float(salary);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}

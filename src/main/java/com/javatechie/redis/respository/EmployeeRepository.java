package com.javatechie.redis.respository;

import com.javatechie.redis.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    @Query(value = "select e from Employee e where e.id = ?1")
    Employee findEById(int id);
}

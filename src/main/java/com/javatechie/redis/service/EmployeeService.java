package com.javatechie.redis.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javatechie.redis.dto.EmployeeDTO;
import com.javatechie.redis.entity.Employee;
import com.javatechie.redis.respository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class EmployeeService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

//    public List<Employee> getAllEmployee() {
//        String key = "employees";
//        List<Employee> employees = null;
//        employees = redisTemplate.opsForHash().values(key);
//        if( employees.size() == 0) {
//            employees = employeeRepository.findAll();
//            System.out.println("SELECT FROM DATABASE");
//            for(Employee employee: employees) {
//                redisTemplate.opsForHash().put(key,employee.getId(),employee);
//            }
//        } else {
//            System.out.println("SELECT FROM REDIS");
//            return employees;
//        }
//        return employees;
//    }

    public Employee getEmployeeById(int id) {
        Gson gson = new Gson();
        String key = "employees";
        String resultRedis = (String) redisTemplate.opsForValue().get(key);
        Employee employee = null;
        if(resultRedis != null) {
            employee = gson.fromJson(resultRedis, new TypeToken<Employee>() {
            }.getType());
            System.out.println("SELECT FROM REDIS");

        } else {
            employee = employeeRepository.findEById(id);
            if(employee != null) {
                redisTemplate.opsForValue().set(key,gson.toJson(employee));
                redisTemplate.expire(key, 30, TimeUnit.MINUTES);
            }
            System.out.println("SELECT FROM DATABASE");
        }
//        Employee employee = (Employee) redisTemplate.opsForHash().get(key,id);
//        if(employee != null) {
//            System.out.println("redis");
//            return employee;
//        } else {
//            employee = employeeRepository.findEById(id);
//            if(employee != null) {
//                redisTemplate.opsForHash().put(key,id,employee);
//            }
//            System.out.println("DB");
//        }
        return employee;
    }

    public void deleteEmployeeById(int id) {
        Employee employee = employeeRepository.findEById(id);
        employeeRepository.delete(employee);
    }

    public Employee saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        if(employeeDTO.getId() != 0) {
            employee = employeeRepository.findEById(employeeDTO.getId());
        }
        employee.setCode(employeeDTO.getCode());
        employee.setName(employeeDTO.getName());
        employee.setPhoneNumber(employeeDTO.getPhoneNumber());
        employeeRepository.save(employee);
        return employee;
    }
}

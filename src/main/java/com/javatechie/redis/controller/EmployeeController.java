package com.javatechie.redis.controller;

import com.google.gson.Gson;
import com.javatechie.redis.dto.EmployeeDTO;
import com.javatechie.redis.entity.Employee;
import com.javatechie.redis.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RedisTemplate redisTemplate;


    Gson gson = new Gson();

//    @GetMapping("/all")
//    public List<Employee> getAll() {
//        return employeeService.getAllEmployee();
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEById(@PathVariable(value = "id") int id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PutMapping()
    public ResponseEntity<Employee> updateE(@RequestBody EmployeeDTO dto) {
        Employee employee = employeeService.saveEmployee(dto);
        String key = "employees:" + dto.getId();
        redisTemplate.opsForValue().set(key, gson.toJson(employee));
        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEById(@PathVariable(value = "id") int id) {
        employeeService.deleteEmployeeById(id);
        String key = "employees:" + id;
        redisTemplate.delete(key);
//        redisTemplate.opsForHash().delete("employees", id);
        return ResponseEntity.ok("ok");
    }
}

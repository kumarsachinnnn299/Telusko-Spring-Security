package com.telusko.springSecurity.controllers;

import com.telusko.springSecurity.models.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
@RestController
public class StudentController {

    List<Student>lis=new ArrayList<>(List.of(
            new Student(1,"Sachin",85),
            new Student(2,"Shikhar",95)
    ));
    @GetMapping("/students")
    public List<Student> getStudents()
    {
        return lis;
    }

    @PostMapping("/student")
    public Student addStudent(@RequestBody Student student)
    {
        lis.add(student);
        return student;
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request)
    {
        return (CsrfToken) request.getAttribute("_csrf");
    }
}

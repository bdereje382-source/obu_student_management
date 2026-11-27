package com.obu.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SystemController {

    @Autowired
    private StudentRepository studentRepository;

    // Home Page
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // 1. Student Registration Page
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    @PostMapping("/register")
    public String registerStudent(@ModelAttribute Student student) {
        student.setRole("STUDENT");
        studentRepository.save(student);
        return "redirect:/register?success";
    }

    // 2. Admin Login Page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginAdmin(@RequestParam String password, Model model) {
        // Admin Password (e.g., admin123)
        if ("admin123".equals(password)) {
            return "redirect:/admin";
        }
        model.addAttribute("error", "Invalid Admin Password!");
        return "login";
    }

    // 3. Admin Dashboard (View all students)
    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "admin_dashboard";
    }

    // 4. Show Edit Student Form
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));
        model.addAttribute("student", student);
        return "edit_student";
    }

    // 5. Save Updated Student Information
    @PostMapping("/admin/update/{id}")
    public String updateStudent(@PathVariable("id") Long id, @ModelAttribute Student updatedStudent) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student Id:" + id));

        existing.setFullName(updatedStudent.getFullName());
        existing.setEmail(updatedStudent.getEmail());
        existing.setCampus(updatedStudent.getCampus());
        existing.setDepartment(updatedStudent.getDepartment());
        existing.setYear(updatedStudent.getYear());
        existing.setDormBlock(updatedStudent.getDormBlock());
        existing.setRoomNumber(updatedStudent.getRoomNumber());
        existing.setSex(updatedStudent.getSex());
        existing.setReligion(updatedStudent.getReligion());

        studentRepository.save(existing);
        return "redirect:/admin";
    }

    // 6. Delete Student
    @GetMapping("/admin/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentRepository.deleteById(id);
        return "redirect:/admin";
    }
}

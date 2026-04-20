package com.school.management.controller;

import com.school.management.entity.Assignment;
import com.school.management.entity.Submission;
import com.school.management.repository.AssignmentRepository;
import com.school.management.repository.SubmissionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class MainController {

    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;

    // Конструктор для впорскування залежностей (Injection)
    public MainController(AssignmentRepository assignmentRepository, SubmissionRepository submissionRepository) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
    }

    // ==========================
    // ПАНЕЛЬ ВЧИТЕЛЯ (Головна)
    // ==========================

    @GetMapping("/")
    public String showHomePage(Model model) {
        // Отримуємо всі завдання, щоб відобразити їх у списку
        List<Assignment> assignments = assignmentRepository.findAll();
        model.addAttribute("assignments", assignments);
        return "index";
    }

    @PostMapping("/add-assignment")
    public String addAssignment(@RequestParam String title,
                                @RequestParam String description,
                                @RequestParam String deadline) {
        Assignment task = new Assignment();
        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline(LocalDate.parse(deadline));
        assignmentRepository.save(task);
        return "redirect:/";
    }

    @PostMapping("/delete-assignment")
    public String deleteAssignment(@RequestParam Long id) {
        assignmentRepository.deleteById(id);
        return "redirect:/";
    }

    // Сторінка зі списком усіх надісланих робіт (для перевірки)
    @GetMapping("/submissions")
    public String viewSubmissions(Model model) {
        model.addAttribute("submissions", submissionRepository.findAll());
        return "submissions";
    }

    // Метод для виставлення оцінки конкретній роботі
    @PostMapping("/grade-submission")
    public String gradeSubmission(@RequestParam Long submissionId, @RequestParam int grade) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Відповідь не знайдена: " + submissionId));

        submission.setGrade(grade);
        submissionRepository.save(submission);

        return "redirect:/submissions";
    }

    // Журнал успішності з аналітикою (Середній бал)
    @GetMapping("/journal")
    public String showJournal(Model model) {
        List<Submission> allSubmissions = submissionRepository.findAll();

        // Відфільтровуємо тільки ті роботи, які вже мають оцінку
        List<Submission> gradedSubmissions = allSubmissions.stream()
                .filter(s -> s.getGrade() != null)
                .toList();

        // Рахуємо середній бал класу через Stream API
        double average = gradedSubmissions.stream()
                .mapToInt(Submission::getGrade)
                .average()
                .orElse(0.0);

        model.addAttribute("gradedSubmissions", gradedSubmissions);
        model.addAttribute("averageGrade", String.format("%.1f", average)); // Форматуємо до одного знака
        model.addAttribute("totalGraded", gradedSubmissions.size());

        return "journal";
    }

    // ==========================
    // ПАНЕЛЬ УЧНЯ
    // ==========================

    @GetMapping("/student")
    public String showStudentPage(Model model) {
        // Отримуємо всі завдання та всі здані роботи (імітуємо, що це роботи одного учня)
        List<Assignment> allAssignments = assignmentRepository.findAll();
        List<Submission> mySubmissions = submissionRepository.findAll();

        // Отримуємо ID завдань, які учень ВЖЕ здав
        List<Long> submittedAssignmentIds = mySubmissions.stream()
                .map(s -> s.getAssignment().getId())
                .toList();

        // Залишаємо тільки ті завдання, які учень ЩЕ НЕ здав
        List<Assignment> availableAssignments = allAssignments.stream()
                .filter(a -> !submittedAssignmentIds.contains(a.getId()))
                .toList();

        // Передаємо в HTML два окремі списки
        model.addAttribute("availableAssignments", availableAssignments);
        model.addAttribute("mySubmissions", mySubmissions);

        return "student/dashboard";
    }
    @GetMapping("/student/submit/{id}")
    public String showSubmitForm(@PathVariable Long id, Model model) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Завдання не знайдено: " + id));
        model.addAttribute("assignment", assignment);
        return "student/submit-assignment";
    }

    @PostMapping("/student/save-submission")
    public String saveSubmission(@RequestParam Long assignmentId, @RequestParam String content) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Завдання не знайдено"));

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setContent(content);
        // Тут можна додати логіку встановлення імені студента, якщо є авторизація
        submissionRepository.save(submission);

        return "redirect:/student";
    }
    // Сторінка "Мої класи" (поки що статична для демонстрації інтерфейсу)
    @GetMapping("/classes")
    public String showClasses() {
        return "classes";
    }
    // Демонстраційна сторінка списку учнів (динамічна)
    @GetMapping("/students/{className}")
    public String showStudentsList(@PathVariable String className, Model model) {
        model.addAttribute("className", className); // Передаємо назву класу в HTML
        return "students";
    }
    // Сторінка входу (Логін)
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
}
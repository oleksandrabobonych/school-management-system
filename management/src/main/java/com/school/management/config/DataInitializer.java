package com.school.management.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

// Якщо щось підкреслено червоним - наведи мишкою і натисни Alt + Enter -> Import class
// Тобі потрібно буде імпортувати твої Entity (User, Assignment) та Репозиторії

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(
            com.school.management.repository.UserRepository userRepository,
            com.school.management.repository.AssignmentRepository assignmentRepository) {

        return args -> {
            // Перевіряємо, чи база порожня
            if (userRepository.count() == 0) {

                // 1. Створюємо Вчителя
                com.school.management.entity.User teacher = new com.school.management.entity.User();
                teacher.setUsername("Марія Іванівна");
                teacher.setPassword("1234"); // Паролі поки не шифруємо для простоти
                teacher.setRole("TEACHER");
                userRepository.save(teacher);

                // 2. Створюємо Учня
                com.school.management.entity.User student = new com.school.management.entity.User();
                student.setUsername("Іван Петренко");
                student.setPassword("1234");
                student.setRole("STUDENT");
                userRepository.save(student);

                // 3. Створюємо Завдання №1
                com.school.management.entity.Assignment task1 = new com.school.management.entity.Assignment();
                task1.setTitle("Математика: Дроби");
                task1.setDescription("Вирішити приклади 1-5 на сторінці 42.");
                task1.setDeadline(LocalDate.now().plusDays(3)); // Дедлайн через 3 дні
                task1.setTeacher(teacher);
                assignmentRepository.save(task1);

                // 4. Створюємо Завдання №2
                com.school.management.entity.Assignment task2 = new com.school.management.entity.Assignment();
                task2.setTitle("Історія: Київська Русь");
                task2.setDescription("Написати коротке есе про Ярослава Мудрого.");
                task2.setDeadline(LocalDate.now().plusDays(7)); // Дедлайн через 7 днів
                task2.setTeacher(teacher);
                assignmentRepository.save(task2);

                System.out.println("✅ МАГІЯ ВІДБУЛАСЯ: Тестові дані успішно завантажено в базу!");
            }
        };
    }
}
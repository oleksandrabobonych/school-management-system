package com.school.management;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.school.management.repository.AssignmentRepository;
import com.school.management.repository.SubmissionRepository;
import com.school.management.entity.Assignment;
import com.school.management.entity.Submission;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {


    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;

    public DataLoader(AssignmentRepository assignmentRepository, SubmissionRepository submissionRepository) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Перевіряємо: якщо база порожня, тоді наповнюємо її
        if (assignmentRepository.count() == 0) {
            System.out.println("⏳ Заповнення бази даних тестовими даними...");

            // 1. СТВОРЮЄМО ЗАВДАННЯ ВІД ВЧИТЕЛЯ
            Assignment task1 = new Assignment();
            task1.setTitle("Математика: Дроби");
            task1.setDescription("Вирішити приклади 1-5 на сторінці 42. Будьте уважні зі спільним знаменником!");
            task1.setDeadline(LocalDate.parse("2024-05-20"));

            Assignment task2 = new Assignment();
            task2.setTitle("Історія: Київська Русь");
            task2.setDescription("Написати коротке есе (мінімум 10 речень) про епоху правління Ярослава Мудрого.");
            task2.setDeadline(LocalDate.parse("2024-05-22"));

            Assignment task3 = new Assignment();
            task3.setTitle("Інформатика: Веб-дизайн");
            task3.setDescription("Створити просту HTML-сторінку зі списками та таблицею.");
            task3.setDeadline(LocalDate.parse("2024-05-25"));

            // Зберігаємо всі завдання в базу
            assignmentRepository.saveAll(List.of(task1, task2, task3));

            // 2. СТВОРЮЄМО ЗДАНІ РОБОТИ ВІД УЧНІВ (імітація)

            // Перша робота - вже перевірена (з оцінкою)
            Submission sub1 = new Submission();
            sub1.setAssignment(task1); // Прив'язуємо до завдання з математики
            sub1.setContent("Ось мої розв'язки: 1) 1/2, 2) 3/4, 3) 5/8. Наче все вийшло!");
            sub1.setGrade(11); // Вчитель уже поставив оцінку

            // Друга робота - щойно здана (без оцінки)
            Submission sub2 = new Submission();
            sub2.setAssignment(task2); // Прив'язуємо до завдання з історії
            sub2.setContent("Ярослав Мудрий був видатним правителем. Він створив першу збірку законів 'Руська Правда'...");
            // Оцінку не ставимо, щоб вчитель міг перевірити її на панелі!

            // Зберігаємо здані роботи в базу
            submissionRepository.saveAll(List.of(sub1, sub2));

            System.out.println("✅ База даних успішно заповнена! Проєкт готовий до роботи.");
        }
    }
}
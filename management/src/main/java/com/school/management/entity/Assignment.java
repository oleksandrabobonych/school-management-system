package com.school.management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL)
    private List<Submission> submissions;
}

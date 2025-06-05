package com.hruday.TaskManager.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "task")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;


    private String description;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;


    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.TODO;

    public enum Status {
        TODO,
        IN_PROGRESS,
        DONE

//        private final String displayName;
//
//        Status(String displayName) {
//            this.displayName = displayName;
//        }
//
//        @Override
//        public String toString() {
//            return displayName;
//        }
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

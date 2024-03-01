package com.rootable.apiserver.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    @Column(length = 500, nullable = false)
    private String title;

    private String content;

    private boolean complete;

    private LocalDate dueDate;

    /*
     * 연관 관계 메서드
     * */
    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void changeComplete(boolean complete) {
        this.complete = complete;
    }

}

package com.IDM3.CRUDmuse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "saved_projects")
public class SavedProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "savedProjectsID")
    private Long savedProjectsID;

    @NotNull(message = "Interest Level is required")
    @Column(name = "interestLevel", length = 10)
    private String interestLevel;

    @NotNull(message = "Note 1 is required")
    @Size(max = 100, message = "Note 1 name must not exceed 100 characters")
    @Column(name = "notes1", length = 100)
    private String notes1;

    @NotNull(message = "Note 2 is required")
    @Size(max = 500, message = "Note 2 name must not exceed 500 characters")
    @Column(name = "notes2", length = 100)
    private String notes2;

    @Setter
    @Column(name = "is_favorite")
    private Boolean favorite = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ProjectId")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

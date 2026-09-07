package com.IDM3.CRUDmuse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project")

public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @NotBlank(message = "Project name is required")
    @Size(max = 100, message = "Project name must not exceed 100 characters")
    @Column(name = "project_name", length = 100, nullable = false)
    private String projectName;

    @Column(name = "project_image", length = 255)
    private String projectImage;

    @NotBlank(message = "Short description is required")
    @Size(max = 100, message = "Short description must not exceed 100 characters")
    @Column(name = "short_description", length = 100)
    private String shortDescription;

    @NotBlank(message = "Long description is required")
    @Size(max = 800, message = "Long description must not exceed 800 characters")
    @Column(name = "long_description", length = 800)
    private String longDescription;

    @Column(name = "document_one", length = 255)
    private String documentOne;

    @NotBlank(message = "Date Created is required")
    @Column(name = "date")
    private String dateCreated;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key column
    private User user;
}



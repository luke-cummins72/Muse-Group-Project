package com.IDM3.CRUDmuse.model;

import lombok.*;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "showcase_project")
public class ShowcaseProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long showcaseProjectId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "showcase_id")
    private Showcases showcase;

    @Column(name = "approved", length = 4, nullable = true)
    private String Approved;
}

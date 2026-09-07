package com.IDM3.CRUDmuse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name = "showcases")
public class Showcases {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "showcase_id")
    private Long showcaseId;

    @NotBlank(message = "Showcase name is required")
    @Size(max = 100, message = "Showcase name must not exceed 100 characters")
    @Column(name = "showcase_name", length = 100)
    private String showcaseName;

    @NotBlank(message = "Short description is required")
    @Size(max = 100, message = "Short description must not exceed 100 characters")
    @Column(name = "short_description", length = 100)
    private String shortDescription;

    @NotBlank(message = "Long description is required")
    @Size(max = 400, message = "Long description must not exceed 400 characters")
    @Column(name = "long_description", length = 400)
    private String longDescription;

    @NotNull(message = "Status is required")
    @Column(name = "status", length = 50)
    private String Status;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Matches HTML5 date input format
    @NotNull(message = "Date is required")
    @Column(name = "submission_deadline")
    private Date submissionDeadline;

    @Column(name = "showcase_image", length = 255)
    private String showcaseImage;

    @Column(name = "top", length = 4, nullable = true)
    private String Top;

}

package com.IDM3.CRUDmuse.service;

import com.IDM3.CRUDmuse.model.Project;
import com.IDM3.CRUDmuse.model.ShowcaseProject;
import com.IDM3.CRUDmuse.repository.ShowcaseProjectRepository;
import com.IDM3.CRUDmuse.repository.ShowcaseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.IDM3.CRUDmuse.model.Showcases;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShowcaseService {

    private static final String UPLOAD_DIR = "src/main/resources/static/assets/images/showcases/";

    @Autowired
    private ShowcaseRepository showcaseRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ShowcaseProjectRepository showcaseProjectRepository;

    public List<Showcases> findAllShowcases(){
        return showcaseRepository.findAll();
    }

    public Showcases findShowcaseById(Long id) {
        return showcaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Showcase not found"));
    }

    public List<Showcases> findAllLiveShowcases(){
        List<Showcases> allLiveShowcases = new ArrayList<>();
        List<Showcases> allShowcases = showcaseRepository.findAll();
        for (Showcases showcases : allShowcases) {
            if(showcases.getStatus().equals("Live")){
                allLiveShowcases.add(showcases);
            }
        }
        return allLiveShowcases;
    }

    public List<Showcases> findAllTopShowcases(){
        List<Showcases> allTopShowcases = new ArrayList<>();
        List<Showcases> allShowcases = showcaseRepository.findAll();
        for (Showcases showcases : allShowcases) {
            if(showcases.getTop().equals("Yes")){
                allTopShowcases.add(showcases);
            }
        }
        return allTopShowcases;
    }

    public List<Showcases> findAllUpcomingShowcases(){
        List<Showcases> allUpcomingShowcases = new ArrayList<>();
        List<Showcases> allShowcases = showcaseRepository.findAll();
        for (Showcases showcases : allShowcases) {
            if(showcases.getStatus().equals("Upcoming")){
                allUpcomingShowcases.add(showcases);
            }
        }
        return allUpcomingShowcases;
    }

    public List<Showcases> findShowcasesByUserProjects(Long userId) {
        // Fetch all projects created by the user using the injected ProjectService instance
        List<Project> userProjects = projectService.findProjectsByUserId(userId);
        List<Showcases> showcases = new ArrayList<>();

        // Loop through projects to find associated showcases
        for (Project project : userProjects) {
            // Use the injected ShowcaseProjectRepository instance
            List<ShowcaseProject> showcaseProjects = showcaseProjectRepository.findByProject_ProjectId(project.getProjectId());
            for (ShowcaseProject showcaseProject : showcaseProjects) {
                if (showcaseProject.getApproved().equalsIgnoreCase("Yes")) {
                    showcases.add(showcaseProject.getShowcase());
                }
            }
        }

        return showcases;
    }

    public Optional<Showcases> findOne(long showcaseId) {
        return showcaseRepository.findById(showcaseId);
    }

    public void deleteByID(long showcaseId) {
        showcaseRepository.deleteById(showcaseId);
    }

    public Showcases updateShowcase(Showcases updatedShowcase) {
        Optional<Showcases> existingShowcase = showcaseRepository.findById(updatedShowcase.getShowcaseId());

        if (existingShowcase.isPresent()) {
            Showcases showcase = existingShowcase.get();
            showcase.setShowcaseName(updatedShowcase.getShowcaseName());
            showcase.setShortDescription(updatedShowcase.getShortDescription());
            showcase.setLongDescription(updatedShowcase.getLongDescription());
            showcase.setStatus(updatedShowcase.getStatus());
            showcase.setSubmissionDeadline(updatedShowcase.getSubmissionDeadline());
            showcase.setShowcaseImage(updatedShowcase.getShowcaseImage());
            showcase.setTop(updatedShowcase.getTop());
            showcaseRepository.save(showcase);
            return showcase;
        } else {
            throw new EntityNotFoundException("Showcase not found with id " + updatedShowcase.getShowcaseId());
        }
    }

    public void updateShowcaseWithImage(Showcases updatedShowcase, MultipartFile file) throws IOException {
        Optional<Showcases> existingShowcaseOpt = showcaseRepository.findById(updatedShowcase.getShowcaseId());

        if (existingShowcaseOpt.isPresent()) {
            Showcases showcase = existingShowcaseOpt.get();

            // If there's a new image, save it
            if (!file.isEmpty()) {
                String filename = saveImage(file);
                showcase.setShowcaseImage(filename);
            }

            // Update other fields
            showcase.setShowcaseName(updatedShowcase.getShowcaseName());
            showcase.setShortDescription(updatedShowcase.getShortDescription());
            showcase.setLongDescription(updatedShowcase.getLongDescription());
            showcase.setStatus(updatedShowcase.getStatus());
            showcase.setSubmissionDeadline(updatedShowcase.getSubmissionDeadline());
            showcase.setTop(updatedShowcase.getTop());

            showcaseRepository.save(showcase);
        } else {
            throw new EntityNotFoundException("Showcase not found with id " + updatedShowcase.getShowcaseId());
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        // Make sure the upload directory exists
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        // Get the original filename
        String filename = file.getOriginalFilename();

        // Define the file path
        Path filePath = Paths.get(UPLOAD_DIR + filename);

        // Write the file to the file system
        Files.write(filePath, file.getBytes());

        return filename;
    }

    public void saveShowcaseWithImage(Showcases showcase, MultipartFile file) throws IOException {
        String filename = "";

        if (!file.isEmpty()) {
            // Save the image if it's not empty
            filename = saveImage(file);
            showcase.setShowcaseImage(filename);
        }

        // Save the showcase to the repository
        showcaseRepository.save(showcase);
    }
}

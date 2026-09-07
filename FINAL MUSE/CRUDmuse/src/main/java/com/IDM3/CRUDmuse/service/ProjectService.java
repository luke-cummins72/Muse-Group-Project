package com.IDM3.CRUDmuse.service;

import com.IDM3.CRUDmuse.model.Project;
import com.IDM3.CRUDmuse.model.Showcases;
import com.IDM3.CRUDmuse.model.User;
import com.IDM3.CRUDmuse.repository.ProjectRepository;
import com.IDM3.CRUDmuse.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private static final String UPLOAD_DIR = "src/main/resources/static/assets/images/projects/";

    private static final String UPLOAD_DIR2 = "src/main/resources/static/assets/documents/";

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public List<Project> findProjectsByUserId(Long userId) {
        return projectRepository.findByUserId(userId);
    }

//    public List<Project> findAllApprovedProjects(){
//    return projectRepository.findAll();}
       // List<Project> allApprovedProjects = new ArrayList<>();
        //List<Project> allProjects = projectRepository.findAll();
          //  for(Project projects : allProjects) {
            //    if(projects.getApproved().equals("Yes")){
              //       allApprovedProjects.add(projects);
            //}
        //}
        //return allApprovedProjects;
    //}


    public Optional<Project> findOne(long projectId) {
        return projectRepository.findById(projectId);
    }

    public void deleteByID(long projectId) {
        projectRepository.deleteById(projectId);
    }

    public Project updateProject(Project updatedProject) {
        Optional<Project> existingProject = projectRepository.findById(updatedProject.getProjectId());

        if (existingProject.isPresent()) {
            Project project = existingProject.get();
            project.setProjectId(updatedProject.getProjectId());
            project.setProjectName(updatedProject.getProjectName());
            project.setShortDescription(updatedProject.getShortDescription());
            project.setLongDescription(updatedProject.getLongDescription());
            project.setProjectImage(updatedProject.getProjectImage());
            project.setDocumentOne(updatedProject.getDocumentOne());
            projectRepository.save(project);
            return project;
        } else {
            throw new EntityNotFoundException("Project not found with id " + updatedProject.getProjectId());
        }
    }

    public void updateProjectWithImage(Project updatedProject, MultipartFile file) throws IOException {
        Optional<Project> existingProjectOpt = projectRepository.findById(updatedProject.getProjectId());

        if (existingProjectOpt.isPresent()) {
            Project project = existingProjectOpt.get();

            if (!file.isEmpty()) {
                String filename = saveImage(file);
                project.setProjectImage(filename);
            }

            // Update other fields
            project.setProjectName(updatedProject.getProjectName());
            project.setShortDescription(updatedProject.getShortDescription());
            project.setLongDescription(updatedProject.getLongDescription());
            project.setDateCreated(updatedProject.getDateCreated());
            project.setDocumentOne(updatedProject.getDocumentOne());

            projectRepository.save(project);
        } else {
            throw new EntityNotFoundException("Project not found with id " + updatedProject.getProjectId());
        }
    }

    public void updateProjectWithDocument(Project updatedProject, MultipartFile file) throws IOException {
        Optional<Project> existingProjectOpt = projectRepository.findById(updatedProject.getProjectId());

        if (existingProjectOpt.isPresent()) {
            Project project = existingProjectOpt.get();

            if (!file.isEmpty()) {
                String filename = saveDocument(file);
                project.setDocumentOne(filename);
            }

            // Update other fields
            project.setProjectName(updatedProject.getProjectName());
            project.setShortDescription(updatedProject.getShortDescription());
            project.setLongDescription(updatedProject.getLongDescription());
            project.setDateCreated(updatedProject.getDateCreated());
            project.setProjectImage(updatedProject.getProjectImage());

            projectRepository.save(project);
        } else {
            throw new EntityNotFoundException("Project not found with id " + updatedProject.getProjectId());
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

    private String saveDocument(MultipartFile file) throws IOException {
        // Make sure the upload directory exists
        Files.createDirectories(Paths.get(UPLOAD_DIR2));

        // Get the original filename
        String filename = file.getOriginalFilename();

        // Define the file path
        Path filePath = Paths.get(UPLOAD_DIR2 + filename);

        // Write the file to the file system
        Files.write(filePath, file.getBytes());

        return filename;
    }

    public void saveProjectWithImageAndDocument(Project project, MultipartFile file, MultipartFile file2, User loggedInUser) throws IOException {
        String filename = "";
        String filename2 = "";

        if (!file.isEmpty()) {
            // Save the image if it's not empty
            filename = saveImage(file);
            filename2 = saveDocument(file2);
            project.setProjectImage(filename);
            project.setDocumentOne(filename2);
        }
        // Set the logged-in user
        project.setUser(loggedInUser); // Set the user here


        projectRepository.save(project);
    }
}


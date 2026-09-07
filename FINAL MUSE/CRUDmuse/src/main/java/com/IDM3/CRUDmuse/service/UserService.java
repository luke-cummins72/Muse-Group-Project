package com.IDM3.CRUDmuse.service;

import com.IDM3.CRUDmuse.model.Project;
import com.IDM3.CRUDmuse.model.ShowcaseProject;
import com.IDM3.CRUDmuse.model.Showcases;
import com.IDM3.CRUDmuse.model.User;
import com.IDM3.CRUDmuse.repository.ProjectRepository;
import com.IDM3.CRUDmuse.repository.ShowcaseProjectRepository;
import com.IDM3.CRUDmuse.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserService {

    private static final String UPLOAD_DIR = "src/main/resources/static/assets/images/users/";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ShowcaseProjectRepository showcaseProjectRepository;

//    public List<Project> findAllSubmittedProjects(){
//        List<Project> allSubmittedProjects = new ArrayList<>();
//        List<Project> allProjects = projectRepository.findAll();
//        for (Project projects : allProjects) {
//            if(projects.getApproved().isBlank()){
//                allSubmittedProjects.add(projects);
//            }
//        }
//        return allSubmittedProjects;
//    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public List<ShowcaseProject> findAllSubmittedShowcaseProjects(long showcaseId){
        List<ShowcaseProject> allSubmittedShowcaseProjects = new ArrayList<>();
        List<ShowcaseProject> allShowcaseProjects = showcaseProjectRepository.findByShowcase_ShowcaseId(showcaseId);
        for (ShowcaseProject showcaseProject : allShowcaseProjects) {
            if(showcaseProject.getApproved().isBlank()){
                allSubmittedShowcaseProjects.add(showcaseProject);
            }
        }
        return allSubmittedShowcaseProjects;
    }

    // Authenticate user
    public User authenticate(String username, String password) {
        return userRepository.findByUserNameAndPassword(username, password);
    }

    private String saveImage(MultipartFile file) throws IOException {

        Files.createDirectories(Paths.get(UPLOAD_DIR));

        String filename = file.getOriginalFilename();

        Path filePath = Paths.get(UPLOAD_DIR + filename);

        Files.write(filePath, file.getBytes());

        return filename;
    }

    public void saveUserWithImage(User user, MultipartFile file) throws IOException {
        String filename = "";

        if (!file.isEmpty()) {
            // Save the image if it's not empty
            filename = saveImage(file);
            user.setUserImage(filename);
        }
        // Save the showcase to the repository
        userRepository.save(user);
    }

    public User updateUser(User updatedUser) {
        Optional<User> existingUser = userRepository.findById(updatedUser.getId());

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUserName(updatedUser.getUserName());
            user.setFirstName(updatedUser.getFirstName());
            user.setSurname(updatedUser.getSurname());
            user.setEmail(updatedUser.getEmail());
            user.setBio(updatedUser.getBio());
            user.setUserImage(updatedUser.getUserImage());
            userRepository.save(user);
            return user;
        } else {
            throw new EntityNotFoundException("User not found with id " + updatedUser.getId());
        }
    }

    public void updateUserWithImage(User updatedUser, MultipartFile file) throws IOException {
        Optional<User> existingUserOpt = userRepository.findById(updatedUser.getId());

        if (existingUserOpt.isPresent()) {
            User user = existingUserOpt.get();

            // If there's a new image, save it
            if (!file.isEmpty()) {
                String filename = saveImage(file);
                user.setUserImage(filename);
            }

            // Update other fields
            user.setUserName(updatedUser.getUserName());
            user.setFirstName(updatedUser.getFirstName());
            user.setSurname(updatedUser.getSurname());
            user.setEmail(updatedUser.getEmail());
            user.setBio(updatedUser.getBio());

            userRepository.save(user);
        } else {
            throw new EntityNotFoundException("Showcase not found with id " + updatedUser.getId());
        }
    }

}

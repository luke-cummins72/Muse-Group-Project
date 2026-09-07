package com.IDM3.CRUDmuse.controllers;

import com.IDM3.CRUDmuse.model.*;
import com.IDM3.CRUDmuse.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Controller()
@RequestMapping(value = {"/projects", "/projects/"})
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SavedProjectService savedProjectService;

    @Autowired
    private ShowcaseService showcaseService;

    @Autowired
    private ShowcaseProjectService showcaseProjectService;

    @Autowired
    private ThumbnailService thumbnailService;

    // Show add project form
    @GetMapping("/add")
    public ModelAndView showAddProjectForm(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        ModelAndView mav = new ModelAndView("addProject");
        mav.addObject("newProject", new Project());
        mav.addObject("upcomingShowcase", showcaseService.findAllUpcomingShowcases()); // Fetch all showcases
        mav.addObject("user", loggedInUser);
        return mav;
    }

    @PostMapping("/addProject")
    public ModelAndView addProject(
            @ModelAttribute("newProject") @Valid Project project,
            BindingResult result,
            @RequestParam("projectImageFile") MultipartFile file,
            @RequestParam("documentOneFile") MultipartFile file2,
            @RequestParam(value = "showcaseId", required = false) Long showcaseId,
            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (result.hasErrors()) {
            // Return view with errors and upcoming showcases
            return new ModelAndView("/addProject", "newProject", project)
                    .addObject("errors", result.getAllErrors())
                    .addObject("upcomingShowcase", showcaseService.findAllUpcomingShowcases());
        }

        try {
            // Save the project with the image
            projectService.saveProjectWithImageAndDocument(project, file, file2, loggedInUser);

            // If showcaseId is not null and not 0, link the project to the showcase
            if (showcaseId != null && showcaseId != 0) {
                Showcases selectedShowcase = showcaseService.findShowcaseById(showcaseId);
                ShowcaseProject showcaseProject = new ShowcaseProject();
                showcaseProject.setProject(project);
                showcaseProject.setShowcase(selectedShowcase);
                showcaseProject.setApproved(" "); // approval state
                showcaseProjectService.save(showcaseProject); // Save showcase-project mapping
            }
            // If showcaseId is 0 or null, the project is added without a showcase link
        } catch (IOException e) {
            e.printStackTrace();
            return new ModelAndView("/error", "error", "Image upload failed")
                    .addObject("upcomingShowcase", showcaseService.findAllUpcomingShowcases());
        }

        // Redirect to user's manage project page
        ModelAndView mav = new ModelAndView("redirect:/user/manageProject");
        mav.addObject("user", loggedInUser);
        mav.addObject("upcomingShowcase", showcaseService.findAllUpcomingShowcases());
        return mav;
    }

    @GetMapping("/allProjects")
    public ModelAndView displayAllProjects(HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        List<Project> allProjects = projectService.findAllProjects();

        List<Project> allProjectsTN = generateThumbnailProject(allProjects);

        ModelAndView mav = new ModelAndView("projects");
        mav.addObject("projects", allProjectsTN);
        mav.addObject("user", loggedInUser);

        // If user is an employer, fetch their saved projects and pass them along
        if (loggedInUser != null && "employer".equalsIgnoreCase(loggedInUser.getUserType())) {
            List<SavedProject> employerSavedProjects = savedProjectService.findAllByEmployer(loggedInUser.getId());
            // Extract project IDs into a set for easy checking
            Set<Long> savedProjectIds = new HashSet<>();
            for (SavedProject sp : employerSavedProjects) {
                savedProjectIds.add(sp.getProject().getProjectId());
            }
            mav.addObject("id", savedProjectIds);
        } else {
            mav.addObject("id", Collections.emptySet());
        }
        return mav;
    }


    @GetMapping("/{id}")
    public ModelAndView viewProject(@PathVariable("id") long id, HttpSession session) {

        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        // Retrieve the project by ID
        Optional<Project> project = projectService.findOne(id);

        if (project.isEmpty()) {
            // Return an error page if the project is not found
            return new ModelAndView("error", "error", "Project not found");
        }

        // Prepare the model and view for the project details
        ModelAndView mav = new ModelAndView("projectDetails");
        mav.addObject("aProject", project.get());
        mav.addObject("user", loggedInUser); // Pass the logged-in user to the view

        if (loggedInUser != null && "employer".equalsIgnoreCase(loggedInUser.getUserType())) {
            Optional<SavedProject> savedProject = savedProjectService.findByUserIdAndProjectId(loggedInUser.getId(), id);
            savedProject.ifPresent(sp -> mav.addObject("savedProject", sp));
        }

        return mav;
    }

    // Edit project
    @GetMapping("/edit/{id}")
    public ModelAndView showEditProjectForm(@PathVariable("id") long id, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        // Fetch the project by ID
        Optional<Project> project = projectService.findOne(id);

        // Handle the case where the project is not found
        if (project.isEmpty()) {
            return new ModelAndView("error", "error", "Project not found");
        }

        ModelAndView mav = new ModelAndView("editProject");

        mav.addObject("user", loggedInUser); // Pass the logged-in user to the view

        mav.addObject("aProject", project.get());

        mav.addObject("upcomingShowcase", showcaseService.findAllUpcomingShowcases());

        return mav;
    }

    // Save or update project
    @PostMapping("/saveProject")
    public ModelAndView saveOrUpdateProject(@ModelAttribute("aProject") Project project,
                                            @RequestParam("projectImageFile") MultipartFile file,
                                            @RequestParam("documentOneFile") MultipartFile file2,
                                            @RequestParam(value = "showcaseId", required = false) Long showcaseId,
                                            HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return new ModelAndView("error", "error", "User not logged in");
        }

        try {
            // Associate the logged-in user with the project
            project.setUser(loggedInUser);

            // Check if the project is new or existing
            if (project.getProjectId() == null) {
                // Save the project with image and document
                projectService.saveProjectWithImageAndDocument(project, file, file2, loggedInUser);
            } else {
                // Update logic for existing projects
                if (!file.isEmpty()) {
                    // Update project with the new image
                    projectService.updateProjectWithImage(project, file);
                } else if (!file2.isEmpty()) {
                    // Update project with the new document
                    projectService.updateProjectWithDocument(project, file2);
                } else {
                    // Update project without image or document
                    projectService.updateProject(project);
                }
            }

            // Handle showcase linkage if applicable
            if (showcaseId != null && showcaseId != 0) {
                Showcases selectedShowcase = showcaseService.findShowcaseById(showcaseId);
                ShowcaseProject showcaseProject = new ShowcaseProject();
                showcaseProject.setProject(project);
                showcaseProject.setShowcase(selectedShowcase);
                showcaseProject.setApproved(" "); // Set approval state
                showcaseProjectService.save(showcaseProject); // Save showcase-project mapping
            }

        } catch (IOException e) {
            e.printStackTrace();
            return new ModelAndView("error", "error", "Failed to save project with image.");
        }

        // Redirect based on user type
        String userType = loggedInUser.getUserType();
        if ("admin".equals(userType)) {
            return new ModelAndView("redirect:/user/manageStudentProjects");
        } else if ("student".equals(userType)) {
            return new ModelAndView("redirect:/user/manageProject");
        }

        return new ModelAndView("error", "error", "User type not recognized");
    }

    // Delete project
    @PostMapping("/delete")
    public ModelAndView deleteProject(@RequestParam("projectId") long id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return new ModelAndView("error", "error", "User not logged in");
        }

        if (projectService.findOne(id).isEmpty()) {
            return new ModelAndView("error", "error", "Project not found");
        }

        projectService.deleteByID(id);

        String userType = loggedInUser.getUserType();
        if ("admin".equals(userType)) {
            return new ModelAndView("redirect:/user/manageStudentProjects");
        } else if ("student".equals(userType)) {
            return new ModelAndView("redirect:/user/manageProjects");
        }

        return new ModelAndView("error", "error", "User type not recognized");
    }

//    @GetMapping("/approve/{projectId}")
//    public ModelAndView approveProject(@PathVariable("projectId") long projectId) {
//       Optional<Project> projectOptional = projectService.findOne(projectId);
//
//        if (projectOptional.isEmpty()) {
//            return new ModelAndView("error", "error", "Project not found");
//        }
//
//        Project project = projectOptional.get();
//
//        project.setApproved("Yes"); // Assuming 'approve' is a String field in the Project entity
//        projectService.updateProject(project);
//
//        return new ModelAndView("redirect:/user/approveProjects");
//    }

        // Thumbnail generation for projects
    private List<Project> generateThumbnailProject(List<Project> allProjects) {

        try{
            String imageDirPathProject = "src/main/resources/static/assets/images/projects/";
            String thumbnailDirPathProject = "src/main/resources/static/assets/images/projects/thumbnail/";

            for(Project project : allProjects) {

                System.out.println(imageDirPathProject + project.getProjectImage());
                File image = new File(imageDirPathProject + "/" +project.getProjectImage());
                System.out.println("thumbnail:" + thumbnailDirPathProject + "thumb_" + image.getName());
                File thumbnailFile = new File(thumbnailDirPathProject + "/" + "thumb_" + image.getName());
                thumbnailService.generateThumbnailProject(image, thumbnailFile);
                System.out.println("Image uploaded and thumbnail created: " + thumbnailFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to upload image or create thumbnail.");
        }
        return allProjects;
    }

}


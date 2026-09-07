package com.IDM3.CRUDmuse.controllers;

import com.IDM3.CRUDmuse.model.*;
import com.IDM3.CRUDmuse.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller()
@RequestMapping(value = {"/user", "/user/"})
public class UserController {

    @Autowired
    private ShowcaseService showcaseService;

    @Autowired
    private ThumbnailService thumbnailService;

    @Autowired
    private SavedProjectService savedProjectService;

    @Autowired
    private ShowcaseProjectService showcaseProjectService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

//    @GetMapping("/edit/{id}")
//    public ModelAndView showEditUserForm(HttpSession session) {
//
//        ModelAndView mav = new ModelAndView("editProfile");
//
//        // Retrieve the logged-in user from the session
//        User loggedInUser = (User) session.getAttribute("loggedInUser");
//
//        Optional<User> user = userService.findUserById(loggedInUser.getId());
//
//        // Check if a user is logged in
//        if (loggedInUser != null) {
//            // Pass user details to the view
//            mav.addObject("user", loggedInUser);
//
//            // Check if user data is available
//            if (user.isPresent()) {
//                mav.addObject("aUser", user.get());
//            } else {
//                // Handle case where user is not found in the database
//                mav.addObject("errorMessage", "User details not found.");
//            }
//        } else {
//            // If no user is logged in, redirect to the login page
//            mav.setViewName("redirect:/Login");
//        }
//
//        return mav;
//    }

    // Save or update showcase
//    @PostMapping("/saveUser")
//    public ModelAndView saveOrUpdateUser(@ModelAttribute("aUser") User user,
//                                         @RequestParam("userImageFile") MultipartFile file,
//                                         BindingResult result,
//                                         HttpSession session) {
//        User loggedInUser = (User) session.getAttribute("loggedInUser");
//
////        if (result.hasErrors()) {
////            String viewName = (user.getId() == null) ? "register" : "editProfile";
////            return new ModelAndView(viewName);
////        }
//
//        try {
//            // Existing showcase
//            if (!file.isEmpty()) {
//                // Update image if a new file is uploaded
//                userService.updateUserWithImage(user, file);
//            } else {
//                // No image update, just update other fields
//                userService.updateUser(user);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ModelAndView("error", "error", "Failed to save showcase with image.");
//        }
//        return new ModelAndView("redirect:/user/" + loggedInUser.getUserType() + "Dashboard");
//    }

    @RequestMapping("/{id}")
    public ModelAndView viewUserProfile(@PathVariable("id") Long userId, HttpSession session) {

        Optional<User> userOpt = userService.findUserById(userId);

        if (userOpt.isEmpty()) {
            return new ModelAndView("error", "error", "User not found");
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        User user = userOpt.get();

        // Fetch the projects associated with the user
        List<Project> userProjects = projectService.findProjectsByUserId(userId);

        ModelAndView mav = new ModelAndView("viewUserProfile");
        mav.addObject("user", user);
        mav.addObject("userLoggedIn", loggedInUser);
        mav.addObject("userProjects", userProjects); // Add the projects list to the model

        return mav;
    }

    @GetMapping("/studentDashboard")
    public ModelAndView studentDashboard(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("studentDashboard");

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Check if a user is logged in
        if (loggedInUser != null) {
            List<Project> userProjects = projectService.findProjectsByUserId(loggedInUser.getId());
            modelAndView.addObject("projects", userProjects);
            modelAndView.addObject("user", loggedInUser);
        } else {
            modelAndView.setViewName("redirect:/Login");
        }

        return modelAndView;
    }

    @RequestMapping(value = {"/manageProject", ""})
    public ModelAndView manageProjects(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Ensure the user is logged in
        if (loggedInUser == null) {
            return new ModelAndView("redirect:/Login");
        }

        // Fetch projects and their showcases
        List<Project> userProjects = projectService.findProjectsByUserId(loggedInUser.getId());
        List<Showcases> userShowcases = showcaseService.findShowcasesByUserProjects(loggedInUser.getId());

        // Create and return the ModelAndView
        ModelAndView mav = new ModelAndView("manageProject");
        mav.addObject("AllProjectsRecentFirst", userProjects);
        mav.addObject("UserShowcases", userShowcases);
        mav.addObject("user", loggedInUser);

        return mav;
    }

    @PostMapping("/deleteProject")
    public ModelAndView deleteProject(@RequestParam("projectId") long id) {
        if (projectService.findOne(id).isEmpty()) {
            return new ModelAndView("/error", "error", "Project not found");
        } else {
            projectService.deleteByID(id);
            return new ModelAndView("redirect:/user/manageProject");
        }
    }

    @GetMapping("/employerDashboard")
    public ModelAndView employerDashboard(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("employerDashboard");

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Check if a user is logged in
        if (loggedInUser != null) {
            // Pass user details to the view
            modelAndView.addObject("user", loggedInUser);
        } else {
            // If no user is logged in, redirect to the login page
            modelAndView.setViewName("redirect:/Login");
        }

        return modelAndView;
    }

    @GetMapping("/manageSavedProject")
    public ModelAndView manageSavedProjects(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        ModelAndView modelAndView = new ModelAndView("manageSavedProject");
        if (loggedInUser != null && "employer".equalsIgnoreCase(loggedInUser.getUserType())) {
            List<SavedProject> employerSavedProjects = savedProjectService.findAllByEmployer(loggedInUser.getId());
            modelAndView.addObject("savedProjects", employerSavedProjects);

            // Predefined list of all possible interest levels
            List<String> allInterestLevels = List.of("High", "Medium", "Low", "Watching");
            modelAndView.addObject("interestLevels", allInterestLevels);
        } else {
            modelAndView.addObject("error", "You must be an employer to view saved projects.");
        }
        modelAndView.addObject("user", loggedInUser);
        return modelAndView;
    }

    @PostMapping("/saveToSavedProjects")
    public ModelAndView saveToSavedProjects(@RequestParam("projectId") Long projectId,
                                            @RequestParam("interestLevel") String interestLevel,
                                            @RequestParam(value = "notes1", required = false) String notes1,
                                            @RequestParam(value = "notes2", required = false) String notes2,
                                            HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"employer".equalsIgnoreCase(loggedInUser.getUserType())) {
            return new ModelAndView("redirect:/MuseGallery/Login");
        }

        Optional<Project> optionalProject = projectService.findOne(projectId);
        if (optionalProject.isEmpty()) {
            return new ModelAndView("error", "error", "Project not found");
        }

        Project project = optionalProject.get();

        // Create a new SavedProject record
        SavedProject savedProject = new SavedProject();
        savedProject.setProject(project);
        savedProject.setUser(loggedInUser);
        savedProject.setInterestLevel(interestLevel); // Set the chosen interest level
        savedProject.setNotes1(notes1);
        savedProject.setNotes2(notes2);
        savedProject.setFavorite(false);

        savedProjectService.saveSavedProjects(savedProject);

        return new ModelAndView("redirect:/projects/allProjects");
    }

    @PostMapping("/removeFromSavedProjects")
    public ModelAndView removeFromSavedProjects(@RequestParam("projectId") Long projectId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"employer".equalsIgnoreCase(loggedInUser.getUserType())) {
            // If not logged in or not an employer, redirect or show an error
            return new ModelAndView("redirect:/MuseGallery/Login");
        }

        // Fetch the saved project record for this user and project.
        // First, find all saved projects for the user, then filter by projectId.
        List<SavedProject> employerSavedProjects = savedProjectService.findAllByEmployer(loggedInUser.getId());
        SavedProject savedProjectToRemove = null;
        for (SavedProject sp : employerSavedProjects) {
            if (sp.getProject().getProjectId().equals(projectId)) {
                savedProjectToRemove = sp;
                break;
            }
        }

        if (savedProjectToRemove != null) {
            savedProjectService.deleteByID(savedProjectToRemove.getSavedProjectsID());
        }

        return new ModelAndView("redirect:/projects/allProjects");
    }

    @GetMapping("/editSavedProject/{savedProjectsID}")
    public String showEditSavedProject(@PathVariable Long savedProjectsID, Model model, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/MuseGallery/Login";
        }

        SavedProject savedProject = savedProjectService.findById(savedProjectsID);
        if (savedProject == null || !savedProject.getUser().getId().equals(loggedInUser.getId())) {
            model.addAttribute("error", "You do not have permission to edit this saved project.");
            return "error";
        }

        model.addAttribute("savedProject", savedProject);
        model.addAttribute("user", loggedInUser);
        return "editSavedProject";
    }

    @PostMapping("/saveUpdatedSavedProject")
    public String saveUpdatedSavedProject(@ModelAttribute("savedProject") SavedProject updatedSavedProject, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/MuseGallery/Login";
        }

        if (updatedSavedProject.getSavedProjectsID() == null) {
            return "redirect:/error";
        }

        SavedProject existingSavedProject = savedProjectService.findById(updatedSavedProject.getSavedProjectsID());
        if (existingSavedProject == null || !existingSavedProject.getUser().getId().equals(loggedInUser.getId())) {
            return "redirect:/error";
        }

        existingSavedProject.setInterestLevel(updatedSavedProject.getInterestLevel());
        existingSavedProject.setNotes1(updatedSavedProject.getNotes1());
        existingSavedProject.setNotes2(updatedSavedProject.getNotes2());
        savedProjectService.save(existingSavedProject);

        return "redirect:/user/manageSavedProject";
    }

    @GetMapping("/deleteSavedProject/{savedProjectsID}")
    public String deleteSavedProject(@PathVariable Long savedProjectsID, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"employer".equalsIgnoreCase(loggedInUser.getUserType())) {
            return "redirect:/MuseGallery/Login";
        }
        savedProjectService.deleteByID(savedProjectsID);
        return "redirect:/user/manageSavedProject";
    }

    @GetMapping("/adminDashboard")
    public ModelAndView adminDashboard(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("adminDashboard");

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Check if a user is logged in
        if (loggedInUser != null) {
            // Pass user details to the view
            modelAndView.addObject("user", loggedInUser);
        } else {
            // If no user is logged in, redirect to the login page
            modelAndView.setViewName("redirect:/Login");
        }
        return modelAndView;
    }

    @RequestMapping(value = {"/manageShowcase", ""})
    public ModelAndView manageShowcases(HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        // Fetch all showcases
        List<Showcases> allShowcases = showcaseService.findAllShowcases();

        Date currentDate = new Date();

        for (Showcases showcase : allShowcases) {
            if (showcase.getSubmissionDeadline().before(currentDate)) {
                showcase.setStatus("Live");
                showcaseService.updateShowcase(showcase);
            } else {
                showcase.setStatus("Upcoming");
                showcaseService.updateShowcase(showcase);

            }
        }

        // Generate thumbnails for the showcases (if necessary)
        generateThumbnailShowcases(allShowcases);

        // Create and return the ModelAndView with the appropriate view and data
        ModelAndView mav = new ModelAndView("manageShowcase"); // Specify the correct view name
        mav.addObject("AllLiveShowcases", allShowcases);
        mav.addObject("user", loggedInUser);

        return mav;
    }

    @PostMapping("/deleteShowcase")
    public ModelAndView deleteShowcase(@RequestParam("showcaseId") long id) {
        if (showcaseService.findOne(id).isEmpty()) {
            return new ModelAndView("/error", "error", "Showcase not found");
        } else {
            showcaseService.deleteByID(id);
            return new ModelAndView("redirect:/user/manageShowcase");
        }
    }

    @GetMapping("/approveShowcaseSubmissions/{id}")
    public ModelAndView approveShowcaseSubmissions(@PathVariable("id") long id, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        List<ShowcaseProject> allSubmittedShowcaseProjects = userService.findAllSubmittedShowcaseProjects(id);

        Optional<Showcases> showcaseOptional = showcaseService.findOne(id);

        ModelAndView modelAndView = new ModelAndView("approveShowcaseSubmissions");
        modelAndView.addObject("showcaseProjects", allSubmittedShowcaseProjects);
        modelAndView.addObject("user", loggedInUser);

        if (showcaseOptional.isPresent()) {
            modelAndView.addObject("aShowcase", showcaseOptional.get());
        } else {
            modelAndView.addObject("aShowcase", null);
        }

        return modelAndView;
    }

    @RequestMapping(value = {"/manageStudentProjects", ""})
    public ModelAndView manageStudentProjects(HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Fetch all showcases
        List<Project> allProjects = projectService.findAllProjects();

        // Generate thumbnails for the showcases (if necessary)
        generateThumbnailProject(allProjects);

        // Create and return the ModelAndView with the appropriate view and data
        ModelAndView mav = new ModelAndView("manageStudentProjects"); // Specify the correct view name
        mav.addObject("allProjects", allProjects);
        mav.addObject("user", loggedInUser);

        return mav;
    }

    @PostMapping("/adminDeleteProject")
    public ModelAndView adminDeleteProject(@RequestParam("projectId") long id) {
        if (projectService.findOne(id).isEmpty()) {
            return new ModelAndView("/error", "error", "Project not found");
        } else {
            projectService.deleteByID(id);
            return new ModelAndView("redirect:/user/manageStudentProjects");
        }
    }

    @PostMapping("/adminDeleteShowcaseSubmission")
    public ModelAndView adminDeleteShowcaseSubmission(@RequestParam("projectId") long id) {
        if (projectService.findOne(id).isEmpty()) {
            return new ModelAndView("/error", "error", "Project not found");
        } else {
            projectService.deleteByID(id);
            return new ModelAndView("redirect:/user/manageStudentProjects");
        }
    }

    private List<Showcases> generateThumbnailShowcases(List<Showcases> allShowcases) {

        try{

            String imageDirPathShowcase = "src/main/resources/static/assets/images/showcases/";
            String thumbnailDirPathShowcase = "src/main/resources/static/assets/images/showcases/thumbnail/";
            for(Showcases showcase : allShowcases) {

                System.out.println(imageDirPathShowcase + showcase.getShowcaseImage());
                File image = new File(imageDirPathShowcase + "/" +showcase.getShowcaseImage());
                System.out.println("thumbnail:" + thumbnailDirPathShowcase + "thumb_" + image.getName());
                File thumbnailFile = new File(thumbnailDirPathShowcase + "/" + "thumb_" + image.getName());
                thumbnailService.generateThumbnailShowcase(image, thumbnailFile);
                System.out.println("Image uploaded and thumbnail created: " + thumbnailFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to upload image or create thumbnail.");
        }
        return allShowcases;
    }

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
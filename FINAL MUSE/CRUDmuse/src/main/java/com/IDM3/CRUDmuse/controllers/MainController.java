package com.IDM3.CRUDmuse.controllers;

import com.IDM3.CRUDmuse.model.Project;
import com.IDM3.CRUDmuse.model.Showcases;
import com.IDM3.CRUDmuse.model.User;
import com.IDM3.CRUDmuse.service.ProjectService;
import com.IDM3.CRUDmuse.service.ShowcaseService;
import com.IDM3.CRUDmuse.service.ThumbnailService;
import com.IDM3.CRUDmuse.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = {"/MuseGallery"})

public class MainController {

    @Autowired
    private ShowcaseService showcaseService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ThumbnailService thumbnailService;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private UserService userService;

    @GetMapping("/About")
    public ModelAndView viewAboutPage(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        ModelAndView mav = new ModelAndView("About");

        mav.addObject("user", loggedInUser);

        return mav;
    }

    @GetMapping("/Login")
public ModelAndView showLoginPage() {
    ModelAndView modelAndView = new ModelAndView("login");
    modelAndView.addObject("user", new User());
    return modelAndView;
}

// Handle Login Submission
@PostMapping("/Login")
public ModelAndView handleLogin(@ModelAttribute("user") @Valid User user,
                                BindingResult result,
                                HttpSession session) {
    ModelAndView modelAndView = new ModelAndView();

    User authenticatedUser = userService.authenticate(user.getUserName(), user.getPassword());

    if (authenticatedUser != null) {
        // Store the authenticated user in the session
        session.setAttribute("loggedInUser", authenticatedUser);
        System.out.println("User Type " + authenticatedUser.getUserType());
        // Redirect based on user type
        if ("admin".equalsIgnoreCase(authenticatedUser.getUserType())) {
            modelAndView.setViewName("redirect:/user/adminDashboard");
        } else if ("student".equalsIgnoreCase(authenticatedUser.getUserType())) {
            modelAndView.setViewName("redirect:/user/studentDashboard");
        } else if ("employer".equalsIgnoreCase(authenticatedUser.getUserType())) {
            modelAndView.setViewName("redirect:/user/employerDashboard");
        } else {
            modelAndView.setViewName("redirect:/user/dashboard");
        }
        modelAndView.addObject("username", authenticatedUser.getUserName());
    } else {
        // Return to login page with error message
        modelAndView.setViewName("login");
        modelAndView.addObject("error", "Invalid username or password");
    }

    return modelAndView;
}


// Display Registration Page
@GetMapping("/Register")
public ModelAndView showRegisterPage() {
    ModelAndView modelAndView = new ModelAndView("register");
    modelAndView.addObject("user", new User());
    return modelAndView;
}

// Handle Registration Submission
@PostMapping("/Register")
public ModelAndView handleRegister(@ModelAttribute("user") @Valid User user,
                                   BindingResult result,
                                   @RequestParam("userImageFile") MultipartFile file) {

    ModelAndView modelAndView = new ModelAndView();

    if (result.hasErrors()) {
        return new ModelAndView("register", "user", user).addObject("errors", result.getAllErrors());
    }

    try {
        // Save user with the uploaded image
        userService.saveUserWithImage(user, file);

        modelAndView.setViewName("redirect:/MuseGallery/Login");
        modelAndView.addObject("message", "Registration successful! Please log in.");
    } catch (IOException e) {
        e.printStackTrace(); // Add logging here if using a logger
        modelAndView.setViewName("register");
        modelAndView.addObject("error", "Image upload failed due to server issues.");
    }


    return modelAndView;
}

@RequestMapping(value = {"/logout", ""})
public ModelAndView logOutUser(HttpSession session) {

        // Retrieve the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Invalidate the session to log out the user
        session.invalidate();

        // Redirect to the desired page (MuseGallery/Index)
        return new ModelAndView("redirect:/MuseGallery/Index");
    }


    // Index page for both Projects and Showcases
@RequestMapping(value = {"/Index", ""})
public ModelAndView setUpIndexPageData(HttpSession session) {

    System.out.println("setUpIndexPageData");
    // Retrieve the logged-in user from the session
    User loggedInUser = (User) session.getAttribute("loggedInUser");

    ModelAndView mav = new ModelAndView("Index");

    // Fetch and generate thumbnails for projects
    List<Project> allProjects = projectService.findAllProjects();
    generateThumbnailProject(allProjects);

    // Fetch and generate thumbnails for showcases Fetch all showcases

    List<Showcases> allLiveShowcases = showcaseService.findAllLiveShowcases();
    List<Showcases> allLiveShowcasesTN = generateThumbnailShowcases(allLiveShowcases);
    List<Showcases> allTopShowcases = showcaseService.findAllTopShowcases();
    List<Showcases> allTopShowcasesTN = generateThumbnailShowcases(allTopShowcases);

    // Add data to model
    mav.addObject("AllProjectsRecentFirst", allProjects);
    mav.addObject("AllLiveShowcases", allLiveShowcasesTN);
    mav.addObject("AllTopShowcases", allTopShowcasesTN);
    mav.addObject("user", loggedInUser);


    return mav;
}

// Thumbnail generation for projects
private void generateThumbnailProject(List<Project> allProjects) {

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
}

private List<Showcases> generateThumbnailShowcases(List<Showcases> allShowcases) {

    try {

        String imageDirPathShowcase = "src/main/resources/static/assets/images/showcases/";
        String thumbnailDirPathShowcase = "src/main/resources/static/assets/images/showcases/thumbnail/";
        for (Showcases showcase : allShowcases) {

            System.out.println(imageDirPathShowcase + showcase.getShowcaseImage());
            File image = new File(imageDirPathShowcase + "/" + showcase.getShowcaseImage());
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

}
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

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller()
@RequestMapping(value={"/showcases", "/showcases/"})
public class ShowcaseController {

    @Autowired
    private ShowcaseService showcaseService;

    @Autowired
    private ShowcaseProjectService showcaseProjectService;

    @Autowired
    private ThumbnailService thumbnailService;

    @Autowired
    private SavedProjectService savedProjectService;

    @GetMapping("/add")
    public ModelAndView showAddShowcaseForm(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        ModelAndView mav = new ModelAndView("addShowcase");
        mav.addObject("newShowcase", new Showcases());
        mav.addObject("user", loggedInUser);
    return mav;
    }

    @PostMapping("/addShowcase")
    public ModelAndView addShowcase(@ModelAttribute("newShowcase")
                                    @Valid Showcases showcase,BindingResult result,
                                    @RequestParam("showcaseImageFile") MultipartFile file,
                                    HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (result.hasErrors()) {
            return new ModelAndView("/addShowcase", "newShowcase", showcase).addObject("errors", result.getAllErrors());
        }
        else {
            try {
                showcaseService.saveShowcaseWithImage(showcase, file);
            } catch (IOException e) {
                e.printStackTrace();
                return new ModelAndView("/error", "error", "Image upload failed");
            }
            return new ModelAndView("redirect:/user/manageShowcase").addObject("user" , loggedInUser);
        }
    }

    @RequestMapping(value = {"allShowcases", ""})
    public ModelAndView displayAllShowcases(HttpSession session) {
        // Fetch all showcases
        ModelAndView mav = new ModelAndView("showcases"); // Specify the correct view name

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        mav.addObject("user", loggedInUser);

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
        List<Showcases> allLiveShowcases = showcaseService.findAllLiveShowcases();
        List<Showcases> allLiveShowcasesTN = generateThumbnailShowcases(allLiveShowcases);

        List<Showcases> allUpcomingShowcases = showcaseService.findAllUpcomingShowcases();
        List<Showcases> allUpcomingShowcasesTN = generateThumbnailShowcases(allUpcomingShowcases);

        // Generate thumbnails for the showcases (if necessary)
        generateThumbnailShowcases(allShowcases);

        mav.addObject("AllUpcomingShowcases", allUpcomingShowcasesTN);
        mav.addObject("AllLiveShowcases", allLiveShowcasesTN);

        return mav;
    }

    // View a specific showcase
    @GetMapping("/showcaseproject/{id}")
    public ModelAndView viewShowcase(@PathVariable("id") long id, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        List<ShowcaseProject> allApprovedShowcaseSubmissions = showcaseProjectService.findApprovedShowcaseProjectsByShowcaseId(id);

        Optional<Showcases> showcaseOptional = showcaseService.findOne(id);

        List<Showcases> allUpcomingShowcases = showcaseService.findAllUpcomingShowcases();

        List<Showcases> allUpcomingShowcasesTN = generateThumbnailShowcases(allUpcomingShowcases);

        ModelAndView mav = new ModelAndView("showcaseProjectDetails");
        mav.addObject("AllApprovedShowcaseSubmissions", allApprovedShowcaseSubmissions);
        mav.addObject("user", loggedInUser);
        mav.addObject("AllUpcomingShowcases", allUpcomingShowcasesTN);

        if (showcaseOptional.isPresent()) {
            mav.addObject("aShowcase", showcaseOptional.get());
        } else {
            mav.addObject("aShowcase", null);
        }

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

    @GetMapping("/edit/{id}")
    public ModelAndView showEditShowcaseForm(@PathVariable("id") long id, HttpSession session) {

        // Retrieve logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        // Fetch showcase by ID
        Optional<Showcases> showcase = showcaseService.findOne(id);

        if (showcase.isEmpty()) {
            // Return an error view if the showcase is not found
            return new ModelAndView("error", "error", "Showcase not found");
        }

        // Create and configure ModelAndView for editing showcase
        ModelAndView mav = new ModelAndView("editShowcase");
        mav.addObject("user", loggedInUser);
        mav.addObject("aShowcase", showcase.get());

        return mav;
    }

    // Save or update showcase
    @PostMapping("/saveShowcase")
    public ModelAndView saveOrUpdateShowcase(@ModelAttribute("aShowcase") Showcases showcase,
                                             @RequestParam("showcaseImageFile") MultipartFile file,
                                             BindingResult result) {
        if (result.hasErrors()) {
            String viewName = (showcase.getShowcaseId() == null) ? "addShowcase" : "editShowcase";
            return new ModelAndView(viewName);
        }

        try {
            if (showcase.getShowcaseId() == null) {
                // New showcase, save with image
                showcaseService.saveShowcaseWithImage(showcase, file);
            } else {
                // Existing showcase
                if (!file.isEmpty()) {
                    // Update image if a new file is uploaded
                    showcaseService.updateShowcaseWithImage(showcase, file);
                } else {
                    // No image update, just update other fields
                    showcaseService.updateShowcase(showcase);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ModelAndView("error", "error", "Failed to save showcase with image.");
        }
        return new ModelAndView("redirect:/user/manageShowcase");
    }

        // Delete showcase
    @PostMapping("/delete")
    public ModelAndView deleteShowcase(@RequestParam("showcaseId") long id) {
        if (showcaseService.findOne(id).isEmpty()) {
            return new ModelAndView("/error", "error", "Showcase not found");
        } else {
            showcaseService.deleteByID(id);
            return new ModelAndView("redirect:/showcases/allShowcases");
        }
    }

    @GetMapping("/showcaseproject/approve/{showcaseProjectId}")
    public ModelAndView approveShowcaseSubmission(@PathVariable("showcaseProjectId") long showcaseProjectId, HttpSession session) {
        Optional<ShowcaseProject> showcaseProjectOptional = showcaseProjectService.findShowcaseProjectById(showcaseProjectId); // Assuming a single project is fetched by ID

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (showcaseProjectOptional.isEmpty()) {
            return new ModelAndView("error", "error", "Project not found");
        }

        ShowcaseProject showcaseProject = showcaseProjectOptional.get();
        showcaseProject.setApproved("Yes"); // Assuming 'approved' is a String field in the entity
        showcaseProjectService.updateShowcaseProject(showcaseProject);

        return new ModelAndView("redirect:/user/manageShowcase").addObject("user" , loggedInUser);
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

}

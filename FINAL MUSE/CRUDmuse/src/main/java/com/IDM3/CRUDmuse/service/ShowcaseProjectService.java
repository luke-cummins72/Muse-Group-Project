package com.IDM3.CRUDmuse.service;

import com.IDM3.CRUDmuse.model.Project;
import com.IDM3.CRUDmuse.model.ShowcaseProject;
import com.IDM3.CRUDmuse.repository.ProjectRepository;
import com.IDM3.CRUDmuse.repository.ShowcaseProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShowcaseProjectService {

    @Autowired
    private ShowcaseProjectRepository showcaseProjectRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public Optional<ShowcaseProject> findShowcaseProjectById(long id) {
        return showcaseProjectRepository.findById(id);
    }

    public List<ShowcaseProject> findApprovedShowcaseProjectsByShowcaseId(long showcaseId) {
        return showcaseProjectRepository.findApprovedShowcaseProjectsByShowcaseId(showcaseId);
    }

    public ShowcaseProject updateShowcaseProject(ShowcaseProject updatedShowcaseProject) {
        Optional<ShowcaseProject> existingShowcaseProject = showcaseProjectRepository.findById(updatedShowcaseProject.getShowcaseProjectId());

        if (existingShowcaseProject.isPresent()) {
            ShowcaseProject showcaseProject = existingShowcaseProject.get();
            showcaseProject.setShowcaseProjectId(updatedShowcaseProject.getShowcaseProjectId());
            showcaseProject.setProject(updatedShowcaseProject.getProject());
            showcaseProject.setShowcase(updatedShowcaseProject.getShowcase());
            showcaseProject.setApproved(updatedShowcaseProject.getApproved());
            showcaseProjectRepository.save(showcaseProject);
            return showcaseProject;
        } else {
            throw new EntityNotFoundException("Showcase Project not found with id " + updatedShowcaseProject.getShowcaseProjectId());
        }
    }

    public void save(ShowcaseProject showcaseProject) {
        showcaseProjectRepository.save(showcaseProject);
    }

}

package com.IDM3.CRUDmuse.service;

import com.IDM3.CRUDmuse.model.SavedProject;
import com.IDM3.CRUDmuse.repository.SavedProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class SavedProjectService {

    private static final String UPLOAD_DIR ="src/main/resources/static/images/";

    @Autowired
    private SavedProjectRepository savedProjectRepository;

    public List<SavedProject> findAllByEmployer(Long userId) {
        return savedProjectRepository.findAllByUser_Id(userId);
    }

    // Retrieve all products
    public List<SavedProject> findAllSavedProjects() {
        return savedProjectRepository.findAll();
    }

    public void saveSavedProjects(SavedProject savedProject) {
        savedProjectRepository.save(savedProject);
    }

    public Optional<SavedProject> findOne(long savedProjectsID) {
        return savedProjectRepository.findById(savedProjectsID);
    }

    public void deleteByID(long savedProjectsID) {

        savedProjectRepository.deleteById(savedProjectsID);
    }

    public SavedProject updateSavedProjects(SavedProject updatedSavedProject) {
        Optional<SavedProject> existingSavedProjects = savedProjectRepository.findById(updatedSavedProject.getSavedProjectsID());

        if (existingSavedProjects.isPresent()) {
            SavedProject savedProject = existingSavedProjects.get();
            savedProject.setInterestLevel(updatedSavedProject.getInterestLevel());
            savedProject.setNotes1(updatedSavedProject.getNotes1());
            savedProject.setNotes2(updatedSavedProject.getNotes2());
            savedProjectRepository.save(savedProject);
            return savedProject;
        } else {
            throw new EntityNotFoundException("Saved Project not found with id " + updatedSavedProject.getSavedProjectsID());
        }
    }

    public Optional<SavedProject> findOne(Long id) {
        return savedProjectRepository.findById(id);
    }

    public SavedProject findById(Long id) {
        return savedProjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Saved project not found with ID: " + id));
    }

    public SavedProject save(SavedProject savedProject) {
        return savedProjectRepository.save(savedProject);
    }

    public void deleteByUserIdAndProjectId(Long userId, Long projectId) {
        savedProjectRepository.findByUser_IdAndProject_ProjectId(userId, projectId)
                .ifPresent(savedProjectRepository::delete);
    }

    public Optional<SavedProject> findByUserIdAndProjectId(Long userId, Long projectId) {
        return savedProjectRepository.findByUser_IdAndProject_ProjectId(userId, projectId);
    }

}

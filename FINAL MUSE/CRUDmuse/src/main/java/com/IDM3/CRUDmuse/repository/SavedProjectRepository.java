package com.IDM3.CRUDmuse.repository;

import com.IDM3.CRUDmuse.model.SavedProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedProjectRepository extends JpaRepository<SavedProject, Long> {

    List<SavedProject> findAllByUser_Id(Long userId);

    Optional<SavedProject> findByUser_IdAndProject_ProjectId(Long userId, Long projectId);


}

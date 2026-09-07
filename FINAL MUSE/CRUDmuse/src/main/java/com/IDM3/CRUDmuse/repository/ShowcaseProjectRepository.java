package com.IDM3.CRUDmuse.repository;


import com.IDM3.CRUDmuse.model.Project;
import com.IDM3.CRUDmuse.model.ShowcaseProject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowcaseProjectRepository extends JpaRepository<ShowcaseProject, Long> {

    @Query("SELECT sp FROM ShowcaseProject sp WHERE sp.showcase.showcaseId = :showcaseId AND sp.Approved = 'yes'")
    List<ShowcaseProject> findApprovedShowcaseProjectsByShowcaseId(@Param("showcaseId") long showcaseId);

    List<ShowcaseProject> findByShowcase_ShowcaseId(Long id);

    List<ShowcaseProject> findByProject_ProjectId(Long projectId);



}

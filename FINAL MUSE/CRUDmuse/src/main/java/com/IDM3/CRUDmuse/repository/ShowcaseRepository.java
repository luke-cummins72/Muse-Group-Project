package com.IDM3.CRUDmuse.repository;

import com.IDM3.CRUDmuse.model.Project;
import com.IDM3.CRUDmuse.model.Showcases;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowcaseRepository extends JpaRepository<Showcases, Long> {

}

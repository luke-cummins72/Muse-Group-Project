package com.IDM3.CRUDmuse.application;

import com.IDM3.CRUDmuse.model.Project;
import com.IDM3.CRUDmuse.model.Showcases;
import com.IDM3.CRUDmuse.service.ProjectService;
import com.IDM3.CRUDmuse.service.ShowcaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication
@EntityScan("com.IDM3.CRUDmuse.model")
@ComponentScan({"com.IDM3.CRUDmuse.service", "com.IDM3.CRUDmuse.repository", "com.IDM3.CRUDmuse.controllers"})
@EnableJpaRepositories("com.IDM3.CRUDmuse.repository")
public class CruDmuseApplication implements CommandLineRunner {

	@Autowired
	private ShowcaseService showcaseService;

    @Autowired
    private ProjectService projectService;

	public static void main(String[] args) {

		SpringApplication.run(CruDmuseApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		List<Showcases> showcasesList = showcaseService.findAllShowcases();
		List<Project> projectsList = projectService.findAllProjects();
		for(Showcases showcases : showcasesList) {
			System.out.println(showcases);
		}
		for(Project project : projectsList) {
			System.out.println(project);
		}
	}

}

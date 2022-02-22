package com.project.service;

import com.project.VO.User;
import com.project.dto.ProjectDto;
import com.project.dto.UpdateProjectDto;
import com.project.entity.Project;
import com.project.exception.ProjectNotFoundException;
import com.project.exception.UserNotFoundException;
import com.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService{

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    RestTemplate restTemplate;

    public Project addProject(ProjectDto projectDto) {

        ResponseEntity<User> user= new RestTemplate()
                .getForEntity("http://localhost:9002/users/getClientById/"+ projectDto.getClientId(),
                        User.class);

        if(user.getBody()== null) {
            throw new UserNotFoundException();
        }

        Project project=new Project();

        project.setProjectInfo(projectDto.getProjectInfo());
        project.setProjectName(projectDto.getProjectName());
        project.setStartedAt(projectDto.getStartedAt());
        project.setClientId(projectDto.getClientId());
        project.setOnGoing(true);

       return projectRepository.saveAndFlush(project);

    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findByProjectId(projectId);
    }

    public Project updateProject(UpdateProjectDto updateProjectDto) {

        Project projectReturned= getProjectById(updateProjectDto.getProjectId());
        if(projectReturned== null) {
            throw new ProjectNotFoundException();
        }else {
            projectReturned.setProjectInfo(updateProjectDto.getProjectInfo());
            projectReturned.setOnGoing(updateProjectDto.isOnGoing());
            if(!updateProjectDto.isOnGoing()) {
                projectReturned.setEndedOn(updateProjectDto.getEndedOn());
            }
            projectReturned.setProjectName(updateProjectDto.getProjectName());

            return projectRepository.save(projectReturned);
        }
    }

    public List<Project> getProjectByClientId(Long clientId) {

        ResponseEntity<User> user= new RestTemplate()
                .getForEntity("http://localhost:9002/users/getClientById/"+ clientId,
                        User.class);

        if(user.getBody()== null) {
            throw new UserNotFoundException();
        }

        return projectRepository.findByClientId(clientId);
    }

    public Boolean deleteProjectById(Long projectId) {
        return projectRepository.deleteByProjectId(projectId);
    }
}

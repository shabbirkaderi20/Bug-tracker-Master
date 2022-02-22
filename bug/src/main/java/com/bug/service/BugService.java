package com.bug.service;

import com.bug.VO.Project;
import com.bug.VO.User;
import com.bug.dto.BugDto;
import com.bug.dto.BugWithoutImage;
import com.bug.entity.Bug;
import com.bug.exception.BugNotAddedException;
import com.bug.exception.ImageNotAddedException;
import com.bug.exception.ProjectNotFoundException;
import com.bug.repository.BugRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BugService {

    @Autowired
    BugRepository bugRepository;

    public Bug addBugs(String bugDto, MultipartFile file) {

        try {
            BugDto toBugDto = new ObjectMapper().readValue(bugDto, BugDto.class);
            Project project= new RestTemplate()
                    .getForObject("http://localhost:9003/projects/projectById/"+ toBugDto.getProjectId(),
                            Project.class);

            if(project== null) {
                throw new ProjectNotFoundException();
            }

            String filename= toBugDto.getProjectId()+ file.getOriginalFilename()+
                    project.getClientId()+ Math.random()*(1000000-1000+1)+1000;

            Bug bug= new Bug();

            bug.setBugNote(toBugDto.getBugNote());
            bug.setSeverity(toBugDto.getSeverity());
            bug.setStatus("pending");
            bug.setImage(file.getBytes());
            bug.setImageName(filename);
            bug.setBugSolved(false);
            bug.setProjectId(toBugDto.getProjectId());
            bug.setStaffAssigned(false);

            return bugRepository.saveAndFlush(bug);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BugNotAddedException();
        }catch (IOException e) {
            e.printStackTrace();
            throw new ImageNotAddedException();
        }

    }

    public ResponseEntity<byte[]> getImageByBugId(Long bugId) {

        Bug bug= bugRepository.findByBugId(bugId);

        if(bug== null) {
            throw new BugNotAddedException();
        }
        byte[] image= bug.getImage();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    public BugWithoutImage getBugImageByBugId(Long bugId) {

        Bug bug= bugRepository.findByBugId(bugId);

        if(bug== null) {
            throw new BugNotAddedException();
        }

        BugWithoutImage bugWithoutImage= new BugWithoutImage();
            bugWithoutImage.setBugId(bug.getBugId());
            bugWithoutImage.setBugNote(bug.getBugNote());
            bugWithoutImage.setBugSolved(bug.isBugSolved());
            bugWithoutImage.setStaffAssigned(bug.isStaffAssigned());
            bugWithoutImage.setSeverity(bug.getSeverity());
            bugWithoutImage.setProjectId(bug.getProjectId());
            bugWithoutImage.setImageName(bug.getImageName());
            bugWithoutImage.setStaffId(bug.getStaffId());
            bugWithoutImage.setStatus(bug.getStatus());

        return bugWithoutImage;
    }


    public List<BugWithoutImage> getAllBugs() {
        List<BugWithoutImage> bugList= new ArrayList<>();
        List<Bug> bugs= bugRepository.findAll();

        for(Bug bug: bugs) {
            BugWithoutImage bugWithoutImage= new BugWithoutImage();
                bugWithoutImage.setBugId(bug.getBugId());
                bugWithoutImage.setBugNote(bug.getBugNote());
                bugWithoutImage.setBugSolved(bug.isBugSolved());
                bugWithoutImage.setStaffAssigned(bug.isStaffAssigned());
                bugWithoutImage.setSeverity(bug.getSeverity());
                bugWithoutImage.setProjectId(bug.getProjectId());
                bugWithoutImage.setImageName(bug.getImageName());
                bugWithoutImage.setStaffId(bug.getStaffId());
                bugWithoutImage.setStatus(bug.getStatus());

                bugList.add(bugWithoutImage);
        }
        return bugList;
    }

    public Boolean tagStaff(Long bugId, Long staffId) {

        Bug bug= bugRepository.findByBugId(bugId);

        User user= new RestTemplate()
                .getForObject("http://localhost:9002/users/getStaffById/"+ staffId, User.class);

        if(bug== null) {
            throw new BugNotAddedException();
        }else if(bug.isBugSolved() || bug.isStaffAssigned()) {
            throw new BugNotAddedException();
        }

        bug.setStaffAssigned(true);
        bug.setStatus("assigned");
        bug.setStaffId(staffId);

        bugRepository.saveAndFlush(bug);

        return true;
    }

    public Boolean solveBug(Long bugId) {
        Bug bug= bugRepository.findByBugId(bugId);

        if(bug== null) {
            throw new BugNotAddedException();
        }else if(!bug.isStaffAssigned() && !bug.getStatus().equals("assigned")) {
            throw new BugNotAddedException();
        }else if(bug.isBugSolved()) {
            throw new BugNotAddedException();
        }else {
            bug.setBugSolved(true);
            bugRepository.save(bug);

            return true;
        }

    }

    public Boolean transferBug(Long bugId, Long yourStaffId, Long staffId) {

        Bug bug= bugRepository.findByBugId(bugId);

        if(bug== null) {
            throw new BugNotAddedException();
        }else if(!bug.isStaffAssigned() && !bug.getStatus().equals("assigned")) {
            throw new BugNotAddedException();
        }else if(bug.isBugSolved()) {
            throw new BugNotAddedException();
        }else if(bug.getStaffId()!= yourStaffId) {
            throw new BugNotAddedException();
        }

        try {
            User user = new RestTemplate()
                    .getForObject("http://localhost:9002/users/getStaffById/" + staffId, User.class);
        }catch(Exception e) {
            e.printStackTrace();
        }
        bug.setStaffId(staffId);

        bugRepository.saveAndFlush(bug);

        return true;
    }
}
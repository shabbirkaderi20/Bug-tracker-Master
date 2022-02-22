package com.bug.controller;

import com.bug.dto.BugWithoutImage;
import com.bug.entity.Bug;
import com.bug.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/bugs")
public class BugController {

    @Autowired
    BugService bugService;

    @PostMapping("/addBug")
    @ResponseBody
    public Bug saveBug(@RequestParam String bugDto, @RequestParam("img") MultipartFile file){

        return bugService.addBugs(bugDto, file);
    }

    @GetMapping("/bugImageById/{id}")
    public ResponseEntity<byte[]> fromDatabaseAsResEntity(@PathVariable("id") Long bugId) throws SQLException, IOException {

        return bugService.getImageByBugId(bugId);
    }

    @GetMapping("/bugById/{id}")
    public BugWithoutImage getBugWithImage(@PathVariable("id") Long bugId) {
        return bugService.getBugImageByBugId(bugId);
    }

    @GetMapping("/")
    public List<BugWithoutImage> getAllBugs() {
        return bugService.getAllBugs();
    }

    @GetMapping("/tagStaff/{bId}/{sId}")
    public Boolean tagStaff(@PathVariable("bId") Long bugId, @PathVariable("sId") Long staffId) {
        return bugService.tagStaff(bugId, staffId);
    }

    @GetMapping("/solveBug/{id}")
    public Boolean solveBug(@PathVariable("id") Long bugId) {
        return bugService.solveBug(bugId);
    }

    @GetMapping("/transferBug/{bId}/{ySId}/{sId}")
    public Boolean solveBug(@PathVariable("bId") Long bugId,
                            @PathVariable("ySId") Long yourStaffId, @PathVariable("sId") Long staffId) {
        return bugService.transferBug(bugId, yourStaffId, staffId);
    }

}

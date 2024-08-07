package swp.internmanagement.internmanagement.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import swp.internmanagement.internmanagement.entity.JobApplication;
import swp.internmanagement.internmanagement.payload.request.PostJobApplicationRequest;
import swp.internmanagement.internmanagement.payload.request.UpdateInternDetailRequest;
import swp.internmanagement.internmanagement.payload.response.JobApplicationResponse;
import swp.internmanagement.internmanagement.service.EmailService;
import swp.internmanagement.internmanagement.service.InternDetailService;
import swp.internmanagement.internmanagement.service.InterviewScheduleService;
import swp.internmanagement.internmanagement.service.JobApplicationService;
import swp.internmanagement.internmanagement.service.JobService;
import swp.internmanagement.internmanagement.service.UserAccountService;


@RestController
@RequestMapping("/internbridge/manager")
@CrossOrigin(origins = "http://localhost:3000" , allowCredentials = "true",allowedHeaders = "*")
public class ManagerController {

    @Autowired
    private JobApplicationService jobApplicationService;

     @Autowired
    private InternDetailService internDetailService;

    @Autowired
    private JobService jobService;

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private InterviewScheduleService interviewScheduleService;

    @PostMapping("/postjob")
    public ResponseEntity<?>  PostRecruitment(
        @RequestParam("field_id") int field_id,
        @RequestParam("company_id") int company_id,
        @RequestParam("job_name") String job_name,
        @RequestParam("job_description") String job_description
    ) {
        PostJobApplicationRequest postJobApplicationRequest=new PostJobApplicationRequest(field_id,company_id,job_name,job_description);
        boolean result = jobApplicationService.postJobApplication(postJobApplicationRequest);
        if (result) {
            return ResponseEntity.ok("Post job submitted successfully.");
        } else {
            return ResponseEntity.status(500).body("Failed to post job.");
        }
    }

    @GetMapping("/downLoadCV")
    public ResponseEntity<byte[]> downLoadCV(@RequestParam ("id") Integer id) {
        Optional<JobApplication> jobApplicationOptional = jobApplicationService.getJobApplicationById(id);
        if(jobApplicationOptional.isPresent()){
            JobApplication jobApplication = jobApplicationOptional.get();
            byte[] cv=jobApplication.getCV();

            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + jobApplication.getFullName() + "_CV.pdf");
            headers.set(HttpHeaders.CONTENT_TYPE, "application/pdf");
            return new ResponseEntity<>(cv, headers, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/jobApplication")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<JobApplicationResponse> getJobApplication
    (
        @RequestParam("companyid") int companyid,
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
        @RequestParam(value = "pageSize", defaultValue = "0", required = false) int pageSize
    ){
        
        return ResponseEntity.ok(jobApplicationService.getAllJobApplication(pageNo, pageSize, companyid));
    }

    @PutMapping("/jobApplication/id={id}&status={status}&userId={userId}")
    public ResponseEntity<?> update(@PathVariable Integer id, @PathVariable Integer status,@PathVariable Integer userId){
        try {
            return ResponseEntity.ok(jobApplicationService.updateJobApplication(id,status,userId));
        } catch (Exception e) {
            e.printStackTrace();
                return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/user/intern/{companyId}&{managerId}")
    public ResponseEntity<?> getAllIntern(@PathVariable Integer companyId, @PathVariable Integer managerId){
        try{
            userAccountService.checkValidId(companyId, managerId);
            return ResponseEntity.ok(userAccountService.getAllIntern(companyId));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    
    @PutMapping("/intern/internDetail/update/{interId}")
    public ResponseEntity<?> updateInternDetail(@RequestBody UpdateInternDetailRequest updateInternDetailRequest, @PathVariable Integer interId){
        return ResponseEntity.ok(internDetailService.updateInternDetail(updateInternDetailRequest,interId));
    }
   
    @GetMapping("/viewJob")
    public ResponseEntity<?> getAllJob(
        @RequestParam("companyid") int companyId,
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
        @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ) {
        return ResponseEntity.ok(jobService.getAllJobsByCompanyId(companyId, pageNo, pageSize));
    }

    @PutMapping("/updateJob")
    public ResponseEntity<?> updateJob(
        @RequestParam("job_id") Integer job_id,
        @RequestParam("job_discription") String job_discription
    ) {
        try {
            if(job_id!=null && job_discription!=null){
                boolean check=jobService.updateJob(job_id, job_discription);
                if(check){
                    return ResponseEntity.ok("Update job submitted successfully.");
                }else{
                 throw new Exception();
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to Update job.");
        }
        return ResponseEntity.status(500).body("Failed to Update job.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteJob(
        @RequestParam ("job_id") Integer job_id
    ){
        try {
            if(job_id!=null){
                boolean check=jobService.deleteJob(job_id);
                if(check){
                    return ResponseEntity.ok("Delete Successfully");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete job.");
        }
        return ResponseEntity.status(500).body("Failed to delete job.");
    }

    @GetMapping("/viewActivity")
    public ResponseEntity<?> getAllActivity(
        @RequestParam("companyid") int companyId,
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
        @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize
    ) {
        return ResponseEntity.ok(jobService.getAllJobsByCompanyId(companyId, pageNo, pageSize));
    }

    @GetMapping("/viewSchedule")
    public ResponseEntity<?> getSchedule(
        @RequestParam("companyid") Integer companyId
    ) {
        
        return ResponseEntity.ok(interviewScheduleService.getAllScheduleOfManager(companyId));
    }

    @GetMapping("/intern/internDetail/{companyId}")
    public ResponseEntity<?> getAllInternDetail(
            @PathVariable Integer companyId,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize){
        try{
            return ResponseEntity.ok(internDetailService.listInternDetail(companyId, pageNo, pageSize));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to list intern detail.");
        }
    }


    //search
    //send managerId and companyId
    //role is optional
    @GetMapping("/search/{managerId}&{companyId}")
    public ResponseEntity<?> search(@PathVariable Integer companyId, @PathVariable Integer managerId,
                                    @RequestParam(required = false) String role,
                                    @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize){
        try {
            return ResponseEntity.ok(userAccountService.searchByManager(companyId, managerId, role, pageNo, pageSize));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to list intern detail.");
        }
    }

    @GetMapping("/report/{companyId}&{managerId}")
    public ResponseEntity<?> getAllReport(@PathVariable Integer companyId, @PathVariable Integer managerId){
        try{
            return ResponseEntity.ok(userAccountService.getListInternResult(companyId, managerId));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


    @PostMapping("/sendCetificate")
    public ResponseEntity<?> sendCertificate(
        @RequestParam("pdf") MultipartFile pdFile,
        @RequestParam("email") String email
    ){
        try {
            byte[] pdfData = pdFile.getBytes();
            emailService.sendCertificate(email,pdfData);
            System.out.println("success");
            return ResponseEntity.ok("Sucess");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

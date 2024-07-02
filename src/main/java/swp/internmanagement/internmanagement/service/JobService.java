package swp.internmanagement.internmanagement.service;

import swp.internmanagement.internmanagement.entity.Job;
import swp.internmanagement.internmanagement.payload.response.GetAllJobRes;
import swp.internmanagement.internmanagement.payload.response.GetAllJobsResponse;
import swp.internmanagement.internmanagement.payload.response.SearchJobsResponse;
import swp.internmanagement.internmanagement.payload.response.jobRes;

public interface JobService {

    GetAllJobRes getAllJobs(int pageNo, int pageSize);

    SearchJobsResponse getJobs(String jobName, int pageNo, int pageSize);

    Job getJobId(Integer id);

    Boolean deleteJob(Integer jobId);

    GetAllJobsResponse getAllJobsByCompanyId(Integer companyId,int pageNo, int pageSize);

    boolean updateJob(Integer id, String discription);
}

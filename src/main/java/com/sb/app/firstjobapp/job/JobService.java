package com.sb.app.firstjobapp.job;

import java.util.List;

public interface JobService {

    List<Job> findAll();
    void createJob(Job job);

    Job getJobById(Long id);

    boolean deleteJobById(Long id);

    boolean updateJob(Long id, Job updatedJob);

    Long findJobCountBy(Long companyId);
}

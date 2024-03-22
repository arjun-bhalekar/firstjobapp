package com.sb.app.firstjobapp.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


/**
 * GET /jobs : Get all jobs <br/>
 * GET /jobs/{id} : Get a Specific job by ID <br/>
 * POST /jobs : Create a new Job (Request body will contain job details) <br/>
 * DELETE /job/{id} : Delete a Specific job by ID <br/>
 * PUT /job/{id} : Update a specific job by ID (Request body will contain updated job details) <br/>
 */
@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<List<Job>> findAll() {
        return ResponseEntity.ok(jobService.findAll());
    }

    @PostMapping
    public ResponseEntity<String> createJob(@RequestBody Job job) {
        jobService.createJob(job);
        return ResponseEntity.ok("Job added successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        //what if job not found for given id => should return 404 as response
        //will go for ResponseEntity for the same
        Job job = jobService.getJobById(id);
        if (Objects.nonNull(job)) {
            return new ResponseEntity<>(job, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobById(@PathVariable Long id) {
        if (jobService.deleteJobById(id))
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    //@PutMapping("/jobs/{id}")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateJobById(@PathVariable Long id, @RequestBody Job job) {
        if (jobService.updateJob(id, job))
            return ResponseEntity.ok("Job updated successfully");
        return ResponseEntity.notFound().build();
    }
}

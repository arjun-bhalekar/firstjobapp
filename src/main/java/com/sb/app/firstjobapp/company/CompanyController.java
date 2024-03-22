package com.sb.app.firstjobapp.company;

import com.sb.app.firstjobapp.job.Job;
import com.sb.app.firstjobapp.job.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private CompanyService companyService;

    private JobService jobService;

    public CompanyController(CompanyService companyService, JobService jobService) {
        this.companyService = companyService;
        this.jobService = jobService;
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @PostMapping
    public ResponseEntity<String> createJob(@RequestBody Company company) {
        companyService.createCompany(company);
        return ResponseEntity.ok("Company added successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        if (Objects.nonNull(company)) {
            return new ResponseEntity<>(company, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        if (companyService.updateCompany(id, company))
            return ResponseEntity.ok("Company updated successfully");
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompanyById(@PathVariable Long id) {

        Company company = companyService.getCompanyById(id);
        if (Objects.nonNull(company)) {
            Long count = jobService.findJobCountBy(company.getId());
            if (count == 0) {
                companyService.deleteCompanyById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok("Can not be delete this Company as Jobs are present against this company");

            }

        } else {
            return ResponseEntity.notFound().build();
        }

    }


}

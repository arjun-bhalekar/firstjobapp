package com.sb.app.firstjobapp.job;

import com.sb.app.firstjobapp.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {

    Long countByCompanyId(Long companyId);

}

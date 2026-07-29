package com.complaint.management.repository;

import com.complaint.management.model.Complaint;
import com.complaint.management.model.ComplaintStatus;
import com.complaint.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByCustomer(User customer);

    List<Complaint> findByCustomerId(Long customerId);

    List<Complaint> findByAssignedAgent(User agent);

    List<Complaint> findByStatus(ComplaintStatus status);

    List<Complaint> findByCustomerAndStatus(User customer, ComplaintStatus status);

    List<Complaint> findByAssignedAgentIsNull();
}
package com.complaint.management.service;

import com.complaint.management.model.Complaint;
import com.complaint.management.model.ComplaintStatus;
import com.complaint.management.model.Role;
import com.complaint.management.model.User;
import com.complaint.management.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserService userService;

    public Complaint createComplaint(Complaint complaint) {
        complaint.setStatus(ComplaintStatus.OPEN);
        return complaintRepository.save(complaint);
    }

    public Optional<Complaint> findById(Long id) {
        return complaintRepository.findById(id);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    public List<Complaint> getComplaintsByCustomer(Long customerId) {
        return complaintRepository.findByCustomerId(customerId);
    }

    public List<Complaint> getComplaintsByAgent(User agent) {
        return complaintRepository.findByAssignedAgent(agent);
    }

    public List<Complaint> getComplaintsByStatus(ComplaintStatus status) {
        return complaintRepository.findByStatus(status);
    }

    public List<Complaint> getUnassignedComplaints() {
        return complaintRepository.findByAssignedAgentIsNull();
    }

    public Complaint assignComplaint(Long complaintId, Long agentId) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(complaintId);
        Optional<User> agentOpt = userService.findById(agentId);

        if (complaintOpt.isPresent() && agentOpt.isPresent()) {
            Complaint complaint = complaintOpt.get();
            User agent = agentOpt.get();

            if (!agent.getRole().equals(Role.SUPPORT_AGENT)) {
                throw new RuntimeException("Only Support Agents can be assigned!");
            }

            complaint.setAssignedAgent(agent);
            complaint.setStatus(ComplaintStatus.IN_PROGRESS);
            return complaintRepository.save(complaint);
        }

        throw new RuntimeException("Complaint or Agent not found!");
    }

    public Complaint updateComplaintStatus(Long complaintId, ComplaintStatus status, String remarks) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(complaintId);

        if (complaintOpt.isPresent()) {
            Complaint complaint = complaintOpt.get();
            complaint.setStatus(status);

            if (remarks != null && !remarks.trim().isEmpty()) {
                complaint.setRemarks(remarks);
            }

            return complaintRepository.save(complaint);
        }

        throw new RuntimeException("Complaint not found!");
    }

    public Complaint closeComplaint(Long complaintId, String remarks) {
        return updateComplaintStatus(complaintId, ComplaintStatus.CLOSED, remarks);
    }

    public Complaint resolveComplaint(Long complaintId, String remarks) {
        return updateComplaintStatus(complaintId, ComplaintStatus.RESOLVED, remarks);
    }
    public void deleteComplaint(Long id) {
        if (complaintRepository.existsById(id)) {
            complaintRepository.deleteById(id);
        } else {
            throw new RuntimeException("Complaint not found with id: " + id);
        }
    }
}
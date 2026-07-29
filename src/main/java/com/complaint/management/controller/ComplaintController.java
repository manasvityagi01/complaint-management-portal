package com.complaint.management.controller;

import com.complaint.management.dto.ApiResponse;
import com.complaint.management.dto.ComplaintRequest;
import com.complaint.management.model.Complaint;
import com.complaint.management.model.ComplaintStatus;
import com.complaint.management.model.User;
import com.complaint.management.service.ComplaintService;
import com.complaint.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private UserService userService;

    // Create new complaint (Customer)
    @PostMapping
    public ResponseEntity<ApiResponse<Complaint>> createComplaint(@RequestBody ComplaintRequest request) {
        Optional<User> customer = userService.findById(request.getCustomerId());

        if (customer.isPresent()) {
            Complaint complaint = new Complaint();
            complaint.setTitle(request.getTitle());
            complaint.setDescription(request.getDescription());
            complaint.setCustomer(customer.get());

            Complaint saved = complaintService.createComplaint(complaint);
            return ResponseEntity.ok(ApiResponse.success("Complaint created successfully!", saved));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("Customer not found!"));
        }
    }

    // Get all complaints (Admin view)
    @GetMapping
    public ResponseEntity<ApiResponse<List<Complaint>>> getAllComplaints() {
        List<Complaint> complaints = complaintService.getAllComplaints();
        return ResponseEntity.ok(ApiResponse.success(complaints));
    }

    // Get complaint by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Complaint>> getComplaintById(@PathVariable Long id) {
        Optional<Complaint> complaint = complaintService.findById(id);

        if (complaint.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(complaint.get()));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("Complaint not found!"));
        }
    }

    // Get complaints by customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<Complaint>>> getCustomerComplaints(@PathVariable Long customerId) {
        List<Complaint> complaints = complaintService.getComplaintsByCustomer(customerId);
        return ResponseEntity.ok(ApiResponse.success(complaints));
    }

    // Get complaints by agent
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<ApiResponse<List<Complaint>>> getAgentComplaints(@PathVariable Long agentId) {
        Optional<User> agent = userService.findById(agentId);

        if (agent.isPresent()) {
            List<Complaint> complaints = complaintService.getComplaintsByAgent(agent.get());
            return ResponseEntity.ok(ApiResponse.success(complaints));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("Agent not found!"));
        }
    }

    // Get complaints by status
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Complaint>>> getByStatus(@PathVariable ComplaintStatus status) {
        List<Complaint> complaints = complaintService.getComplaintsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(complaints));
    }
}
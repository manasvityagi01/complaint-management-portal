package com.complaint.management.controller;

import com.complaint.management.dto.ApiResponse;
import com.complaint.management.model.Complaint;
import com.complaint.management.model.ComplaintStatus;
import com.complaint.management.service.ComplaintService;
import com.complaint.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private UserService userService;

    // Assign complaint to agent
    @PutMapping("/complaints/{complaintId}/assign/{agentId}")
    public ResponseEntity<ApiResponse<Complaint>> assignComplaint(
            @PathVariable Long complaintId,
            @PathVariable Long agentId) {
        try {
            Complaint complaint = complaintService.assignComplaint(complaintId, agentId);
            return ResponseEntity.ok(ApiResponse.success("Complaint assigned successfully!", complaint));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Update complaint status
    @PutMapping("/complaints/{complaintId}/status")
    public ResponseEntity<ApiResponse<Complaint>> updateStatus(
            @PathVariable Long complaintId,
            @RequestParam ComplaintStatus status,
            @RequestParam(required = false) String remarks) {
        try {
            Complaint complaint = complaintService.updateComplaintStatus(complaintId, status, remarks);
            return ResponseEntity.ok(ApiResponse.success("Status updated successfully!", complaint));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Resolve complaint
    @PutMapping("/complaints/{complaintId}/resolve")
    public ResponseEntity<ApiResponse<Complaint>> resolveComplaint(
            @PathVariable Long complaintId,
            @RequestParam(required = false) String remarks) {
        try {
            Complaint complaint = complaintService.resolveComplaint(complaintId, remarks);
            return ResponseEntity.ok(ApiResponse.success("Complaint resolved!", complaint));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    // Close complaint
    @PutMapping("/complaints/{complaintId}/close")
    public ResponseEntity<ApiResponse<Complaint>> closeComplaint(
            @PathVariable Long complaintId,
            @RequestParam(required = false) String remarks) {
        try {
            Complaint complaint = complaintService.closeComplaint(complaintId, remarks);
            return ResponseEntity.ok(ApiResponse.success("Complaint closed!", complaint));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }

    }
    @DeleteMapping("/complaints/{complaintId}")
    public ResponseEntity<ApiResponse<String>> deleteComplaint(@PathVariable Long complaintId) {
        try {
            complaintService.deleteComplaint(complaintId);
            return ResponseEntity.ok(ApiResponse.success("Complaint deleted successfully!", null));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
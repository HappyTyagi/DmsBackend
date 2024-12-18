package com.dmsBackend.controller;

import com.dmsBackend.entity.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import com.dmsBackend.exception.ResourceConflictException;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.repository.CategoryMasterRepository;
import com.dmsBackend.response.DocumentSaveRequest;
import com.dmsBackend.response.SearchCriteria;
import com.dmsBackend.service.BranchMasterService;
import com.dmsBackend.service.DocumentDetailsService;
import com.dmsBackend.service.DocumentHeaderService;
import com.dmsBackend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.nio.file.Path;
import java.nio.file.Paths;

//@CrossOrigin(origins = "https://happytyagi.github.io/DmsFrontend/")
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentHeaderService documentHeaderService;

    @Autowired
    private DocumentDetailsService documentDetailsService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CategoryMasterRepository categoryMasterRepository;
    @Autowired
    private BranchMasterService branchMasterService;

    // ================== DocumentHeader Operations ================== //

    // Create a new DocumentHeader
//    @PostMapping("/save")
//    public ResponseEntity<?> saveDocumentHeader(
//            @RequestBody DocumentHeader documentHeader,
//            @RequestParam("filePaths") List<String> filePaths) {
//        try {
//            // Save the document header
//            DocumentHeader savedDocumentHeader = documentHeaderService.saveDocumentHeader(documentHeader);
//
//            // Associate and save file details
//            documentDetailsService.saveFileDetails(savedDocumentHeader, filePaths);
//
//            return ResponseEntity.ok("Document saved successfully with files");
//        } catch (ResourceConflictException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the document");
//        }
//    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDocumentWithFiles(
            @RequestBody DocumentSaveRequest documentSaveRequest) { // Expect the full payload as a single object
        try {
            // Extract DocumentHeader and file paths from the request
            DocumentHeader documentHeader = documentSaveRequest.getDocumentHeader();
            List<String> filePaths = documentSaveRequest.getFilePaths();

            // Save DocumentHeader
            DocumentHeader savedDocumentHeader = documentHeaderService.saveDocumentHeader(documentHeader);

            // Save file details associated with the saved document header
            documentDetailsService.saveFileDetails(savedDocumentHeader, filePaths);

            return ResponseEntity.ok("Document and files saved successfully");

        } catch (ResourceConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving document and files");
        }
    }



    @PostMapping("/update")
    public ResponseEntity<?> updateDocumentWithFiles(
            @RequestBody DocumentSaveRequest documentSaveRequest) {
        System.out.println("Received document: " + documentSaveRequest.getDocumentHeader());
        System.out.println("Received file paths: " + documentSaveRequest.getFilePaths());
        try {
            // Extract DocumentHeader and file paths from the request
            DocumentHeader documentHeader = documentSaveRequest.getDocumentHeader();
            List<String> filePaths = documentSaveRequest.getFilePaths();

            // Update DocumentHeader
            DocumentHeader updatedDocumentHeader = documentHeaderService.updateDocumentHeader(documentHeader);

            // Update file details associated with the updated document header
            documentDetailsService.updateFileDetails(updatedDocumentHeader, filePaths);

            return ResponseEntity.ok("Document and files updated successfully");

        } catch (ResourceConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating document and files");
        }
    }

    // Update an existing DocumentHeader
//    @PutMapping("/update/{id}")
//    public ResponseEntity<?> updateDocumentHeader(@PathVariable Integer id,
//                                                  @RequestBody DocumentHeader updatedDocument) {
//        try {
//            // Log incoming request for debugging
//            System.out.println("Updating DocumentHeader with ID: " + id);
//            System.out.println("Incoming DocumentHeader: " + updatedDocument);
//
//            DocumentHeader documentHeader = documentHeaderService.updateDocumentHeader(id, updatedDocument);
//            return ResponseEntity.ok(documentHeader);
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Return 404 if not found
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the document."); // Handle other exceptions
//        }
//    }


    //get all enum
    @GetMapping("/docApprovalStatuses")
    public List<Map<String, String>> getAllDocApprovalStatuses() {
        return Arrays.stream(DocApprovalStatus.values())
                .map(status -> Map.of(status.name(), ""))
                .collect(Collectors.toList());
    }

    // Find a document by ID
    @GetMapping("/{id}")
    public ResponseEntity<DocumentHeader> getDocumentHeaderById(@PathVariable Integer id) {
        DocumentHeader documentHeader = documentHeaderService.findDocumentHeaderById(id);
        return new ResponseEntity<>(documentHeader, HttpStatus.OK);
    }

    // Find all documents
    @GetMapping
    public ResponseEntity<List<DocumentHeader>> getAllDocumentHeaders() {
        List<DocumentHeader> documentHeaders = documentHeaderService.findAllDocumentHeaders();
        return new ResponseEntity<>(documentHeaders, HttpStatus.OK);
    }

    // Delete a document by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentHeader(@PathVariable Integer id) {
        documentHeaderService.deleteByIdDocumentHeader(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Update the approval status of a document
    @PatchMapping("/{id}/approval-status")
    public ResponseEntity<DocumentHeader> updateApprovalStatus(
            @PathVariable Integer id,
            @RequestParam("status") DocApprovalStatus status,
            @RequestParam(value = "rejectionReason", required = false) String rejectionReason,
            @RequestHeader("employeeId") Integer employeeId) {  // Extract employeeId from header

        // Call the service method to update approval status
        DocumentHeader updatedDocument = documentHeaderService.updateApprovalStatus(id, status, rejectionReason, employeeId);

        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }


    // Update the active status of a document
    @PatchMapping("/{id}/active-status")
    public ResponseEntity<DocumentHeader> updateActiveStatus(
            @PathVariable Integer id, @RequestParam("isActive") boolean isActive) {
        DocumentHeader updatedDocument = documentHeaderService.updateActiveStatus(id, isActive);
        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }

    // Get all approved documents
    @GetMapping("/approved")
    public ResponseEntity<List<DocumentHeader>> getAllApproved() {
        List<DocumentHeader> approvedDocuments = documentHeaderService.getAllApproved();
        return new ResponseEntity<>(approvedDocuments, HttpStatus.OK);
    }

    // Get all rejected documents
    @GetMapping("/rejected")
    public ResponseEntity<List<DocumentHeader>> getAllRejected() {
        List<DocumentHeader> rejectedDocuments = documentHeaderService.getAllRejected();
        return new ResponseEntity<>(rejectedDocuments, HttpStatus.OK);
    }

    // Get all pending documents
    @GetMapping("/pending")
    public ResponseEntity<List<DocumentHeader>> getAllPending() {
        List<DocumentHeader> pendingDocuments = documentHeaderService.getAllPending();
        return new ResponseEntity<>(pendingDocuments, HttpStatus.OK);
    }

    // Get all approved documents for a specific employee
    @GetMapping("/approved/employee/{employeeId}")
    public ResponseEntity<List<DocumentHeader>> getAllApprovedByEmployeeId(@PathVariable Integer employeeId) {
        List<DocumentHeader> approvedDocuments = documentHeaderService.getAllApprovedByEmployeeId(employeeId);
        return new ResponseEntity<>(approvedDocuments, HttpStatus.OK);
    }

    // Get all rejected documents for a specific employee
    @GetMapping("/rejected/employee/{employeeId}")
    public ResponseEntity<List<DocumentHeader>> getAllRejectedByEmployeeId(@PathVariable Integer employeeId) {
        List<DocumentHeader> rejectedDocuments = documentHeaderService.getAllRejectedByEmployeeId(employeeId);
        return new ResponseEntity<>(rejectedDocuments, HttpStatus.OK);
    }

    // Get all pending documents for a specific employee
    @GetMapping("/pending/employee/{employeeId}")
    public ResponseEntity<List<DocumentHeader>> getAllPendingByEmployeeId(@PathVariable Integer employeeId) {
        List<DocumentHeader> pendingDocuments = documentHeaderService.getAllPendingByEmployeeId(employeeId);
        return new ResponseEntity<>(pendingDocuments, HttpStatus.OK);
    }

    // Get all documents for a specific employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<DocumentHeader>> getAllDocumentHeadersByEmployeeId(@PathVariable Integer employeeId) {
        List<DocumentHeader> employeeDocuments = documentHeaderService.findAllDocumentHeadersByEmployeeId(employeeId);
        return new ResponseEntity<>(employeeDocuments, HttpStatus.OK);
    }

    // Get all rejected documents updated by an employee
    @GetMapping("/rejectedByEmp")
    public ResponseEntity<List<DocumentHeader>> getRejectedByEmployee(@RequestHeader("employeeId") Integer employeeId) {
        List<DocumentHeader> rejectedDocuments = documentHeaderService.findAllRejectedByActionEmployeeId(employeeId);
        return new ResponseEntity<>(rejectedDocuments, HttpStatus.OK);
    }

    // Get all approved documents updated by an employee
    @GetMapping("/approvedByEmp")
    public ResponseEntity<List<DocumentHeader>> getApprovedByEmployee(@RequestHeader("employeeId") Integer employeeId) {
        List<DocumentHeader> approvedDocuments = documentHeaderService.findAllApprovedByActionEmployeeId(employeeId);
        return new ResponseEntity<>(approvedDocuments, HttpStatus.OK);
    }


    @GetMapping("/byDocumentHeader/{id}")
    public ResponseEntity<List<DocumentDetails>> getDocumentsByHeader(@PathVariable Long id) {
        List<DocumentDetails> documents = documentDetailsService.findDocumentsByHeaderId(id);
        return ResponseEntity.ok(documents);
    }


    //for graph
    @GetMapping("/documents-summary/{employeeId}")
    public ResponseEntity<Map<String, Object>> getDocumentsSummaryByEmployeeId(
            @PathVariable Integer employeeId,
            @RequestParam("startDate") Timestamp startDate,
            @RequestParam("endDate") Timestamp endDate) {
        Map<String, Object> result = documentHeaderService.countAllDocumentsByIdWithMonth(employeeId, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/document/summary/by/{employeeId}")
    public ResponseEntity<Map<String, Object>> getDocumentSummaryByEmployee(@PathVariable Integer employeeId) {
        Map<String, Object> summary = documentHeaderService.getApprovalSummaryByEmployeeId(employeeId);
        return ResponseEntity.ok(summary);
    }


    // ================== DocumentDetails (File Upload) Operations ================== //

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("category") String category) {  // Add category parameter back
        try {
            // Call the service method to upload files with the category
            List<String> filePaths = documentDetailsService.uploadFiles(files, category);
            return ResponseEntity.ok(filePaths);  // Return the file paths
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonList("Error uploading files: " + e.getMessage()));
        }
    }

    @GetMapping("/{year}/{month}/{category}/{fileName}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String year,
            @PathVariable String month,
            @PathVariable String category,
            @PathVariable String fileName) {

        try {
            // Construct the file path
            String filePath = String.format(
                    "D:/Dheeraj_Codes/Backend/Java/Projects/dms/DocumentServer/%s/%s/%s/%s",
                    year, month, category, fileName);

            // Ensure the file path is valid
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Determine the MIME type
                String mimeType = Files.probeContentType(path);

                if (mimeType == null) {
                    // Fallback to generic binary stream if MIME type cannot be determined
                    mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                }

                // Return the file as a resource
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(mimeType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("File not found or not readable: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error retrieving file: " + fileName, e);
        } catch (IOException e) {
            throw new RuntimeException("Error determining MIME type for file: " + fileName, e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while downloading the file: " + fileName, e);
        }
    }

    // You can add more endpoints for retrieving DocumentDetails if needed

    @GetMapping("/pendingByBranch/{branchId}/{departmentId}")
    public ResponseEntity<List<DocumentHeader>> getPendingDocumentsByBranch(
            @PathVariable Integer branchId,
            @PathVariable(required = false) Integer departmentId,  // Allow departmentId to be optional
            @AuthenticationPrincipal UserDetails userDetails) {
        List<DocumentHeader> pendingDocuments = new ArrayList<>(); // Default to empty list

        // Check if the user has the 'admin' role
        if (userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN"))) {
            // Admin can view all pending documents for all branches
            pendingDocuments = documentHeaderService.getAllPendingDocuments();
        } else if (userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("BRANCH ADMIN"))) {
            // Branch admins can view all pending documents for their branch
            pendingDocuments = documentHeaderService.getPendingDocumentsByBranch(branchId);
        } else if (userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("DEPARTMENT ADMIN"))) {
            // Department admins can only view pending documents for their department within their branch
            if (departmentId != null) {
                pendingDocuments = documentHeaderService.getPendingDocumentsByDepartment(departmentId);
            } else {
                // Handle case where departmentId is null, depending on your use case
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
            }
        } else {
            // Other users are unauthorized to view pending documents
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
        }

        return ResponseEntity.ok(pendingDocuments);
    }


    @GetMapping("/pendingByBranch/{branchId}")
    public ResponseEntity<List<DocumentHeader>> getPendingDocumentsByBranchOnly(
            @PathVariable Integer branchId,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<DocumentHeader> pendingDocuments = new ArrayList<>();

        if (userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN"))) {
            pendingDocuments = documentHeaderService.getAllPendingDocuments();
        } else if (userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("BRANCH ADMIN"))) {
            pendingDocuments = documentHeaderService.getPendingDocumentsByBranch(branchId);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
        }

        return ResponseEntity.ok(pendingDocuments);
    }



    @GetMapping("/approvedByBranch/{employeeId}")
    public ResponseEntity<List<DocumentHeader>> getApprovedDocumentsByBranchAdmin(@PathVariable Integer employeeId) {
        // Call the service method to fetch the approved documents for this employee
        List<DocumentHeader> approvedDocuments = documentHeaderService.findApprovedDocumentsByBranchAdmin(employeeId);
        return ResponseEntity.ok(approvedDocuments);
    }


    @PostMapping("/search")
    public List<DocumentHeader> searchDocuments(@RequestBody SearchCriteria searchCriteria) {
        return documentHeaderService.searchDocuments(
                searchCriteria.getFileNo(),
                searchCriteria.getTitle(),
                searchCriteria.getSubject(),
                searchCriteria.getVersion(),
                searchCriteria.getCategoryId(),
                searchCriteria.getBranchId(),
                searchCriteria.getDepartmentId()
        );
    }


//    @GetMapping("/filter")
//    public List<DocumentHeader> getFilteredDocuments(
//            @RequestParam Integer categoryId,
//            @RequestParam DocApprovalStatus approvalStatus,
//            @RequestParam(required = false) Timestamp createdOn, // Optional
//            @RequestParam Integer branchId,
//            @RequestParam Integer departmentId) {
//        return documentHeaderService.getFilteredDocuments(categoryId, approvalStatus, createdOn, branchId, departmentId);
//    }


//    @GetMapping("/filter")
//    public List<DocumentHeader> getFilteredDocuments(
//            @RequestParam Integer categoryId,
//            @RequestParam DocApprovalStatus approvalStatus,
//            @RequestParam(required = false) Timestamp startDate, // Optional
//            @RequestParam(required = false) Timestamp endDate, // Optional
//            @RequestParam Integer branchId,
//            @RequestParam Integer departmentId) {
//        return documentHeaderService.getFilteredDocuments(categoryId, approvalStatus, startDate, endDate, branchId, departmentId);
//    }

    @GetMapping("/filter")
    public List<DocumentHeader> getFilteredDocuments(
            @RequestParam Integer categoryId,
            @RequestParam DocApprovalStatus approvalStatus,
            @RequestParam(required = false) Timestamp startDate, // Optional
            @RequestParam(required = false) Timestamp endDate, // Optional
            @RequestParam Integer branchId,
            @RequestParam Integer departmentId) {
        return documentHeaderService.getFilteredDocuments(categoryId, approvalStatus, startDate, endDate, branchId, departmentId);
    }



}
package com.dmsBackend.service;

import com.dmsBackend.entity.DocumentHeader;
import com.dmsBackend.entity.DocApprovalStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface DocumentHeaderService {

    DocumentHeader saveDocumentHeader(DocumentHeader documentHeader);

//    DocumentHeader updateDocumentHeader(Integer id, DocumentHeader updatedDocument);

    DocumentHeader findDocumentHeaderById(Integer id);

    List<DocumentHeader> findAllDocumentHeaders();

    void deleteByIdDocumentHeader(Integer id);

    DocumentHeader updateApprovalStatus(Integer id, DocApprovalStatus status, String rejectionReason, Integer employeeId);

    DocumentHeader updateDocumentHeader(DocumentHeader documentHeader);

    DocumentHeader updateActiveStatus(Integer id, boolean isActive);

    List<DocumentHeader> getAllApproved();

    List<DocumentHeader> getAllRejected();

    List<DocumentHeader> getAllPending();

    List<DocumentHeader> getAllApprovedByEmployeeId(Integer employeeId);

    List<DocumentHeader> getAllRejectedByEmployeeId(Integer employeeId);

    List<DocumentHeader> getAllPendingByEmployeeId(Integer employeeId);

    List<DocumentHeader> findAllDocumentHeadersByEmployeeId(Integer employeeId);

    //  Admin
    List<DocumentHeader> findAllRejectedByActionEmployeeId(Integer employeeId);

    List<DocumentHeader> findAllApprovedByActionEmployeeId(Integer employeeId);



    //For Count

    long countApprovedDocuments();

    long countRejectedDocuments();

    long countPendingDocuments();

    long countApprovedDocumentsByEmployeeId(Integer employeeId);

    long countRejectedDocumentsByEmployeeId(Integer employeeId);

    long countPendingDocumentsByEmployeeId(Integer employeeId);

    long countDocumentHeadersByEmployeeId(Integer employeeId);

    long countRejectedByActionEmployeeId(Integer employeeId);

    long countApprovedByActionEmployeeId(Integer employeeId);

    //For Graph
    Map<String, Object> countAllDocumentsByIdWithMonth(Integer employeeId, Timestamp startDate, Timestamp endDate);

    Map<String, Object> getApprovalSummaryByEmployeeId(Integer employeeId);


    public List<DocumentHeader> getPendingDocumentsByBranch(Integer branchId);

    //List<DocumentHeader> getApprovedDocumentsByBranchAdmin(Integer userId);

    public List<DocumentHeader> findApprovedDocumentsByBranchAdmin(Integer employeeId);

    List<DocumentHeader> getAllPendingDocuments();

    // List<DocumentHeader> getPendingDocumentsForAllBranches();


//    DocumentHeader getDocumentHeaderById(Integer headerId);

    Long countDocumentHeadersByBranchId(Integer branch);
    Long countPendingDocumentsByBranchId(Integer branch);
    Long countApprovedByBranchId(Integer branch);
    Long countRejectedByBranchId(Integer branch);

    List<DocumentHeader> searchDocuments(String fileNo, String title, String subject, String version, String category);

    List<DocumentHeader> getPendingDocumentsByDepartment(Integer departmentId);
}
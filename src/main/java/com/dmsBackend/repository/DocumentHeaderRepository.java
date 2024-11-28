package com.dmsBackend.repository;

import com.dmsBackend.entity.BranchMaster;
import com.dmsBackend.entity.DepartmentMaster;
import com.dmsBackend.entity.DocApprovalStatus;
import com.dmsBackend.entity.DocumentHeader;
import io.micrometer.common.lang.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentHeaderRepository extends JpaRepository<DocumentHeader, Integer> {

    // Find document by file number
    Optional<DocumentHeader> findByFileNo(String fileNo);

    // Find all documents by approval status
    List<DocumentHeader> findAllByApprovalStatus(DocApprovalStatus approvalStatus);

    List<DocumentHeader> findAllByEmployeeId(Integer employeeId);  // Get all documents for an employee

    // Find all documents by employee ID and approval status
    List<DocumentHeader> findAllByEmployeeIdAndApprovalStatus(Integer employeeId, DocApprovalStatus approvalStatus);

    // Find documents by employee ID, approval status, and a date range
    List<DocumentHeader> findAllByEmployeeIdAndApprovalStatusAndUpdatedOnBetween(
            Integer employeeId, DocApprovalStatus approvalStatus, Timestamp startDate, Timestamp endDate
    );


    // Count documents by approval status
    long countByApprovalStatus(DocApprovalStatus approvalStatus);

    // Count documents by employee ID and approval status
    long countByApprovalStatusAndEmployeeId(DocApprovalStatus approvalStatus, Integer employeeId);

    // Count total documents for a specific employee
    long countByEmployeeId(Integer employeeId);


    // Custom queries to count approved/rejected documents by employee who approved/rejected
    @Query("SELECT COUNT(d) FROM DocumentHeader d WHERE d.employeeBy.id = :employeeId AND d.approvalStatus = :approvalStatus")
    long countByEmployeeByAndApprovalStatus(
            @Param("employeeId") Integer employeeId,
            @Param("approvalStatus") DocApprovalStatus approvalStatus);

    // Find all approved/rejected documents handled by a specific employee
    @Query("SELECT d FROM DocumentHeader d WHERE d.employeeBy.id = :employeeId AND d.approvalStatus = :approvalStatus")
    List<DocumentHeader> findAllByEmployeeByAndApprovalStatus(
            @Param("employeeId") Integer employeeId,
            @Param("approvalStatus") DocApprovalStatus approvalStatus);

    // graph cout

    @Query("SELECT MONTH(d.createdOn), COUNT(d) FROM DocumentHeader d WHERE d.employeeBy.id = :employeeId AND d.approvalStatus = :approvalStatus GROUP BY MONTH(d.createdOn)")
    List<Object[]> countByEmployeeAndApprovalStatusGroupedByMonth(
            @Param("employeeId") Integer employeeId,
            @Param("approvalStatus") DocApprovalStatus approvalStatus);

    @Query("SELECT dh FROM DocumentHeader dh WHERE dh.employee.branch.id = :branchId AND dh.approvalStatus = 'PENDING'")
    List<DocumentHeader> findPendingByBranch(@Param("branchId") Integer branchId);

    List<DocumentHeader> findByEmployeeByIdAndApprovalStatus(Integer employeeId, DocApprovalStatus approvalStatus);

    @Query("SELECT d FROM DocumentHeader d WHERE d.employee.branch = :branch AND d.approvalStatus = :approvalStatus")
    List<DocumentHeader> findAllApprovedInBranch(
            @Param("branch") BranchMaster branch,
            @Param("approvalStatus") DocApprovalStatus approvalStatus);

    @Query("SELECT d FROM DocumentHeader d WHERE d.employee.branch = :branch AND d.approvalStatus = :approvalStatus")
    List<DocumentHeader> findAllRejectedByBranch(
            @Param("branch") BranchMaster branch,
            @Param("approvalStatus") DocApprovalStatus approvalStatus);


    // Find documents by admin's ID and approval status
    // List<DocumentHeader> findByEmployeeByIdAndApprovalStatus(Integer employeeId, DocApprovalStatus approvalStatus);

    Long countByEmployee_BranchId(Integer branch);

    Long countByEmployee_BranchIdAndApprovalStatus(Integer branch, DocApprovalStatus approvalStatus);

    @Query("SELECT d FROM DocumentHeader d WHERE d.employee.department = :department AND d.approvalStatus = :approvalStatus")
    List<DocumentHeader> findAllApprovedInDepartment(
            @Param("department") DepartmentMaster department,
            @Param("approvalStatus") DocApprovalStatus approvalStatus);

    @Query("SELECT d FROM DocumentHeader d WHERE d.employee.department = :department AND d.approvalStatus = :approvalStatus")
    List<DocumentHeader> findAllRejectedByDepartment(
            @Param("department") DepartmentMaster department,
            @Param("approvalStatus") DocApprovalStatus approvalStatus);


    /**
     * Custom query to search for documents based on various criteria.
     *
     * @param fileNo         The file number to search for.
     * @param title          The title to search for.
     * @param subject        The subject to search for.
     * @param version        The version to search for.
     * @param categoryMaster The category ID to search for.
     * @param branch         The branch ID to search for.
     * @param department     The department ID to search for.
     * @return A list of DocumentHeader entities matching the criteria.
     */
    @Query("SELECT d FROM DocumentHeader d " +
            "WHERE (:fileNo IS NULL OR LOWER(d.fileNo) LIKE LOWER(CONCAT('%', :fileNo, '%'))) " +
            "AND (:title IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:subject IS NULL OR LOWER(d.subject) LIKE LOWER(CONCAT('%', :subject, '%'))) " +
            "AND (:version IS NULL OR LOWER(d.version) LIKE LOWER(CONCAT('%', :version, '%'))) " +
            "AND (:categoryMaster IS NULL OR d.categoryMaster.id = :categoryMaster) " +
            "AND (:branch IS NULL OR d.employee.branch.id = :branch) " +
            "AND (:department IS NULL OR d.employee.department.id = :department)")
    List<DocumentHeader> searchDocuments(
            @Param("fileNo") String fileNo,
            @Param("title") String title,
            @Param("subject") String subject,
            @Param("version") String version,
            @Param("categoryMaster") Integer categoryMaster,
            @Param("branch") Integer branch,
            @Param("department") Integer department
    );

    @Query("SELECT dh FROM DocumentHeader dh WHERE dh.employee.department.id = :departmentId AND dh.approvalStatus = 'PENDING'")
    List<DocumentHeader> findPendingByDepartment(@Param("departmentId") Integer departmentId);



//    @Query("""
//       SELECT dh
//       FROM DocumentHeader dh
//       JOIN dh.employee e
//       WHERE dh.categoryMaster.id = :categoryId
//         AND dh.approvalStatus = :approvalStatus
//         AND (COALESCE(:createdOn, dh.createdOn) = dh.createdOn OR dh.createdOn >= :createdOn)
//         AND e.branch.id = :branchId
//         AND e.department.id = :departmentId
//       """)
//    List<DocumentHeader> findDocumentsByFilters(
//            @Param("categoryId") Integer categoryId,
//            @Param("approvalStatus") DocApprovalStatus approvalStatus,
//            @Param("createdOn") Timestamp createdOn,
//            @Param("branchId") Integer branchId,
//            @Param("departmentId") Integer departmentId
//    );

    @Query("""
       SELECT dh
       FROM DocumentHeader dh
       JOIN dh.employee e
       WHERE dh.categoryMaster.id = :categoryId
         AND dh.approvalStatus = :approvalStatus
         AND (dh.createdOn BETWEEN :startDate AND :endDate)
         AND e.branch.id = :branchId
         AND e.department.id = :departmentId
       """)
    List<DocumentHeader> findDocumentsByFilters(
            @Param("categoryId") Integer categoryId,
            @Param("approvalStatus") DocApprovalStatus approvalStatus,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate,
            @Param("branchId") Integer branchId,
            @Param("departmentId") Integer departmentId
    );







}


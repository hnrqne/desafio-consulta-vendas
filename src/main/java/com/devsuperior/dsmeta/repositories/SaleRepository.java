package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(obj.seller.name, SUM(obj.amount)) "
            + "FROM Sale obj "
            + "WHERE obj.date BETWEEN :minDate AND :maxDate "
            + "GROUP BY obj.seller.name")
    List<SaleSummaryDTO> searchSalesSummary(@Param("minDate") LocalDate minDate, @Param("maxDate") LocalDate maxDate);
    
    @Query(value = "SELECT new com.devsuperior.dsmeta.dto.SaleReportDTO(obj.id, obj.date, obj.amount, obj.seller.name) "
    		+ "FROM Sale obj "
    		+ "WHERE obj.date BETWEEN :minDate AND :maxDate "
    		+ "AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :sellerName, '%'))",
    countQuery = "SELECT COUNT(obj) "
    		+ "FROM Sale obj "
    		+ "WHERE obj.date BETWEEN :minDate AND :maxDate "
    		+ "AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :sellerName, '%'))")
    Page<SaleReportDTO> searchSalesReport(@Param("minDate") LocalDate minDate, @Param("maxDate") LocalDate maxDate, @Param("sellerName") String sellerName, Pageable pageable);
    
	/* --------> SQL NATIVO 
    @Query(nativeQuery = true, value = "SELECT tb_seller.name AS sellerName, SUM(tb_sales.amount) AS total "
    		+ "FROM tb_seller "
    		+ "INNER JOIN tb_sales ON tb_sales.seller_id = tb_seller.id "
    		+ "WHERE tb_sales.date BETWEEN :minDate AND :maxDate "
    		+ "GROUP BY tb_seller.name")
    List<SaleSummaryProjection> searchSalesSummary(@Param("minDate") LocalDate minDate, @Param("maxDate") LocalDate maxDate);
    
    @Query(nativeQuery = true, value = "SELECT tb_sales.id, tb_sales.date, tb_sales.amount, tb_seller.name AS sellerName "
    		+ "FROM tb_sales "
    		+ "INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id "
    		+ "WHERE tb_sales.date BETWEEN :minDate  AND :maxDate "
    		+ "AND UPPER(tb_seller.name) LIKE UPPER('%' || :sellerName || '%') ",
    countQuery = "SELECT COUNT(*) "
    		+ "FROM tb_sales "
    		+ "INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id "
    		+ "WHERE tb_sales.date BETWEEN :minDate AND :maxDate "
    		+ "AND UPPER(tb_seller.name) LIKE UPPER('%' || :sellerName || '%')")
    Page<SaleReportProjection> searchSalesReport(@Param("minDate") LocalDate minDate, @Param("maxDate") LocalDate maxDate, @Param("sellerName") String sellerName, Pageable pageable);
    */
}

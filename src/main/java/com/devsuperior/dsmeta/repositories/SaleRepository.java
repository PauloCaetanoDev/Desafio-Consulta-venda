package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projections.SumaryMinProjection;

public interface SaleRepository extends JpaRepository<Sale, Long> {

	// CONSULTA REPORT JPQL SEM O COUNT QUERY (1 TENTATIVA) ***

	// "SELECT s.id, s.date, s.amount, UPPER(s.seller.name) " + "FROM Sale s " +
	// "WHERE s.date >= :dataInicial "
	// + "AND s.date <= :dataFinal "
	// + "AND UPPER(s.seller.name) LIKE CONCAT('%', UPPER(:trechoNomeVendedor), '%')
	// " + "ORDER BY s.date DESC")

	// CONSULTA REPORT SQL PARA FINS DE ESTUDOS, POREM NAO UTIL POIS ATIVIDADE
	// NESCESSITA DE UM RETORNO PAGINADO ***

	// "SELECT tb_sales.id, tb_sales.date, tb_sales.amount, tb_seller.name "
	// +"FROM tb_sales "
	// +"INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id "
	// +"WHERE tb_sales.date BETWEEN :dataInicial AND :dataFinal "
	// +"AND UPPER (tb_seller.name) LIKE UPPER(CONCAT('%', :trechoNomeVendedor,
	// '%')) ",
	// countQuery = "SELECT COUNT(tb_sales.id) "
	// + "FROM tb_sales "
	// + "INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id "
	// + "WHERE tb_sales.date BETWEEN :dataInicial AND :dataFinal "
	// + "AND UPPER (tb_seller.name) LIKE UPPER(CONCAT('%', :trechoNomeVendedor,
	// '%'))"

	@Query(value = "SELECT obj " + "FROM Sale obj " + "JOIN FETCH obj.seller "
			+ "WHERE (obj.date >= :dataInicial AND obj.date <= :dataFinal) "
			+ "AND (UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :trechoNomeVendedor, '%'))) ", countQuery = "SELECT COUNT(obj) "
					+ "FROM Sale obj " + "JOIN obj.seller "
					+ "WHERE (obj.date >= :dataInicial AND obj.date <= :dataFinal) "
					+ "AND (UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :trechoNomeVendedor, '%')))")
	Page<Sale> searchByReport(LocalDate dataInicial, LocalDate dataFinal, String trechoNomeVendedor, Pageable pageable);

	// CONSULTA JPQL SUMMARY PARA FINS DE ESTUDO

	// "SELECT new com.devsuperior.dsmeta.dto.SumaryDTO(obj.seller.name,
	// SUM(obj.amount)) "
	// + "FROM Sale obj "
	// + "WHERE obj.date >= :dataInicial "
	// + "AND obj.date <= :dataFinal "
	// + "GROUP BY obj.seller.name "

	@Query(nativeQuery = true, value = "SELECT s.seller_name AS Name, SUM(s.amount) AS totalVendas " + "FROM ( "
			+ "    SELECT sl.name AS seller_name, sa.amount " + "    FROM tb_sales sa\r\n"
			+ "    INNER JOIN tb_seller sl ON sa.seller_id = sl.id " + "    WHERE "
			+ "        (:dataInicial IS NULL OR sa.date >= :dataInicial) "
			+ "        AND (:dataFinal IS NULL OR sa.date <= :dataFinal) " + ") s " + "GROUP BY s.seller_name")
	List<SumaryMinProjection> searchBySumary(LocalDate dataInicial, LocalDate dataFinal);

}


package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleDTO;
import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	} 

	public Page<SaleDTO> salesReport(String dataInicial, String dataFinal, String trechoNomeVendedor,
			Pageable pageable) {
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

		LocalDate max = dataFinal.equals("") ? today : LocalDate.parse(dataFinal);
		LocalDate min = dataInicial.equals("") ? max.minusYears(1L) : LocalDate.parse(dataInicial);

		Page<Sale> result = repository.searchByReport(min, max, trechoNomeVendedor, pageable);

		return result.map(x -> new SaleDTO(x));

	}
	}

// Primeira Tentativa do relatorio Report , resolvi trabalhar com operadores ternarios.

	// LocalDate initialDate = null;
	// LocalDate finalDate = null;

	// Verificar se as strings de data são nulas ou vazias antes de fazer o parsing
	// if (dataInicial != null && !dataInicial.isEmpty()) {
	// initialDate = LocalDate.parse(dataInicial);
	// }
	// if (dataFinal != null && !dataFinal.isEmpty()) {
	// finalDate = LocalDate.parse(dataFinal);
	// }

	// // Verificar se ambas as datas são nulas
	// (initialDate == null && finalDate == null) {
	// minhaData = LocalDate.now();
	// LocalDate defaultDate = minhaData.minusYears(1L);
	// initialDate = defaultDate;
	// = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
	// } else if (initialDate == null) {
	// Se a data inicial for nula, defina-a para uma data padrão
	// initialDate = finalDate.minusYears(1L);
	// } else if (finalDate == null) {
	// Se a data final for nula, defina-a como a data atual
	// finalDate = LocalDate.now();
	// }

	// Page<Sale> list = repository.searchByReport(initialDate, finalDate,
	// trechoNomeVendedor, pageable);
	// Page<SaleDTO> result = list.map(x-> new SaleDTO(x));
	// return result;
package com.devsuperior.dsmeta.dto;

import com.devsuperior.dsmeta.projections.SumaryMinProjection;

public class SumaryDTO {

	private String name;
	private Double totalVendas;

	public SumaryDTO(SumaryMinProjection projection) {
		name = projection.getName();
		totalVendas = projection.getTotalVendas();
	}

	public SumaryDTO(String name, Double totalVendas) {
		super();
		this.name = name;
		this.totalVendas = totalVendas;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getTotalVendas() {
		return totalVendas;
	}

	public void setTotalVendas(Double totalVendas) {
		this.totalVendas = totalVendas;
	}

}

package com.joaomarcos.chamado_TI.dto;

import com.joaomarcos.chamado_TI.enums.Prioridade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChamadoRequest {
	@NotNull(message = "A prioridade é obrigatória")
	private Prioridade prioridade;
	@NotBlank(message = "A descrição do problema é obrigatória")
	private String problema;
	
	public ChamadoRequest() {}
	
	public ChamadoRequest(Prioridade prioridade,String problema) {
		this.prioridade = prioridade;
		this.problema = problema;
	}
	
	public Prioridade getPrioridade() {
		return prioridade;
	}
	public void setPrioridade(Prioridade prioridade) {
		this.prioridade = prioridade;
	}
	public String getProblema() {
		return problema;
	}
	public void setProblema(String problema) {
		this.problema = problema;
	}
	
	
}

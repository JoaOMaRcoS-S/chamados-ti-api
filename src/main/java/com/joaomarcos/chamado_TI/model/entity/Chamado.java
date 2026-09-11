package com.joaomarcos.chamado_TI.model.entity;


import java.time.LocalDateTime;

import com.joaomarcos.chamado_TI.enums.Prioridade;
import com.joaomarcos.chamado_TI.enums.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="chamados")
public class Chamado {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private Prioridade prioridade;
	@Enumerated(EnumType.STRING)
	private Status status;
	
	private String problema;
	private LocalDateTime dataAbertura;
	private LocalDateTime dataFechamento;
	
	public Chamado() {}
	
	public Chamado(Prioridade prioridade,String problema) {
		this.prioridade = prioridade;
		this.dataAbertura = LocalDateTime.now();
		this.dataFechamento = null;
		this.status = Status.ABERTO;
		this.problema = problema;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Prioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(Prioridade prioridade) {
		this.prioridade = prioridade;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getProblema() {
		return problema;
	}

	public void setProblema(String problema) {
		this.problema = problema;
	}

	public LocalDateTime getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(LocalDateTime dataAbertura) {
		this.dataAbertura = dataAbertura;
	}
	
	public LocalDateTime getDataFechamento() {
		return dataFechamento;
	}
	
	public void setDataFechamento(LocalDateTime dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	@Override
	public String toString() {
		return "Chamado [id=" + id + ", prioridade=" + prioridade + ", status=" + status + ", problema=" + problema
				+ ", dataAbertura=" + dataAbertura + ", dataFechamento"+dataFechamento +"]";
	}
}

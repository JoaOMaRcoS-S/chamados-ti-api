package com.joaomarcos.chamado_TI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joaomarcos.chamado_TI.dto.ChamadoRequest;
import com.joaomarcos.chamado_TI.model.entity.Chamado;
import com.joaomarcos.chamado_TI.service.ChamadoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/chamados")
public class ChamadoController {
	@Autowired
	private ChamadoService service;
	
	@PostMapping
	public Chamado registrarChamado(@Valid @RequestBody ChamadoRequest novo) {
		return service.registrarChamado(novo);
	}
	
	@GetMapping
	public List<Chamado> listarTodos(){
		return service.listarTodos();
	}
	
	@GetMapping("/abertos")
	public List<Chamado> listarAbertos(){
		return service.listarAbertos();
	}
	
	@PutMapping("/{id}/fechar")
	public Chamado fechar(@PathVariable Long id) {
		return service.fechar(id);
	}
}

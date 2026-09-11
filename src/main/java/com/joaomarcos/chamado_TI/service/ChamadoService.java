package com.joaomarcos.chamado_TI.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joaomarcos.chamado_TI.dto.ChamadoRequest;
import com.joaomarcos.chamado_TI.enums.Status;
import com.joaomarcos.chamado_TI.exception.ChamadoJaFechadoException;
import com.joaomarcos.chamado_TI.exception.ChamadoNaoEncontradoException;
import com.joaomarcos.chamado_TI.model.entity.Chamado;
import com.joaomarcos.chamado_TI.repository.ChamadoRepository;

@Service
public class ChamadoService {
	@Autowired
	private ChamadoRepository repository;
	
	public Chamado registrarChamado(ChamadoRequest dto) {
		Chamado novo = new Chamado(dto.getPrioridade(),dto.getProblema());
		return repository.save(novo);
	}
	
	public List<Chamado> listarTodos(){
		return repository.findAll();
	}
	
	public List<Chamado> listarAbertos(){
		return repository.findByStatus(Status.ABERTO);
	}
	
	public Chamado fechar(Long id) {
		Chamado atual = repository
				.findById(id)
				.orElseThrow(() -> new ChamadoNaoEncontradoException("O chamado ["+id+"] Não foi encontrado"));
		 if(atual.getStatus()==Status.FECHADO) {
			 throw new ChamadoJaFechadoException("O chamado ["+id+"] Já possui seu STATUS como 'FECHADO'");
		 }
		 atual.setStatus(Status.FECHADO);
		 atual.setDataFechamento(LocalDateTime.now());
		 return repository.save(atual);
	}
}

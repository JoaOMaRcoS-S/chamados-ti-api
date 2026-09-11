package com.joaomarcos.chamado_TI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.joaomarcos.chamado_TI.enums.Prioridade;
import com.joaomarcos.chamado_TI.enums.Status;
import com.joaomarcos.chamado_TI.exception.ChamadoJaFechadoException;
import com.joaomarcos.chamado_TI.model.entity.Chamado;
import com.joaomarcos.chamado_TI.repository.ChamadoRepository;
import com.joaomarcos.chamado_TI.service.ChamadoService;
@ExtendWith(MockitoExtension.class)
class ChamadoServiceTest {

	@Mock
	private ChamadoRepository repository;
	
	@InjectMocks
	private ChamadoService service;
	
	@Test
	void deveFecharChamadoAberto() {
		//Arrange
		Chamado teste = new Chamado(Prioridade.ALTA,"qualquer coisa");
		when(repository.findById(1L)).thenReturn(Optional.of(teste));
		when(repository.save(teste)).thenReturn(teste);
		//Act
		Chamado resultado = service.fechar(1L);
		//Assert
		assertEquals(Status.FECHADO,resultado.getStatus());
		assertNotNull(resultado.getDataFechamento());
		verify(repository).save(teste);
		
	}
	
	@Test
	void deveLancarExcecaoAoFecharChamadoJaFechado() {
		//Arrange
		Chamado teste = new Chamado(Prioridade.BAIXA, "outra qualquer coisa");
		when(repository.findById(1L)).thenReturn(Optional.of(teste));
		service.fechar(1L);
		//Act + Assert
		assertThrows(ChamadoJaFechadoException.class, ()-> service.fechar(1L));
		verify(repository, times(1)).save(teste);
	}
	
}

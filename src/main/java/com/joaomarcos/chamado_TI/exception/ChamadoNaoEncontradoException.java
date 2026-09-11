package com.joaomarcos.chamado_TI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@SuppressWarnings("serial")
public class ChamadoNaoEncontradoException extends RuntimeException{
	public ChamadoNaoEncontradoException(String mensagem) {
		super(mensagem);
	}
}

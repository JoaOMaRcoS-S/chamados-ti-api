package com.joaomarcos.chamado_TI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@SuppressWarnings("serial")
public class ChamadoJaFechadoException extends RuntimeException{
	public ChamadoJaFechadoException(String mensagem) {
		super(mensagem);
	}
}

package com.joaomarcos.chamado_TI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.joaomarcos.chamado_TI.enums.Status;
import com.joaomarcos.chamado_TI.model.entity.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Long>{
	public List<Chamado> findByStatus(Status status);
}

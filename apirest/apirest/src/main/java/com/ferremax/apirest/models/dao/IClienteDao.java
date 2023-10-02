package com.ferremax.apirest.models.dao;

import com.ferremax.apirest.models.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface IClienteDao extends CrudRepository<Cliente,Long> {
}

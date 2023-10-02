package com.ferremax.apirest.controllers;

import com.ferremax.apirest.models.entity.Cliente;
import com.ferremax.apirest.models.service.IClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClienteRestController {
    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    public List<Cliente> index() {
        return clienteService.findAll();
    }

    @GetMapping("/clientes/{id}")//busca por Id de cliente
    public ResponseEntity<?> buscarporid(@PathVariable Long id) {
        Cliente cliente = null;
        Map<String, Object> response = new HashMap<>();

        try {
            cliente = clienteService.findById(id);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar la consulta en la base de datos");
            response.put("Error!", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (cliente == null) {
            response.put("Mensaje:", "el ID del cliente: ".concat(id.toString().concat(" No existe en la base de datos, por favor ingresa un ID valido!!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
    }


    @PostMapping("/clientes")//crea un cliente
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {
        Cliente clienteNew = null;
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()){

            List<String>errors=result.getFieldErrors()
                    .stream()
                    .map(error->"El campo '"+error.getField()+"'"+error.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors",errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            clienteNew = clienteService.save(cliente);
        } catch (DataAccessException e) {
            response.put("Mensaje", "Error al realizar el insert en la base de datos");
            response.put("Error!", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("Mensaje", "El cliente ha sido creado con exito!");
        response.put("cliente", clienteNew);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/clientes/{id}")//sirve para actualizar
    public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result ,@PathVariable Long id) {

        Cliente clienteActual = clienteService.findById(id);
        Cliente clienteUpdate = null;

        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()){

            List<String>errors=result.getFieldErrors()
                    .stream()
                    .map(error->"El campo '"+error.getField()+"'"+error.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors",errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if (clienteActual == null) {
            response.put("Mensaje:", "Error: no se pudo editar el cliente ya que el ID: ".concat(id.toString().concat(" No existe en la base de datos, por favor ingresa un ID valido!!")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        try {
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setApellido(cliente.getApellido());
            clienteActual.setEmail(cliente.getEmail());
            clienteActual.setCreteAt(cliente.getCreteAt());
            clienteUpdate = clienteService.save(clienteActual);
        } catch (DataAccessException e) {
            response.put("Mensaje:", "Error al actualizar en la base de datos");
            response.put("Error!", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("Mensaje:", "El cliente ha sido actualizado con exito!");
        response.put("cliente", clienteUpdate);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }


    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Cliente cliente = clienteService.findById(id);

        if (cliente == null) {
            response.put("mensaje", "El cliente con ID " + id + " no existe");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            clienteService.delete(id);
            response.put("mensaje", "Cliente eliminado con éxito");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            response.put("mensaje", "No se puede eliminar el cliente. Existe una restricción de integridad.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar el cliente en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}


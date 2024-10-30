package com.codigo.ms_registros.controller;

import com.codigo.ms_registros.entity.EmpresaEntity;
import com.codigo.ms_registros.service.EmpresaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/empresa")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<EmpresaEntity> guardarEmpresa(@RequestParam("ruc") String ruc) {
        EmpresaEntity empresa = empresaService.guardarEmpresa(ruc);
        return new ResponseEntity<>(empresa, HttpStatus.CREATED);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleInvalidArgument(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}

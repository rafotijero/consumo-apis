package com.codigo.ms_registros.service.impl;

import com.codigo.ms_registros.aggregates.constants.Constants;
import com.codigo.ms_registros.aggregates.response.ResponseSUNAT;
import com.codigo.ms_registros.client.ClientSUNAT;
import com.codigo.ms_registros.entity.EmpresaEntity;
import com.codigo.ms_registros.repository.EmpresaRepository;
import com.codigo.ms_registros.service.EmpresaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Objects;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final ClientSUNAT clientSUNAT;

    @Value("${token.api}")
    private String token;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository, ClientSUNAT clientSUNAT) {
        this.empresaRepository = empresaRepository;
        this.clientSUNAT = clientSUNAT;
    }

    @Override
    public EmpresaEntity guardarEmpresa(String ruc) {
        try {
            EmpresaEntity empresa = getEntity(ruc);
            return empresaRepository.save(empresa);
        } catch (FeignException e) {
            if (e.status() == 422) {
                String mensajeError = extraerMensajeError(e);
                throw new IllegalArgumentException(mensajeError);
            }
            throw e;
        }
    }

    private String extraerMensajeError(FeignException e) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode errorBody = objectMapper.readTree(e.contentUTF8());
            return errorBody.get("message").asText();
        } catch (Exception ex) {
            return Constants.ERROR_GENERAL + " EmpresaServiceImpl -> extraerMensajeError";
        }
    }

    private EmpresaEntity getEntity(String ruc) {
        EmpresaEntity empresaEntity = new EmpresaEntity();
        ResponseSUNAT responseSUNAT = executionSUNAT(ruc);
        if (Objects.nonNull(responseSUNAT)) {
            empresaEntity.setRazonSocial(responseSUNAT.getRazonSocial());
            empresaEntity.setTipoDocumento(responseSUNAT.getTipoDocumento());
            empresaEntity.setNumeroDocumento(responseSUNAT.getNumeroDocumento());
            empresaEntity.setEstado(responseSUNAT.getEstado());
            empresaEntity.setCondicion(responseSUNAT.getCondicion());
            empresaEntity.setDireccion(responseSUNAT.getDireccion());
            empresaEntity.setUbigeo(responseSUNAT.getUbigeo());
            empresaEntity.setViaTipo(responseSUNAT.getViaTipo());
            empresaEntity.setViaNombre(responseSUNAT.getViaNombre());
            empresaEntity.setZonaCodigo(responseSUNAT.getZonaCodigo());
            empresaEntity.setZonaTipo(responseSUNAT.getZonaTipo());
            empresaEntity.setNumero(responseSUNAT.getNumero());
            empresaEntity.setInterior(responseSUNAT.getInterior());
            empresaEntity.setLote(responseSUNAT.getLote());
            empresaEntity.setDpto(responseSUNAT.getDpto());
            empresaEntity.setManzana(responseSUNAT.getManzana());
            empresaEntity.setKilometro(responseSUNAT.getKilometro());
            empresaEntity.setDistrito(responseSUNAT.getDistrito());
            empresaEntity.setProvincia(responseSUNAT.getProvincia());
            empresaEntity.setDepartamento(responseSUNAT.getDepartamento());
            empresaEntity.setEsAgenteRetencion(responseSUNAT.getEsAgenteRetencion());
            empresaEntity.setTipo(responseSUNAT.getTipo());
            empresaEntity.setActividadEconomica(responseSUNAT.getActividadEconomica());
            empresaEntity.setNumeroTrabajadores(responseSUNAT.getNumeroTrabajadores());
            empresaEntity.setTipoFacturacion(responseSUNAT.getTipoFacturacion());
            empresaEntity.setTipoContabilidad(responseSUNAT.getTipoContabilidad());
            empresaEntity.setComercioExterior(responseSUNAT.getComercioExterior());
            empresaEntity.setFlagEstado(Constants.ESTADO_ACTIVO);
            empresaEntity.setUserCreated(Constants.USER_CREATED);
            empresaEntity.setDateCreated(new Timestamp(System.currentTimeMillis()));
        }

        return empresaEntity;
    }


    private ResponseSUNAT executionSUNAT(String ruc) {
        String tokenOK = Constants.BEARER + token;
        return clientSUNAT.getEmpresaSUNAT(ruc, tokenOK);
    }

}

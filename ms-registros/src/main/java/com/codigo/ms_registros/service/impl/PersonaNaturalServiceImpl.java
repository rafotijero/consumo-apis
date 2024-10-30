package com.codigo.ms_registros.service.impl;

import com.codigo.ms_registros.aggregates.constants.Constants;
import com.codigo.ms_registros.aggregates.response.ResponseReniec;
import com.codigo.ms_registros.client.ClientReniec;
import com.codigo.ms_registros.entity.PersonaNaturalEntity;
import com.codigo.ms_registros.repository.PersonaNaturalRepository;
import com.codigo.ms_registros.service.PersonaNaturalService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Objects;

@Service
public class PersonaNaturalServiceImpl implements PersonaNaturalService {

    private final PersonaNaturalRepository personaNaturalRepository;
    private final ClientReniec clientReniec;

    @Value("${token.api}")
    private String token;

    public PersonaNaturalServiceImpl(PersonaNaturalRepository personaNaturalRepository, ClientReniec clientReniec) {
        this.personaNaturalRepository = personaNaturalRepository;
        this.clientReniec = clientReniec;
    }

    @Override
    public PersonaNaturalEntity guardarPersona(String dni) {
        PersonaNaturalEntity personaNatural = getEntity(dni);
        if(Objects.nonNull(personaNatural))
            return personaNaturalRepository.save(personaNatural);
        else
            return null;
    }

    private PersonaNaturalEntity getEntity(String dni) {
        PersonaNaturalEntity personaNaturalEntity = new PersonaNaturalEntity();
        ResponseReniec responseReniec = executionReniec(dni);
        if (Objects.nonNull(responseReniec)) {
            personaNaturalEntity.setNombres(responseReniec.getNombres());
            personaNaturalEntity.setApellidoPaterno(responseReniec.getApellidoPaterno());
            personaNaturalEntity.setApellidoMaterno(responseReniec.getApellidoMaterno());
            personaNaturalEntity.setNumeroDocumento(responseReniec.getNumeroDocumento());
            personaNaturalEntity.setTipoDocumento(responseReniec.getTipoDocumento());
            personaNaturalEntity.setDigitoVerificador(responseReniec.getDigitoVerificador());
            personaNaturalEntity.setEstado(Constants.ESTADO_ACTIVO);
            personaNaturalEntity.setUserCreated(Constants.USER_CREATED);
            personaNaturalEntity.setDateCreated(new Timestamp(System.currentTimeMillis()));
        }


        return personaNaturalEntity;
    }

    private ResponseReniec executionReniec(String dni) {
        String tokenOK = Constants.BEARER + token;
        return clientReniec.getPersonaReniec(dni, tokenOK);
    }


}

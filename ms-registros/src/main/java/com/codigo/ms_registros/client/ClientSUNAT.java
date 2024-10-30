package com.codigo.ms_registros.client;

import com.codigo.ms_registros.aggregates.response.ResponseReniec;
import com.codigo.ms_registros.aggregates.response.ResponseSUNAT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "client-sunat", url = "https://api.apis.net.pe/v2/sunat/ruc/")
public interface ClientSUNAT {

    @GetMapping("/full")
    ResponseSUNAT getEmpresaSUNAT(@RequestParam("numero") String numero,
                                  @RequestHeader("Authorization") String authorization);

}

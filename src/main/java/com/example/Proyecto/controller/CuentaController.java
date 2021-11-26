package com.example.Proyecto.controller;

import com.example.Proyecto.dto.CuentaDto;
import com.example.Proyecto.entity.Cliente;
import com.example.Proyecto.entity.Cuenta;
import com.example.Proyecto.repository.CuentaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
public class CuentaController {

    @Autowired
    CuentaRepository cuentaRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping("/agregarCuenta/{usuario}")
    public ResponseEntity<CuentaDto> addCuenta(@PathVariable("usuario") String usuario, @RequestBody Cuenta cuenta) {

        //Falta validar que el usuario este logueado


        Cliente cliente = consumirCliente(usuario);

        if (cliente.getUsuario() != null) {
            CuentaDto cuentaDto = modelMapper.map(cuenta, CuentaDto.class);

            if (cuenta.getTipoDeCuenta().equalsIgnoreCase("ahorro")) {
                Cuenta agregarCuenta = cuentaRepository.creacionCuentaAhorro(usuario, cuenta);
                ResponseEntity<Cuenta> clienteResponse = restTemplate.postForEntity("http://localhost:8080/agregarCuenta/" + usuario, cuenta, Cuenta.class);

            } else {
                Cuenta agregarCuenta = cuentaRepository.creacionCuentaCorriente(usuario, cuenta);
                ResponseEntity<Cuenta> clienteResponse = restTemplate.postForEntity("http://localhost:8080/agregarCuenta/" + usuario, cuenta, Cuenta.class);
            }
            return ResponseEntity.ok(cuentaDto);

        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public Cliente consumirCliente(String usuario) {
        try {
            Cliente cliente = restTemplate.getForObject("http://localhost:8080/buscarCliente/" + usuario, Cliente.class);
            return cliente;
        } catch (Exception e) {
            return new Cliente();
        }
    }


    @GetMapping("/ListarCuentas/{usuario}")
    public ResponseEntity<List<Cuenta>> getListaCuentas(@PathVariable("usuario") String usuario) {
        Cliente cliente = consumirCliente(usuario);

        if (cliente.getUsuario() != null) {
            List<Cuenta> lista = cuentaRepository.getCuentas(cliente);

            if (lista.size() < 1) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST + ": El cliente no contiene cuentas", HttpStatus.BAD_REQUEST);
            } else {
                return ResponseEntity.ok(lista);
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/porCbu/{cbu}")
    public ResponseEntity<Cuenta> getCuentaPorCbu(@PathVariable("cbu") String cbu) {
        Optional<Cuenta> cuenta = cuentaRepository.getbyCbu(cbu);
        if (cuenta.isEmpty()) {
            return ResponseEntity.notFound().build();

        } else {
            return ResponseEntity.ok(cuenta.get());
        }
    }

    //Modificar el cbu

@PutMapping("/update/{usuario}")
    public ResponseEntity<Cuenta> modificarCbu(@PathVariable("usuario") String usuario, @RequestBody Cuenta cuenta){
    Optional<Cuenta> cbuExiste=cuentaRepository.getbyCbu(cuenta.getCbu());
    if(cbuExiste.isPresent()){
        return ResponseEntity.ok(cuentaRepository.modificarCbu(cuenta));
    }else{
        return ResponseEntity.notFound().build();
    }

}
    @GetMapping("/usuario/")
    public ResponseEntity<Cuenta> getPorCbu(@PathVariable("cbu") String cbu) {
        Optional<Cuenta> cuenta = cuentaRepository.getbyCbu(cbu);
        if (cuenta.isEmpty()) {
            return ResponseEntity.notFound().build();

        } else {
            return ResponseEntity.ok(cuenta.get());
        }
    }



}

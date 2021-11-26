package com.example.Proyecto.repository;

import com.example.Proyecto.entity.Cliente;
import com.example.Proyecto.entity.Cuenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Repository
public class CuentaRepository{

    @Autowired
    CuentaRepositoryDao cuentaRepositoryDao;

    public Cuenta creacionCuentaAhorro(String usuario,Cuenta cuenta) {
        String cadena=usuario+"Ah"+numeroRandom();
        cuenta.setTipoDeCuenta("Ahorro");
        cuenta.setCbu(cadena);
        cuentaRepositoryDao.save(cuenta);
        return cuenta;
    }

    public Cuenta creacionCuentaCorriente(String usuario,Cuenta cuenta) {
        String cadena=usuario+"Co"+numeroRandom();
        cuenta.setTipoDeCuenta("Corriente");
        cuenta.setAcuerdo((float) 3000);
        cuenta.setCbu(cadena);
        cuentaRepositoryDao.save(cuenta);
        return cuenta;
    }


    public Integer numeroRandom(){
        Random r = new Random();
        return r.nextInt(10000)+1;
    }

    private List<Cuenta> listaCuenta = new ArrayList<>();

    public Optional<Cuenta> getbyCbu(String cbu){
        return cuentaRepositoryDao.findById(cbu);
    }

    public List<Cuenta> getCuentas(Cliente cliente) {
        List<Cuenta> lista = new ArrayList<>();
        for(Cuenta c: cliente.getCuentas()){
            lista.add(c);
        }
        return lista;
    }

  /* public Cuenta modificarCbu(Cuenta cuenta){
       return cuentaRepositoryDao.modificarCbu(cuenta);


       }


   */
  public Cuenta modificarCbu (Cuenta cuenta){
      Cuenta actualizarCbu = cuentaRepositoryDao.findById(cuenta.getCbu()).get();
      actualizarCbu.setCbu(cuenta.getCbu());
      return cuentaRepositoryDao.save(actualizarCbu);
  }
//me marca un error :)
   /* public List<Cuenta> getCbu(Cliente cuenta) {
        List<Cuenta> listasCbu = new ArrayList<>();
        for(Cuenta c: cuenta.getCbu){
            listasCbu.add(c.getCbu());
        }
        return listasCbu;
    }

    */
}
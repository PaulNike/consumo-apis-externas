package com.codigo.consumo_apis_externas.dao;

import com.codigo.consumo_apis_externas.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<PersonaEntity,Long> {
    PersonaEntity findByNumDoc(String numDoc);
    Boolean existsByNumDoc(String numDoc);
}

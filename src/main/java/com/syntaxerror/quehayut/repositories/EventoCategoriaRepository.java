package com.syntaxerror.quehayut.repositories;

import com.syntaxerror.quehayut.models.EventoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoCategoriaRepository extends JpaRepository<EventoCategoria, Long> {
}


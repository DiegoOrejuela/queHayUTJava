package com.syntaxerror.quehayut.repositories;

import com.syntaxerror.quehayut.models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
}


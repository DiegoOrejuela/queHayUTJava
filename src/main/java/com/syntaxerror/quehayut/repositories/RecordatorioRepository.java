package com.syntaxerror.quehayut.repositories;

import com.syntaxerror.quehayut.models.Recordatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordatorioRepository extends JpaRepository<Recordatorio, Long> {
}


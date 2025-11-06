package com.syntaxerror.quehayut.controllers;

import com.syntaxerror.quehayut.models.Evento;
import com.syntaxerror.quehayut.repositories.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;

    @GetMapping("/eventos")
    @Transactional(readOnly = true)
    public List<Evento> eventos() {
        return eventoRepository.findAll();
    }
}

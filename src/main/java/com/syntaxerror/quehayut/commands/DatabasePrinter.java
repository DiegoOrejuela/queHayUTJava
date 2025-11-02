package com.syntaxerror.quehayut.commands;

import com.syntaxerror.quehayut.models.*;
import com.syntaxerror.quehayut.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@Order(2)
public class DatabasePrinter implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabasePrinter.class);
    
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final EventoRepository eventoRepository;
    private final RecordatorioRepository recordatorioRepository;
    private final EventoCategoriaRepository eventoCategoriaRepository;

    public DatabasePrinter(
            UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository,
            EventoRepository eventoRepository,
            RecordatorioRepository recordatorioRepository,
            EventoCategoriaRepository eventoCategoriaRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.eventoRepository = eventoRepository;
        this.recordatorioRepository = recordatorioRepository;
        this.eventoCategoriaRepository = eventoCategoriaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) throws Exception {
        // Solo ejecutar si se pasa el argumento --show, show o db:show
        boolean shouldShow = Arrays.asList(args).contains("--show") || 
                            Arrays.asList(args).contains("show") ||
                            Arrays.stream(args).anyMatch(arg -> arg.equals("db:show"));

        if (!shouldShow) {
            return;
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("                  REGISTROS DE LA BASE DE DATOS");
        System.out.println("=".repeat(80) + "\n");

        // Imprimir Usuarios
        printUsuarios();
        
        // Imprimir Categorías
        printCategorias();
        
        // Imprimir Eventos
        printEventos();
        
        // Imprimir Eventos-Categorías
        printEventosCategorias();
        
        // Imprimir Recordatorios
        printRecordatorios();

        System.out.println("\n" + "=".repeat(80));
        System.out.println("                            FIN DEL REPORTE");
        System.out.println("=".repeat(80) + "\n");
        
        // Terminar la aplicación después de imprimir
        System.exit(0);
    }

    private void printUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        System.out.println("\n" + "─".repeat(80));
        System.out.println("USUARIOS (Total: " + usuarios.size() + ")");
        System.out.println("─".repeat(80));
        
        if (usuarios.isEmpty()) {
            System.out.println("  No hay usuarios registrados.");
        } else {
            for (Usuario usuario : usuarios) {
                System.out.println("\n  ID: " + usuario.getId());
                System.out.println("  Nombre completo: " + 
                    usuario.getPrimerNombre() + " " + 
                    (usuario.getSegundoNombre() != null ? usuario.getSegundoNombre() + " " : "") +
                    usuario.getPrimerApellido() + " " +
                    (usuario.getSegundoApellido() != null ? usuario.getSegundoApellido() : ""));
                System.out.println("  Fecha de nacimiento: " + 
                    (usuario.getFechaDeNacimiento() != null ? usuario.getFechaDeNacimiento() : "N/A"));
                System.out.println("  Teléfono: " + 
                    (usuario.getTelefono() != null ? usuario.getTelefono() : "N/A"));
                System.out.println("  Creado: " + usuario.getCreatedAt());
            }
        }
    }

    private void printCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        System.out.println("\n" + "─".repeat(80));
        System.out.println("CATEGORÍAS (Total: " + categorias.size() + ")");
        System.out.println("─".repeat(80));
        
        if (categorias.isEmpty()) {
            System.out.println("  No hay categorías registradas.");
        } else {
            for (Categoria categoria : categorias) {
                System.out.println("\n  ID: " + categoria.getId());
                System.out.println("  Nombre: " + categoria.getNombre());
                System.out.println("  Descripción: " + 
                    (categoria.getDescripcion() != null ? categoria.getDescripcion() : "N/A"));
                System.out.println("  Estado: " + categoria.getEstado());
                System.out.println("  Creado: " + categoria.getCreatedAt());
            }
        }
    }

    private void printEventos() {
        List<Evento> eventos = eventoRepository.findAll();
        System.out.println("\n" + "─".repeat(80));
        System.out.println("EVENTOS (Total: " + eventos.size() + ")");
        System.out.println("─".repeat(80));
        
        if (eventos.isEmpty()) {
            System.out.println("  No hay eventos registrados.");
        } else {
            for (Evento evento : eventos) {
                System.out.println("\n  ID: " + evento.getId());
                System.out.println("  Nombre: " + evento.getNombre());
                System.out.println("  Descripción: " + 
                    (evento.getDescripcion() != null && evento.getDescripcion().length() > 100 
                        ? evento.getDescripcion().substring(0, 100) + "..." 
                        : (evento.getDescripcion() != null ? evento.getDescripcion() : "N/A")));
                System.out.println("  Ubicación: " + 
                    (evento.getUbicacion() != null ? evento.getUbicacion() : "N/A"));
                System.out.println("  Fecha inicio: " + evento.getFechaInicio());
                System.out.println("  Fecha fin: " + 
                    (evento.getFechaFin() != null ? evento.getFechaFin() : "N/A"));
                System.out.println("  Estado: " + evento.getEstado());
                System.out.println("  Organizador: " + 
                    (evento.getOrganizador() != null ? evento.getOrganizador() : "N/A"));
                System.out.println("  Creado: " + evento.getCreatedAt());
            }
        }
    }

    private void printEventosCategorias() {
        List<EventoCategoria> eventosCategorias = eventoCategoriaRepository.findAll();
        System.out.println("\n" + "─".repeat(80));
        System.out.println("EVENTOS-CATEGORÍAS (Total: " + eventosCategorias.size() + ")");
        System.out.println("─".repeat(80));
        
        if (eventosCategorias.isEmpty()) {
            System.out.println("  No hay asociaciones eventos-categorías.");
        } else {
            for (EventoCategoria ec : eventosCategorias) {
                System.out.println("\n  ID: " + ec.getId());
                System.out.println("  Evento ID: " + ec.getEvento().getId() + 
                    " - " + ec.getEvento().getNombre());
                System.out.println("  Categoría ID: " + ec.getCategoria().getId() + 
                    " - " + ec.getCategoria().getNombre());
                System.out.println("  Creado: " + ec.getCreatedAt());
            }
        }
    }

    private void printRecordatorios() {
        List<Recordatorio> recordatorios = recordatorioRepository.findAll();
        System.out.println("\n" + "─".repeat(80));
        System.out.println("RECORDATORIOS (Total: " + recordatorios.size() + ")");
        System.out.println("─".repeat(80));
        
        if (recordatorios.isEmpty()) {
            System.out.println("  No hay recordatorios registrados.");
        } else {
            for (Recordatorio recordatorio : recordatorios) {
                System.out.println("\n  ID: " + recordatorio.getId());
                System.out.println("  Usuario ID: " + recordatorio.getUsuario().getId() + 
                    " - " + recordatorio.getUsuario().getPrimerNombre() + " " + 
                    recordatorio.getUsuario().getPrimerApellido());
                System.out.println("  Evento ID: " + recordatorio.getEvento().getId() + 
                    " - " + recordatorio.getEvento().getNombre());
                System.out.println("  Estado: " + recordatorio.getEstado());
                System.out.println("  Creado: " + recordatorio.getCreatedAt());
            }
        }
    }
}


package com.syntaxerror.quehayut.seeders;

import com.syntaxerror.quehayut.models.*;
import com.syntaxerror.quehayut.models.enums.EstadoCategoria;
import com.syntaxerror.quehayut.models.enums.EstadoEvento;
import com.syntaxerror.quehayut.models.enums.EstadoRecordatorio;
import com.syntaxerror.quehayut.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final EventoRepository eventoRepository;
    private final RecordatorioRepository recordatorioRepository;
    private final EventoCategoriaRepository eventoCategoriaRepository;

    public DataSeeder(
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
    public void run(String... args) throws Exception {
        // Solo ejecutar si se pasa el argumento --seed o seed
        boolean shouldSeed = Arrays.asList(args).contains("--seed") || 
                            Arrays.asList(args).contains("seed") ||
                            Arrays.stream(args).anyMatch(arg -> arg.equals("db:seed"));

        if (!shouldSeed) {
            logger.debug("Argumento --seed no encontrado. Saltando seed.");
            return;
        }

        logger.info("=== Iniciando seed de datos ===");
        
        // Limpiar datos existentes (opcional, para desarrollo)
        logger.info("Limpiando datos existentes...");
        recordatorioRepository.deleteAll();
        eventoCategoriaRepository.deleteAll();
        eventoRepository.deleteAll();
        categoriaRepository.deleteAll();
        usuarioRepository.deleteAll();

        // 1. Crear Usuarios
        logger.info("Creando usuarios...");
        List<Usuario> usuarios = crearUsuarios();
        logger.info("Usuarios creados: {}", usuarios.size());

        // 2. Crear Categorías
        logger.info("Creando categorías...");
        List<Categoria> categorias = crearCategorias();
        logger.info("Categorías creadas: {}", categorias.size());

        // 3. Crear Eventos
        logger.info("Creando eventos...");
        List<Evento> eventos = crearEventos();
        logger.info("Eventos creados: {}", eventos.size());

        // 4. Asociar Eventos con Categorías
        logger.info("Asociando eventos con categorías...");
        asociarEventosConCategorias(eventos, categorias);
        logger.info("Asociaciones creadas");

        // 5. Crear Recordatorios
        logger.info("Creando recordatorios...");
        crearRecordatorios(usuarios, eventos);
        logger.info("Recordatorios creados");

        logger.info("=== Seed completado exitosamente ===");
        logger.info("Resumen:");
        logger.info("  - Usuarios: {}", usuarioRepository.count());
        logger.info("  - Categorías: {}", categoriaRepository.count());
        logger.info("  - Eventos: {}", eventoRepository.count());
        logger.info("  - Recordatorios: {}", recordatorioRepository.count());
        logger.info("  - Eventos-Categorías: {}", eventoCategoriaRepository.count());
        
        // Terminar la aplicación después de ejecutar el seed
        System.exit(0);
    }

    private List<Usuario> crearUsuarios() {
        Usuario usuario1 = new Usuario();
        usuario1.setPrimerNombre("María");
        usuario1.setSegundoNombre("Isabel");
        usuario1.setPrimerApellido("González");
        usuario1.setSegundoApellido("Pérez");
        usuario1.setFechaDeNacimiento(LocalDate.of(2000, 3, 15));
        usuario1.setTelefono("3001234567");
        
        Usuario usuario2 = new Usuario();
        usuario2.setPrimerNombre("Carlos");
        usuario2.setSegundoNombre("Andrés");
        usuario2.setPrimerApellido("Rodríguez");
        usuario2.setSegundoApellido("Martínez");
        usuario2.setFechaDeNacimiento(LocalDate.of(1999, 7, 22));
        usuario2.setTelefono("3102345678");

        Usuario usuario3 = new Usuario();
        usuario3.setPrimerNombre("Ana");
        usuario3.setSegundoNombre("Sofía");
        usuario3.setPrimerApellido("López");
        usuario3.setSegundoApellido("García");
        usuario3.setFechaDeNacimiento(LocalDate.of(2001, 11, 5));
        usuario3.setTelefono("3203456789");

        Usuario usuario4 = new Usuario();
        usuario4.setPrimerNombre("Juan");
        usuario4.setPrimerApellido("Ramírez");
        usuario4.setSegundoApellido("Torres");
        usuario4.setFechaDeNacimiento(LocalDate.of(1998, 5, 30));
        usuario4.setTelefono("3504567890");

        Usuario usuario5 = new Usuario();
        usuario5.setPrimerNombre("Laura");
        usuario5.setSegundoNombre("Camila");
        usuario5.setPrimerApellido("Vargas");
        usuario5.setSegundoApellido("Sánchez");
        usuario5.setFechaDeNacimiento(LocalDate.of(2002, 9, 18));
        usuario5.setTelefono("3015678901");

        return usuarioRepository.saveAll(Arrays.asList(usuario1, usuario2, usuario3, usuario4, usuario5));
    }

    private List<Categoria> crearCategorias() {
        Categoria categoria1 = new Categoria();
        categoria1.setNombre("Académico");
        categoria1.setDescripcion("Eventos relacionados con actividades académicas, conferencias y talleres");
        categoria1.setEstado(EstadoCategoria.ACTIVO);

        Categoria categoria2 = new Categoria();
        categoria2.setNombre("Cultural");
        categoria2.setDescripcion("Eventos culturales, conciertos, exposiciones y presentaciones artísticas");
        categoria2.setEstado(EstadoCategoria.ACTIVO);

        Categoria categoria3 = new Categoria();
        categoria3.setNombre("Deportivo");
        categoria3.setDescripcion("Competencias deportivas, torneos y actividades físicas");
        categoria3.setEstado(EstadoCategoria.ACTIVO);

        Categoria categoria4 = new Categoria();
        categoria4.setNombre("Social");
        categoria4.setDescripcion("Eventos sociales, integraciones y actividades comunitarias");
        categoria4.setEstado(EstadoCategoria.ACTIVO);

        Categoria categoria5 = new Categoria();
        categoria5.setNombre("Investigación");
        categoria5.setDescripcion("Seminarios, simposios y eventos relacionados con investigación");
        categoria5.setEstado(EstadoCategoria.ACTIVO);

        Categoria categoria6 = new Categoria();
        categoria6.setNombre("Emprendimiento");
        categoria6.setDescripcion("Ferias, charlas y eventos de emprendimiento e innovación");
        categoria6.setEstado(EstadoCategoria.ACTIVO);

        return categoriaRepository.saveAll(Arrays.asList(categoria1, categoria2, categoria3, categoria4, categoria5, categoria6));
    }

    private List<Evento> crearEventos() {
        LocalDateTime ahora = LocalDateTime.now();

        Evento evento1 = new Evento();
        evento1.setNombre("Semana de la Ingeniería");
        evento1.setDescripcion("Una semana completa dedicada a la ingeniería con conferencias, talleres y exposiciones de proyectos estudiantiles. Incluye charlas magistrales con ingenieros destacados y demostraciones prácticas.");
        evento1.setUbicacion("Auditorio Principal - Universidad del Tolima");
        evento1.setCover("https://ejemplo.com/eventos/semana-ingenieria.jpg");
        evento1.setFechaInicio(ahora.plusDays(15).withHour(8).withMinute(0));
        evento1.setFechaFin(ahora.plusDays(19).withHour(18).withMinute(0));
        evento1.setEstado(EstadoEvento.PROGRAMADO);
        evento1.setOrganizador("Facultad de Ingeniería");

        Evento evento2 = new Evento();
        evento2.setNombre("Festival de Música Universitaria");
        evento2.setDescripcion("Gran festival de música con presentaciones de estudiantes y bandas locales. Incluye diversos géneros musicales y espacios para nuevos talentos.");
        evento2.setUbicacion("Plaza Central - Universidad del Tolima");
        evento2.setCover("https://ejemplo.com/eventos/festival-musica.jpg");
        evento2.setFechaInicio(ahora.plusDays(10).withHour(14).withMinute(0));
        evento2.setFechaFin(ahora.plusDays(10).withHour(22).withMinute(0));
        evento2.setEstado(EstadoEvento.PROGRAMADO);
        evento2.setOrganizador("Dirección de Bienestar Universitario");

        Evento evento3 = new Evento();
        evento3.setNombre("Torneo Interfacultades de Fútbol");
        evento3.setDescripcion("Competencia deportiva entre las diferentes facultades. Gran final con premiación y entrega de trofeos.");
        evento3.setUbicacion("Cancha Principal - Universidad del Tolima");
        evento3.setCover("https://ejemplo.com/eventos/torneo-futbol.jpg");
        evento3.setFechaInicio(ahora.plusDays(5).withHour(9).withMinute(0));
        evento3.setFechaFin(ahora.plusDays(12).withHour(17).withMinute(0));
        evento3.setEstado(EstadoEvento.PROGRAMADO);
        evento3.setOrganizador("Departamento de Deportes");

        Evento evento4 = new Evento();
        evento4.setNombre("Conferencia: Inteligencia Artificial en la Educación");
        evento4.setDescripcion("Charla magistral sobre el uso de IA en el ámbito educativo. Dirigida a docentes, estudiantes y profesionales interesados en tecnología educativa.");
        evento4.setUbicacion("Aula Magna - Universidad del Tolima");
        evento4.setCover("https://ejemplo.com/eventos/conferencia-ia.jpg");
        evento4.setFechaInicio(ahora.plusDays(8).withHour(10).withMinute(0));
        evento4.setFechaFin(ahora.plusDays(8).withHour(12).withMinute(0));
        evento4.setEstado(EstadoEvento.PROGRAMADO);
        evento4.setOrganizador("Facultad de Ciencias de la Educación");

        Evento evento5 = new Evento();
        evento5.setNombre("Feria de Emprendimiento Estudiantil");
        evento5.setDescripcion("Exposición de proyectos emprendedores de estudiantes. Oportunidad para networking y conocer innovaciones desarrolladas por la comunidad universitaria.");
        evento5.setUbicacion("Polideportivo - Universidad del Tolima");
        evento5.setCover("https://ejemplo.com/eventos/feria-emprendimiento.jpg");
        evento5.setFechaInicio(ahora.plusDays(20).withHour(9).withMinute(0));
        evento5.setFechaFin(ahora.plusDays(20).withHour(18).withMinute(0));
        evento5.setEstado(EstadoEvento.PROGRAMADO);
        evento5.setOrganizador("Centro de Emprendimiento");

        Evento evento6 = new Evento();
        evento6.setNombre("Taller de Investigación Científica");
        evento6.setDescripcion("Taller práctico sobre metodologías de investigación. Incluye sesiones de trabajo grupal y asesoría personalizada.");
        evento6.setUbicacion("Laboratorio de Investigación - Edificio Central");
        evento6.setCover("https://ejemplo.com/eventos/taller-investigacion.jpg");
        evento6.setFechaInicio(ahora.plusDays(3).withHour(8).withMinute(0));
        evento6.setFechaFin(ahora.plusDays(3).withHour(17).withMinute(0));
        evento6.setEstado(EstadoEvento.PROGRAMADO);
        evento6.setOrganizador("Vicerrectoría de Investigación");

        Evento evento7 = new Evento();
        evento7.setNombre("Jornada de Integración Estudiantil");
        evento7.setDescripcion("Día de integración con actividades recreativas, juegos y competencias para fortalecer la comunidad universitaria.");
        evento7.setUbicacion("Campus Universitario");
        evento7.setCover("https://ejemplo.com/eventos/integracion.jpg");
        evento7.setFechaInicio(ahora.plusDays(25).withHour(8).withMinute(0));
        evento7.setFechaFin(ahora.plusDays(25).withHour(16).withMinute(0));
        evento7.setEstado(EstadoEvento.PROGRAMADO);
        evento7.setOrganizador("Dirección de Bienestar Universitario");

        // Un evento en curso
        Evento evento8 = new Evento();
        evento8.setNombre("Ciclo de Conferencias de Ciencias Básicas");
        evento8.setDescripcion("Serie de conferencias sobre matemáticas, física y química aplicadas.");
        evento8.setUbicacion("Auditorio de Ciencias");
        evento8.setCover("https://ejemplo.com/eventos/conferencias-ciencias.jpg");
        evento8.setFechaInicio(ahora.minusDays(2).withHour(9).withMinute(0));
        evento8.setFechaFin(ahora.plusDays(5).withHour(17).withMinute(0));
        evento8.setEstado(EstadoEvento.EN_CURSO);
        evento8.setOrganizador("Facultad de Ciencias Básicas");

        // Un evento finalizado
        Evento evento9 = new Evento();
        evento9.setNombre("Semana de Bienvenida 2024");
        evento9.setDescripcion("Actividades de bienvenida para estudiantes nuevos del semestre.");
        evento9.setUbicacion("Campus Universitario");
        evento9.setCover("https://ejemplo.com/eventos/bienvenida.jpg");
        evento9.setFechaInicio(ahora.minusDays(30).withHour(8).withMinute(0));
        evento9.setFechaFin(ahora.minusDays(25).withHour(18).withMinute(0));
        evento9.setEstado(EstadoEvento.FINALIZADO);
        evento9.setOrganizador("Dirección de Bienestar Universitario");

        return eventoRepository.saveAll(Arrays.asList(
                evento1, evento2, evento3, evento4, evento5, evento6, evento7, evento8, evento9
        ));
    }

    private void asociarEventosConCategorias(List<Evento> eventos, List<Categoria> categorias) {
        // Semana de la Ingeniería -> Académico, Investigación
        crearEventoCategoria(eventos.get(0), categorias.get(0)); // Académico
        crearEventoCategoria(eventos.get(0), categorias.get(4)); // Investigación

        // Festival de Música -> Cultural, Social
        crearEventoCategoria(eventos.get(1), categorias.get(1)); // Cultural
        crearEventoCategoria(eventos.get(1), categorias.get(3)); // Social

        // Torneo de Fútbol -> Deportivo, Social
        crearEventoCategoria(eventos.get(2), categorias.get(2)); // Deportivo
        crearEventoCategoria(eventos.get(2), categorias.get(3)); // Social

        // Conferencia IA -> Académico, Investigación
        crearEventoCategoria(eventos.get(3), categorias.get(0)); // Académico
        crearEventoCategoria(eventos.get(3), categorias.get(4)); // Investigación

        // Feria Emprendimiento -> Emprendimiento, Académico
        crearEventoCategoria(eventos.get(4), categorias.get(5)); // Emprendimiento
        crearEventoCategoria(eventos.get(4), categorias.get(0)); // Académico

        // Taller Investigación -> Investigación, Académico
        crearEventoCategoria(eventos.get(5), categorias.get(4)); // Investigación
        crearEventoCategoria(eventos.get(5), categorias.get(0)); // Académico

        // Integración Estudiantil -> Social
        crearEventoCategoria(eventos.get(6), categorias.get(3)); // Social

        // Conferencias Ciencias -> Académico, Investigación
        crearEventoCategoria(eventos.get(7), categorias.get(0)); // Académico
        crearEventoCategoria(eventos.get(7), categorias.get(4)); // Investigación

        // Semana Bienvenida -> Social, Cultural
        crearEventoCategoria(eventos.get(8), categorias.get(3)); // Social
        crearEventoCategoria(eventos.get(8), categorias.get(1)); // Cultural
    }

    private void crearEventoCategoria(Evento evento, Categoria categoria) {
        EventoCategoria eventoCategoria = new EventoCategoria();
        eventoCategoria.setEvento(evento);
        eventoCategoria.setCategoria(categoria);
        eventoCategoriaRepository.save(eventoCategoria);
    }

    private void crearRecordatorios(List<Usuario> usuarios, List<Evento> eventos) {
        // Usuario 1 se suscribe a varios eventos
        crearRecordatorio(usuarios.get(0), eventos.get(0), EstadoRecordatorio.PENDIENTE); // Semana Ingeniería
        crearRecordatorio(usuarios.get(0), eventos.get(3), EstadoRecordatorio.PENDIENTE); // Conferencia IA
        crearRecordatorio(usuarios.get(0), eventos.get(5), EstadoRecordatorio.PENDIENTE); // Taller Investigación

        // Usuario 2 se suscribe a eventos culturales y deportivos
        crearRecordatorio(usuarios.get(1), eventos.get(1), EstadoRecordatorio.PENDIENTE); // Festival Música
        crearRecordatorio(usuarios.get(1), eventos.get(2), EstadoRecordatorio.PENDIENTE); // Torneo Fútbol
        crearRecordatorio(usuarios.get(1), eventos.get(6), EstadoRecordatorio.PENDIENTE); // Integración

        // Usuario 3 se suscribe a eventos académicos y de emprendimiento
        crearRecordatorio(usuarios.get(2), eventos.get(0), EstadoRecordatorio.PENDIENTE); // Semana Ingeniería
        crearRecordatorio(usuarios.get(2), eventos.get(4), EstadoRecordatorio.PENDIENTE); // Feria Emprendimiento
        crearRecordatorio(usuarios.get(2), eventos.get(5), EstadoRecordatorio.ENVIADO); // Taller (ya enviado)

        // Usuario 4 se suscribe a eventos diversos
        crearRecordatorio(usuarios.get(3), eventos.get(2), EstadoRecordatorio.PENDIENTE); // Torneo Fútbol
        crearRecordatorio(usuarios.get(3), eventos.get(7), EstadoRecordatorio.PENDIENTE); // Conferencias Ciencias

        // Usuario 5 se suscribe a eventos culturales
        crearRecordatorio(usuarios.get(4), eventos.get(1), EstadoRecordatorio.PENDIENTE); // Festival Música
        crearRecordatorio(usuarios.get(4), eventos.get(6), EstadoRecordatorio.PENDIENTE); // Integración
    }

    private void crearRecordatorio(Usuario usuario, Evento evento, EstadoRecordatorio estado) {
        Recordatorio recordatorio = new Recordatorio();
        recordatorio.setUsuario(usuario);
        recordatorio.setEvento(evento);
        recordatorio.setEstado(estado);
        recordatorioRepository.save(recordatorio);
    }
}


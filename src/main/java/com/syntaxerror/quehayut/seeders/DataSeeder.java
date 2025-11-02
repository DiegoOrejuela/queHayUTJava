package com.syntaxerror.quehayut.seeders;

import com.syntaxerror.quehayut.models.Usuario;
import com.syntaxerror.quehayut.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Order(1)
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    
    private final UsuarioRepository usuarioRepository;

    public DataSeeder(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Solo ejecutar si se pasa el argumento --seed o seed
        boolean shouldSeed = Arrays.asList(args).contains("--seed") || 
                            Arrays.asList(args).contains("seed") ||
                            Arrays.stream(args).anyMatch(arg -> arg.equals("db:seed"));

        if (!shouldSeed) {
            logger.debug("Argumento --seed no encontrado. Saltando seed de usuarios.");
            return;
        }

        logger.info("Iniciando seed de usuarios...");
        
        // Crear 5 usuarios
        for (int i = 1; i <= 5; i++) {
            Usuario usuario = new Usuario();
            usuarioRepository.save(usuario);
            logger.info("Usuario {} creado con ID: {}", i, usuario.getId());
        }
        
        logger.info("Seed completado. Total de usuarios: {}", usuarioRepository.count());
        
        // Terminar la aplicación después de ejecutar el seed
        System.exit(0);
    }
}


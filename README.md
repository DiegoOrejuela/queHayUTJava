# QuéHayUT - Guía de Configuración y Uso

## 📋 Tabla de Contenidos
1. [Requisitos Previos](#requisitos-previos)
2. [Instalación](#instalación)
3. [Configuración de PostgreSQL](#configuración-de-postgresql)
4. [Configuración del Proyecto](#configuración-del-proyecto)
5. [Comandos Disponibles](#comandos-disponibles)
6. [Estructura del Proyecto](#estructura-del-proyecto)
7. [Entidades del Sistema](#entidades-del-sistema)
8. [Troubleshooting](#troubleshooting)

---

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

### Software Necesario
- **Java 21** o superior
  - Verificar instalación: `java -version`
- **PostgreSQL** 12 o superior
  - Verificar instalación: `psql --version`
- **Gradle** (opcional, el proyecto incluye Gradle Wrapper)

### Verificar Instalaciones

```bash
# Verificar Java
java -version
# Debe mostrar: openjdk version "21.x.x"

# Verificar PostgreSQL
psql --version
# Debe mostrar: psql (PostgreSQL) 12.x o superior
```

---

## 🚀 Instalación

### 1. Clonar el Repositorio
```bash
git clone <url-del-repositorio>
cd quehayut
```

### 2. Compilar el Proyecto
```bash
./gradlew compileJava
```

O en Windows:
```bash
gradlew.bat compileJava
```

---

## 🗄️ Configuración de PostgreSQL

### 1. Crear la Base de Datos

Conectarse a PostgreSQL:
```bash
psql -U postgres
```

Crear la base de datos:
```sql
CREATE DATABASE quehayut;
```

Verificar que se creó:
```sql
\l
```

Salir de psql:
```sql
\q
```

### 2. Eliminar la Base de Datos (si es necesario)

```sql
DROP DATABASE quehayut;
```

---

## ⚙️ Configuración del Proyecto

### Archivo `application.properties`

El archivo de configuración se encuentra en:
```
src/main/resources/application.properties
```

### Configuración de la Base de Datos

Edita las siguientes propiedades según tu entorno:

```properties
# Nombre de la aplicación
spring.application.name=QueHayUT

# Configuración de la base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/quehayut
spring.datasource.username=postgres          # ← Cambiar si es necesario
spring.datasource.password=postgres          # ← Cambiar tu contraseña
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuración de JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update         # Crea/actualiza tablas automáticamente
spring.jpa.show-sql=true                     # Muestra SQL en consola (desarrollo)
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
```

### Opciones de `ddl-auto`

- `update`: Crea/actualiza tablas automáticamente (recomendado para desarrollo)
- `create`: Crea tablas y elimina datos existentes (⚠️ peligroso)
- `create-drop`: Crea al iniciar, elimina al cerrar (solo desarrollo)
- `validate`: Solo valida el esquema, no modifica (producción)
- `none`: No hace nada (esquema manual)

---

## 🎯 Comandos Disponibles

### Compilación

```bash
# Compilar el proyecto
./gradlew compileJava

# Compilar y ejecutar tests
./gradlew build
```

### Base de Datos

```bash
# Crear datos de prueba (seed)
./gradlew dbSeed

# Mostrar todos los registros de la base de datos
./gradlew dbShow
```

### Ejecución del Servidor

```bash
# Ejecutar la aplicación
./gradlew bootRun

# Ejecutar en puerto personalizado
./gradlew bootRun --args='--server.port=8081'
```

### Otras Tareas

```bash
# Limpiar archivos compilados
./gradlew clean

# Ver todas las tareas disponibles
./gradlew tasks
```

---

## 📁 Estructura del Proyecto

```
quehayut/
├── src/
│   ├── main/
│   │   ├── java/com/syntaxerror/quehayut/
│   │   │   ├── commands/          # Comandos (DatabasePrinter)
│   │   │   ├── controllers/       # Controladores REST
│   │   │   ├── models/            # Entidades JPA
│   │   │   │   ├── enums/         # Enumeraciones
│   │   │   ├── repositories/      # Repositorios Spring Data JPA
│   │   │   ├── seeders/           # Generadores de datos (DataSeeder)
│   │   │   └── QueHayUtApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── build.gradle
```

---

## 🗂️ Entidades del Sistema

El sistema incluye las siguientes entidades principales:

### Usuario
- Información personal de los usuarios
- Campos: nombre, apellidos, fecha de nacimiento, teléfono

### Evento
- Eventos universitarios
- Campos: nombre, descripción, ubicación, fechas, estado, organizador
- Estados: PROGRAMADO, EN_CURSO, FINALIZADO, CANCELADO

### Categoría
- Categorías de eventos
- Campos: nombre, descripción, estado
- Estados: ACTIVO, INACTIVO

### Recordatorio
- Recordatorios de eventos para usuarios
- Campos: estado
- Estados: PENDIENTE, ENVIADO, FALLIDO, CANCELADO
- Relaciones: Usuario, Evento

### EventoCategoria
- Tabla de unión muchos-a-muchos entre Eventos y Categorías

---

## 🔧 Troubleshooting

### Error: No se puede conectar a PostgreSQL

**Solución:**
1. Verificar que PostgreSQL esté corriendo:
   ```bash
   # macOS/Linux
   brew services list
   # o
   sudo systemctl status postgresql
   
   # Windows
   # Verificar en Services
   ```

2. Verificar credenciales en `application.properties`
3. Verificar que la base de datos existe:
   ```sql
   psql -U postgres -l
   ```

### Error: LazyInitializationException

**Causa:** Acceso a relaciones lazy fuera de una transacción.

**Solución:** Ya está resuelto en `DatabasePrinter` con `@Transactional`.

### Error: Port 8080 already in use

**Solución:** Cambiar el puerto en `application.properties`:
```properties
server.port=8081
```

O ejecutar en otro puerto:
```bash
./gradlew bootRun --args='--server.port=8081'
```

### Error: Tablas no se crean

**Solución:**
1. Verificar `spring.jpa.hibernate.ddl-auto=update` en `application.properties`
2. Verificar que la conexión a la base de datos funciona
3. Revisar logs de la aplicación

### Verificar si la base de datos tiene datos

```bash
# Ejecutar el comando de visualización
./gradlew dbShow
```

---

## 📚 Referencias

### Documentación Oficial
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Gradle Documentation](https://docs.gradle.org)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

### Enlaces Útiles
- [Spring Boot Gradle Plugin](https://docs.spring.io/spring-boot/3.5.7/gradle-plugin)
- [Gradle Build Scans](https://scans.gradle.com#gradle)

---

## 💡 Tips

1. **Desarrollo:** Usa `spring.jpa.show-sql=true` para ver las queries SQL
2. **Producción:** Cambia `ddl-auto` a `validate` o `none`
3. **Datos de prueba:** Ejecuta `./gradlew dbSeed` después de crear la base de datos
4. **Ver datos:** Usa `./gradlew dbShow` para verificar los registros

---

## 📞 Soporte

Para problemas o preguntas:
1. Revisa la sección [Troubleshooting](#troubleshooting)
2. Verifica los logs de la aplicación
3. Consulta la documentación oficial de Spring Boot


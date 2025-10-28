# Academy 2025 | Segunda Evaluación: Sistema de Gestión de Votaciones

Proyecto de ejemplo (Java 17, Spring Boot) para gestionar candidaturas, partidos políticos y votos.

## Descripción
- Es una API REST desarrollada con Spring Boot que permite gestionar entidades básicas de una votación: candidatos, partidos políticos y votos.
- Contiene controladores (`CandidatoController`, `PartidoPoliticoController`, `VotoController`) y servicios para la lógica de negocio.
- Los datos iniciales están en formato JSON dentro de `src/main/java/com/pinnguino/academy/segunda_evaluacion/repository/data/`.
- Usa Spring Data JPA y una base de datos H2 en memoria para ejecución local.

## Puntos clave
- Lenguaje: Java 17
- Framework: Spring Boot
- Dependencias principales: Spring Web, Spring Data JPA, H2, Lombok (opcional)

---

## Estructura
- Controladores: `com.pinnguino.academy.segunda_evaluacion.controller`
- Modelos: `com.pinnguino.academy.segunda_evaluacion.model`
- Repositorios y datos de muestra: `com.pinnguino.academy.segunda_evaluacion.repository`
- Servicios: `com.pinnguino.academy.segunda_evaluacion.service`

Ejecutar localmente (Windows - PowerShell)
- Compilar y ejecutar con el wrapper Maven incluido:

```powershell
# Compilar
.\
wnw.cmd -q clean package

# Ejecutar la app
.\mvnw.cmd spring-boot:run
```

(Alternativa rápida para ejecutar sin empaquetar)

```powershell
.\mvnw.cmd spring-boot:run
```

> [!NOTE]
> Luego de correr el proyecto, en la colección de Postman hay 3 peticiones llamadas **init**. Esas peticiones insertan datos de prueba en la base de datos para probar el funcionamiento de los endpoints.

Ejecutar tests

```powershell
.\mvnw.cmd test
```

---

### Notas
- Los endpoints exactos y formatos de request/response están definidos en los controladores del proyecto. Revisa `controller/` para ver las rutas disponibles.
- La aplicación está pensada como proyecto didáctico para una evaluación; es mínima y fácil de extender.

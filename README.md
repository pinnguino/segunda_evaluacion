# Academy 2025 | Segunda Evaluación: Sistema de Gestión de Votaciones

Proyecto de ejemplo (Java 17, Spring Boot) para gestionar candidaturas, partidos políticos y votos.

## Descripción
- Es una API REST desarrollada con Spring Boot que permite gestionar entidades básicas de una votación: candidatos, partidos políticos y votos.
- Enfocada en el uso de buenas prácticas gracias al analizador SonarQube.
- Contiene controladores (`CandidatoController`, `PartidoPoliticoController`, `VotoController`) y servicios para la lógica de negocio.
- Los datos iniciales están en formato JSON dentro de `src/main/java/com/pinnguino/academy/segunda_evaluacion/repository/data/`.
- Usa Spring Data JPA y una base de datos H2 en memoria para ejecución local.

## Tech stack
- Lenguaje: Java 17
- Framework: Spring Boot
- Dependencias principales: Spring Web, Spring Data JPA, H2, Lombok (opcional)
- Testing: JUnit 5 y Mockito para pruebas unitarias
- Documentación: OpenAPI/Swagger para documentación de APIs

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
.\mvnw.cmd -q clean package

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

# Documentación de la API
La API está completamente documentada usando OpenAPI/Swagger. Puedes acceder a la documentación desde SwaggerUI con accediento al siguiente enlace con la aplicación levantada:

```
http://localhost:8080/swagger-ui/index.html
```

## Tests Unitarios
El proyecto incluye una suite completa de pruebas unitarias implementadas con:
- **JUnit 5**: Framework de testing
- **Mockito**: Framework de mocking para simular dependencias

Los tests cubren:
- **Controladores**: Pruebas de endpoints REST
- **Servicios**: Pruebas de lógica de negocio
- **Casos de éxito y error**: Validación de flujos normales y excepcionales

Ubicación de los tests:

```
src/test/java/com/pinnguino/academy/segunda_evaluacion/
├── controller/
│   ├── CandidatoControllerTest.java
│   ├── PartidoPoliticoControllerTest.java
│   └── VotoControllerTest.java
└── service/
    ├── CandidatoServiceTest.java
    ├── PartidoPoliticoServiceTest.java
    └── VotoServiceTest.java
```

---

# Buenas prácticas

A continuación se ve una captura de pantalla de el análisis de SonarQube para todo el proyecto. No se encontraron problemas.
![SonarQube](/assets/sonarQube-screenshot.png)
# Spaceships
**CRUD de Spaceships** con **Arquitectura Hexagonal**.

## 📋 Características del proyecto
### Gestión de calidad (Checkstyle)

Con el siguiente comando se ejecutará el proceso de análisis de código Checkstyle que comprobará si el código fuente
cumple con las reglas de codificación.
```
mvn checkstyle:checkstyle
```

### Documentación de la API

Documentación referente a la API [Spaceship API.](http://localhost:8080/swagger-ui/index.html#/)

### PIT Mutation Testing

Para poder generar el informe de **PITest** referente al proyecto será necesario hacerlo usando el siguiente comando:
```
mvn test-compile org.pitest:pitest-maven:mutationCoverage
```

Esto generará una carpeta (pit-reports) dentro del target del proyecto. Dicha carpeta a su vez tendrá un fichero
index.html que se podrá abrir en el navegador y explorar el informe de testing generado.

### ArchUnit
Biblioteca para definir y hacer cumplir la arquitectura de la aplicación.

### Seguridad de la API

- `ROLE:` User (consultar todas las naves espaciales, consultar naves espaciales por id, consultar naves espaciales que
contengan un substring).
    - `Credenciales:` user, userPass.
- `ROLE:` Admin (todas las operaciones definidas).
    - `Credenciales:` admin, adminPass.


## ️ ⚙️ Ejecución ️y configuración

Para ejecutar la aplicación bien se puede hacer accediendo a la clase [SpaceshipsApplication](src%2Fmain%2Fjava%2Fcom%2Fw2m%2Fspaceships%2FSpaceshipsApplication.java) o bien ejecutando
los siguientes comandos.

### Instalación del artefacto en local.
```
mvn clean install
```

### Empaquetado el proyecto
```
mvn clean package
```

### Ejecución del proyecto
```
mvn clean spring-boot:run
```

### Ejecución de docker-compose
```
docker-compose up
```

### Ejecución del proyecto exceptuando tests
```
mvn clean package -DskipTests
```

### Ejecución de tests
```
mvn test
```

###  Base de datos en memoria h2

La ejecución de la API habilitará una base de datos en memoria [h2 database](http://localhost:8080/h2-console).

`JDBC URL:`jdbc:h2:mem:spaceshipsdb

`Username:` sa

`Password:` pass



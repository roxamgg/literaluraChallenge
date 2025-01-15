# Proyecto Literalura

## Descripción

**Literalura** es una aplicación desarrollada en Java utilizando Spring Boot que permite interactuar con libros y autores a través de una base de datos local y una API externa (Gutenberg). La aplicación proporciona diversas funcionalidades como la búsqueda de libros y autores, la obtención de estadísticas de descargas y la gestión de la base de datos de libros y autores registrados.

## Funcionalidades

La aplicación ofrece un menú interactivo para realizar las siguientes acciones:

1. **Buscar libro por título**: Permite buscar un libro por su título, ya sea en la base de datos o a través de la API de Gutenberg.
2. **Buscar autor por nombre**: Permite buscar un autor por su nombre en la base de datos.
3. **Listar libros registrados**: Muestra una lista de todos los libros almacenados en la base de datos.
4. **Listar autores registrados**: Muestra una lista de todos los autores registrados en la base de datos.
5. **Listar autores vivos en un determinado año**: Permite encontrar autores vivos en un año específico proporcionado por el usuario.
6. **Listar libros por idioma**: Permite filtrar los libros registrados por idioma (Español, Francés, Inglés, Portugués).
7. **Top 10 libros más buscados**: Muestra los 10 libros más buscados según los datos de la base de datos.
8. **Generar estadísticas de descargas de libros**: Proporciona estadísticas sobre el número de descargas de los libros registrados, como la media, máximo y mínimo de descargas.

## Tecnologías utilizadas

Este proyecto está construido con las siguientes tecnologías:

- **Java 21**: La última versión estable de Java.
- **Spring Boot**: Framework para crear aplicaciones Java basadas en Spring.
- **JPA (Java Persistence API)**: Para interactuar con la base de datos.
- **PostgreSQL**: Base de datos relacional utilizada para almacenar libros y autores.
- **Jackson**: Biblioteca para la conversión de objetos Java a JSON y viceversa.
- **Gutenberg API**: API externa para obtener información sobre libros de dominio público.

## Estructura del Proyecto

El proyecto se organiza en los siguientes paquetes:

1. **`com.example.literalura`**: Contiene la clase principal que inicia la aplicación.
2. **`com.example.literalura.principal`**: Contiene la lógica principal que maneja la interacción con el usuario.
3. **`com.example.literalura.model`**: Contiene las clases modelo que representan los datos de los libros y autores.
4. **`com.example.literalura.repository`**: Contiene las interfaces para interactuar con la base de datos.
5. **`com.example.literalura.service`**: Contiene los servicios que consumen la API externa y procesan los datos.

## Instalación

### Requisitos

Antes de ejecutar el proyecto, asegúrate de tener instalados:

- **Java 21**
- **Maven**
- **PostgreSQL**

### Pasos para ejecutar la aplicación

1. **Clonar el repositorio**.
2. **Configuración base datos**:
Antes de ejecutar el proyecto, asegúrate de que tienes una base de datos PostgreSQL configurada y en funcionamiento. Crea una base de datos, por ejemplo,       literalura_db, y asegúrate de tener las credenciales correctas.
3. **Modificar la configuración de la base de datos**:   
Abre el archivo src/main/resources/application.properties y modifica las siguientes líneas para conectar la aplicación con tu base de datos PostgreSQL:
  - spring.datasource.url=jdbc:postgresql://localhost:5432/literalura_db
  - spring.datasource.username=tu_usuario
  - spring.datasource.password=tu_contraseña
  - spring.datasource.driver-class-name=org.postgresql.Driver
  - spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
  - spring.jpa.hibernate.ddl-auto=update
  - spring.jpa.show-sql=true
    
**Asegúrate de reemplazar tu_usuario, tu_contraseña y literalura_db con las variables de entorno correspondientes a la configuración de PostgreSQL.**
4. **Compilar y ejecutar el proyecto**.
5. **Acceder a la aplicación**:
Una vez que la aplicación esté ejecutándose, podrás interactuar con ella a través de la consola. Se te presentará un menú para seleccionar las diferentes opciones disponibles.

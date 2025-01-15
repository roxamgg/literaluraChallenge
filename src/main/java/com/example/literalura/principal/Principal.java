package com.example.literalura.principal;

import com.example.literalura.model.*;
import com.example.literalura.repository.AutorRepository;
import com.example.literalura.service.ConsumoAPI;
import com.example.literalura.service.ConvierteDatos;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private String URL_BASE = "https://gutendex.com/books/";
    private AutorRepository repository;

    public Principal(AutorRepository repository) {
        this.repository = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        var menu = """
            
            ----- Bienvenido(a)📚 -----
            -------------------------------------
            1 - Buscar libro por título
            2 - Buscar autor por nombre
            3 - Listar libros registrados
            4 - Listar autores registrados
            5 - Listar autores vivos en un determinado año
            6 - Listar libros por idioma
            7 - Top 10 libros más buscados
            8 - Generar estadísticas descarga libros
            -------------------------------------
            0 - SALIR
            ----------------------------------------------
            Seleccione una opción:
            """;

        while (opcion != 0) {
            System.out.println(menu);
            try {
                opcion = Integer.valueOf(teclado.nextLine());
                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        buscarAutorPorNombre();
                        break;
                    case 3:
                        listarLibrosRegistrados();
                        break;
                    case 4:
                        listarAutoresRegistrados();
                        break;
                    case 5:
                        listarAutoresVivos();
                        break;
                    case 6:
                        listarLibrosPorIdioma();
                        break;
                    case 7:
                        buscarTop10Libros();
                        break;
                    case 8:
                        generarEstadisticas();
                        break;
                    case 0:
                        System.out.println("Cerrando la aplicacion...");
                        break;
                    default:
                        System.out.println("Opción inválida!");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida: " + e.getMessage());

            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Escribe el nombre del libro que desea buscar:");
        var nombre = teclado.nextLine();

        try {
            var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombre.replace(" ", "+").toLowerCase());

            if (!json.isEmpty()) {
                //Se obtienen los resultados del API.
                var datos = conversor.obtenerDatos(json, DatosAPI.class);
                //Se obtiene el primer libro encontrado.
                Optional<DatosLibro> libroBuscadoAPI = datos.libros().stream().findFirst();
                if (libroBuscadoAPI.isPresent()) {
                    DatosLibro libro = libroBuscadoAPI.get();

                    //Se obtiene el autor del libro, si no tiene, se utiliza un valor por default.
                    Optional<Autor> autorAPI = libro.autores().stream()
                            .map(Autor::new)
                            .findFirst()
                            .or(() -> Optional.of(new Autor(new DatosAutor("Desconocido", null, null))));

                    //Se consulta si el libro se encuentra en BD.
                    Optional<Libro> libroBD = repository.buscarLibroPorNombre(libro.titulo());
                    if (libroBD.isPresent()) {
                        System.out.println("No se puede guardar el libro más de una vez.");
                    } else {
                        //Se consulta si el autor se encuentra en BD.
                        Optional<Autor> autorBD = repository.findByNombreContainsIgnoreCase(autorAPI.get().getNombre());
                        Autor autor = autorBD.orElse(autorAPI.get()); //Si esta presente el autorBD se utiliza, de lo contrario se utiliza autorAPI.
                        //Se asigna el libro al autor.
                        autor.setLibros(libroBuscadoAPI.stream().map(a -> new Libro(a)).collect(Collectors.toList()));
                        repository.save(autor);
                        //Se muestra la información del libro.
                        System.out.println(
                                "\n------------- LIBRO--------------" +
                                "\nTítulo: " + libroBuscadoAPI.get().titulo() +
                                "\nAutor: " + libroBuscadoAPI.get().autores().stream()
                                .map(a -> a.nombre()).limit(1).collect(Collectors.joining()) +
                                "\nIdioma: " + libroBuscadoAPI.get().idiomas().stream().collect(Collectors.joining()) +
                                "\nNúmero de descargas: " + libroBuscadoAPI.get().numeroDescargas() +
                                "\n--------------------------------------"
                        );
                    }
                }
                else {
                    System.out.println("Libro no encontrado!");
                }
            }
            else {
                System.out.println("Libro no encontrado!");
            }
        }
        catch (Exception e) {
            System.out.println("Warning! " + e.getMessage());
        }
    }

    private void buscarAutorPorNombre() {
        System.out.println("Ingrese el nombre del autor que desea buscar:");
        var nombre = teclado.nextLine();
        Optional<Autor> autor = repository.buscarAutorPorNombre(nombre);
        if (autor.isPresent()) {
            System.out.println(
                    "\n-------------- AUTOR -----------------" +
                    "\nAutor: " + autor.get().getNombre() +
                    "\nFecha nacimiento: " + autor.get().getNacimiento() +
                    "\nFecha fallecimiento: " + autor.get().getFallecimiento() +
                    "\nLibros: " + autor.get().getLibros().stream()
                    .map(t -> t.getTitulo()).collect(Collectors.toList()) +
                    "\n----------------------------------------"
            );
        } else {
            System.out.println("El autor no está registrado en la base de datos.");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = repository.buscarLibrosRegistrados();
        if (!libros.isEmpty()) {
            libros.forEach(l -> System.out.println(
                    "\n-------------- LIBRO -----------------" +
                    "\nTítulo: " + l.getTitulo() +
                    "\nAutor: " + l.getAutor().getNombre() +
                    "\nIdioma: " + l.getIdioma() +
                    "\nNúmero descargas: " + l.getNumeroDescargas() +
                    "\n----------------------------------------"
            ));
        } else {
            System.out.println("No existen libros registrados.");
        }

    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = repository.findAll();
        if (!autores.isEmpty()) {
            autores.forEach(a -> System.out.println(
                    "\n-------------- AUTOR -----------------" +
                    "\nAutor: " + a.getNombre() +
                    "\nFecha nacimiento: " + a.getNacimiento() +
                    "\nFecha fallecimiento: " + a.getFallecimiento() +
                    "\nLibros: " + a.getLibros().stream()
                    .map(t -> t.getTitulo()).collect(Collectors.toList()) +
                    "\n----------------------------------------"
            ));
        } else {
            System.out.println("No existen autores registrados.");
        }
    }

    private void listarAutoresVivos() {
        System.out.println("Ingrese un año para buscar los autor(es) vivos:");
        var fecha = Integer.valueOf(teclado.nextLine());

        try {
            List<Autor> autores = repository.buscarAutoresVivos(fecha);
            if (!autores.isEmpty()) {
                autores.forEach(a -> System.out.println(
                        "\n-------------- AUTOR -----------------" +
                        "\nAutor: " + a.getNombre() +
                        "\nFecha nacimiento: " + a.getNacimiento() +
                        "\nFecha fallecimiento: " + a.getFallecimiento() +
                        "\nLibros: " + a.getLibros().stream()
                        .map(t -> t.getTitulo()).collect(Collectors.toList()) +
                        "\n----------------------------------------"
                ));
            } else {
                System.out.println("No existen autores vivos en el año indicado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ingrese un año válido " + e.getMessage());
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                    ---------------------------------------------------
                    Ingrese el idioma del libro que desea buscar:
                    ---------------------------------------------------
                    1 - Español
                    2 - Francés
                    3 - Inglés
                    4 - Portugués
                    ----------------------------------------------------
                    """);
        try {
            var idioma = Integer.parseInt(teclado.nextLine());

            switch (idioma) {
                case 1:
                    buscarLibrosPorIdioma("es");
                    break;
                case 2:
                    buscarLibrosPorIdioma("fr");
                    break;
                case 3:
                    buscarLibrosPorIdioma("en");
                    break;
                case 4:
                    buscarLibrosPorIdioma("pt");
                    break;
                default:
                    System.out.println("Opción inválida!");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ingrese una opción válida: " + e.getMessage());
        }
    }

    private void buscarLibrosPorIdioma(String opcion) {
        try{
            var idioma = Idioma.fromAbre(opcion);
            List<Libro> libros = repository.buscarLibrosPorIdioma(idioma);
            if (libros.isEmpty()) {
                System.out.println("No existen libros registrados con ese idioma.");
            } else {
                System.out.println("\nLos libros del idioma indicado son:");
                libros.forEach(l -> System.out.println(
                        "\n-------------- LIBRO -----------------" +
                        "\nTítulo: " + l.getTitulo() +
                        "\nAutor: " + l.getAutor().getNombre() +
                        "\nIdioma: " + l.getIdioma() +
                        "\nNúmero descargas: " + l.getNumeroDescargas() +
                        "\n----------------------------------------"
                ));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void buscarTop10Libros() {
        List<Libro> libros = repository.top10Libros();
        libros.forEach(l -> System.out.println(
                "\n-------------- LIBRO -----------------" +
                "\nTítulo: " + l.getTitulo() +
                "\nAutor: " + l.getAutor().getNombre() +
                "\nIdioma: " + l.getIdioma() +
                "\nNúmero descargas: " + l.getNumeroDescargas() +
                "\n----------------------------------------"
        ));
    }

    private void generarEstadisticas() {
        try {
            List<Libro> libros = repository.buscarLibrosRegistrados();
            if (!libros.isEmpty()) {
                IntSummaryStatistics st = libros.stream()
                        .filter(l -> l.getNumeroDescargas() > 0)
                        .collect(Collectors.summarizingInt(Libro::getNumeroDescargas));
                Integer media = (int) st.getAverage();
                System.out.println("\n--------- ESTADÍSTICAS DE DESCARGAS LIBROS ------------" +
                "\nMedia descargas: " + media +
                "\nMáxima descargas: " + st.getMax() +
                "\nMínima descargas: " + st.getMin() +
                "\nTotal registros de cálculo de estadísticas: " + st.getCount() +
                "\n---------------------------------------------------");
            }
            else {
                System.out.println("No existen libros registrados en la base de datos para el cálculo de estadísticas de descargas.");
            }
        } catch(Exception e) {
            System.out.println("Warning! " + e.getMessage());
        }
    }
}

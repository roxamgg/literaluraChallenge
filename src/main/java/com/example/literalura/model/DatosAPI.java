package com.example.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAPI(
        @JsonAlias("count") Integer total,
        @JsonAlias("results") List<DatosLibro> libros) {
}

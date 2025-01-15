package com.example.literalura.model;

public enum Idioma {
    ES("es", "Español"),
    FR("fr", "Francés"),
    EN("en", "Inglés"),
    PT("pt", "Portugués");

    private String idiomaAbre;
    private String idiomaCompleto;

    Idioma (String idioma, String idiomaCompleto){
        this.idiomaAbre = idioma;
        this.idiomaCompleto = idiomaCompleto;
    }

    public static Idioma fromAbre(String text) {
        for (Idioma item : Idioma.values()) {
            if (item.idiomaAbre.equalsIgnoreCase(text)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Ninguna idioma encontrado con la abreviatura: " + text);
    }

    public static Idioma fromCompleta(String text) {
        for (Idioma item : Idioma.values()) {
            if (item.idiomaCompleto.equalsIgnoreCase(text)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Ninguna idioma encontrado con: " + text);
    }
}

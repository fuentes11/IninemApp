package com.ininem.ininemapp;


import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Product implements Serializable {

    @Exclude
    private String id;

    private String Nombre_completo, Correo_electronico, isUser;


    public Product() {

    }

    public Product(String Nombre_completo, String Correo_electronico, String isUser) {
        this.Nombre_completo = Nombre_completo;
        this.Correo_electronico = Correo_electronico;
        this.isUser = isUser;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre_completo() {
        return Nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        Nombre_completo = nombre_completo;
    }

    public String getCorreo_electronico() {
        return Correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        Correo_electronico = correo_electronico;
    }

    public String getIsUser() {
        return isUser;
    }

    public void setIsUser(String isUser) {
        this.isUser = isUser;
    }
}

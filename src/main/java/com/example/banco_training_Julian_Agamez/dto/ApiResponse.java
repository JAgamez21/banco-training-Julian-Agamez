package com.example.banco_training_Julian_Agamez.dto;

public class ApiResponse<T> {
    private int status;
    private String codigo;
    private String mensaje;
    private T data;

    public ApiResponse(int status, String codigo, String mensaje, T data) {
        this.status = status;
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

package com.example.microserviciomapas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String asunto;
    private String descripcion;
    private String direccion;
    private String estado;
    private String zona;
    private String prioridad;
    private Double latitud;
    private Double longitud;
    private Boolean activo;

    @Column(name = "id_tipo")
    private Long idTipo;

    @Column(name = "cluster_id")
    private Long clusterId;

    // Getters
    public Long getId() { return id; }
    public String getAsunto() { return asunto; }
    public String getDescripcion() { return descripcion; }
    public String getDireccion() { return direccion; }
    public String getEstado() { return estado; }
    public String getZona() { return zona; }
    public String getPrioridad() { return prioridad; }
    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
    public Boolean getActivo() { return activo; }
    public Long getIdTipo() { return idTipo; }
    public Long getClusterId() { return clusterId; }
}
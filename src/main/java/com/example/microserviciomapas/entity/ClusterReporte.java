package com.example.microserviciomapas.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clusters_reporte")
public class ClusterReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean activo;

    @Column(name = "estado_cluster")
    private String estadoCluster;

    @Column(name = "latitud_centroide")
    private Double latitudCentroide;

    @Column(name = "longitud_centroide")
    private Double longitudCentroide;

    @Column(name = "nombre_area")
    private String nombreArea;

    @Column(name = "nombre_tipo_reporte")
    private String nombreTipoReporte;

    @Column(name = "total_reportes")
    private Integer totalReportes;

    @Column(name = "ids_reportes")
    private String idsReportes;

    // Getters
    public Long getId() { return id; }
    public Boolean getActivo() { return activo; }
    public String getEstadoCluster() { return estadoCluster; }
    public Double getLatitudCentroide() { return latitudCentroide; }
    public Double getLongitudCentroide() { return longitudCentroide; }
    public String getNombreArea() { return nombreArea; }
    public String getNombreTipoReporte() { return nombreTipoReporte; }
    public Integer getTotalReportes() { return totalReportes; }
    public String getIdsReportes() { return idsReportes; }
}
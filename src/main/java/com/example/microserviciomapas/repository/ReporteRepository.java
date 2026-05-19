package com.example.microserviciomapas.repository;
import com.example.microserviciomapas.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    @Query("SELECT r FROM Reporte r WHERE r.clusterId IS NULL AND r.activo = true")
    List<Reporte> findReportesSinCluster();

    @Query("SELECT r FROM Reporte r WHERE r.clusterId IS NULL AND r.activo = true AND r.zona = :zona")
    List<Reporte> findReportesSinClusterPorZona(@Param("zona") String zona);

    @Query("SELECT r FROM Reporte r WHERE r.activo = true AND r.latitud IS NOT NULL AND r.longitud IS NOT NULL")
    List<Reporte> findTodosParaCalor();
}
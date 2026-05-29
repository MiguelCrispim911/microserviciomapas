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

        @Query(value = """
                SELECT r.*
                FROM reportes r
                JOIN tipos_reporte t ON t.id = r.id_tipo
                WHERE r.cluster_id IS NULL
                    AND r.activo = true
                    AND (:zona IS NULL OR :zona = '' OR r.zona = :zona)
                    AND (:estado IS NULL OR :estado = '' OR LOWER(r.estado) = LOWER(:estado))
                    AND (:tipoReporte IS NULL OR :tipoReporte = '' OR LOWER(t.nombre) = LOWER(:tipoReporte))
                """, nativeQuery = true)
        List<Reporte> findReportesSinClusterFiltrados(
                        @Param("zona") String zona,
                        @Param("estado") String estado,
                        @Param("tipoReporte") String tipoReporte);

    @Query("SELECT r FROM Reporte r WHERE r.activo = true AND r.latitud IS NOT NULL AND r.longitud IS NOT NULL")
    List<Reporte> findTodosParaCalor();

        @Query(value = """
            SELECT r.*
            FROM reportes r
            JOIN tipos_reporte t ON t.id = r.id_tipo
            WHERE r.activo = true
              AND r.latitud IS NOT NULL
              AND r.longitud IS NOT NULL
              AND (:zona IS NULL OR :zona = '' OR r.zona = :zona)
              AND (:tipoReporte IS NULL OR :tipoReporte = '' OR LOWER(t.nombre) = LOWER(:tipoReporte))
            """, nativeQuery = true)
        List<Reporte> findTodosParaCalorFiltrados(
            @Param("zona") String zona,
            @Param("tipoReporte") String tipoReporte);
}
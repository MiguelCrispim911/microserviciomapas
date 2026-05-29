package com.example.microserviciomapas.repository;

import com.example.microserviciomapas.entity.ClusterReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ClusterRepository extends JpaRepository<ClusterReporte, Long> {

    List<ClusterReporte> findByActivoTrue();

    List<ClusterReporte> findByNombreAreaAndActivoTrue(String nombreArea);

        @Query(value = """
                SELECT c.*
                FROM clusters_reporte c
                WHERE c.activo = true
                    AND (:zona IS NULL OR :zona = '' OR c.nombre_area = :zona)
                    AND (:estado IS NULL OR :estado = '' OR LOWER(c.estado_cluster) = LOWER(:estado))
                    AND (:tipoReporte IS NULL OR :tipoReporte = '' OR LOWER(c.nombre_tipo_reporte) = LOWER(:tipoReporte))
                """, nativeQuery = true)
        List<ClusterReporte> findClustersFiltrados(
                        @Param("zona") String zona,
                        @Param("estado") String estado,
                        @Param("tipoReporte") String tipoReporte);
}
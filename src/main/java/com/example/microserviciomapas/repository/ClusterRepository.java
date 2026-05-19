package com.microserviciomapas.repository;

import com.microserviciomapas.entity.ClusterReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClusterRepository extends JpaRepository<ClusterReporte, Long> {

    List<ClusterReporte> findByActivoTrue();

    List<ClusterReporte> findByNombreAreaAndActivoTrue(String nombreArea);
}
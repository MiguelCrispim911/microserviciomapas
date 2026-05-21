package com.example.microserviciomapas.service;

import com.example.microserviciomapas.entity.ClusterReporte;
import com.example.microserviciomapas.entity.Reporte;
import com.example.microserviciomapas.repository.ClusterRepository;
import com.example.microserviciomapas.repository.ReporteRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MapaService {

    private final ReporteRepository reporteRepo;
    private final ClusterRepository clusterRepo;

    public MapaService(ReporteRepository reporteRepo, ClusterRepository clusterRepo) {
        this.reporteRepo = reporteRepo;
        this.clusterRepo = clusterRepo;
    }

    public Map<String, Object> getMapaGeneral(String area, String estado) {
        List<Map<String, Object>> features = new ArrayList<>();
        String estadoFiltro = estado == null ? "" : estado.trim().toLowerCase();
        boolean filtrarEstado = !estadoFiltro.isEmpty() && !"todos".equals(estadoFiltro);

        List<Reporte> reportes = (area != null && !area.isEmpty())
            ? reporteRepo.findReportesSinClusterPorZona(area)
            : reporteRepo.findReportesSinCluster();

        for (Reporte r : reportes) {
            if (filtrarEstado && (r.getEstado() == null || !r.getEstado().trim().equalsIgnoreCase(estadoFiltro))) {
                continue;
            }
            if (r.getLatitud() != null && r.getLongitud() != null) {
                Map<String, Object> props = new HashMap<>();
                props.put("id", r.getId());
                props.put("tipo", "reporte");
                props.put("asunto", r.getAsunto());
                props.put("estado", r.getEstado());
                props.put("direccion", r.getDireccion());
                features.add(buildFeature(r.getLongitud(), r.getLatitud(), props));
            }
        }

        List<ClusterReporte> clusters = (area != null && !area.isEmpty())
            ? clusterRepo.findByNombreAreaAndActivoTrue(area)
            : clusterRepo.findByActivoTrue();

        for (ClusterReporte c : clusters) {
            if (filtrarEstado && (c.getEstadoCluster() == null || !c.getEstadoCluster().trim().equalsIgnoreCase(estadoFiltro))) {
                continue;
            }
            if (c.getLatitudCentroide() != null && c.getLongitudCentroide() != null) {
                Map<String, Object> props = new HashMap<>();
                props.put("id", c.getId());
                props.put("tipo", "cluster");
                props.put("total", c.getTotalReportes());
                props.put("nombre", c.getNombreTipoReporte());
                props.put("area", c.getNombreArea());
                features.add(buildFeature(c.getLongitudCentroide(), c.getLatitudCentroide(), props));
            }
        }

        return buildFeatureCollection(features);
    }

    public Map<String, Object> getReporte(Long id) {
        Reporte r = reporteRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado: " + id));

        Map<String, Object> props = new HashMap<>();
        props.put("id", r.getId());
        props.put("asunto", r.getAsunto());
        props.put("descripcion", r.getDescripcion());
        props.put("estado", r.getEstado());
        props.put("direccion", r.getDireccion());
        props.put("prioridad", r.getPrioridad());

        return buildFeatureCollection(List.of(
            buildFeature(r.getLongitud(), r.getLatitud(), props)
        ));
    }

    public Map<String, Object> getCluster(Long id) {
        ClusterReporte c = clusterRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Cluster no encontrado: " + id));

        List<Map<String, Object>> features = new ArrayList<>();

        Map<String, Object> propsC = new HashMap<>();
        propsC.put("id", c.getId());
        propsC.put("tipo", "centroide");
        propsC.put("total", c.getTotalReportes());
        propsC.put("nombre", c.getNombreTipoReporte());
        features.add(buildFeature(c.getLongitudCentroide(), c.getLatitudCentroide(), propsC));

        if (c.getIdsReportes() != null && !c.getIdsReportes().isEmpty()) {
            String[] ids = c.getIdsReportes().split(",");
            for (String rid : ids) {
                try {
                    reporteRepo.findById(Long.parseLong(rid.trim())).ifPresent(r -> {
                        if (r.getLatitud() != null && r.getLongitud() != null) {
                            Map<String, Object> props = new HashMap<>();
                            props.put("id", r.getId());
                            props.put("tipo", "reporte_cluster");
                            props.put("asunto", r.getAsunto());
                            props.put("estado", r.getEstado());
                            features.add(buildFeature(r.getLongitud(), r.getLatitud(), props));
                        }
                    });
                } catch (NumberFormatException e) {
                    // ignorar IDs malformados
                }
            }
        }

        return buildFeatureCollection(features);
    }

    public Map<String, Object> getCalor(String area) {
        List<Reporte> reportes = reporteRepo.findTodosParaCalor();
        List<Map<String, Object>> features = new ArrayList<>();

        for (Reporte r : reportes) {
            if (area == null || area.isEmpty() || area.equals(r.getZona())) {
                Map<String, Object> props = new HashMap<>();
                props.put("weight", 1);
                features.add(buildFeature(r.getLongitud(), r.getLatitud(), props));
            }
        }

        return buildFeatureCollection(features);
    }

    private Map<String, Object> buildFeature(Double lng, Double lat, Map<String, Object> props) {
        Map<String, Object> geometry = new LinkedHashMap<>();
        geometry.put("type", "Point");
        geometry.put("coordinates", new Double[]{lng, lat});

        Map<String, Object> feature = new LinkedHashMap<>();
        feature.put("type", "Feature");
        feature.put("geometry", geometry);
        feature.put("properties", props);
        return feature;
    }

    private Map<String, Object> buildFeatureCollection(List<Map<String, Object>> features) {
        Map<String, Object> fc = new LinkedHashMap<>();
        fc.put("type", "FeatureCollection");
        fc.put("features", features);
        return fc;
    }
}
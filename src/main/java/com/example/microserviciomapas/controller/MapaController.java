package com.example.microserviciomapas.controller;

import com.example.microserviciomapas.service.MapaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/mapas")
public class MapaController {

    private final MapaService mapaService;

    public MapaController(MapaService mapaService) {
        this.mapaService = mapaService;
    }

    @GetMapping("/reportes")
    public ResponseEntity<Map<String, Object>> getMapaGeneral(
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String estado) {
        return ResponseEntity.ok(mapaService.getMapaGeneral(area, estado));
    }

    @GetMapping("/reporte/{id}")
    public ResponseEntity<Map<String, Object>> getReporte(@PathVariable Long id) {
        return ResponseEntity.ok(mapaService.getReporte(id));
    }

    @GetMapping("/cluster/{id}")
    public ResponseEntity<Map<String, Object>> getCluster(@PathVariable Long id) {
        return ResponseEntity.ok(mapaService.getCluster(id));
    }

    @GetMapping("/calor")
    public ResponseEntity<Map<String, Object>> getCalor(
            @RequestParam(required = false) String area) {
        return ResponseEntity.ok(mapaService.getCalor(area));
    }
}
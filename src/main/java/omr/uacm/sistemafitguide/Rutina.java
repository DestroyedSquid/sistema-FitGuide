/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package omr.uacm.sistemafitguide;


import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author migue
 */
public class Rutina {
    private String grupoMuscular;
    private String tipoPeso;
    private String nivel;
    private int tiempoDescanso;
    private Map<String, Integer> mapaRepeticiones;

    public Rutina(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
        this.mapaRepeticiones = new HashMap<>();
        this.tiempoDescanso = 30; // Descanso por defecto de 30 segundos
    }


    public String getGrupoMuscular() { return grupoMuscular; }
    public void setGrupoMuscular(String grupoMuscular) { this.grupoMuscular = grupoMuscular; }

    public String getTipoPeso() { return tipoPeso; }
    public void setTipoPeso(String tipoPeso) { this.tipoPeso = tipoPeso; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public int getTiempoDescanso() { return tiempoDescanso; }
    public void setTiempoDescanso(int tiempoDescanso) { this.tiempoDescanso = tiempoDescanso; }

    public Map<String, Integer> getMapaRepeticiones() { return mapaRepeticiones; }
    public void setMapaRepeticiones(Map<String, Integer> mapaRepeticiones) { this.mapaRepeticiones = mapaRepeticiones; }
}
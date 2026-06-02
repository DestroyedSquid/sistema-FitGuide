/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package omr.uacm.sistemafitguide;
import java.util.List;
import java.util.ArrayList;

/**
 * JavaFX App
 */

public class GestionRutina {
    private static GestionRutina instancia;
    private List<Ejercicio> listaCargada = new ArrayList<>();
    private int ejercicioActual = 1;
    private int totalRepeticionesReales = 0;
    private String musculoSeleccionado = "";
    private String nivelSeleccionado = "";
    private boolean usaMancuernas = false;
    private String llaveRutina = ""; 

    private GestionRutina() {}

    public static GestionRutina getInstance() {
        if (instancia == null) { instancia = new GestionRutina(); }
        return instancia;
    }

    public void configurarRutina() {
        this.listaCargada = CatalogoEjercicios.obtenerRutina(musculoSeleccionado, nivelSeleccionado, usaMancuernas);
        if (this.listaCargada != null && !this.listaCargada.isEmpty()) {
            Rutina configUsuario = PantallaConfiguracionRutinaController.mapaRutinas.get(llaveRutina);
            if (configUsuario != null && configUsuario.getMapaRepeticiones() != null) {
                for (Ejercicio ej : listaCargada) {
                    if (configUsuario.getMapaRepeticiones().containsKey(ej.getNombre())) {
                        ej.setRepeticionesObjetivo(configUsuario.getMapaRepeticiones().get(ej.getNombre()));
                    }
                }
            }
        }
        this.ejercicioActual = 1;
        this.totalRepeticionesReales = 0;
    }

   
    public int getTiempoDescansoConfigurado() {
        Rutina r = PantallaConfiguracionRutinaController.mapaRutinas.get(llaveRutina);
        return (r != null) ? r.getTiempoDescanso() : 30;
    }

    
    public Ejercicio getEjercicioActualData() {
        if (listaCargada != null && !listaCargada.isEmpty() && ejercicioActual <= listaCargada.size()) {
            return listaCargada.get(ejercicioActual - 1);
        }
        return null;
    }
    public Ejercicio getSiguienteEjercicioData() {
        if (listaCargada != null && ejercicioActual < listaCargada.size()) {
            return listaCargada.get(ejercicioActual);
        }
        return null;
    }
    public String obtenerSiguientePantalla() {
        return (ejercicioActual >= getTotalEjercicios()) ? "PantallaResumen" : "PantallaDescanso";
    }
    public int getTotalEjercicios() { return listaCargada != null ? listaCargada.size() : 0; }
    public int getEjercicioActual() { return ejercicioActual; }
    public void avanzarAlSiguienteEjercicio() { if (ejercicioActual < getTotalEjercicios()) { ejercicioActual++; } }
    public void sumarRepeticiones(int reps) { if (reps > 0) { this.totalRepeticionesReales += reps; } }
    public int getTotalRepeticionesReales() { return totalRepeticionesReales; }
    public void reiniciarRutina() {
        this.ejercicioActual = 1;
        this.totalRepeticionesReales = 0;
        this.listaCargada.clear();
    }
    public String getMusculoSeleccionado() { return musculoSeleccionado; }
    public String getNivelSeleccionado() { return nivelSeleccionado; }
    public boolean isUsaMancuernas() { return usaMancuernas; }
    public void setMusculoSeleccionado(String m) { this.musculoSeleccionado = m; }
    public void setNivelSeleccionado(String n) { this.nivelSeleccionado = n; }
    public void setUsaMancuernas(boolean u) { this.usaMancuernas = u; }
    public void setLlaveRutina(String l) { this.llaveRutina = l; } 
    public String getLlaveRutina() { return llaveRutina; }         
}
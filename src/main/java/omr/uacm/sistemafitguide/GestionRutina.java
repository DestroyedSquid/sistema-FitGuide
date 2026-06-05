package omr.uacm.sistemafitguide;

import omr.uacm.sistemafitguide.controlador.PantallaConfiguracionRutinaController;
import java.util.List;
import java.util.ArrayList;

/**
 * JavaFX App - Gestor Secuencial de Rutinas (Singleton)
 */
public class GestionRutina {
    private static GestionRutina instancia;

    private List<Ejercicio> listaCargada = new ArrayList<>();
    private int ejercicioActual = 0; // Índice en Java empieza en 0
    private int totalRepeticionesReales = 0;

    // Variables de configuración de la rutina
    private String musculoSeleccionado = "";
    private String nivelSeleccionado = "";
    private boolean usaMancuernas = false;
    private String llaveRutina = "";
    private double pesoExtraKg = 0.0;
    private int seriesTotales = 2; // Por defecto 2 series
    private int serieActual = 1;

    // Sesión del usuario
    private int idUsuarioLogueado;

    private GestionRutina() {}

    public static GestionRutina getInstance() {
        if (instancia == null) {
            instancia = new GestionRutina();
        }
        return instancia;
    }

    public void configurarRutina() {
        // 1. Obtenemos los ejercicios base
        List<Ejercicio> ejerciciosBase = CatalogoEjercicios.obtenerRutina(musculoSeleccionado, nivelSeleccionado, usaMancuernas);

        // 2. Limpiamos todo para iniciar una rutina desde cero
        this.listaCargada.clear();
        this.ejercicioActual = 0;
        this.serieActual = 1;
        this.totalRepeticionesReales = 0;
        this.pesoExtraKg = 0.0;
        this.seriesTotales = 2;

        // 3. Obtenemos la configuración personalizada
        Rutina configUsuario = PantallaConfiguracionRutinaController.mapaRutinas.get(llaveRutina);

        if (configUsuario != null) {
            this.pesoExtraKg = configUsuario.getPesoExtraKg();

            if (configUsuario.getSeries() > 0) {
                this.seriesTotales = configUsuario.getSeries();
            }

            // Aplicamos las repeticiones personalizadas a los ejercicios base
            if (ejerciciosBase != null && configUsuario.getMapaRepeticiones() != null) {
                for (Ejercicio ej : ejerciciosBase) {
                    if (configUsuario.getMapaRepeticiones().containsKey(ej.getNombre())) {
                        ej.setRepeticionesObjetivo(configUsuario.getMapaRepeticiones().get(ej.getNombre()));
                    }
                }
            }
        }

        // 4. ¡LA MAGIA DEL CIRCUITO! Multiplicamos la lista según las series.
        if (ejerciciosBase != null && !ejerciciosBase.isEmpty()) {
            for (int s = 0; s < this.seriesTotales; s++) {
                for (Ejercicio ejBase : ejerciciosBase) {
                    // Copiamos el objeto para evitar conflictos de memoria
                    Ejercicio copiaEjercicio = new Ejercicio(
                            ejBase.getNombre(),
                            ejBase.getRutavideo(),
                            ejBase.getRutaImagen(),
                            ejBase.getIntrucciones(),
                            ejBase.getRepeticionesObjetivo()
                    );
                    this.listaCargada.add(copiaEjercicio);
                }
            }
        }
    }

    public int getTiempoDescansoConfigurado() {
        Rutina r = PantallaConfiguracionRutinaController.mapaRutinas.get(llaveRutina);
        return (r != null) ? r.getTiempoDescanso() : 30;
    }

    public Ejercicio getEjercicioActualData() {
        if (listaCargada != null && !listaCargada.isEmpty() && ejercicioActual < listaCargada.size()) {
            return listaCargada.get(ejercicioActual);
        }
        return null;
    }

    public Ejercicio getSiguienteEjercicioData() {
        if (listaCargada != null && (ejercicioActual + 1) < listaCargada.size()) {
            return listaCargada.get(ejercicioActual + 1);
        }
        return null;
    }

    public String obtenerSiguientePantalla() {
        ejercicioActual++;

        // Actualizamos en qué "serie real" va basándonos en el tamaño base
        List<Ejercicio> base = CatalogoEjercicios.obtenerRutina(musculoSeleccionado, nivelSeleccionado, usaMancuernas);
        if (base != null && !base.isEmpty()) {
            this.serieActual = (ejercicioActual / base.size()) + 1;
        }

        if (ejercicioActual >= getTotalEjercicios()) {
            return "PantallaResumen";
        }
        return "PantallaDescanso";
    }

    public void avanzarAlSiguienteEjercicio() {
        if (ejercicioActual < getTotalEjercicios()) {
            ejercicioActual++;
            List<Ejercicio> base = CatalogoEjercicios.obtenerRutina(musculoSeleccionado, nivelSeleccionado, usaMancuernas);
            if (base != null && !base.isEmpty()) {
                this.serieActual = (ejercicioActual / base.size()) + 1;
            }
        }
    }

    public int getTotalEjercicios() {
        return listaCargada != null ? listaCargada.size() : 0;
    }

    // Ojo: Para fines VISUALES (mostrar "Ejercicio 1", "Ejercicio 2")
    public int getEjercicioActual() {
        return ejercicioActual + 1;
    }

    public void sumarRepeticiones(int reps) {
        if (reps > 0) {
            this.totalRepeticionesReales += reps;
        }
    }

    public int getTotalRepeticionesReales() {
        return totalRepeticionesReales;
    }

    public void reiniciarRutina() {
        this.ejercicioActual = 0;
        this.serieActual = 1;
        this.totalRepeticionesReales = 0;
        this.listaCargada.clear();
        this.pesoExtraKg = 0.0;
    }

    // ==========================================
    // GETTERS Y SETTERS
    // ==========================================

    public String getMusculoSeleccionado() { return musculoSeleccionado; }
    public void setMusculoSeleccionado(String m) { this.musculoSeleccionado = m; }

    public String getNivelSeleccionado() { return nivelSeleccionado; }
    public void setNivelSeleccionado(String n) { this.nivelSeleccionado = n; }

    public boolean isUsaMancuernas() { return usaMancuernas; }
    public void setUsaMancuernas(boolean u) { this.usaMancuernas = u; }

    public String getLlaveRutina() { return llaveRutina; }
    public void setLlaveRutina(String l) { this.llaveRutina = l; }

    public int getIdUsuarioLogueado() { return idUsuarioLogueado; }
    public void setIdUsuarioLogueado(int idUsuarioLogueado) { this.idUsuarioLogueado = idUsuarioLogueado; }

    public double getPesoExtraKg() { return pesoExtraKg; }
    public void setPesoExtraKg(double pesoExtraKg) { this.pesoExtraKg = pesoExtraKg; }

    public int getSeriesTotales() { return seriesTotales; }
    public void setSeriesTotales(int seriesTotales) { this.seriesTotales = seriesTotales; }

    public int getSerieActual() { return Math.min(serieActual, seriesTotales); } // Para no pasar el límite en UI
}
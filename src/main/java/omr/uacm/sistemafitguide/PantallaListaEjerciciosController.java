/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

package omr.uacm.sistemafitguide;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author MiguelDiaz 
 */
public class PantallaListaEjerciciosController implements Initializable {

    @FXML private ComboBox<String> comboNivel;
    @FXML private ComboBox<String> comboEquipo; 
    
   
    @FXML private VBox panelInfoMusculo;
    @FXML private Label lblInfoTitulo;
    @FXML private Label lblInfoMensaje;
    
   @Override
public void initialize(URL url, ResourceBundle rb) {
    comboNivel.getItems().addAll("Básico", "Intermedio", "Avanzado");
    comboEquipo.getItems().addAll("Peso Propio", "Peso extra");

    
    String grupoActivo = PantallaConfiguracionRutinaController.ultimoGrupoSeleccionadoG;
    
    if (grupoActivo != null && !grupoActivo.isEmpty()) {
        
        // buscamos directamente con el nombre en mayuscula en el mapa
        if (PantallaConfiguracionRutinaController.mapaRutinas.containsKey(grupoActivo)) {
            Rutina rutinaActiva = PantallaConfiguracionRutinaController.mapaRutinas.get(grupoActivo);

            // ajustar Nivel en el combo
            if (rutinaActiva.getNivel() != null && !rutinaActiva.getNivel().isEmpty()) {
                comboNivel.setValue(rutinaActiva.getNivel());
            } else {
                comboNivel.setValue("Básico"); 
            }
            
            // ajustar Equipo/Peso en el combo
            if (rutinaActiva.getTipoPeso() != null && !rutinaActiva.getTipoPeso().isEmpty()) {
                String pesoAjustado = rutinaActiva.getTipoPeso();
                if (pesoAjustado.equalsIgnoreCase("Peso Extra")) pesoAjustado = "Peso extra";
                comboEquipo.setValue(pesoAjustado);
            } else {
                comboEquipo.setValue("Peso Propio"); 
            }
        }
    } else {
        comboNivel.setValue("Básico");
        comboEquipo.setValue("Peso Propio");
    }

    if (panelInfoMusculo != null) {
        panelInfoMusculo.setVisible(false);
    }
}

    @FXML public void iniciarBrazo(ActionEvent event) throws IOException { ejecutarRutina("Brazo"); }
    @FXML public void iniciarAbdomen(ActionEvent event) throws IOException { ejecutarRutina("Abdomen"); }
    @FXML public void iniciarPierna(ActionEvent event) throws IOException { ejecutarRutina("Pierna"); }

    private void ejecutarRutina(String musculo) throws IOException {
    // lee los combos que el usuario tiene seleccionados en pantalla
    String nivelCombo = comboNivel.getValue();
    String equipoCombo = comboEquipo.getValue();

    if (nivelCombo == null || equipoCombo == null) {
        Alert a = new Alert(Alert.AlertType.WARNING, "Por favor selecciona nivel y equipo antes de continuar.");
        a.showAndWait();
        return;
    }

    //normalizamos la rutina
    String equipoNormalizado = equipoCombo.equalsIgnoreCase("Peso extra") ? "Peso Extra" : "Peso Propio";

    String llaveCompuesta = musculo + "_" + nivelCombo + "_" + equipoNormalizado;

    Rutina rutinaGuardada = PantallaConfiguracionRutinaController.mapaRutinas.get(llaveCompuesta);

    // si no esxist esta configuraicion le avuisamos al usuario
    if (rutinaGuardada == null) {
        Alert alertaValidacion = new Alert(Alert.AlertType.WARNING);
        alertaValidacion.setTitle("Rutina No Configurada");
        alertaValidacion.setHeaderText("¡Sin configuración para " + musculo + " " + nivelCombo + "!");
        alertaValidacion.setContentText(
            "No has guardado una rutina para: " + musculo + " · " + nivelCombo + " · " + equipoCombo +
            "\nVe a 'Personalizar Rutina' y configura esta combinación."
        );
        alertaValidacion.showAndWait();
        return;
    }

    boolean usaMancuernas = rutinaGuardada.getTipoPeso().equalsIgnoreCase("Peso Extra");

    GestionRutina rutina = GestionRutina.getInstance();
    rutina.setMusculoSeleccionado(musculo);
    rutina.setNivelSeleccionado(rutinaGuardada.getNivel());
    rutina.setUsaMancuernas(usaMancuernas);
    rutina.setLlaveRutina(llaveCompuesta);
    rutina.configurarRutina();

    App.setRoot("PantallaEjecucion");
}


    // METODOS PARA LAS TARJETAS DE INFORMACION 


    private void mostrarTarjetaInfo(String titulo, String mensaje) {
        if (panelInfoMusculo != null && lblInfoTitulo != null && lblInfoMensaje != null) {
            lblInfoTitulo.setText(titulo);
            lblInfoMensaje.setText(mensaje);
            panelInfoMusculo.setVisible(true);
        }
    }

    @FXML
    public void accionCerrarInfo() {
        if (panelInfoMusculo != null) {
            panelInfoMusculo.setVisible(false);
        }
    }

    @FXML
    public void mostrarInfoBrazo() {
        mostrarTarjetaInfo("Músculos del Brazo", "Desarrolla la fuerza e hipertrofia en tus Bíceps, Tríceps y Antebrazos utilizando patrones de empuje y flexiones.");
    }

    @FXML
    public void mostrarInfoAbdomen() {
        mostrarTarjetaInfo("Músculos del Core", "Estabiliza tu sección media trabajando el recto abdominal, oblicuos y transverso para mejorar postura y fuerza central.");
    }

    @FXML
    public void mostrarInfoPierna() {
        mostrarTarjetaInfo("Músculos Inferiores", "Potencia tus Cuádriceps, Glúteos e Isquiotibiales mediante sentadillas dinámicas enfocadas en resistencia.");
    }
    
    @FXML
    public void regresarMenu(ActionEvent event) throws IOException {
        App.setRoot("PantallaBienvenidoUsuario"); 
    }
}
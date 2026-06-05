package omr.uacm.sistemafitguide;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author MiguelDiaz
 */
public class PantallaInicioController implements Initializable {

    
    @FXML private VBox vboxLogin;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField pfContrasenaLogin; 
    @FXML private Button btnIngresaSesion;
    @FXML private Hyperlink linkOlvidoContrasena; 
   

    @FXML private VBox vboxRegistro;
    @FXML private TextField txtUsuarioRegistro;
    @FXML private TextField txtPesoRegistro;
    @FXML private TextField txtAlturaRegistro; 
    @FXML private DatePicker dpFechaNacimiento;
    @FXML private PasswordField pfContrasenaRegistro; 
    @FXML private Button btnRegistrarmeEnBD;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vboxLogin.setVisible(true);
        vboxRegistro.setVisible(false);
    }


    @FXML
    private void mostrarPantallaLogin(ActionEvent event) {
        vboxLogin.setVisible(true);
        vboxRegistro.setVisible(false);
    }

    @FXML
    private void mostrarPantallaRegistro(ActionEvent event) {
        vboxLogin.setVisible(false);
        vboxRegistro.setVisible(true);
    }

        // PASO 1: INICIAR SESION

    @FXML
    private void accionIniciarSesion(ActionEvent event) {
        try {
            String usuario = txtUsuario.getText(); 
            String contrasena = pfContrasenaLogin.getText();
        
            // si algun campo esta vacio se lanza exepcion de que faltan datos
            if (usuario == null || usuario.trim().isEmpty() || contrasena == null || contrasena.trim().isEmpty()) {
                throw new CuentaDatosIncompletosException("Faltan datos. Por favor, ingresa tanto tu usuario como tu contraseña.");
            }
            
            //  si el usuario o la ocntraseña no es la correcta lanza exepcion ("yo" y "202123")
            if (!usuario.equals("yo") || !contrasena.equals("202123")) {
                throw new CredencialesInvalidasException("Usuario o contraseña incorrectos. Inténtalo de nuevo.");
            }
        
            // si todo es correcto pasa a la sigueinte pantalla
            App.setRoot("PantallaBienvenidoUsuario");
        
        } catch (CuentaDatosIncompletosException e) {
            mostrarAlertaError("Datos Incompletos", e.getMessage());
        } catch (CredencialesInvalidasException e) {
            mostrarAlertaError("Acceso Denegado", e.getMessage());
        } catch (IOException e) {
            mostrarAlertaError("Error de Pantalla", "No se pudo cargar la siguiente pantalla.");
        }
    }   

    @FXML
    private void recuperarContraseña(ActionEvent event) {
        
    }


    // PASO 2: ACCIONES DE REGISTRO


    @FXML
    private void accionRegistrarmeBaseDatos(ActionEvent event) {
        try {
            String usuario = txtUsuarioRegistro.getText();
            String pesoStr = txtPesoRegistro.getText();
            String alturaStr = txtAlturaRegistro.getText();
            String contrasena = pfContrasenaRegistro.getText();
            LocalDate fechaNacimiento = dpFechaNacimiento.getValue();

            // validamos que los campos no esten vacios
            if (usuario == null || usuario.trim().isEmpty() ||
                pesoStr == null || pesoStr.trim().isEmpty() ||
                alturaStr == null || alturaStr.trim().isEmpty() ||
                contrasena == null || contrasena.trim().isEmpty() ||
                fechaNacimiento == null) {
                throw new CuentaDatosIncompletosException("Faltan datos. Por favor, llena todos los campos del registro.");
            }

            // validacion mas de 20 kilos y maximo de 200 kg si es alo afuera de ese rangose lanza exepcion
            double peso = Double.parseDouble(pesoStr);
            if (peso < 20 || peso > 200) {
                throw new PesoFueraDeRangoException("El peso es inválido. tienes que pesar minimo 20kg .");
            }

            // validacion, si es menro que 1 metro o mayor que 3 metros sale del rango y selenza exepcion
            double altura = Double.parseDouble(alturaStr);
            if (altura <= 1.0 || altura > 3.0) {
                throw new EstaturaFueraDeRangoException("La estatura es inválida. Tienes que medir minimo 1 metro.");
            }

            // validamos edad, tien que ser mayor a 15 y menos que 100 años si sale del rango exepcion
            LocalDate hoy = LocalDate.now();
            int edad = Period.between(fechaNacimiento, hoy).getYears();
            
            //si pone una fecha de un año en el futuro la edad es incorrecta
            if (edad < 15 || edad > 100) {
                throw new EdadFueraDeRangoException("Para usar FitGuide debes tener minimo 15 años.");
            }

            // si pasa todo, entonces pasamos a la sigueinte pantalla
            System.out.println("Registro validado exitosamente. Avanzando...");
            App.setRoot("PantallaBienvenidoUsuario");

        } catch (CuentaDatosIncompletosException e) {
            mostrarAlertaError("Datos de Registro Incompletos", e.getMessage());
        } catch (PesoFueraDeRangoException e) {
            mostrarAlertaError("Error en Peso", e.getMessage());
        } catch (EstaturaFueraDeRangoException e) {
            mostrarAlertaError("Error en Estatura", e.getMessage());
        } catch (EdadFueraDeRangoException e) {
            mostrarAlertaError("Error en Edad", e.getMessage());
        } catch (NumberFormatException e) {
            // Atrapa si el usuario escribe letras en los campos de peso o altura
            mostrarAlertaError("Formato Incorrecto", "El peso y la altura deben ser valores numéricos (ej. 70.5).");
        } catch (Exception e) {
            mostrarAlertaError("Error", "Ocurrió un error inesperado al intentar registrar.");
            e.printStackTrace();
        }
    }   
    
    // mostrar alertas

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
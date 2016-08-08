/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.controller;

import edu.unapec.cine.MainApp;
import edu.unapec.cine.controller.util.ConvertionUtil;
import edu.unapec.cine.model.Artista;
import edu.unapec.cine.model.Cine;
import edu.unapec.cine.model.Pelicula;
import edu.unapec.cine.model.Rol;
import edu.unapec.cine.model.Sala;
import edu.unapec.cine.model.dao.PeliculaJpaController;
import edu.unapec.cine.model.dao.exceptions.IllegalOrphanException;
import edu.unapec.cine.model.dao.exceptions.NonexistentEntityException;
import edu.unapec.cine.model.util.EMFUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author yumarx
 */
public class PeliculaController implements Initializable {

    @FXML
    private TitledPane registroPelicula;
    @FXML
    private TextField txtTitulo;
    @FXML
    private DatePicker dateFechaProduccion;
    @FXML
    private Button btnGuardar;
    @FXML
    private ComboBox<String> cbxNacionalidad;
    @FXML
    private TextArea txtSipnosis;
    @FXML
    private Button btnCancelar;
    @FXML
    private ListView<Rol> elencoList;
    @FXML
    private TableView<Pelicula> peliculasTable;
    @FXML
    private TableColumn<Pelicula, String> anoColumn;
    @FXML
    private TableColumn<Pelicula, String> tituloColumn;
    @FXML
    private TableColumn<Pelicula, String> nacionalidadColumn;

    private Pelicula selected;
    private List<Pelicula> peliculas;
    private PeliculaJpaController dao;
    @FXML
    private AnchorPane peliculaMainPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selected = new Pelicula();
        dao = new PeliculaJpaController(EMFUtil.emf);

        cbxNacionalidad.getItems().addAll("Nacional", "Extranjera");
        //initialize data
        loadList();

        //tableView Columns
        anoColumn.setCellValueFactory(new PropertyValueFactory<Pelicula, String>("anoProduccion"));
        tituloColumn.setCellValueFactory(new PropertyValueFactory<Pelicula, String>("titulo"));
        nacionalidadColumn.setCellValueFactory(new PropertyValueFactory<Pelicula, String>("nacionalidad"));

        //table view Listeners
        peliculasTable.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Pelicula>() {
                    @Override
                    public void changed(ObservableValue<? extends Pelicula> observable, Pelicula oldValue, Pelicula newValue) {
                        if (newValue != null) {
                            changeSelection();
                        }
                    }
                });
    }

    public void selectedToUI() {
        txtTitulo.setText(selected.getTitulo());
        dateFechaProduccion.setValue(ConvertionUtil.dateToLocalDate(selected.getAnoProduccion()));
        cbxNacionalidad.setValue(selected.getNacionalidad());
        txtSipnosis.setText(selected.getSinopsis());

        List<Rol> roles = (List<Rol>) selected.getRolCollection();
        elencoList.setItems(FXCollections.observableList(roles));
    }

    public void selectedFromUI() {
        selected.setTitulo(txtTitulo.getText());
        selected.setAnoProduccion(ConvertionUtil.localDateToDate(dateFechaProduccion.getValue()));
        selected.setNacionalidad(cbxNacionalidad.getValue());
        selected.setSinopsis(txtSipnosis.getText());
        selected.setRolCollection(elencoList.getItems());
    }

    //Action Handlers
    private void changeSelection() {
        selected = peliculasTable.getSelectionModel().getSelectedItem();
        selectedToUI();
    }

    @FXML
    private void save() {
        selectedFromUI();
        try {
            if (this.selected.getIdpelicula() == null) {
                dao.create(selected);
            } else {
                dao.edit(selected);
            }
        } catch (Exception ex) {
            Logger.getLogger(CineController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //reload list
        loadList();
    }

    @FXML
    private void delete() {
        try {
            dao.destroy(selected.getIdpelicula());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CineController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(CineController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //refresh list
        loadList();
    }

    @FXML
    private void clearSelection() {
        setSelected(new Pelicula());
        selectedToUI();
    }

    private void loadList() {
        peliculas = dao.findPeliculaEntities();
        peliculasTable.setItems(FXCollections.observableList(peliculas));
    }

    public Pelicula getSelected() {
        return selected;
    }

    public void setSelected(Pelicula selected) {
        this.selected = selected;
    }

    @FXML
    private void addArtist(ActionEvent event) {
        Rol newRol = new Rol();
        addElencoDialog(newRol);
        elencoList.getItems().add(newRol);
    }

    @FXML
    private void removeArtist(ActionEvent event) {
        Rol selectedRol = elencoList.getSelectionModel().getSelectedItem();
        elencoList.getItems().remove(selectedRol);
    }

    public boolean addElencoDialog(Rol rol) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Rol.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Elenco");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            
            Stage stage = (Stage) peliculaMainPane.getScene().getWindow();
            dialogStage.initOwner(stage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            
            // Set the person into the controller
            RolController rolController =  loader.getController();
            rolController.setDialogStage(dialogStage);
            rolController.setRol(rol);
            
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            
            return rolController.isOkClicked();
        } catch (IOException ex) {
            Logger.getLogger(PeliculaController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}

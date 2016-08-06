/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.controller;

import edu.unapec.cine.controller.util.ConvertionUtil;
import edu.unapec.cine.model.Artista;
import edu.unapec.cine.model.dao.ArtistaJpaController;
import edu.unapec.cine.model.dao.exceptions.IllegalOrphanException;
import edu.unapec.cine.model.dao.exceptions.NonexistentEntityException;
import edu.unapec.cine.model.util.EMFUtil;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author ibatista as
 */
public class ArtistaController implements Initializable {

    @FXML
    private TextField artistaTxt;
    @FXML
    private DatePicker fechanacDate;
    @FXML
    private TableView<Artista> artistaTable;
    @FXML
    private TableColumn<Artista, String> artistaNombreColumn;
    @FXML
    private TableColumn<Artista, Date> artistaFechaNacColumn;

    private Artista selected;
    private ArtistaJpaController dao;
    private List<Artista> artistas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selected = new Artista();
        dao = new ArtistaJpaController(EMFUtil.emf);

        //initialize data
        loadList();

        //tableView Columns
        artistaNombreColumn.setCellValueFactory(new PropertyValueFactory<Artista, String>("nombre"));
        artistaFechaNacColumn.setCellValueFactory(new PropertyValueFactory<Artista, Date>("fechaNacimiento"));

        //table view Listeners
        artistaTable.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Artista>() {
                    @Override
                    public void changed(ObservableValue<? extends Artista> observable, Artista oldValue, Artista newValue) {
                        if (newValue != null) {
                            changeSelection();
                        }
                    }
                });
    }

    public void selectedToUI() {
        artistaTxt.setText(selected.getNombre());
        fechanacDate.setValue(ConvertionUtil.dateToLocalDate(selected.getFechaNacimiento()));
    }

    public void selectedFromUI() {
        selected.setNombre(artistaTxt.getText());
        selected.setFechaNacimiento(ConvertionUtil.localDateToDate(fechanacDate.getValue()));
    }

    //Action Handlers
    private void changeSelection() {
        selected = artistaTable.getSelectionModel().getSelectedItem();
        selectedToUI();
    }

    @FXML
    private void save() {
        selectedFromUI();
        try {
            if (this.selected.getIdartista() == null) {
                dao.create(selected);
            } else {
                dao.edit(selected);
            }
        } catch (Exception ex) {
            Logger.getLogger(ArtistaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //reload list
        loadList();
    }

    @FXML
    private void delete() {
        try {
            dao.destroy(selected.getIdartista());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ArtistaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(ArtistaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        clearSelection();
        //refresh list
        loadList();
    }

    @FXML
    private void clearSelection() {
        setSelected(new Artista());
        artistaTxt.clear();
        fechanacDate.getEditor().clear();
    }

    private void loadList() {
        artistas = dao.findArtistaEntities();
        artistaTable.setItems(FXCollections.observableList(artistas));
    }

    public Artista getSelected() {
        return selected;
    }

    public void setSelected(Artista selected) {
        this.selected = selected;
    }

}

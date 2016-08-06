/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.controller;

import edu.unapec.cine.model.Cine;
import edu.unapec.cine.model.Sala;
import edu.unapec.cine.model.dao.CineJpaController;
import edu.unapec.cine.model.dao.SalaJpaController;
import edu.unapec.cine.model.dao.exceptions.IllegalOrphanException;
import edu.unapec.cine.model.dao.exceptions.NonexistentEntityException;
import edu.unapec.cine.model.util.EMFUtil;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import javax.persistence.Converter;

/**
 * FXML Controller class
 *
 * @author yumarx
 */
public class SalaController implements Initializable {

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEliminar;
    @FXML
    private TextField nombreSalaTxt;
    @FXML
    private Button btnLimpiar;
    @FXML
    private TableView<Sala> salaTable;
    @FXML
    private TableColumn<Sala, Cine> salaCineColumn;
    @FXML
    private TableColumn<Sala, String> salaNombreColumn;
    @FXML
    private ComboBox<Cine> cineCbx;

    private Sala selected;
    private SalaJpaController dao;
    private CineJpaController cineDao;
    private List<Cine> cines;
    private List<Sala> salas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selected = new Sala();
        dao = new SalaJpaController(EMFUtil.emf);
        cineDao = new CineJpaController(EMFUtil.emf);

        cines = cineDao.findCineEntities();
        cineCbx.setItems(FXCollections.observableList(cines));

        //initialize data
        loadList();

        //tableView Columns
        salaNombreColumn.setCellValueFactory(new PropertyValueFactory<Sala, String>("nombre"));
        salaCineColumn.setCellValueFactory(new PropertyValueFactory<Sala, Cine>("idcine"));

        //table view Listeners
        salaTable.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Sala>() {
                    @Override
                    public void changed(ObservableValue<? extends Sala> observable, Sala oldValue, Sala newValue) {
                        if (newValue != null) {
                            changeSelection();
                        }
                    }
                });
    }

    public void selectedToUI() {
        nombreSalaTxt.setText(selected.getNombre());
        cineCbx.getSelectionModel().select(selected.getIdcine());
        
    }

    public void selectedFromUI() {
        selected.setNombre(nombreSalaTxt.getText());
        selected.setIdcine(cineCbx.getSelectionModel().getSelectedItem());
    }

    //Action Handlers
    private void changeSelection() {
        selected = salaTable.getSelectionModel().getSelectedItem();
        selectedToUI();
    }

    @FXML
    private void save() {
        selectedFromUI();
        try {
            if (this.selected.getIdsala() == null) {
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
            dao.destroy(selected.getIdsala());
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
        setSelected(new Sala());
        selectedToUI();
    }
    
    
    private void loadList() {
        salas = dao.findSalaEntities();
        salaTable.setItems(FXCollections.observableList(salas));
    }

    public Sala getSelected() {
        return selected;
    }

    public void setSelected(Sala selected) {
        this.selected = selected;
    }
    
    
}

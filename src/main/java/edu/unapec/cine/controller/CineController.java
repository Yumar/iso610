/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.controller;

import edu.unapec.cine.model.Cine;
import edu.unapec.cine.model.dao.CineJpaController;
import edu.unapec.cine.model.dao.exceptions.IllegalOrphanException;
import edu.unapec.cine.model.dao.exceptions.NonexistentEntityException;
import edu.unapec.cine.model.util.EMFUtil;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author ibatista
 */
public class CineController implements Initializable {

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEliminar;
    @FXML
    private TableView<Cine> cinesTable;
    @FXML
    private TableColumn<Cine, String> cineNombreColumn;
    @FXML
    private TableColumn<Cine, String> cineSalasColumn;
    @FXML
    private TextField nombreCineTxt;
    @FXML
    private Button btnLimpiar;
    
    private Cine selected;
    private CineJpaController dao;
    private List<Cine> cines;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selected = new Cine();
        dao = new CineJpaController(EMFUtil.emf);
        
        //initialize data
        loadList();

        //tableView Columns
        cineNombreColumn.setCellValueFactory(new PropertyValueFactory<Cine, String>("nombre"));
        //cineSalasColumn.setCellValueFactory(new PropertyValueFactory<Cine, String>("descripcion"));
        
        //table view Listeners
        cinesTable.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Cine>() {
                    @Override
                    public void changed(ObservableValue<? extends Cine> observable, Cine oldValue, Cine newValue) {
                        if (newValue != null) {
                            changeSelection();
                        }
                    }
                });
    }    
    
    public void selectedToUI() {
        nombreCineTxt.setText(selected.getNombre());
    }

    public void selectedFromUI() {
        selected.setNombre(nombreCineTxt.getText());
    }

    //Action Handlers
    private void changeSelection() {
        selected = cinesTable.getSelectionModel().getSelectedItem();
        selectedToUI();
    }

    @FXML
    private void save() {
        selectedFromUI();
        try {
            if (this.selected.getIdcine() == null) {
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
            dao.destroy(selected.getIdcine());
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
        setSelected(new Cine());
        selectedToUI();
    }
    
    
    private void loadList() {
        cines = dao.findCineEntities();
        cinesTable.setItems(FXCollections.observableList(cines));
    }

    public Cine getSelected() {
        return selected;
    }

    public void setSelected(Cine selected) {
        this.selected = selected;
    }
    
    
}

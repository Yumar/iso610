/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.controller;

import edu.unapec.cine.model.Artista;
import edu.unapec.cine.model.Rol;
import edu.unapec.cine.model.dao.ArtistaJpaController;
import edu.unapec.cine.model.util.EMFUtil;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author yumarx
 */
public class RolController implements Initializable {

    @FXML
    private ComboBox<Artista> artistaSelect;
    @FXML
    private TextField rolTxt;

    private Stage dialogStage;
    private Rol rol;
    private boolean okClicked = false;
    private ArtistaJpaController artistaDao;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artistaDao = new ArtistaJpaController(EMFUtil.emf);
        
        List<Artista> artistas = artistaDao.findArtistaEntities();
        artistaSelect.setItems(FXCollections.observableList(artistas));
        // TODO
    }

    @FXML
    private void ok(ActionEvent event) {
        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void cancel(ActionEvent event) {
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }

}

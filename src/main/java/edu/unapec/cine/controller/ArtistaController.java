/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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
    private TableView<?> artistaTable;
    @FXML
    private TableColumn<?, ?> artistaNombreColumn;
    @FXML
    private TableColumn<?, ?> artistaFechaNacColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

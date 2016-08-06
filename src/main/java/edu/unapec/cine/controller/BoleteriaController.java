/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unapec.cine.controller;

import edu.unapec.cine.model.Cine;
import edu.unapec.cine.model.Tanda;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author yumarx
 */
public class BoleteriaController implements Initializable {

    @FXML
    private ComboBox<Tanda> tandaCbx;
    @FXML
    private TextField cantidadTxt;
    @FXML
    private ComboBox<Cine> cineCbx;
    @FXML
    private DatePicker fechaDate;
    @FXML
    private Text precioBoletoTxt;
    @FXML
    private Text totalTxt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtCalorie"
    private TextField txtCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="txtPassi"
    private TextField txtPassi; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCorrelate"
    private Button btnCorrelate; // Value injected by FXMLLoader

    @FXML // fx:id="btnCammino"
    private Button btnCammino; // Value injected by FXMLLoader

    @FXML // fx:id="boxPorzioni"
    private ComboBox<String> boxPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCammino(ActionEvent event) {
    	txtResult.clear();
    	
    	int N;
    	try {
    		N = Integer.parseInt(this.txtPassi.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Prima indicare un numero massimo di passi");
    		return;
    	}
    	
    	double c;
    	try {
    		c = Double.parseDouble(this.txtCalorie.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Prima indica un numero minimo di calorie e creare il grafo");
    		return;
    	}
    	
    	String selezione = this.boxPorzioni.getValue();
    	
    	if(selezione == null) {
    		this.txtResult.setText("Prima selezionare un vertice del grafo creato");
    		return;
    	}else {
    		txtResult.appendText("Cerco cammino peso massimo...");
    		
    		List<String> cammino = model.trovaCammino(selezione, N);
    		txtResult.clear();
    		
    		this.txtResult.appendText("Hai selezionato il vertice: " + selezione + " il cui cammino minimo risulta essere: \n\n");
    		for(String s: cammino) {
    			this.txtResult.appendText(s + "\n");
    		}
    		
    		this.txtResult.appendText("\n Il peso totale: " + model.getPesoMassimo());
    	}
    	
    }

    @FXML
    void doCorrelate(ActionEvent event) {
    	txtResult.clear();
    	
    	double c;
    	try {
    		c = Double.parseDouble(this.txtCalorie.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Prima indica un numero minimo di calorie e creare il grafo");
    		return;
    	}
    	
    	String selezione = this.boxPorzioni.getValue();
    	
    	if(selezione == null) {
    		this.txtResult.setText("Prima selezionare un vertice del grafo creato");
    		return;
    	}else {
    		List<Adiacenza> res = model.getCorrelate(selezione);
    		
    		if(res != null) {
    			
    			this.txtResult.appendText("Hai indicato il vertice: " + selezione +"\n I suoi vicini sono: \n\n");
    			for(Adiacenza a: res) {
    				this.txtResult.appendText(a.getP2() + " - peso: " + a.getPeso()  + "\n");
    			}
    			
    		}
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	double c;
    	try {
    		c = Double.parseDouble(this.txtCalorie.getText());
    	}catch(NumberFormatException nfe) {
    		this.txtResult.setText("Prima indica un numero minimo di calorie");
    		return;
    	}
    	
    	this.model.creaGrafo(c);
    	
    	txtResult.appendText("Creato grafo\n\n");
    	this.txtResult.appendText("#vertici: " + model.getNVertici() +"\n");
    	this.txtResult.appendText("#archi: " + model.getNArchi());
    	this.boxPorzioni.getItems().addAll(model.getVertici());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtCalorie != null : "fx:id=\"txtCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtPassi != null : "fx:id=\"txtPassi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCorrelate != null : "fx:id=\"btnCorrelate\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCammino != null : "fx:id=\"btnCammino\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxPorzioni != null : "fx:id=\"boxPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    }
}

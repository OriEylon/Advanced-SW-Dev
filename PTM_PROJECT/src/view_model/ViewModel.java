package view_model;

import java.util.Observable;
import java.util.Observer;

import Model_Pack.Model;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ViewModel extends Observable implements Observer {
	Model m;
	public StringProperty Script;
	ClientSim client = new ClientSim();

	public Model getM() {
		return m;
	}

	public ClientSim getClient() {
		return client;
	}

	public ViewModel(Model m) {
		this.m = m;
		Script = new SimpleStringProperty();
		m.s.bind(Script);
	}

	public void interpet(StringProperty s) {
		m.interpet(s.getValue());
	}

	public void connect(String ip, int port) {
		client.connect(ip,port);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}

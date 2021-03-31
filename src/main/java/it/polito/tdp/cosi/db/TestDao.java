package it.polito.tdp.cosi.db;

import it.polito.tdp.corsi.model.Corso;

public class TestDao {

	public static void main(String[] args) {
	
		CorsoDAO dao = new CorsoDAO();
		System.out.println(dao.getStudentiByCorsi(new Corso("01KSUPG",null,null,null)) ); //creo un oggetto fittizio

	}

}

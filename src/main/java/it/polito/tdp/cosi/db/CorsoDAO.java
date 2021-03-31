package it.polito.tdp.cosi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.corsi.model.Corso;
import it.polito.tdp.corsi.model.Studente;

public class CorsoDAO {

	public List<Corso> getCorsiByPeriodo(Integer periodo){
		
		String sql="SELECT * FROM corso WHERE pd = ?";
		
		List <Corso> result = new ArrayList <Corso>();
	
		//perhè le operazion sul database possono dare sql exception
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			         //cioè il primo parametro nella query
			st.setInt(1, periodo);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
					Corso c = new Corso(rs.getString("codins"), rs.getInt("crediti"), rs.getString("nome"), rs.getInt("pd") );
			        
					result.add(c);
			}
			
			//chiudi sempre la connessione e le altre risosrse
			rs.close();
			st.close();
			conn.close();
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
			
		}
		
		return result;
		
	}
	
	public Map <Corso, Integer> getIscrittibyPeriodo(Integer pd){
		

		String sql="SELECT c.codins, c.nome, c.crediti, c.pd, c.CDS COUNT(*) AS tot FROM corso c, iscrizione i WHERE c.codins = i.codins AND c.pd=? GROUP BY c.codins, c.nome, c.crediti ,c.pd";
		
		Map <Corso,Integer> result = new HashMap <Corso, Integer>();
	
		//perhè le operazion sul database possono dare sql exception
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			         //cioè il primo parametro nella query
			st.setInt(1,pd);
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
					Corso c = new Corso(rs.getString("codins"), rs.getInt("crediti"), rs.getString("nome"), rs.getInt("pd") );
			        Integer n = rs.getInt("tot");
					result.put(c, n);
			}
			
			//chiudi sempre la connessione e le altre risosrse
			rs.close();
			st.close();
			conn.close();
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
			
		}
		
		return result;
		
	}
	

	public List<Studente> getStudentiByCorsi (Corso corso){
		String sql ="SELECT s.matricola, s.cognome, s.nome, s.CDS FROM studente s, iscrizione i WHERE s.matricola = i.matricola AND i.codins = ?";
		
		List<Studente> risultato = new LinkedList<Studente>();
		try {
			Connection conn=DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
	         
	       st.setString(1,corso.getCodins());
	       ResultSet rs = st.executeQuery();
	
	while (rs.next()) {
		Studente s = new Studente(rs.getInt("matricola"), rs.getString("nome"),rs.getString("cognome"), rs.getString("CDS"));
		risultato.add(s);
		}
	st.close();
	rs.close();
	conn.close();
	
		}catch(SQLException e) {
			throw new RuntimeException(e);
			
		}
		
		return risultato;
		
		
	         }

	public boolean esisteCorso(Corso corso) {
		String sql ="SELECT * FROM corso WHERE codins = ?";
		try {
			
		Connection conn=DBConnect.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
	    st.setString(1,corso.getCodins());
	    ResultSet rs = st.executeQuery();
	    
	    if(rs.next()) {
	    	st.close();
	    	rs.close();
	    	conn.close(); //perchè si potrebbe chiudere in entrambe le situazioni
	    	return true;
	    }else {
	    	st.close();
	    	rs.close();
	    	conn.close();
	    	return false;
	    }
		
	    
	    
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
				
		
	}

	public Map<String, Integer> getDivisioneStudenti(Corso corso) {
		
		String sql = "SELECT s.CDS, COUNT(*) AS tot FROM studente s, iscrizione i "
				+ "WHERE s.matricola = i.matricola AND i.codins = ? AND s.cds <> '' "
				+ "GROUP BY s.CDS";
		Map<String, Integer> result = new HashMap<String,Integer>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, corso.getCodins());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				result.put(rs.getString("CDS"), rs.getInt("tot"));
			}
			rs.close();
			st.close();
			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	
}

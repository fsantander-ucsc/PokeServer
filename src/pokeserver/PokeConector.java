/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokeserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PokeConector {

    private String url = "pokeBaseDatos.db";
    private Connection conn = null;

    public void pokeConectarBaseDatos() {

        try {
            //Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + url);
            if (conn != null) {
                System.out.println("Conectado");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void mostrarPokemon(){
        
         ResultSet result = null;
         
         try {
            PreparedStatement st = conn.prepareStatement("select * from POKEMON");
            result = st.executeQuery();
            while (result.next()) {   
                System.out.print("Nombre: ");
                System.out.println(result.getString("name")); 
                System.out.println("=======================");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
         
    }

}

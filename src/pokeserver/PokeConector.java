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
import java.util.ArrayList;

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
    
    //Metodo creado para recuperar los stats del pokemon ingresado en el orden:
        //tipo_id hp attack defense specialAttack specialDefense speed
    public ArrayList<Integer> recuperaStatsPokemon(String nombrePokemon, int idPokemon){
        ArrayList<Integer> statsPokemon = new ArrayList(); 
        
        nombrePokemon = nombrePokemon.toUpperCase();
        
        ResultSet result = null;
         
         try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM POKEMON WHERE name='"+nombrePokemon+"' OR id='"+idPokemon+"';");
            result = st.executeQuery();
            while (result.next()) {   
                statsPokemon.add(result.getInt("tipo_id"));
                statsPokemon.add(result.getInt("hp"));
                statsPokemon.add(result.getInt("attack"));
                statsPokemon.add(result.getInt("defense"));
                statsPokemon.add(result.getInt("specialAttack"));
                statsPokemon.add(result.getInt("specialDefense"));
                statsPokemon.add(result.getInt("speed"));
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return statsPokemon;
    }
    //Metodo creado para recuperar nombre de un pokemon por id
    public String recuperaNombrePokemon(int idPokemon){
        String nombreRecuperado="no encontrado";
        ResultSet result;
         
         try {
            PreparedStatement st = conn.prepareStatement("SELECT name FROM POKEMON WHERE id="+idPokemon);
            result = st.executeQuery();
            while (result.next()) {   
                nombreRecuperado = result.getString("name");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return nombreRecuperado;

    }
    
    //Metodo creado para recuperar la ventaja de tipo del pokemon
    public Double ventajaTipo(int idTipoA, int idTipoB){
        Double ventajaTipoReal;
        int ventajaTipoTabla=2;
        String nombreTipoB = nombreTipo(idTipoB);
        
        ResultSet result;

        try {
            PreparedStatement st = conn.prepareStatement("SELECT * FROM tipo WHERE id='"+idTipoA+"';");
            result = st.executeQuery();
            while (result.next()) {   
                ventajaTipoTabla = result.getInt(nombreTipoB);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        ventajaTipoReal = transformadorMultiplicador(ventajaTipoTabla);
        
        return ventajaTipoReal;
    }
    //Metodo creado para recuperar nombre de tipo dependiendo del id
    public String nombreTipo(int id){
        String nombreRecuperado="no encontrado";
        ResultSet result;
         
         try {
            PreparedStatement st = conn.prepareStatement("SELECT name FROM tipo WHERE id="+id);
            result = st.executeQuery();
            while (result.next()) {   
                nombreRecuperado = result.getString("name");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return nombreRecuperado;
    }
    
    //Metodo para transformar valores en tabla de tipo a los reales
    public static Double transformadorMultiplicador(int valorTabla){
    Double valorReal;
    switch(valorTabla){
        case 0:
            valorReal = 0.0;
            break;
        case 1:
            valorReal = 0.5;
            break;
        case 2:
            valorReal = 1.0;
            break;
        case 3:
            valorReal = 2.0;
            break;
        default:
            valorReal = 1.0;
            break;
    }

    return valorReal;
    }

}

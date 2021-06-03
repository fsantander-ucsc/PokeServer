/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokeserver;

import irPokemon.irPokemon;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class PokeServer {

    public PokeServer() {

        try {
            //el nombre debe estar disponible para el cliente y el servidor
            String nombre ="Pokemon";
            //Dejamos el objeto remoto disponible para el cliente
            //se crea el objeto remoto
            irPokemon poke = new Pokemon();
            //El casteo se hace porque no soporta el objeto 
            //retorna un objeto remoto sin tipo
            //por lo que lo casteamos para que nos sirva
            irPokemon stub = (irPokemon)UnicastRemoteObject.exportObject(poke,0);
            //el 1099 es el puertoque se usa en RCP
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(nombre,stub);
            System.out.println("Servidor Corriendo");
            
        } catch (Exception e) {
            System.err.println("Error en el servidor");
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
         new PokeServer();
    }
    
}

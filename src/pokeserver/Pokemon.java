/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokeserver;


import irPokemon.irPokemon;
import java.rmi.RemoteException;

public class Pokemon implements irPokemon {

    @Override
    public String pokeHoroscopo(int anho) throws RemoteException {

        PokeConector pokeconn = new PokeConector();
        pokeconn.pokeConectarBaseDatos();
        pokeconn.mostrarPokemon();

        String pokemon = "";

        String[] listaPokemon = {
            "Umbreon",
            "Pikachu",
            "Piplup",
            "Jinx",
            "Gengar",
            "Latios",
            "Dragonite",
            "Torracat",
            "Phanpy",
            "Bulbasaur",
            "Appletun",
            "Arceus"};

        int num_signo = (Math.abs(2020 - anho)) % 12;
        if (anho < 2020) {
            num_signo = 12 - num_signo;
        }

        return listaPokemon[num_signo];
    }

    

}

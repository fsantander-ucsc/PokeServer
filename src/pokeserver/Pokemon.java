/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokeserver;

import pokeserver.PokeBankService;
import irPokemon.irPokemon;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

public class Pokemon implements irPokemon {
    static int posibilidadFallo=75;
    PokeBankService bankService = new PokeBankService();

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
    
    @Override
    public ArrayList<String> batalla(String pokemonIngresado) throws RemoteException{
        //Poke variables para usar la pkDb
        PokeConector pokeconn = new PokeConector();
        pokeconn.pokeConectarBaseDatos();
        pokeconn.mostrarPokemon();
        
        pokemonIngresado = pokemonIngresado.toUpperCase();
        //Array list que contendra el desarrollo de la batalla
        ArrayList<String> resumenBatalla = new ArrayList();
        
        //Recuperar pokemon ingresado y otro recuperado al azar
        ArrayList<Integer> statsPokemonIngresado;
        ArrayList<Integer> statsPokemonOponente;
        
        statsPokemonIngresado = pokeconn.recuperaStatsPokemon(pokemonIngresado, 0);
        Random random = new Random();
        int idPokemonRandom = random.nextInt(721);
        statsPokemonOponente = pokeconn.recuperaStatsPokemon("", idPokemonRandom);
        
        //stats, se utilizan at1 y def1 como variables base de ataque y defensa, pero si sus valores SP son mayores se les asigna ese valor 
            //cada pkmn se defendera con su defensa del tipo correspondiente al ataque mas alto del otro
        int atk1,atk2,def1,def2,spA1,spA2,spD1,spD2,speedIngresado,speedRandom, hit, contadorTurnos=0;
        Double hp1, hp2, multiplicador1, multiplicador2 , calculoDano;
        String nombre1, nombre2, resumenTurno;

        //Se recuperan velocidades
        speedIngresado = statsPokemonIngresado.get(6);
        speedRandom = statsPokemonOponente.get(6);
        
        //Comparacion stats para determinar orden y asignar stats
        if(speedIngresado>=speedRandom){
            //Se le pasan los stats del pokemon del entrenador al pokemon que parte
            nombre1 = pokemonIngresado;
            nombre2 = pokeconn.recuperaNombrePokemon(idPokemonRandom);
            
            hp1 = statsPokemonIngresado.get(1).doubleValue();
            hp2 = statsPokemonOponente.get(1).doubleValue();
            
            atk1 = statsPokemonIngresado.get(2);
            atk2 = statsPokemonOponente.get(2);
            spA1 = statsPokemonIngresado.get(4);
            spA2 = statsPokemonOponente.get(4);
            
            def1 = statsPokemonIngresado.get(3);
            def2 = statsPokemonOponente.get(3);
            spD1 = statsPokemonIngresado.get(5);
            spD2 = statsPokemonOponente.get(5);
            
            //Se recupera el multiplicador dependiendo de los dos tipo de los pokemon 
                //En el argumento del transformador ingresar el int reucperado correspondiente
                //PENDIENTE
            multiplicador1 = pokeconn.ventajaTipo(statsPokemonIngresado.get(0), statsPokemonOponente.get(0));
            multiplicador2 = pokeconn.ventajaTipo(statsPokemonOponente.get(0), statsPokemonIngresado.get(0));
        }else{
            //Se le pasan los stats del pokemon del entrenador al pokemon que parte
            nombre1 = pokeconn.recuperaNombrePokemon(idPokemonRandom);
            nombre2 = pokemonIngresado;
            
            hp1 = statsPokemonOponente.get(1).doubleValue();
            hp2 = statsPokemonIngresado.get(1).doubleValue();
            
            atk1 = statsPokemonOponente.get(2);
            atk2 = statsPokemonIngresado.get(2);
            spA1 = statsPokemonOponente.get(4);
            spA2 = statsPokemonIngresado.get(4);
            
            def1 = statsPokemonOponente.get(3);
            def2 = statsPokemonIngresado.get(3);
            spD1 = statsPokemonOponente.get(5);
            spD2 = statsPokemonIngresado.get(5);
            
            multiplicador1 = pokeconn.ventajaTipo(statsPokemonOponente.get(0), statsPokemonIngresado.get(0));
            multiplicador2 = pokeconn.ventajaTipo(statsPokemonIngresado.get(0), statsPokemonOponente.get(0));
        }
        
        //se evalua que tipo de ataque y defensa usar para cada uno
        if(atk1<spA1){
            atk1=spA1;
            def2=spD2;
        }
        if(atk2<spA2){
            atk2=spA2;
            def1=spD1;
        }
        
        //Loop batalla:
        resumenBatalla.add("El pokemon mas rapido es: "+nombre1);
        resumenBatalla.add(nombre1+" tiene "+hp1+" puntos de vida totales");
        resumenBatalla.add(nombre2+" tiene "+hp2+" puntos de vida totales");
        resumenBatalla.add(nombre1+" tiene "+multiplicador1+" como multiplicador de tipo");
        resumenBatalla.add(nombre2+" tiene "+multiplicador2+" como multiplicador de tipo\n\nCOMIENZA BATALLA!\n");
        while(hp1>0&&hp2>0){
            contadorTurnos++;
            hit = hitChance();
            if(contadorTurnos%2!=0){
                calculoDano = (atk1*multiplicador1 - def2);
                if(calculoDano<=0) calculoDano=5.0;
                
                hp2 = hp2 - hit*calculoDano;
                if(hp2<0.0) hp2 = 0.0;
                resumenTurno = "--TURNO "+contadorTurnos+"--\n"
                    + "EXITO DE GOLPE de "+nombre1+" es: "+hit+"\n"
                    + "DAÑO A REALIZAR por "+nombre1+" es: "+hit*calculoDano+"\n"
                    + "A "+nombre2+" le queda "+hp2+" puntos de vida\n";
                resumenBatalla.add(resumenTurno);
            }else{
                calculoDano = (atk2*multiplicador2 - def1);
                if(calculoDano<=0) calculoDano=1.0;
                
                hp1 = hp1 - hit*calculoDano;
                if(hp1<0.0) hp1 = 0.0;
                resumenTurno = "--TURNO "+contadorTurnos+"--\n"
                    + "EXITO DE GOLPE de "+nombre2+" es: "+hit+"\n"
                    + "DAÑO A REALIZAR por "+nombre2+" es: "+hit*calculoDano+"\n"
                    + "A "+nombre1+" le queda "+hp1+" puntos de vida\n";
                resumenBatalla.add(resumenTurno);
            }
            
            if(hp1<=0||hp2<=0){
                resumenTurno = "¡¡BATALLA TERMINADA!!";
                if(hp1>hp2){
                    resumenTurno = resumenTurno + " " +nombre1+" ES EL GANADOR";
                }else{
                    resumenTurno = resumenTurno + " " +nombre2+" ES EL GANADOR";
                }
                resumenBatalla.add(resumenTurno); 
            }
        }
        
        return resumenBatalla;
    }
    
    
    //Metodo que devuelve 1 si le pego y 0 si misseo
    public static int hitChance(){
        int hit;
        Random random = new Random();
        int fallo = random.nextInt(100);
        if(fallo>=posibilidadFallo){
            hit=0;
        }else{
            hit=1;
        }
        return hit;
    }
}
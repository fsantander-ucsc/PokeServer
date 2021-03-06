/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokeserver;

import irPokemon.irPokemon;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;

public class Pokemon implements irPokemon {

    private static int posibilidadFallo = 75;

    private static int probabilidadCaptura = 20;
    private static int probabilidadHuir = 20;
    private static int cantidadPokebola = 10;
    private static String pokemonSafari = "";

    PokeBankService bankService = new PokeBankService();

    private ArrayList<String> listaPokemon = new ArrayList<>();
    
    //Se define metodo para mostrar el signo zodiacal de pokemon al usuario ingresnado su año de nacimiento
    @Override
    public String pokeHoroscopo(int anho) throws RemoteException {

        PokeConector pokeconn = new PokeConector();
        pokeconn.pokeConectarBaseDatos();

        String pokemon = "";
        //Lista de signos pokemon 
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
        
        //Se calcula que signo corresponde al año ingresado siguiendo el ciclo de 12 definido por el horoscopo chino
        int num_signo = (Math.abs(2020 - anho)) % 12;
        if (anho < 2020) {
            num_signo = 12 - num_signo;
        }
        
        //Se retorna el pokemon correspondiente al año ingresado
        return listaPokemon[num_signo];
    }
    
    //Se define el metodo batalla donde se agregan las interacciones y turnos de ésta a un arraylist
    @Override
    public ArrayList<String> batalla(String pokemonIngresado) throws RemoteException {
        //Poke variables para usar la pkDb
        PokeConector pokeconn = new PokeConector();
        pokeconn.pokeConectarBaseDatos();

        pokemonIngresado = pokemonIngresado.toUpperCase();
        //Array list que contendra el desarrollo de la batalla
        ArrayList<String> resumenBatalla = new ArrayList();

        //Recuperar pokemon ingresado y otro recuperado al azar
        ArrayList<Integer> statsPokemonIngresado;
        ArrayList<Integer> statsPokemonOponente;

        statsPokemonIngresado = pokeconn.recuperaStatsPokemon(pokemonIngresado, 0);
        Random random = new Random();
        int idPokemonRandom = random.nextInt(721) + 1;
        statsPokemonOponente = pokeconn.recuperaStatsPokemon("", idPokemonRandom);

        //stats, se utilizan at1 y def1 como variables base de ataque y defensa, pero si sus valores SP son mayores se les asigna ese valor 
        //cada pkmn se defendera con su defensa del tipo correspondiente al ataque mas alto del otro
        int atk1, atk2, def1, def2, spA1, spA2, spD1, spD2, speedIngresado, speedRandom, hit, contadorTurnos = 0;
        Double hp1, hp2, multiplicador1, multiplicador2, calculoDano;
        String nombre1, nombre2, resumenTurno;

        //Se recuperan velocidades
        speedIngresado = statsPokemonIngresado.get(6);
        speedRandom = statsPokemonOponente.get(6);

        //Comparacion stats para determinar orden y asignar stats
        if (speedIngresado >= speedRandom) {
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
        } else {
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
        if (atk1 < spA1) {
            atk1 = spA1;
            def2 = spD2;
        }
        if (atk2 < spA2) {
            atk2 = spA2;
            def1 = spD1;
        }

        //Loop batalla:
        resumenBatalla.add("¡Un " + pokeconn.recuperaNombrePokemon(idPokemonRandom)+" salvaje ha aparecido!\n\n");
        resumenBatalla.add("El pokemon mas rapido es: " + nombre1+"\n");
        resumenBatalla.add(nombre1 + " tiene " + hp1 + " puntos de vida totales\n");
        resumenBatalla.add(nombre2 + " tiene " + hp2 + " puntos de vida totales\n");
        resumenBatalla.add(nombre1 + " tiene " + multiplicador1 + " como multiplicador de tipo\n");
        resumenBatalla.add(nombre2 + " tiene " + multiplicador2 + " como multiplicador de tipo\n\nCOMIENZA BATALLA!\n");
        while (hp1 > 0 && hp2 > 0) {
            contadorTurnos++;
            hit = hitChance();
            if (contadorTurnos % 2 != 0) {
                calculoDano = (atk1 * multiplicador1 - def2);
                if (calculoDano <= 0) {
                    calculoDano = 5.0;
                }

                hp2 = hp2 - hit * calculoDano;
                if (hp2 < 0.0) {
                    hp2 = 0.0;
                }
                resumenTurno = "--TURNO " + contadorTurnos + "--\n"
                        + "EXITO DE GOLPE de " + nombre1 + " es: " + hit + "\n"
                        + "DAÑO A REALIZAR por " + nombre1 + " es: " + hit * calculoDano + "\n"
                        + "A " + nombre2 + " le queda " + hp2 + " puntos de vida\n";
                resumenBatalla.add(resumenTurno);
            } else {
                calculoDano = (atk2 * multiplicador2 - def1);
                if (calculoDano <= 0) {
                    calculoDano = 1.0;
                }

                hp1 = hp1 - hit * calculoDano;
                if (hp1 < 0.0) {
                    hp1 = 0.0;
                }
                resumenTurno = "--TURNO " + contadorTurnos + "--\n"
                        + "EXITO DE GOLPE de " + nombre2 + " es: " + hit + "\n"
                        + "DAÑO A REALIZAR por " + nombre2 + " es: " + hit * calculoDano + "\n"
                        + "A " + nombre1 + " le queda " + hp1 + " puntos de vida\n";
                resumenBatalla.add(resumenTurno);
            }

            if (hp1 <= 0 || hp2 <= 0) {
                resumenTurno = "¡¡BATALLA TERMINADA!!";
                if (hp1 > hp2) {
                    resumenTurno = resumenTurno + " " + nombre1 + " ES EL GANADOR";
                    //Se agrega dinero si el pokemon1 coincide con el ingresado, en caso contrario se resta
                    if(nombre1.equals(pokemonIngresado)){
                        agregarDinero(100);
                    }else{
                        quitarDinero(50);
                    }
                } else {
                    resumenTurno = resumenTurno + " " + nombre2 + " ES EL GANADOR";
                    //Se agrega dinero si el pokemon1 coincide con el ingresado, en caso contrario se resta
                    if(nombre2.equals(pokemonIngresado)){
                        agregarDinero(100);
                    }else{
                        quitarDinero(50);
                    }
                }
                resumenBatalla.add(resumenTurno);
            }
        }

        return resumenBatalla;
    }

    //Metodo que devuelve 1 si le pego y 0 si misseo
    public static int hitChance() {
        int hit;
        Random random = new Random();
        int fallo = random.nextInt(100);
        if (fallo >= posibilidadFallo) {
            hit = 0;
        } else {
            hit = 1;
        }
        return hit;
    }

    /**
     *
     * @param boolean probabilidad
     * @return
     */
    private static boolean probabilidad(int probabilidad) {
        //Retorna True o False dependiendo si el número random generado generado y la probabilidad ingresada
        boolean exito;
        Random random = new Random();
        int fallo = random.nextInt(100);
        if (fallo >= probabilidad) {
            exito = false;
        } else {
            exito = true;
        }

        return exito;
    }

    /**
     *
     * @param String pokemonIngresado
     * @return
     * @throws RemoteException
     */
    public String consultaPokemon(String pokemonIngresado) throws RemoteException {
        //Retorna la información de un pokémon en base a la ID de un pokemon ingresado

        PokeConector pokeconn = new PokeConector();
        pokeconn.pokeConectarBaseDatos();
        String informacionPokemon = pokeconn.informacionPokemon(pokemonIngresado);

        return informacionPokemon;

    }

    /**
     *
     * @return @throws RemoteException
     */
    public String safariPokemon() throws RemoteException {
        //Se inicializan la variables del  safari por cada llamada
        this.quitarDinero(500);
        this.probabilidadCaptura = 20;
        this.probabilidadHuir = 20;
        this.cantidadPokebola = 10;
        this.pokemonSafari = "";

        Random random = new Random();
        int pokemon = random.nextInt(721) + 1;

        PokeConector pokeconn = new PokeConector();
        pokeconn.pokeConectarBaseDatos();
        String nombrePokemon = pokeconn.nombrePokemon(pokemon);
        pokeconn.close();
        this.pokemonSafari = nombrePokemon;
        
        
        return (nombrePokemon + " salvaje ha aparecido");

    }

    public ArrayList<String> getListaPokemon() throws RemoteException{
        return listaPokemon;
    }

    public String arrojarPokebola() throws RemoteException {

        String resultado;
        cantidadPokebola -= 1;

        if (probabilidad(probabilidadCaptura)) {
            resultado = "false-Has Capturado a " + this.pokemonSafari;
            listaPokemon.add(this.pokemonSafari);
        } else {
            if (probabilidad(probabilidadHuir)) {
                resultado = "false-" + this.pokemonSafari + " ha huido";
            } else {

                if (cantidadPokebola == 0) {
                    resultado = "false-Ya no tienes pokebolas";
                } else {
                    resultado = "true-Fallaste! Pokebolas restantes: " + cantidadPokebola;
                }
            }
        }

        return resultado;
    }

    public String arrojarPiedra() throws RemoteException {

        String resultado;
        if (probabilidad(probabilidadHuir)) {
            resultado = "false-" + this.pokemonSafari + " ha huido";
        } else {
            probabilidadCaptura += 5;
            resultado = "true-El pokemon parece enfurecido";
        }

        return resultado;

    }

    public String arrojarCebo() throws RemoteException {
        String resultado;
        probabilidadHuir -= 5;
        if (probabilidad(probabilidadHuir)) {
            resultado = "false-" + this.pokemonSafari + " ha huido";
        } else {
            probabilidadCaptura -= 5;
            resultado = "true-El pokemon parece interesado";
        }

        return resultado;
    }

    public String agregarDinero(float monto) throws RemoteException{
        return this.bankService.agregarAlBalance(monto);
    }

    public String quitarDinero(float monto) throws RemoteException{
        return this.bankService.quitarAlBalance(monto);
    }

    public String verBalance() throws RemoteException{
        return this.bankService.verBalanceTotal();
    }
    
    public ArrayList<String> getListaPokemonCapturados() throws RemoteException{
        return this.listaPokemon;
    }
}

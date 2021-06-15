/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pokeserver;

/**
 *
 * @author Gravity
 */
public class PokeBankService {
    private float balanceTotal = 0;
    
    public String agregarAlBalance(float monto){
        this.balanceTotal = this.balanceTotal + monto;
        return "Se ha agregado "+monto+"!";
    }
    
    public String quitarAlBalance(float monto){
        if(this.balanceTotal < monto) {
            this.balanceTotal = 0;
            return "Se ha quitado "+monto+"!";
        }
        this.balanceTotal = this.balanceTotal - monto;
        return "Se ha quitado "+monto+"!";
    }
    
    public String verBalanceTotal(){
        return String.valueOf(this.balanceTotal);
    }
}

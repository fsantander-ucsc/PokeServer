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
    
    public void agregarAlBalance(float monto){
        this.balanceTotal +=  monto;
    }
    
    public void quitarAlBalance(float monto){
        if(this.balanceTotal > monto){
            this.balanceTotal -= monto;
        }
        else{
            this.balanceTotal = 0;
        }
    }
    
    public String verBalanceTotal(){
        return String.valueOf(this.balanceTotal);
    }
}

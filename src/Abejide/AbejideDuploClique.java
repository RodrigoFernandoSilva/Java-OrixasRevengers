/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Abejide;

import static Abejide.AbejideTeclado.teclaBaixo;
import static Abejide.AbejideTeclado.teclaDireita;
import static Abejide.AbejideTeclado.teclaEsquerda;

import static MetodosPrincipais.DeltaTime.dormir;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class AbejideDuploClique extends Thread{
    
    boolean rodando = true;
    double tempoMaximo = 0.3;
    String tecla;
    
    AbejideDuploClique(String t){
        tecla = t;
    }
    
    @Override
    public void run() {
        long segundosPassado = System.currentTimeMillis();
        double segundos = 0;
        
        while(rodando){
            if(System.currentTimeMillis() - segundosPassado > (1000 / 4)){
                segundosPassado = System.currentTimeMillis();
                segundos += 0.1;
            }
            
            if(segundos > tempoMaximo){
                switch (tecla) {
                case "teclaDireita":
                    teclaDireita = 0;
                    break;
                case "teclaEsquerda":
                    teclaEsquerda = 0;
                    break;
                case "teclaBaixo":
                    teclaBaixo = 0;
                    break;
                default:
                    break;
                }
                break;
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(AbejideDuploClique.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void MatarThread(){
        rodando = false;
    }
    
}

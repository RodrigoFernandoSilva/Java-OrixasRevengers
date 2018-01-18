/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

package Abejide;

import static Abejide.AbejideTeclado.teclaEsquerda;
import static Abejide.AbejideTeclado.teclaDireita;
import static Abejide.AbejideTeclado.teclaBaixo;
import static MetodosPrincipais.DeltaTime.dormir;
import static MetodosPrincipais.Main.teclado;
import static java.lang.Thread.sleep;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Rodrigo Fernando da Silva
 

//Essa thread serve para zerar a variável de teclas pressionadas, porque tem um contador que conta
//quantas vezes determinada tecla foi pressionada, então quando pasa um serto tempo a tecla tem
//tem que ser zerada, porque se não for feito isso, a vaisavel mais sempre ficar igual a 2 e,
//o sistema de combo não vai funcionar
public class AbejideZerarContador extends Thread{
    
    int  tecla;
    String nomeVariavel;
            
    public AbejideZerarContador(int tec, String nomeVar){
        tecla = tec;
        nomeVariavel = nomeVar;
    }
    
    @Override
    public void run() {
        while(MetodosPrincipais.Executando.executando){
            if(teclado.keyDown(tecla)){
                try {
                    sleep(800);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AbejideZerarContador.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(nomeVariavel == "teclaEsquerda" && teclaEsquerda != 0){
                    teclaEsquerda = 0;
                }
                if(nomeVariavel == "teclaDireita" && teclaDireita != 0){
                    teclaDireita = 0;
                }
                if(nomeVariavel == "teclaBaixo" && teclaBaixo != 0){
                    teclaBaixo = 0;
                }
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
*/
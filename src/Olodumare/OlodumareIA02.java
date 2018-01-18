/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Olodumare;

import Abejide.AbejideTeclado;
import static Olodumare.OlodumareThread.qualThreadAtaque;
import static Olodumare.OlodumareThread.VELOCIDADE;
import static Olodumare.OlodumareThread.x;
import static Olodumare.OlodumareThread.estaThreadIA02;

import static MetodosPrincipais.DeltaTime.deltaTime;
import static MetodosPrincipais.DeltaTime.dormir;
import static MetodosPrincipais.Executando.pausado;
import static java.lang.Thread.sleep;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class OlodumareIA02 extends Thread{
    
    public int distanciaPercorer;
    public static int direcao  = 0;
    
    public OlodumareIA02(int dist){
        distanciaPercorer = dist;
    }
    
    @Override
    public void run(){
        Random gerador = new Random();
        
        direcao  = 1;
        
        if(gerador.nextBoolean()){
            direcao = -1;
        }
        int distanciaMover = gerador.nextInt(distanciaPercorer);
        distanciaMover = 500;
        
        for(int i = 0; i < distanciaMover; i++){
            while (pausado) {
                try {
                    sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            x += (VELOCIDADE * deltaTime) * direcao;
            
            if(x > MetodosPrincipais.Executando.chao.x + MetodosPrincipais.Executando.chao.width){
                direcao = -1;
            } else if(x < MetodosPrincipais.Executando.chao.x){
                direcao = 1;
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumareIA02.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        estaThreadIA02 = false;
        qualThreadAtaque = 0;
    }
}

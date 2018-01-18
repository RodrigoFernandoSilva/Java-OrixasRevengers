/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Olodumare;

import static MetodosPrincipais.DeltaTime.dormir;
import static MetodosPrincipais.Executando.pausado;
import static MetodosPrincipais.ControladoDoLevel.tempoParado;

import static Olodumare.OlodumareThread.x;
import static Olodumare.OlodumareThread.alma;
import static Olodumare.OlodumareThread.qualThreadAtaque;
import static Olodumare.OlodumareThread.estaThreadIA01;
import static Olodumare.OlodumareThread.spriteSheetBaseDireita;
import static Olodumare.OlodumareThread.spriteSheetBaseEsquerda;
import static Olodumare.OlodumareThread.TEMPO_PARADO;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class OlodumareIA01 extends Thread{
    public Random gerador = new Random();;
    public int proTeletransporte;
    
    public OlodumareIA01(int pro1){
        proTeletransporte = pro1;
    }
    
    public void run(){
        
        
        if(gerador.nextInt(100) < proTeletransporte){
            spriteSheetBaseEsquerda.setSequenceTime(19, 29, true, 1500);
            spriteSheetBaseDireita.setSequenceTime(19, 29, true, 1500);
            while ((spriteSheetBaseEsquerda.getCurrFrame() + 1) < (spriteSheetBaseEsquerda.getFinalFrame())) {
                try {
                    sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(OlodumareIA01.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Teletransporte();
            spriteSheetBaseDireita.setSequenceTime(0, 8, true, TEMPO_PARADO);
            spriteSheetBaseEsquerda.setSequenceTime(0, 8, true, TEMPO_PARADO);
        }
        Parado();
        
        estaThreadIA01 = false;
        qualThreadAtaque = 0;
    }
    
    private void Teletransporte(){
        x = (gerador.nextDouble() * MetodosPrincipais.Executando.chao.width);
        if(x > MetodosPrincipais.Executando.chao.x){
            x = x - alma.width;
        }
    }
    
    private void Parado(){
        int tempoAtual = 0;
        int tempoFinal = gerador.nextInt(200) + 250;
        tempoFinal -= tempoParado;
        
        while (tempoAtual < tempoFinal) {
            
            while (pausado) {
                try {
                    sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(OlodumareIA01.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumareIA01.class.getName()).log(Level.SEVERE, null, ex);
            }
            tempoAtual ++;
        }
    }
}

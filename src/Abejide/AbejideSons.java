/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Abejide;

import static MetodosPrincipais.DeltaTime.dormir;

import static Abejide.AbejideTeclado.boTeclaDireita;
import static Abejide.AbejideTeclado.boTeclaEsquerda;
import static MetodosPrincipais.Executando.executando;
import static Abejide.AbejideTeclado.boEstaAtacando;
import static Abejide.ContadorPosAtaque.PodeAndar;
import static Abejide.AbejideThread.alma;
import static Abejide.AbejideThread.spriteSheetBaseDireita;
import static Abejide.AbejideAnimacao.boBolaDeFogo;
import static Abejide.AbejideAnimacao.boAtaqueFraco;

import jplay.Sound;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class AbejideSons extends Thread {
    //É zeradda na thread de animação do Abejide, quando a animação de ataque acaba
    public static boolean boAtaqueEspada = false;
    
    public void run() {
        String stAndando = "Audios/Abejide/Andando.wav";
        Sound andando = new Sound(stAndando);
        boolean boAndando = false;
        String stAtaqueEspada= "Audios/Abejide/AtaqueEspada.wav";
        Sound ataqueEspada = new Sound(stAtaqueEspada);
        
        double ultimoX = alma.x;
        
        while(executando){
            //Eprites que tem que tocar o som de passos
            if(spriteSheetBaseDireita.getCurrFrame() != 11 &&
               spriteSheetBaseDireita.getCurrFrame() != 15 &&
               spriteSheetBaseDireita.getCurrFrame() != 19 &&
               spriteSheetBaseDireita.getCurrFrame() != 23 && boAndando && alma.getVelocityY() == 0){
               boAndando = false; 
            }
            if(PodeAndar && (boTeclaDireita || boTeclaEsquerda) && !boAndando &&
              (spriteSheetBaseDireita.getCurrFrame() == 11 ||
               spriteSheetBaseDireita.getCurrFrame() == 15 ||
               spriteSheetBaseDireita.getCurrFrame() == 19 ||
               spriteSheetBaseDireita.getCurrFrame() == 23) && !boEstaAtacando && alma.getVelocityY() == 0){
                andando = new Sound(stAndando);
                andando.setVolume(-10);
                andando.play();
                boAndando = true;
            }
            
            if(boEstaAtacando && !boAtaqueEspada && boAtaqueFraco){
                ataqueEspada = new Sound(stAtaqueEspada);
                ataqueEspada.play();
                boAtaqueEspada = true;
            }
            
            try{
                sleep(dormir);
            } catch(InterruptedException ex){
                //Deu ruim
            }
            
            ultimoX = alma.x;
        }
        
        
    }
}

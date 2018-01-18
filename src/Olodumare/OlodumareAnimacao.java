/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Olodumare;

import Abejide.AbejideThread;
import static Olodumare.OlodumareThread.spriteSheetBaseDireita;
import static Olodumare.OlodumareThread.spriteSheetBaseEsquerda;
import static Olodumare.OlodumareIA02.direcao;
import static Olodumare.OlodumareThread.estaThreadIA02;
import static Olodumare.OlodumareThread.mostrandoEsquerda;
import static Olodumare.OlodumareThread.qualThreadAtaque;
import static Olodumare.OlodumareThread.olodumarePedraDoChao;
import static Olodumare.OlodumareThread.olodumarePedraGigante;

import static MetodosPrincipais.Executando.executando;
import static MetodosPrincipais.DeltaTime.dormir;
import static Olodumare.OlodumareThread.alma;
import static Olodumare.OlodumareThread.estaBolaDeFogo;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class OlodumareAnimacao extends Thread{
    
    @Override
    public void run() {
        
        boolean jaPedraSobeDoChao = false;
        
        while (executando) {
            
            //Troca a animção de direta e esquerda de acordo com a movimentação do Olodumare
            if (estaThreadIA02) {
                if (direcao == 1 && mostrandoEsquerda) {
                    spriteSheetBaseDireita.unhide();
                    spriteSheetBaseEsquerda.hide();
                    mostrandoEsquerda = false;
                } else if (direcao == -1 && !mostrandoEsquerda) {
                    spriteSheetBaseDireita.hide();
                    spriteSheetBaseEsquerda.unhide();
                    mostrandoEsquerda = true;
                }
            }
            
            if (qualThreadAtaque == 5 && !jaPedraSobeDoChao && !olodumarePedraDoChao.isAlive()) {
                jaPedraSobeDoChao = true;
                if ("Esquerda".equals(EsquerdaOuDireita()) && !mostrandoEsquerda) { //Onde Oloduamre esta em relação a Abejide
                    spriteSheetBaseDireita.hide();
                    spriteSheetBaseEsquerda.unhide();
                    mostrandoEsquerda = true;
                } else if ("Direita".equals(EsquerdaOuDireita()) && mostrandoEsquerda) {
                    spriteSheetBaseDireita.unhide();
                    spriteSheetBaseEsquerda.hide();
                    mostrandoEsquerda = false;
                }
            }
            
            if (qualThreadAtaque == 7 && !jaPedraSobeDoChao && olodumarePedraGigante.isAlive()) {
                if (!olodumarePedraGigante.PegarJaPodeDarDano()) {
                    jaPedraSobeDoChao = true;
                    if ("Esquerda".equals(EsquerdaOuDireita()) && !mostrandoEsquerda) { //Onde Oloduamre esta em relação a Abejide
                        spriteSheetBaseDireita.hide();
                        spriteSheetBaseEsquerda.unhide();
                        mostrandoEsquerda = true;
                    } else if ("Direita".equals(EsquerdaOuDireita()) && mostrandoEsquerda) {
                        spriteSheetBaseDireita.unhide();
                        spriteSheetBaseEsquerda.hide();
                        mostrandoEsquerda = false;
                    }
                }
            }
            
            if (jaPedraSobeDoChao && olodumarePedraDoChao.isAlive()) {
                jaPedraSobeDoChao = false;
            } else if (olodumarePedraGigante.isAlive()) {
                if (jaPedraSobeDoChao && olodumarePedraGigante.PegarJaPodeDarDano()) {
                    jaPedraSobeDoChao = false;
                }
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumareAnimacao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String EsquerdaOuDireita(){
        if(AbejideThread.alma.x + (AbejideThread.alma.width / 2) > alma.x + (alma.width / 2)){
            return "Direita";
        } else{
            return "Esquerda";
        }
    }
}

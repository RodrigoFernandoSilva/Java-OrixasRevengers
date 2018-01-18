/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MetodosPrincipais;

import static MetodosPrincipais.Executando.executando;
import static MetodosPrincipais.ControladoDoLevel.danoAtaqueOlodumare;
import static MetodosPrincipais.ControladoDoLevel.abejideGanhaMP;

import static Olodumare.OlodumareEnvenenado.envenenado;
import static Olodumare.OlodumareThread.olodumareBolaDeFogo;
import static Olodumare.OlodumareThread.olodumareBolaDeFogoCriadas;
import static Olodumare.OlodumareThread.olodumarePentagramaBola;
import static Olodumare.OlodumareThread.olodumarePedraDoChao;
import static Olodumare.OlodumareThread.olodumarePedraGigante;

import static Abejide.AbejideThread.alma;
import static Abejide.AbejideThread.mp;
import static Abejide.AbejideThread.hp;
import static Abejide.AbejideThread.ajustarCaixaDeColisao;
import static Abejide.AbejideThread.caixaDeColisao;
import static Abejide.AbejideAnimacao.qualAtaque;
import static Abejide.AbejideThread.spriteSheetAtaqueFracoDireita;
import static Abejide.AbejideThread.spriteSheetVenenoDireita;
import static Abejide.AbejideAnimacao.jaRodou;
import static Abejide.AbejideAnimacao.jaAtaqueEspecialForte;
import static Abejide.AbejideAnimacao.ataqueEspecial03;
import Abejide.AbejideTeclado;

import static MetodosPrincipais.DeltaTime.dormir;
import static MetodosPrincipais.Executando.pausado;
import static java.lang.Thread.sleep;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class Colisao extends Thread{
    
    public static int contadorDeHits = 0;
    public static double acressimoDeDanoHits = 0;
    public final int TEMPO_ZERAR_CONTADOR_MAX = 150;
    public static int tempoZerarContador = 0;
    public static final int BONUS_CONTADOR_ATAUQUE = 5;
    public static int bonusContadorAtaque1 = 0;
    public static int bonusContadorAtaque2 = 0;
    public static int[] contadorAtaque = new int[4]; //Salva quantas vezes determinado ataque foi
    //dado, tipo o z + baixo, z + esquerda e assim vai
    //0: z
    //1: z + direita
    //2: z + esquerda
    //3: z + baixo
    
    public static double olodumareBolaDeFogoDano = 0;
    
    @Override
    public void run(){
        int i; //Usada em laço de repetição
        int ganharMp = 4;
        
        //Danos que Abejide recebe quando é atacado
        double abejideBolaDeFogo;
        double abejidePedra;
        double abejidePedraGigante;
        
        //Dano que Olodumare recebe quando é atacado
        olodumareBolaDeFogoDano = 3;
        double olodumareAtaqueFracoDano = 3;
        double olodumareEspadaEncantadaDano = 3;
        double olodumareVeneno = 1;
        
        boolean DanoNoOlodumare = false;
        
        int tempoAtual = 5;
        int tempoFinal = 5;
        
        while(executando){
            
            while (pausado) {
                try {
                    sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            abejideBolaDeFogo = 3 + danoAtaqueOlodumare;
            abejidePedra = 13 + danoAtaqueOlodumare;
            abejidePedraGigante = 7 + danoAtaqueOlodumare;

            //Controla o dano adicional em relação ao número de hits e o combo
            if (tempoZerarContador > TEMPO_ZERAR_CONTADOR_MAX) {
                contadorDeHits = 0;
                for (i = 0; i < contadorAtaque.length; i ++)
                    contadorAtaque[i] = 0;
            }
            for (int n : contadorAtaque) {
                if (n > 8) {
                    for (i = 0; i < contadorAtaque.length; i ++)
                        contadorAtaque[i] = 0;
                    break;
                }
            }
            
            bonusContadorAtaque1 = (contadorAtaque[0] + contadorAtaque[1]) - (contadorAtaque[2] + contadorAtaque[3]);
            if (bonusContadorAtaque1 > 0) {
                bonusContadorAtaque1 *= -1;
            }
            bonusContadorAtaque2 = (contadorAtaque[1] + contadorAtaque[2]) - (contadorAtaque[0] + contadorAtaque[3]);
            if (bonusContadorAtaque2 > 0) {
                bonusContadorAtaque2 *= -1;
            }
            
            bonusContadorAtaque1 = (bonusContadorAtaque1 + bonusContadorAtaque2);
            bonusContadorAtaque1 += BONUS_CONTADOR_ATAUQUE;
            if (bonusContadorAtaque1 < 0) {
                bonusContadorAtaque1 = 0;
            }
            
            tempoZerarContador ++;
            acressimoDeDanoHits = (contadorDeHits / 3) + bonusContadorAtaque1;
            
            //Dano no Abejide ---------------------------------------------------------
            if (!jaRodou && !jaAtaqueEspecialForte) {
                for (i = 0; i < olodumareBolaDeFogoCriadas; i ++) {
                    if (olodumareBolaDeFogo[i].isAlive()) {
                        if ( alma.collided(olodumareBolaDeFogo[i].PegarSprie()) &&
                             olodumareBolaDeFogo[i].PegarEstaViva() &&
                             !olodumareBolaDeFogo[i].PegarBateuNoChao()) {
                            hp -= abejideBolaDeFogo;
                            olodumareBolaDeFogo[i].MatarThread();
                            contadorDeHits = 0;
                            tempoZerarContador = 0;
                        }
                    }
                }

                if (olodumarePentagramaBola.isAlive()) {
                    for (i = 0; i < olodumarePentagramaBola.PegarTamanhoDoVetor(); i ++) {
                        if (olodumarePentagramaBola.PegarEstaViva(i)) {
                            if ( alma.collided(olodumarePentagramaBola.PegarSprite(i)) &&
                                 !olodumarePentagramaBola.PegarJaDeuDanoNoAbejide(i)) {
                                hp -= abejideBolaDeFogo;
                                olodumarePentagramaBola.JaDeuDanoNoAbejide(i);
                                contadorDeHits = 0;
                                tempoZerarContador = 0;
                            }
                        }
                    }
                }

                if (olodumarePedraGigante.isAlive()) {
                    if (alma.collided(olodumarePedraGigante.PegarPedraGiganteCaixaDeColisao())
                        && !olodumarePedraGigante.PegarJaDeuDanoNoAbejide()
                        && olodumarePedraGigante.PegarJaPodeDarDano()) {
                        hp -= abejidePedraGigante;
                        olodumarePedraGigante.JaDeuDanoNoAbejide();
                    }
                }

                if (olodumarePedraDoChao.isAlive()) {
                    for (i = 0; i < olodumarePedraDoChao.PegarTamanhoDoVetor(); i++) {
                        if (olodumarePedraDoChao.PegarPedraJaCriada(i))
                        if (alma.collided(olodumarePedraDoChao.PegarCaixaDeColisao(i))
                            && !olodumarePedraDoChao.PegarJaDeuDanoNoAbejide(i)) {
                            hp -= abejidePedra;
                            olodumarePedraDoChao.JaDeuDanoNoAbejide(i);
                        }
                    }
                }
            }
            
            if(alma.x < Olodumare.OlodumareThread.alma.x){
                MudarTamanhoCaixaDeColisaoDireita();
            } else{
                MudarTamanhoCaixaDeColisaoEsquerda();
            }
            
            //Dano no Olodumare ---------------------------------------------------------
            //Da o dano no Oloduamre quando for o frame da animação de dar dano do abejide, tipo
            //quando a espada for colidir com o Olodumare
            
            if (ataqueEspecial03 && caixaDeColisao.collided(Olodumare.OlodumareThread.alma)
                && tempoAtual >= tempoFinal) {
                Olodumare.OlodumareThread.hpTemp -= (olodumareAtaqueFracoDano + acressimoDeDanoHits);
                tempoAtual = 0;
            } else if (tempoAtual < tempoFinal) {
                tempoAtual ++;
            }
            
            if(qualAtaque != 0 && !DanoNoOlodumare && caixaDeColisao.collided(Olodumare.OlodumareThread.alma)){
                //Escudo
                if(qualAtaque == 1 && spriteSheetAtaqueFracoDireita.getCurrFrame() == 2){
                    Olodumare.OlodumareThread.hpTemp -= (olodumareAtaqueFracoDano + acressimoDeDanoHits);
                    DanoNoOlodumare = true;
                    mp += ganharMp + abejideGanhaMP;
                    contadorDeHits ++;
                    tempoZerarContador = 0;
                    contadorAtaque[0] ++;
                //Espadada, para cima e a para baixo
                } else if((qualAtaque == 2 || qualAtaque == 3) &&
                        (spriteSheetAtaqueFracoDireita.getCurrFrame() == 8 ||
                         spriteSheetAtaqueFracoDireita.getCurrFrame() == 13)){
                    Olodumare.OlodumareThread.hpTemp -= (olodumareAtaqueFracoDano + acressimoDeDanoHits);
                    DanoNoOlodumare = true;
                    mp += ganharMp + abejideGanhaMP;
                    contadorDeHits ++;
                    tempoZerarContador = 0;
                    if (qualAtaque == 2)
                        contadorAtaque[1] ++;
                    else {
                        contadorAtaque[2] ++;
                    }
                //Ataque com a bola de veneno, são dois hits
                } else if((qualAtaque == 5 && spriteSheetVenenoDireita.getCurrFrame() == 3) ||
                          (qualAtaque == 5 && spriteSheetVenenoDireita.getCurrFrame() == 6)){
                    Olodumare.OlodumareThread.hpTemp -= (olodumareVeneno + acressimoDeDanoHits);
                    contadorDeHits ++;
                    tempoZerarContador = 0;
                    envenenado += 50;
                    if(envenenado > 100){
                        envenenado = 100;
                    }
                    DanoNoOlodumare = true;
                //Ataque com a espada encantada, são dois hits, para cima e para baixo
                } else if((qualAtaque == 6 && spriteSheetVenenoDireita.getCurrFrame() == 11) ||
                          (qualAtaque == 6 && spriteSheetVenenoDireita.getCurrFrame() == 15)){
                    Olodumare.OlodumareThread.hpTemp -= (10 + acressimoDeDanoHits);
                    DanoNoOlodumare = true;
                    contadorDeHits ++;
                    tempoZerarContador = 0;
                } else if(qualAtaque == 7 && spriteSheetAtaqueFracoDireita.getCurrFrame() == 17){
                    Olodumare.OlodumareThread.hpTemp -= (3 + acressimoDeDanoHits);
                    DanoNoOlodumare = true;
                    mp += ganharMp + abejideGanhaMP;
                    contadorDeHits ++;
                    tempoZerarContador = 0;
                    contadorAtaque[3] ++;
                }
            }
            
            if((qualAtaque == 0 && DanoNoOlodumare) ||
               (qualAtaque == 5 && spriteSheetVenenoDireita.getCurrFrame() != 6 && spriteSheetVenenoDireita.getCurrFrame() != 3) ||
               (qualAtaque == 6 && spriteSheetVenenoDireita.getCurrFrame() != 11 && spriteSheetVenenoDireita.getCurrFrame() != 15)){
                DanoNoOlodumare = false;
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void MudarTamanhoCaixaDeColisaoDireita(){
        switch (qualAtaque) {
            case 1:
                caixaDeColisao.width = 39;
                ajustarCaixaDeColisao = 30;
                break;
            case 2:
            case 3:
            case 6:
                caixaDeColisao.width = 47;
                ajustarCaixaDeColisao = 40;
                break;
            case 5:
                caixaDeColisao.width = 115;
                ajustarCaixaDeColisao = 80;
                break;
            case 7:
                caixaDeColisao.width = 37;
                ajustarCaixaDeColisao = 28;
                break;
            default:
                break;
        }
    }
    
    public void MudarTamanhoCaixaDeColisaoEsquerda(){
        int ajusteX = 10;
        switch (qualAtaque) {
            case 1:
                caixaDeColisao.width = 39;
                ajustarCaixaDeColisao = -27 + ajusteX;
                break;
            case 2:
            case 3:
            case 6:
                caixaDeColisao.width = 47;
                ajustarCaixaDeColisao = -35 + ajusteX;
                break;
            case 5:
                caixaDeColisao.width = 90;
                ajustarCaixaDeColisao = -70 + ajusteX;
                break;
            case 7:
                caixaDeColisao.width = 37;
                ajustarCaixaDeColisao = -25 + ajusteX;
                break;
            default:
                break;
        }
    }
}

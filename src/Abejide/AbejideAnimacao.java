/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Abejide;

import static MetodosPrincipais.DeltaTime.dormir;

import static Abejide.AbejideThread.spriteSheetBaseDireita;
import static Abejide.AbejideThread.spriteSheetAtaqueFracoDireita;
import static Abejide.AbejideThread.spriteSheetBaseEsquerda;
import static Abejide.AbejideThread.spriteSheetAtaqueFracoEsquerda;
import static Abejide.AbejideThread.spriteSheetVenenoDireita;
import static Abejide.AbejideThread.spriteSheetVenenoEsquerda;
import static Abejide.AbejideTeclado.boEstaAtacando;
import static Abejide.AbejideTeclado.boTeclaEsquerda;
import static Abejide.AbejideTeclado.boTeclaDireita;
import static Abejide.AbejideThread.perderSmAtacando;
import static Abejide.AbejideThread.sm;
import static Abejide.AbejideThread.mp;
import static Abejide.AbejideTeclado.teclaDireita;
import static Abejide.AbejideTeclado.teclaEsquerda;
import static Abejide.AbejideTeclado.teclaBaixo;
import static Abejide.ContadorPosAtaque.PodeAndar;
import static Abejide.AbejideSons.boAtaqueEspada;
import static Abejide.AbejideThread.alma;
import static Abejide.AbejideThread.bolaDeFogo;
import static Abejide.AbejideThread.bolaDeFogoEspecial01;
import static Abejide.AbejideThread.bolaDeFogoEspecial01_0;
import static Abejide.AbejideThread.bolaDeFogoEspecial01_1;
import static Abejide.AbejideThread.spriteSheetRolandoDireita;
import static Abejide.AbejideThread.spriteSheetRolandoEsquerda;
import static Abejide.AbejideThread.perderSmRolando;
import static Abejide.AbejideThread.mpCarregando;
import static Abejide.AbejideThread.level;
import static Abejide.AbejideThread.faixaDeErnegia;
import static Abejide.AbejideThread.hp;

import static MetodosPrincipais.Camera.velocidadeCorendo;
import static MetodosPrincipais.Executando.barraGrade;
import static MetodosPrincipais.Executando.barraHP;
import static MetodosPrincipais.Executando.barraMP;
import static MetodosPrincipais.Executando.barraMPcarregando;
import static MetodosPrincipais.Executando.barraSM;
import static MetodosPrincipais.Main.teclado;

import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import jplay.Keyboard;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class AbejideAnimacao extends Thread{
    //Constantes que é usada como base para setara velocidade da animação do personagem,
    //pq o valor muda de acordo com a estamina
    static final int TEMPO_ATACANDO = 600;
    static final int TEMPO_PARADO = 2000;
    
    public static int qualAtaque = 0;
    //1: Ataque com o escudo
    //2: Ataque com a espada subindo
    //3: Ataque com a espada decendo
    //7: Ataque de baixo para cima
    //4: Ataque com a bola de fogo  - Ataque especial fraco, gasta MP
    //5: Ataque com veneno - Ataque especial fraco, gasta MP
    //6: Combo espada subindo e desendo com ela encantada - Ataque especial fraco, gasta MP
    
    public static boolean boAtaqueEspecial = false;
    public static boolean boAtaqueFraco = false;
    public static boolean boBolaDeFogo = false; //Fala se esta atacando com a bola de fogo
    public static boolean boPodeBolaDeFogo = true; //Impedi que a thread de criar bolo de fogo
                                                   //seja chamada varias vezes seguidas
    public static boolean jaRodou = false;
    public static int direcaoRodou = 1;
    public static boolean jaAtaqueEspecialForte = false;
    
    public static boolean ataqueEspecial03 = false;
    
    @Override
    public void run() {
        
        ContadorPosAtaque contadorPosAtaque = new ContadorPosAtaque();
        contadorPosAtaque.start();
        
        //Variáveis que controla o tempo das animações
        int tempoParado;
        int tempoAndando = 1000;
        int tempoAtacando;
        int tempoRolando = 800;
        int i;
        
        //Controla para entrar em algumas desições só uma unica vez
        boolean jaParou = false;
        boolean jaAndou = false;
        boolean ataqueEspecial01 = false;
        boolean ataqueEspecial02 = false;        
        int especial01QuantidadeLancada = 0;
        boolean bolaDeFogoDirecao = true;
        
        boolean jaBolaDeFogoEspecial01;
        boolean jaFaixaDeErnegiaEspecil01;
        int intdiceAtaqueEspecial01 = 0;
        
        while(MetodosPrincipais.Executando.executando){
            
            tempoAtacando = (int) (TEMPO_ATACANDO + ((50 - sm) * 7));
            tempoParado   = (int) (TEMPO_PARADO   - ((50 - sm) * 10));
            
            //Ataque especial forte, gasta mp
            if (!teclado.keyDown(KeyEvent.VK_Z)) {
                if (!jaAtaqueEspecialForte && mpCarregando >= 90 && level >= 10) {
                    ataqueEspecial03 = true;  
                } else if (!jaAtaqueEspecialForte && mpCarregando >= 64 && level >= 9) {
                    ataqueEspecial02 = true;
                } else if (!jaAtaqueEspecialForte && mpCarregando >= 32 && level >= 7) {
                    ataqueEspecial01 = true;
                } else {
                    mpCarregando = 0;
                }
            }
            
            if (ataqueEspecial03) {
                jaAtaqueEspecialForte = true;
                
                spriteSheetBaseDireita.hide();
                spriteSheetBaseEsquerda.hide();
                spriteSheetAtaqueFracoDireita.hide();
                spriteSheetAtaqueFracoEsquerda.hide();
                spriteSheetVenenoDireita.hide();
                spriteSheetVenenoEsquerda.hide();
                spriteSheetRolandoDireita.hide();
                spriteSheetRolandoEsquerda.hide();
                
                if ("Esquerda".equals(EsquerdaOuDireita())) {
                    spriteSheetAtaqueFracoDireita.unhide();
                } else {
                    spriteSheetAtaqueFracoEsquerda.unhide();
                }
                
                spriteSheetAtaqueFracoDireita.setSequenceTime(0, 20, true, tempoAtacando - 200);
                spriteSheetAtaqueFracoEsquerda.setSequenceTime(0, 20, true, tempoAtacando - 200);
                
                mpCarregando = 0;
                mp = 0;
                ColocarCoisasNasBarras();
                
                int tempoAtual = 0;
                int tempoFinal = 40;
                
                while (tempoAtual < tempoFinal) {
                    
                    if (spriteSheetAtaqueFracoDireita.getCurrFrame() <= 5) {
                        qualAtaque = 0;
                    } else if (spriteSheetAtaqueFracoDireita.getCurrFrame() <= 10) {
                        qualAtaque = 2;
                    } else if (spriteSheetAtaqueFracoDireita.getCurrFrame() <= 15) {
                        qualAtaque = 3;
                    }
                     else if (spriteSheetAtaqueFracoDireita.getCurrFrame() <= 20) {
                        qualAtaque = 7;
                    }
                    
                    tempoAtual ++;
                    
                    try {
                        sleep(dormir + 100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AbejideAnimacao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                jaAtaqueEspecialForte = false;
                ataqueEspecial03 = false;
            }
            
            if (ataqueEspecial02) {
                jaAtaqueEspecialForte = true;
                
                spriteSheetBaseDireita.hide();
                spriteSheetBaseEsquerda.hide();
                spriteSheetAtaqueFracoDireita.hide();
                spriteSheetAtaqueFracoEsquerda.hide();
                spriteSheetVenenoDireita.hide();
                spriteSheetVenenoEsquerda.hide();
                spriteSheetRolandoDireita.hide();
                spriteSheetRolandoEsquerda.hide();
                
                if ("Esquerda".equals(EsquerdaOuDireita())) {
                    spriteSheetAtaqueFracoDireita.unhide();
                    bolaDeFogoDirecao = false;
                } else {
                    spriteSheetAtaqueFracoEsquerda.unhide();
                    bolaDeFogoDirecao = true;
                }
                
                spriteSheetAtaqueFracoDireita.setSequenceTime(5, 10, true, tempoAtacando);
                spriteSheetAtaqueFracoEsquerda.setSequenceTime(5, 10, true, tempoAtacando);
                
                especial01QuantidadeLancada = 0;
                jaFaixaDeErnegiaEspecil01 = false;
                
                mpCarregando = 0;
                mp -= 66.6;
                ColocarCoisasNasBarras();
                
                while (true) {
                    
                    if (spriteSheetAtaqueFracoDireita.getCurrFrame() >= 6 && !jaFaixaDeErnegiaEspecil01) {
                        faixaDeErnegia[especial01QuantidadeLancada] = new FaixaDeErnegia(alma.x, alma.y, bolaDeFogoDirecao, true);
                        faixaDeErnegia[especial01QuantidadeLancada].start();
                        faixaDeErnegia[especial01QuantidadeLancada].DeixarViva();
                        especial01QuantidadeLancada ++;
                        jaFaixaDeErnegiaEspecil01 = true;
                    } else if (spriteSheetAtaqueFracoDireita.getCurrFrame() < 6 && jaFaixaDeErnegiaEspecil01) {
                        jaFaixaDeErnegiaEspecil01 = false;
                    }
                    
                    if (especial01QuantidadeLancada >= faixaDeErnegia.length) {
                        break;
                    }
                    
                    try {
                        sleep(dormir);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AbejideAnimacao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                ataqueEspecial02 = false;
                jaAtaqueEspecialForte = false;
            }
            
            if (ataqueEspecial01) {
                jaAtaqueEspecialForte = true;
                 
                spriteSheetBaseDireita.hide();
                spriteSheetBaseEsquerda.hide();
                spriteSheetAtaqueFracoDireita.hide();
                spriteSheetAtaqueFracoEsquerda.hide();
                spriteSheetVenenoDireita.hide();
                spriteSheetVenenoEsquerda.hide();
                spriteSheetRolandoDireita.hide();
                spriteSheetRolandoEsquerda.hide();
                
                if ("Esquerda".equals(EsquerdaOuDireita())) {
                    spriteSheetAtaqueFracoDireita.unhide();
                    bolaDeFogoDirecao = false;
                } else {
                    spriteSheetAtaqueFracoEsquerda.unhide();
                    bolaDeFogoDirecao = true;
                }
                
                spriteSheetAtaqueFracoDireita.setSequenceTime(0, 5, true, tempoAtacando - 200);
                spriteSheetAtaqueFracoEsquerda.setSequenceTime(0, 5, true, tempoAtacando - 200);
                mpCarregando = 0;
                mp -= 33.3;
                ColocarCoisasNasBarras();
                
                especial01QuantidadeLancada = 0;
                jaBolaDeFogoEspecial01 = false;
                
                if (intdiceAtaqueEspecial01 >= 3) {
                    intdiceAtaqueEspecial01 = 0;
                }
                
                while (true) {
                    if (spriteSheetAtaqueFracoDireita.getCurrFrame() >= 2 && !jaBolaDeFogoEspecial01) {
                        switch (intdiceAtaqueEspecial01) {
                            case 0:
                                bolaDeFogoEspecial01[especial01QuantidadeLancada] = new BolaDeFogo(alma.x, alma.y, bolaDeFogoDirecao, true);
                                bolaDeFogoEspecial01[especial01QuantidadeLancada].start();
                                bolaDeFogoEspecial01[especial01QuantidadeLancada].DeixarViva();
                                especial01QuantidadeLancada ++;
                                jaBolaDeFogoEspecial01 = true;
                                break;
                            case 1:
                                bolaDeFogoEspecial01_0[especial01QuantidadeLancada] = new BolaDeFogo(alma.x, alma.y, bolaDeFogoDirecao, true);
                                bolaDeFogoEspecial01_0[especial01QuantidadeLancada].start();
                                bolaDeFogoEspecial01_0[especial01QuantidadeLancada].DeixarViva();
                                especial01QuantidadeLancada ++;
                                jaBolaDeFogoEspecial01 = true;
                                break;
                            default:
                                bolaDeFogoEspecial01_1[especial01QuantidadeLancada] = new BolaDeFogo(alma.x, alma.y, bolaDeFogoDirecao, true);
                                bolaDeFogoEspecial01_1[especial01QuantidadeLancada].start();
                                bolaDeFogoEspecial01_1[especial01QuantidadeLancada].DeixarViva();
                                especial01QuantidadeLancada ++;
                                jaBolaDeFogoEspecial01 = true;
                                break;
                        }
                    }
                    if (jaBolaDeFogoEspecial01 && spriteSheetAtaqueFracoDireita.getCurrFrame() < 2) {
                        jaBolaDeFogoEspecial01 = false;
                    }
                    
                    
                    if (intdiceAtaqueEspecial01 == 0 && especial01QuantidadeLancada >= bolaDeFogoEspecial01.length) {
                        break;
                    }
                    if (intdiceAtaqueEspecial01 == 1 && especial01QuantidadeLancada >= bolaDeFogoEspecial01_0.length) {
                        break;
                    }
                    if (intdiceAtaqueEspecial01 == 2 && especial01QuantidadeLancada >= bolaDeFogoEspecial01_1.length) {
                        break;
                    }
                    
                    try {
                        sleep(dormir);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(AbejideAnimacao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                intdiceAtaqueEspecial01 ++;
                ataqueEspecial01 = false;
                jaAtaqueEspecialForte = false;
            }
            
            if (!jaAtaqueEspecialForte) {
                //Volta a animação quando Abejide termiar de rolar
                if (jaRodou && spriteSheetRolandoDireita.getCurrFrame() >= (spriteSheetRolandoDireita.getFinalFrame() - 1)) {
                    if("Esquerda".equals(EsquerdaOuDireita())){
                        spriteSheetBaseDireita.unhide();
                        spriteSheetBaseEsquerda.hide();
                        spriteSheetAtaqueFracoDireita.hide();
                        spriteSheetAtaqueFracoEsquerda.hide();
                        spriteSheetVenenoDireita.hide();
                        spriteSheetVenenoEsquerda.hide();
                        spriteSheetRolandoDireita.hide();
                        spriteSheetRolandoEsquerda.hide();
                    } else{
                        spriteSheetBaseDireita.hide();
                        spriteSheetBaseEsquerda.unhide();
                        spriteSheetAtaqueFracoDireita.hide();
                        spriteSheetAtaqueFracoEsquerda.hide();
                        spriteSheetVenenoDireita.hide();
                        spriteSheetVenenoEsquerda.hide();
                        spriteSheetRolandoDireita.hide();
                        spriteSheetRolandoEsquerda.hide();
                    }

                    jaAndou = false;
                    jaRodou = false;
                }

                //Sistema de rola
                if (teclado.keyDown(Keyboard.SPACE_KEY)) {
                    if(boTeclaDireita && !jaRodou){
                        spriteSheetBaseDireita.hide();
                        spriteSheetAtaqueFracoDireita.hide();
                        spriteSheetBaseEsquerda.hide();
                        spriteSheetAtaqueFracoEsquerda.hide();
                        spriteSheetVenenoDireita.hide();
                        spriteSheetVenenoEsquerda.hide();
                        spriteSheetRolandoDireita.unhide();
                        spriteSheetRolandoEsquerda.hide();
                        jaRodou = true;
                        spriteSheetRolandoDireita.setSequenceTime(0, 8, true, tempoRolando);
                        spriteSheetRolandoEsquerda.setSequenceTime(0, 8, true, tempoRolando);
                        sm -= perderSmRolando;
                        direcaoRodou = 1;
                    } else if (boTeclaEsquerda && !jaRodou) {
                        spriteSheetBaseDireita.hide();
                        spriteSheetAtaqueFracoDireita.hide();
                        spriteSheetBaseEsquerda.hide();
                        spriteSheetAtaqueFracoEsquerda.hide();
                        spriteSheetVenenoDireita.hide();
                        spriteSheetVenenoEsquerda.hide();
                        spriteSheetRolandoDireita.hide();
                        spriteSheetRolandoEsquerda.unhide();
                        jaRodou = true;
                        spriteSheetRolandoDireita.setSequenceTime(0, 8, true, tempoRolando);
                        spriteSheetRolandoEsquerda.setSequenceTime(0, 8, true, tempoRolando);
                        sm -= perderSmRolando;
                        direcaoRodou = -1;
                    }
                }
                if (!jaRodou) {
                    //Ataque especial fraco
                    if(teclado.keyDown(KeyEvent.VK_X) && !boEstaAtacando ){
                        if("Esquerda".equals(EsquerdaOuDireita())){
                            if(teclaDireita == 2 && mp >= 16.5 && !boBolaDeFogo && level >=1){
                                EsquerdaOuDireitaAtaqueFraco();
                                spriteSheetAtaqueFracoDireita.setSequenceTime(0, 5, true, tempoAtacando);
                                spriteSheetAtaqueFracoEsquerda.setSequenceTime(0, 5, true, tempoAtacando);
                                bolaDeFogoDirecao = false;
                                boEstaAtacando = true;
                                boBolaDeFogo = true;
                            } else if(teclaEsquerda == 2 && mp >= 16.5 && level >= 3){
                                EsquerdaOuDireitaAtaqueEspecial();
                                spriteSheetVenenoDireita.setSequenceTime(0, 8, true, tempoAtacando + 200);
                                spriteSheetVenenoEsquerda.setSequenceTime(0, 8, true, tempoAtacando + 200);
                                boEstaAtacando = true;
                                boAtaqueEspecial = true;
                                qualAtaque = 5;
                                mp = mp - 16.5;
                            } else if(teclaBaixo == 2 && mp >= 16.5 && level >= 5){
                                EsquerdaOuDireitaAtaqueEspecial();
                                spriteSheetVenenoDireita.setSequenceTime(8, 18, true, tempoAtacando + 250);
                                spriteSheetVenenoEsquerda.setSequenceTime(8, 18, true, tempoAtacando + 250);
                                boEstaAtacando = true;
                                boAtaqueEspecial = true;
                                qualAtaque = 6;
                                mp = mp - 16.6;
                            }
                        } else{
                            if(teclaEsquerda == 2 && mp >= 16.5 && !boBolaDeFogo && level >= 1){
                                EsquerdaOuDireitaAtaqueFraco();
                                spriteSheetAtaqueFracoDireita.setSequenceTime(0, 5, true, tempoAtacando);
                                spriteSheetAtaqueFracoEsquerda.setSequenceTime(0, 5, true, tempoAtacando);
                                bolaDeFogoDirecao = true;
                                boEstaAtacando = true;
                                boBolaDeFogo = true;
                            } else if(teclaDireita == 2 && mp >= 16.5 && level >= 3){
                                EsquerdaOuDireitaAtaqueEspecial();
                                spriteSheetVenenoDireita.setSequenceTime(0, 8, true, tempoAtacando + 300);
                                spriteSheetVenenoEsquerda.setSequenceTime(0, 8, true, tempoAtacando + 300);
                                boEstaAtacando = true;
                                boAtaqueEspecial = true;
                                qualAtaque = 5;
                                mp = mp - 16.5;
                            } else if(teclaBaixo == 2 && mp >= 16.5 && level >= 5){
                                EsquerdaOuDireitaAtaqueEspecial();
                                spriteSheetVenenoDireita.setSequenceTime(8, 18, true, tempoAtacando + 250);
                                spriteSheetVenenoEsquerda.setSequenceTime(8, 18, true, tempoAtacando + 250);
                                boEstaAtacando = true;
                                boAtaqueEspecial = true;
                                qualAtaque = 6;
                                mp = mp - 16.5;
                            }
                        }
                    }

                    //Ataque fraco
                    if(boBolaDeFogo && (spriteSheetAtaqueFracoDireita.getCurrFrame() == 2)){
                        //Vê qual das thread de bola de fogo esta morta, para que possa ser criado
                        //uma nova bola de fogo encima desta thread morta
                        for(i = 0; i < bolaDeFogo.length; i ++){
                            if(bolaDeFogo[i].EstaViva() == false){
                                break;
                            }
                        }
                        if(i < bolaDeFogo.length){
                            if("Esquerda".equals(EsquerdaOuDireita())){
                                bolaDeFogo[i] = new BolaDeFogo(alma.x, alma.y, bolaDeFogoDirecao, false);
                            } else{
                                bolaDeFogo[i] = new BolaDeFogo(alma.x, alma.y, bolaDeFogoDirecao, false);
                            }
                            bolaDeFogo[i].start();
                            bolaDeFogo[i].DeixarViva();
                            qualAtaque = 4;
                            boPodeBolaDeFogo = false;
                            boBolaDeFogo = false;
                            mp = mp - 16.5;
                        }
                    }

                    //Vê se o jogador esta atacando
                    if(teclado.keyDown(KeyEvent.VK_Z) && !boEstaAtacando && qualAtaque == 0){
                        //Depois vê qual foi o tipo de ataque o o jogador esta fazendo
                        boEstaAtacando = true;
                        boAtaqueFraco = true;
                        velocidadeCorendo = 0;
                        if(sm > 0){
                            sm -=perderSmAtacando;
                        }

                        //Só faz esse ataque se a telca direita estiver pressionada e a esquerda
                        //não estiver pressionada, o mesmo se aplica em outros, só que invertido
                        if(teclado.keyDown(Keyboard.LEFT_KEY) && !teclado.keyDown(Keyboard.RIGHT_KEY)){
                            EsquerdaOuDireitaAtaqueFraco();
                            spriteSheetAtaqueFracoDireita.setSequenceTime(5, 10, true, tempoAtacando);
                            spriteSheetAtaqueFracoEsquerda.setSequenceTime(5, 10, true, tempoAtacando);
                            qualAtaque = 3;
                        } else if(!teclado.keyDown(Keyboard.LEFT_KEY) && teclado.keyDown(Keyboard.RIGHT_KEY)){
                            EsquerdaOuDireitaAtaqueFraco();
                            spriteSheetAtaqueFracoDireita.setSequenceTime(10, 15, true, tempoAtacando);
                            spriteSheetAtaqueFracoEsquerda.setSequenceTime(10, 15, true, tempoAtacando);
                            qualAtaque = 2;
                        } else if(teclado.keyDown(Keyboard.DOWN_KEY)){
                            EsquerdaOuDireitaAtaqueFraco();
                            spriteSheetAtaqueFracoDireita.setSequenceTime(15, 20, true, tempoAtacando);
                            spriteSheetAtaqueFracoEsquerda.setSequenceTime(15, 20, true, tempoAtacando);
                            qualAtaque = 7;
                        } else{
                            EsquerdaOuDireitaAtaqueFraco();
                            spriteSheetAtaqueFracoDireita.setSequenceTime(0, 5, true, tempoAtacando);
                            spriteSheetAtaqueFracoEsquerda.setSequenceTime(0, 5, true, tempoAtacando);
                            qualAtaque = 1;
                        }
                    } else{
                        //Se o jogador estive se movendo faz a animação dele andando
                        if(jaAndou == false && PodeAndar){
                            //Temque vê se uma é verdadeiro e a outra é falso, porque se não fizer isso,
                            //se as duas teclas for pressionada ao mesmo tempo, o personagem vai fazer
                            //a animação e porém não vai sair do lugar
                            if(boTeclaDireita && !boTeclaEsquerda){
                                spriteSheetBaseDireita.setSequenceTime(11, 18, true, tempoAndando);
                                spriteSheetBaseEsquerda.setSequenceTime(18, 26, true, tempoAndando);
                                jaAndou = true;
                                jaParou = false;
                            } else if(!boTeclaDireita && boTeclaEsquerda){
                                spriteSheetBaseDireita.setSequenceTime(18, 26, true, tempoAndando);
                                spriteSheetBaseEsquerda.setSequenceTime(11, 18, true, tempoAndando);
                                jaAndou = true;
                                jaParou = false;
                            }
                        }

                        //Caso contrario, quer dizer que eles esta parado, então muda para a animação
                        //dele parado. Se as duas teclas de movimento estiverem sendo pressionadas,
                        //o personagem também vai efetuar a animação de parado
                        if((((!jaParou && boTeclaEsquerda == false && boTeclaDireita == false) ||
                            (!jaParou && boTeclaDireita == true && boTeclaEsquerda == true))) &&
                            !boEstaAtacando){
                            if("Esquerda".equals(EsquerdaOuDireita())){
                                spriteSheetBaseDireita.unhide();
                                spriteSheetAtaqueFracoDireita.hide();
                                spriteSheetBaseEsquerda.hide();
                                spriteSheetAtaqueFracoEsquerda.hide();
                                spriteSheetVenenoDireita.hide();
                                spriteSheetVenenoEsquerda.hide();
                                spriteSheetRolandoDireita.hide();
                                spriteSheetRolandoEsquerda.hide();
                                spriteSheetBaseDireita.setSequenceTime(0, 10, true, tempoParado);
                            } else{
                                spriteSheetBaseDireita.hide();
                                spriteSheetAtaqueFracoDireita.hide();
                                spriteSheetBaseEsquerda.unhide();
                                spriteSheetAtaqueFracoEsquerda.hide();
                                spriteSheetVenenoDireita.hide();
                                spriteSheetVenenoEsquerda.hide();
                                spriteSheetRolandoDireita.hide();
                                spriteSheetRolandoEsquerda.hide();
                                spriteSheetBaseEsquerda.setSequenceTime(0, 10, true, tempoParado);
                            }

                            jaParou = true;
                            jaAndou = false;
                        }
                    }

                    //Zera a animação após a animação de ataque acabar
                    if((!boAtaqueEspecial && (spriteSheetAtaqueFracoDireita.getFinalFrame() - 1 ) == spriteSheetAtaqueFracoDireita.getCurrFrame()) ||
                       (boAtaqueEspecial  && (spriteSheetVenenoDireita.getFinalFrame() - 1 ) == spriteSheetVenenoDireita.getCurrFrame())){
                        if(boEstaAtacando == true){
                            if("Esquerda".equals(EsquerdaOuDireita())){
                                spriteSheetBaseDireita.unhide();
                                spriteSheetBaseEsquerda.hide();
                                spriteSheetAtaqueFracoDireita.hide();
                                spriteSheetAtaqueFracoEsquerda.hide();
                                spriteSheetVenenoDireita.hide();
                                spriteSheetVenenoEsquerda.hide();
                                spriteSheetRolandoDireita.hide();
                                spriteSheetRolandoEsquerda.hide();
                            } else{
                                spriteSheetBaseDireita.hide();
                                spriteSheetBaseEsquerda.unhide();
                                spriteSheetAtaqueFracoDireita.hide();
                                spriteSheetAtaqueFracoEsquerda.hide();
                                spriteSheetVenenoDireita.hide();
                                spriteSheetVenenoEsquerda.hide();
                                spriteSheetRolandoDireita.hide();
                                spriteSheetRolandoEsquerda.hide();
                            }
                            if(!teclado.keyDown(KeyEvent.VK_Z)){
                                boEstaAtacando = false;
                                boAtaqueEspada = false;
                            }

                            if(jaAndou == true){
                                spriteSheetBaseDireita.setSequenceTime(0, 10, true, tempoParado);
                                spriteSheetBaseEsquerda.setSequenceTime(0, 10, true, tempoParado);
                            }

                            jaAndou = false;
                            qualAtaque = 0;
                            boAtaqueFraco = false;
                            boAtaqueEspecial = false;

                        }
                    }

                    //Este if atualiza o tempo da animação do personagem quando ele estiver parado,
                    //porque conforme a estamina acaba, o personagem respira mais rapido, se for mudado
                    //a todo momento a velocidade da animação vai dar ruim, porque vai interferir nas
                    //outras animações
                    if((spriteSheetBaseDireita.getFinalFrame() - 1 ) == spriteSheetBaseDireita.getCurrFrame()){
                        if(!boTeclaEsquerda && !boTeclaDireita){
                            spriteSheetBaseDireita.setTotalDuration(tempoParado);
                            spriteSheetBaseEsquerda.setTotalDuration(tempoParado);                    
                        }
                    }
                }
            }
            
            try {
                //Se não tiverse isso tava buga, não sei porque
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(AbejideAnimacao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String EsquerdaOuDireita(){
        if(AbejideThread.alma.x + (AbejideThread.alma.width / 2) > Olodumare.OlodumareThread.alma.x + (Olodumare.OlodumareThread.alma.width / 2)){
            return "Direita";
        } else{
            return "Esquerda";
        }
    }
    
    //Ataque fraco, mosta a animação certa, se é direita ou esquerda
    public void EsquerdaOuDireitaAtaqueFraco(){
        if(AbejideThread.alma.x + (AbejideThread.alma.width / 2) > Olodumare.OlodumareThread.alma.x + (Olodumare.OlodumareThread.alma.width / 2)){
            spriteSheetBaseDireita.hide();
            spriteSheetBaseEsquerda.hide();
            spriteSheetAtaqueFracoDireita.hide();
            spriteSheetAtaqueFracoEsquerda.unhide();
            spriteSheetVenenoDireita.hide();
            spriteSheetVenenoEsquerda.hide();
            spriteSheetRolandoDireita.hide();
            spriteSheetRolandoEsquerda.hide();
        } else{
            spriteSheetBaseDireita.hide();
            spriteSheetBaseEsquerda.hide();
            spriteSheetAtaqueFracoDireita.unhide();
            spriteSheetAtaqueFracoEsquerda.hide();
            spriteSheetVenenoDireita.hide();
            spriteSheetVenenoEsquerda.hide();
            spriteSheetRolandoDireita.hide();
            spriteSheetRolandoEsquerda.hide();
        }
    }
    
    public void EsquerdaOuDireitaAtaqueEspecial(){
        spriteSheetBaseDireita.hide();
        spriteSheetBaseEsquerda.hide();
        spriteSheetRolandoDireita.hide();
        spriteSheetRolandoEsquerda.hide();
        if(AbejideThread.alma.x + (AbejideThread.alma.width / 2) > Olodumare.OlodumareThread.alma.x + (Olodumare.OlodumareThread.alma.width / 2)){
            spriteSheetVenenoEsquerda.unhide();
        } else{
            spriteSheetVenenoDireita.unhide();
        }
    }
    
    private void ColocarCoisasNasBarras() {
    //Muda a tamanho das barras do HUD.
            //É tirado o pitoco que tem depois da barra no (<248 - x>, no inicio da conta), e o
            //do inicio no (< + x> | ta no final da conta)
            barraHP.width = (int) (((barraGrade.width - 14) * hp) / 100);
            barraMP.width = (int) (((barraGrade.width - 53) * mp) / 100);
  barraMPcarregando.width = (int) (((barraGrade.width - 53) * mpCarregando) / 100);
            barraSM.width = (int) (((barraGrade.width - 53) * sm) / 100);
    }
}

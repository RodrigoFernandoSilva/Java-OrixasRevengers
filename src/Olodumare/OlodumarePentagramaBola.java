/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Olodumare;

import Abejide.AbejideTeclado;
import static MetodosPrincipais.DeltaTime.deltaTime;
import static MetodosPrincipais.DeltaTime.dormir;
import static MetodosPrincipais.Executando.chao;
import static MetodosPrincipais.Executando.cameraSprite;
import static MetodosPrincipais.ControladoDoLevel.tempoIniciarPentagrama;
import static MetodosPrincipais.ControladoDoLevel.velocidadeBolaDefogo;

import static Olodumare.OlodumareThread.estaPentagramaBola;

import static Abejide.AbejideThread.alma;
import static MetodosPrincipais.Executando.pausado;
import static java.lang.Thread.sleep;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import jplay.Animation;
import jplay.Sprite;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class OlodumarePentagramaBola extends Thread{
    
    double velocidade = 300 + (velocidadeBolaDefogo * 100);
    int velocidadeAnimacao = 500;
    
    Animation pentagrama = new Animation("Imagens/Olodumare/Ataques/Pentagrama.png", 9);
    Sprite bolaDeFogo[] = new Sprite[100];
    double[]x = new double[bolaDeFogo.length];
    double[]y = new double[bolaDeFogo.length];
    boolean[] bateuNoChao = new boolean[bolaDeFogo.length];
    boolean[] estaViva = new boolean[bolaDeFogo.length];
    boolean[] jaDeuDanoNoAbejide = new boolean[bolaDeFogo.length];
    boolean[] jaExplodio = new boolean[bolaDeFogo.length];
    
    int i;
    int qualRotina;
    int[] tempoAtual = new int[bolaDeFogo.length];
    int[] tempoFinal = new int[bolaDeFogo.length];
    int[] tempoDanoAtual = new int[bolaDeFogo.length];
    int[] tempoDanoFinal = new int[bolaDeFogo.length];
    double xPentagrama;
    boolean estaVivaPrincipal = true;
    
    Random gerador = new Random();
    
    @Override
    public void run() {
        
        pentagrama.setSequenceTime(0, 8, true, 1000);
        xPentagrama = (alma.x) - ((pentagrama.width - alma.width) / 2);
        xPentagrama -= cameraSprite.x;
        
        pentagrama.y = chao.y + pentagrama.height + 52;
        
        qualRotina = gerador.nextInt(2) + 1;
        
        switch (qualRotina) {
            case 1:
                BolaDeFogo();
                break;
            case 2:
                Espinhos();
                break;
        }
        
        estaPentagramaBola = false;
    }
    
    private void BolaDeFogo() {
        int tempoInicialAtual = 0;
        int tempoInicialFinal = 250 - tempoIniciarPentagrama;
        
        while (tempoInicialAtual < tempoInicialFinal) {
            while (pausado) {
                try {
                    sleep(1);
                            } catch (InterruptedException ex) {
                    Logger.getLogger(OlodumarePentagramaBola.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumarePentagramaBola.class.getName()).log(Level.SEVERE, null, ex);
            }
            tempoInicialAtual ++;
        }
        
        //Inicializa todas as bolas de fogo, colocando elas em um lugar randomico dentro
        //do pentagrama e esplando elas em um grande espaço no eixo y, par ameio que fazer
        //uma chuma de bola de fogo
        for (i = 0; i < bolaDeFogo.length; i++) {
            bolaDeFogo[i] = new Sprite("Imagens/Olodumare/BolaDeFogo.png", 5);
            bolaDeFogo[i].setRotation((90 * 1.575) / 90);
            bolaDeFogo[i].setSequenceTime(0, 4, true, velocidadeAnimacao);
            x[i] = (xPentagrama) + (gerador.nextInt(pentagrama.width  - 60));
            y[i] = -bolaDeFogo[i].height - gerador.nextInt(2000);
            bateuNoChao[i] = false;
            estaViva[i] = true;
            jaDeuDanoNoAbejide[i] = false;
        }
        
        
        while (estaVivaPrincipal) {
            while (pausado) {
                try {
                    sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            BaterNoChao();
            
            for (i = 0; i < bolaDeFogo.length; i ++) {
                if (bolaDeFogo[i].getCurrFrame() >= (bolaDeFogo[i].getFinalFrame() - 1) && jaExplodio[i]) {
                    estaViva[i] = false;
                }
            }
            
            for (i = 0; i < bolaDeFogo.length; i ++) {
                if (estaViva[i]){
                    break;
                }
            }
            if (i == bolaDeFogo.length) {
                estaVivaPrincipal = false;
            }
            
            for (i = 0; i < bolaDeFogo.length; i ++) {
                if (jaDeuDanoNoAbejide[i] && !jaExplodio[i] && !bateuNoChao[i]) {
                    jaExplodio[i] = true;
                    bolaDeFogo[i] = new Sprite("Imagens/Olodumare/Explosao.png", 7);
                    bolaDeFogo[i].setSequenceTime(0, 6, false, 500);
                    x[i] = x[i] - (bolaDeFogo[i].width / 2);
                    x[i] += 15;
                    bolaDeFogo[i].y += alma.y - (bolaDeFogo[i].width / 2);
                }
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumarePentagramaBola.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void Espinhos() {
        
        int tempoInicialAtual = 0;
        int tempoInicialFinal = 300 - tempoIniciarPentagrama;
        
        while (tempoInicialAtual < tempoInicialFinal) {
            while (pausado) {
                try {
                    sleep(1);
                            } catch (InterruptedException ex) {
                    Logger.getLogger(OlodumarePentagramaBola.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumarePentagramaBola.class.getName()).log(Level.SEVERE, null, ex);
            }
            tempoInicialAtual ++;
        }
        
        //Inicializa todas as bolas de fogo, colocando elas em um lugar randomico dentro
        //do pentagrama e esplando elas em um grande espaço no eixo y, par ameio que fazer
        //uma chuma de bola de fogo
        for (i = 0; i < bolaDeFogo.length; i++) {
            bolaDeFogo[i] = new Sprite("Imagens/Olodumare/Espinho.png", 13);
            bolaDeFogo[i].setRotation(0);
            bolaDeFogo[i].setSequenceTime(0, 12, true, velocidadeAnimacao);
            x[i] = (xPentagrama) + (gerador.nextInt(pentagrama.width  - 12));
            y[i] = alma.getFloor() - 54;
            tempoAtual[i] = 0;
            tempoFinal[i] = gerador.nextInt(500);
            bateuNoChao[i] = false;
            estaViva[i] = true;
            jaDeuDanoNoAbejide[i] = false;
        }
        
        
        while (estaVivaPrincipal) {
            while (pausado) {
                try {
                    sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            for (i = 0; i < bolaDeFogo.length; i ++) {
                if (bolaDeFogo[i].getCurrFrame()>= bolaDeFogo[i].getFinalFrame() - 1) {
                    estaViva[i] = false;
                }
            }
            
            i = 0;
            for (i = 0; i < bolaDeFogo.length; i ++) {
                if (estaViva[i]){
                    break;
                }
            }
            if (i == bolaDeFogo.length) {
                estaVivaPrincipal = false;
            }
            
            //Temporizador para o espinho poder da dano novamente
            for (i = 0; i < bolaDeFogo.length; i ++) {
                if (jaDeuDanoNoAbejide[i]) {
                    if (tempoDanoAtual[i] > tempoDanoFinal[i]) {
                        jaDeuDanoNoAbejide[i] = false;
                    } else {
                        tempoDanoAtual[i] ++;
                    }
                }
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumarePentagramaBola.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void BaterNoChao() {
        for (int i = 0; i < bolaDeFogo.length; i ++) {
            if (!bateuNoChao[i] && bolaDeFogo[i].y > chao.y + 20) {
                bateuNoChao[i] = true;
                bolaDeFogo[i] = new Sprite("Imagens/Olodumare/BolaDeFogoExplosao.png", 8);
                bolaDeFogo[i].setSequenceTime(0, 7, true, velocidadeAnimacao + 200);
                y[i] = alma.getFloor() - bolaDeFogo[i].height;
                x[i] -= 20;
                bolaDeFogo[i].x = x[i] + cameraSprite.x;
                bolaDeFogo[i].y = y[i];
                
            }

            if (bateuNoChao[i] && bolaDeFogo[i].getCurrFrame() == (bolaDeFogo[i].getFinalFrame() - 1)) {
                estaViva[i] = false;
            }
        }
    }
    
    public void PrintarPentagrama() {
        pentagrama.x = xPentagrama + cameraSprite.x;
        
        pentagrama.draw();
        pentagrama.update();
    }
    
    public void PrintarBolaDeFogo() {
        if (qualRotina == 1) {
            for (int i0 = 0; i0 < bolaDeFogo.length; i0 ++) {
                if (estaViva[i0]) {
                    bolaDeFogo[i0].x = x[i0] + cameraSprite.x;
                    
                    if (jaDeuDanoNoAbejide[i0] && bolaDeFogo[i0].getCurrFrame() < bolaDeFogo[i0].getFinalFrame() - 1) {
                        bolaDeFogo[i0].draw();
                        if (!pausado)
                            bolaDeFogo[i0].update();
                    } else if (!bateuNoChao[i0] && !jaDeuDanoNoAbejide[i0]) {
                        bolaDeFogo[i0].y = y[i0];
                        bolaDeFogo[i0].draw();
                        if (!pausado) {
                            y[i0] += velocidade * deltaTime;
                            bolaDeFogo[i0].update();
                        }
                    } else {
                        bolaDeFogo[i0].draw();
                        if (!pausado)
                            bolaDeFogo[i0].update();
                    }
                }
            }
        } else if (qualRotina == 2) {
            for (int i0 = 0; i0 < bolaDeFogo.length; i0 ++) {
                if (estaViva[i0] && tempoAtual[i0] > tempoFinal[i0]) {
                    bolaDeFogo[i0].x = x[i0] + cameraSprite.x;
                    bolaDeFogo[i0].y = y[i0];
                    
                    bolaDeFogo[i0].draw();
                    if (!pausado)
                        bolaDeFogo[i0].update();
                } else if (!pausado) {
                    tempoAtual[i0]++;
                }
            }
        }
    }
    
    public Sprite PegarSprite(int qual){
        return bolaDeFogo[qual];
    }
    
    public boolean PegarEstaViva(int qual){
        return estaViva[qual];
    }
    
    public int PegarTamanhoDoVetor() {
        return bolaDeFogo.length;
    }
    
    public boolean PegarJaDeuDanoNoAbejide(int qual) {
        return jaDeuDanoNoAbejide[qual];
    }
    
    public void JaDeuDanoNoAbejide(int qual){
        jaDeuDanoNoAbejide[qual] = true;
        tempoDanoAtual[qual] = 0;
        tempoDanoFinal[qual] = 20;
        jaExplodio[qual] = false;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Olodumare;

import Abejide.AbejideTeclado;
import Abejide.AbejideThread;

import static Olodumare.OlodumareThread.qualThreadAtaque;
import static Olodumare.OlodumareThread.estaPedraGigante;

import static MetodosPrincipais.DeltaTime.deltaTime;
import static MetodosPrincipais.DeltaTime.dormir;
import static MetodosPrincipais.Executando.cameraSprite;
import static MetodosPrincipais.ControladoDoLevel.distanciaPedraGigante;
import static MetodosPrincipais.ControladoDoLevel.pedraGiganteVelocidade;
import static MetodosPrincipais.Executando.pausado;
import static java.lang.Thread.sleep;

import java.util.logging.Level;
import java.util.logging.Logger;

import jplay.Animation;
import jplay.GameImage;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class OlodumarePedraGigante extends Thread{
    
    Animation pedraGigante = new Animation("Imagens/Olodumare/Ataques/PedraGigante.png", 30);
    GameImage pedraGiganteCaixaDeColisao = new GameImage("Imagens/Olodumare/alma.png");
    
    int velocidade = 60;
    double x;
    boolean jaSubio = false;
    boolean podeMatar = false;
    boolean jaDeuDanoNoAbejide = false;
    boolean jaPodeDarDano = false;
    int tempoMatarAtual = 0;
    int tempoMatarFinal = 200 + distanciaPedraGigante;
    int tempoDarDanoAtual = 0;
    int tempoDarDanoFinal = 20;
    int direcao = 0;
    
    public OlodumarePedraGigante(double pX, int dir) {
        x = pX - pedraGigante.width - 10;
        if (dir == -1) {
            x += pedraGigante.width + 65;
        }
        pedraGigante.y = AbejideThread.alma.getFloor() - pedraGigante.height;
        pedraGigante.y += 12;
        pedraGigante.setSequenceTime(0, 19, false, 1500);
        pedraGiganteCaixaDeColisao.y = pedraGigante.y + pedraGigante.height;
        pedraGiganteCaixaDeColisao.y -= pedraGiganteCaixaDeColisao.height;
        direcao = dir;
    }
    
    @Override
    public void run() {
        
        while (true) {
            
            while (pausado) {
                try {
                    sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (pedraGigante.getCurrFrame() > 12) {
                x -= ((velocidade + pedraGiganteVelocidade) * deltaTime) * direcao;
            }

            
            if (!jaSubio && pedraGigante.getCurrFrame() >= (pedraGigante.getFinalFrame() - 1)) {
                jaSubio = true;
                pedraGigante = new Animation("Imagens/Olodumare/Ataques/PedraGigante.png", 30);
                pedraGigante.setSequenceTime(18, 20, true, 300);
                pedraGigante.y = AbejideThread.alma.getFloor() - pedraGigante.height;
                pedraGigante.y += 12;
            }
            
            if (jaSubio && !podeMatar) {
                if (tempoMatarAtual > tempoMatarFinal) {
                    pedraGigante = new Animation("Imagens/Olodumare/Ataques/PedraGigante.png", 30);
                    pedraGigante.setSequenceTime(21, 29, true, 1000);
                    pedraGigante.y = AbejideThread.alma.getFloor() - pedraGigante.height;
                    pedraGigante.y += 12;
                    podeMatar = true;
                } else {
                    tempoMatarAtual ++;
                }
            }
            
            if (podeMatar && pedraGigante.getCurrFrame() >= (pedraGigante.getFinalFrame() - 1)) {
                break;
            }
            
            AjustarTamanhocCaixaDeColisao();
                    
            if (jaDeuDanoNoAbejide) {
                if (tempoDarDanoAtual > tempoDarDanoFinal) {
                    jaDeuDanoNoAbejide = false;
                } else {
                    tempoDarDanoAtual ++;
                }
            }
            
            
            if (!jaPodeDarDano && pedraGigante.getCurrFrame() >= 13) {
                jaPodeDarDano = true;
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumarePedraGigante.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        qualThreadAtaque = 0;
        estaPedraGigante = false;
    }
    
    public void PrintarPedraGigante() {
        pedraGigante.x = x + cameraSprite.x;
        pedraGiganteCaixaDeColisao.x = pedraGigante.x;
        pedraGiganteCaixaDeColisao.x += pedraGigante.width / 2;
        pedraGiganteCaixaDeColisao.x -= pedraGiganteCaixaDeColisao.width / 2;
        //pedraGiganteCaixaDeColisao.draw();
        pedraGigante.draw();
        if (!pausado)
            pedraGigante.update();
        }
    
    private void AjustarTamanhocCaixaDeColisao() {
        if (pedraGigante.getCurrFrame() >= 28) {
            pedraGiganteCaixaDeColisao.width = 32;
        } else if (pedraGigante.getCurrFrame() >= 27) {
            pedraGiganteCaixaDeColisao.width = 64;
        } else if (pedraGigante.getCurrFrame() >= 26) {
            pedraGiganteCaixaDeColisao.width = 128;
        } else if (pedraGigante.getCurrFrame() >= 25) {
            pedraGiganteCaixaDeColisao.width = 192;
        } else if (pedraGigante.getCurrFrame() >= 24) {
            pedraGiganteCaixaDeColisao.width = 224;
        } else if (pedraGigante.getCurrFrame() >= 16) {
            pedraGiganteCaixaDeColisao.width = 256;
        } else if (pedraGigante.getCurrFrame() >= 15) {
            pedraGiganteCaixaDeColisao.width = 224;
        } else if (pedraGigante.getCurrFrame() >= 14) {
            pedraGiganteCaixaDeColisao.width = 192;
        }
    }
    
    public GameImage PegarPedraGiganteCaixaDeColisao() {
        return pedraGiganteCaixaDeColisao;
    }
    
    public boolean PegarJaDeuDanoNoAbejide() {
        return jaDeuDanoNoAbejide;
    }
    
    public boolean PegarJaPodeDarDano() {
        return jaPodeDarDano;
    }
    
    public void JaDeuDanoNoAbejide() {
        jaDeuDanoNoAbejide = true;
        tempoDarDanoAtual = 0;
    }
    
}

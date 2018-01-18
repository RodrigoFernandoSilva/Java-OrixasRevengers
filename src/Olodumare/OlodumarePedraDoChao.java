/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Olodumare;

import Abejide.AbejideThread;
import static Olodumare.OlodumareThread.estaPedraDoChao;
import static Olodumare.OlodumareThread.qualThreadAtaque;

import static MetodosPrincipais.Executando.cameraSprite;
import static MetodosPrincipais.DeltaTime.dormir;
import static MetodosPrincipais.ControladoDoLevel.numeroDePedras;
import static MetodosPrincipais.ControladoDoLevel.pedrasDoChaoTempo;
import static MetodosPrincipais.ControladoDoLevel.pedrasDoChaoTempoCriarNova;

import java.util.logging.Level;
import java.util.logging.Logger;

import jplay.Animation;
import jplay.GameImage;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class OlodumarePedraDoChao extends Thread{
    
    Animation pedra[] = new Animation[7];
    GameImage[] pedraCaixaDeColisao = new GameImage[pedra.length];
    int i;
    int direcao = 0;
    int pedraLength = numeroDePedras;
    int[] tempoAtual = new int[pedra.length];
    int[] tempoFinal = new int[pedra.length];
    int[] tamanhoCaixaDeColisao = new int[12];
    double[] x = new double[pedra.length];
    boolean[] pedraVivas = new boolean[pedra.length];
    boolean[] pedraJaCriada = new boolean[pedra.length];
    boolean[] jaDeuDanoNoAbejide = new boolean[pedra.length];
    
    public OlodumarePedraDoChao (double pX, int dir) {
        for (i = 0; i < x.length; i ++) {
            x[i] = pX - cameraSprite.x;
        }
        direcao = dir;
    }
    
    @Override
    public void run () {
        
        tamanhoCaixaDeColisao[0] =  32;
        tamanhoCaixaDeColisao[1] =  56;
        tamanhoCaixaDeColisao[2] =  80;
        tamanhoCaixaDeColisao[3] =  80;
        tamanhoCaixaDeColisao[4] =  80;
        tamanhoCaixaDeColisao[5] =  80;
        tamanhoCaixaDeColisao[6] =  80;
        tamanhoCaixaDeColisao[7] =  80;
        tamanhoCaixaDeColisao[8] =  80;
        tamanhoCaixaDeColisao[9] =  80;
        tamanhoCaixaDeColisao[10] = 56;
        tamanhoCaixaDeColisao[11] = 32;
        
        for (i = 0; i < pedraLength; i ++) {
            pedra[i] = new Animation("Imagens/Olodumare/Ataques/Pedra.png", 13);
            pedra[i].setSequenceTime(0, 12, false, 1500- pedrasDoChaoTempo);
            pedra[i].y = (AbejideThread.alma.getFloor() - pedra[i].height) + 15;
            pedraCaixaDeColisao[i] = new GameImage("Imagens/Olodumare/alma.png");
            pedraCaixaDeColisao[i].height = pedra[i].height;
            pedraCaixaDeColisao[i].y = pedra[i].y + pedra[i].height;
            pedraCaixaDeColisao[i].y -= pedraCaixaDeColisao[i].height;
            pedraVivas[i] = true;
            pedraJaCriada[i] = false;
            jaDeuDanoNoAbejide[i] = false;
            tempoAtual[i] = 0;
            tempoFinal[i] = (i * pedrasDoChaoTempoCriarNova);
            x[i] -= ((90 * i) + pedra[i].width) * direcao;
            if (direcao == -1) {
                x[i] -= 20;
            }
        }
        
        while (true) {
            
            for (i = 0; i < pedraLength; i ++) {
                if (pedra[i].getCurrFrame() >= (pedra[i].getFinalFrame() - 1) && pedraVivas[i]) {
                    pedraVivas[i] = false;
                }
            }
            
            i = 0;
            for (boolean b: pedraVivas) {
                if (!b) {
                    i++;
                }
            }
            if (i >= pedra.length) {
                break;
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumarePedraDoChao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        estaPedraDoChao = false;
        qualThreadAtaque = 0;
    }
    
    public void PrintarPedra() {
        for (int i0 = 0; i0 < pedraLength; i0 ++) {
            if (tempoAtual[i0] > tempoFinal[i0] && pedraVivas[i0]) {
                pedraJaCriada[i0] = true;
                pedra[i0].x = x[i0] + cameraSprite.x;
                
                pedraCaixaDeColisao[i0].width = tamanhoCaixaDeColisao[pedra[i0].getCurrFrame()];
                pedraCaixaDeColisao[i0].x = pedra[i0].x;
                pedraCaixaDeColisao[i0].x += pedra[i0].width / 2;
                pedraCaixaDeColisao[i0].x -= pedraCaixaDeColisao[i0].width / 2;
                
                
                pedra[i0].draw();
                pedra[i0].update();
                //pedraCaixaDeColisao[i0].draw();

            } else {
                tempoAtual[i0] ++;
            }
        }
    }
    
    public int PegarTamanhoDoVetor() {
        return pedraLength;
    }
    
    public boolean PegarJaDeuDanoNoAbejide(int qual) {
        return jaDeuDanoNoAbejide[qual];
    }
    
    public boolean PegarPedraJaCriada(int qual) {
        return pedraJaCriada[qual];
    }
    
    public GameImage PegarCaixaDeColisao(int qual) {
        return pedraCaixaDeColisao[qual];
    }
    
    public void JaDeuDanoNoAbejide(int qual) {
        jaDeuDanoNoAbejide[qual] = true;
    }
}

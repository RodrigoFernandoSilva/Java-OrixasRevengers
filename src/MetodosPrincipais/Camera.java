/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MetodosPrincipais;

import static Abejide.AbejideAnimacao.direcaoRodou;
import static Abejide.AbejideAnimacao.jaRodou;
import Abejide.AbejideTeclado;
import static MetodosPrincipais.DeltaTime.deltaTime;

import static Abejide.AbejideTeclado.boTeclaEsquerda;
import static Abejide.AbejideTeclado.boTeclaDireita;
import static Abejide.AbejideTeclado.boEstaAtacando;
import static Abejide.ContadorPosAtaque.PodeAndar;
import static Abejide.AbejideThread.alma;
import static Abejide.AbejideThread.poeira;
import static Abejide.AbejideThread.poeiraX;
import static Abejide.AbejideThread.velocidaeRodouPassar;

import static MetodosPrincipais.DeltaTime.dormir;
import static MetodosPrincipais.Main.lua;
import static MetodosPrincipais.Main.ceu;
import static MetodosPrincipais.Main.montanhas;
import static MetodosPrincipais.Main.nuvem1;
import static MetodosPrincipais.Main.nuvem2;
import static MetodosPrincipais.Main.sol;
import static MetodosPrincipais.Executando.cameraSprite;
import static MetodosPrincipais.Executando.DentroDaCamera;
import static MetodosPrincipais.Executando.chao;
import static MetodosPrincipais.Executando.planoFundo0;
import static MetodosPrincipais.Executando.planoFundo1;
import static MetodosPrincipais.Executando.planoFundo2;
import static MetodosPrincipais.Executando.planoFundo3;
import static MetodosPrincipais.Main.janela;

import static MetodosPrincipais.PlanoDeFundo.luaX;
import static MetodosPrincipais.PlanoDeFundo.solX;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

import jplay.Collision;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class Camera extends Thread{
    
    public static float esquerdaDistancia;
    public static float direitaDistancia;
    public static double velocidade = 35.0f;
    public static double velocidadeCorendo = 0.0f;
    
    public void run() {
        int i;
        
        while(Executando.executando){
            
            //Se a câmera se move demais pode acabar saindo fora do cenário, isses ifs ajusta ela
            //caso isso venha a acontecer
            if (cameraSprite.x < -chao.width + cameraSprite.width) {
                cameraSprite.x = -chao.width + cameraSprite.width;
            } else if (cameraSprite.x > 0) {
                cameraSprite.x = 0;
            }
            
            if(alma.x > Olodumare.OlodumareThread.alma.x){
                esquerdaDistancia = 0.50f;
                direitaDistancia = 0.8f;
            } else{
                esquerdaDistancia = 0.12f;
                direitaDistancia = 0.50f;
            }
            
            if(PodeAndar){
                MoverCameraEmRelacaoAbejide();
            }
            
            MoveCameraEmRelacaoOlodumare();
            
            //Coloca tudo na tela usando como referencia a câmera do jogo
            chao.x = cameraSprite.x;
            planoFundo0.x = cameraSprite.x * 0.94;
            planoFundo1.x = cameraSprite.x * 0.86;
            planoFundo2.x = cameraSprite.x * 0.78;
            planoFundo3.x = cameraSprite.x * 0.7;
            montanhas.x = cameraSprite.x * 0.6;
            nuvem2.x = cameraSprite.x * 0.5;
            nuvem1.x = cameraSprite.x * 0.4;
            ceu.x = cameraSprite.x * 0.3;
            
            sol.x = (cameraSprite.x + solX);
            lua.x = (cameraSprite.x + luaX);
            Olodumare.OlodumareThread.alma.x = cameraSprite.x + Olodumare.OlodumareThread.x;
            
            poeira.x = (cameraSprite.x + poeiraX);
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void MoverCameraEmRelacaoAbejide(){
        //Sistema de movimentação da câmera.
        //Só move ela se o persongaem não estiver no final do cenário e se ele não
        // estiver atacando
        if(!boTeclaDireita && boTeclaEsquerda && cameraSprite.x < 0 && boEstaAtacando == false){
            if(alma.x < janela.getWidth() * esquerdaDistancia){
                if (!jaRodou){
                    cameraSprite.x += ((velocidade + velocidadeCorendo) * deltaTime);
                } else if (jaRodou) {
                    cameraSprite.x -= (velocidaeRodouPassar);
                }
            }
        }
        if(!boTeclaEsquerda && boTeclaDireita && cameraSprite.x > -(chao.width - janela.getWidth()) && boEstaAtacando == false){
            if(alma.x > janela.getWidth() * direitaDistancia){
                if (!jaRodou){
                    cameraSprite.x -= ((velocidade + velocidadeCorendo) * deltaTime);
                } else if (jaRodou) {
                    cameraSprite.x -= (velocidaeRodouPassar);
                }
            }
        }

        //Se o personagem estiver pulando e atacando, esses if faz ele se mover no ar caso
        // o mesmo esteja pressionando a tecla de movimento
        if(boEstaAtacando == true && alma.y < (alma.getFloor() - alma.height)){
            if(boTeclaDireita){
                alma.x -= ((velocidade + velocidadeCorendo) * deltaTime);
            }
            if(boTeclaEsquerda){
                alma.x += ((velocidade + velocidadeCorendo) * deltaTime);
            }
        }
    }
    
    public void MoveCameraEmRelacaoOlodumare(){
        //Ajusta a câmera se o Abejide estiver na frente do Olodumare, coloca Olodumare na câmera
        //se ele começar a entrar dentro do emquadramento, o mesmo vale para o atras
        if(!boTeclaDireita && Olodumare.OlodumareThread.alma.x > janela.getWidth() * 0.8 &&
          alma.x > janela.getWidth() * esquerdaDistancia &&
          cameraSprite.x > -(chao.width - janela.getWidth()) &&
          Collision.collided(DentroDaCamera, Olodumare.OlodumareThread.alma)){

            alma.x -= ((Olodumare.OlodumareThread.VELOCIDADE) * deltaTime);
            cameraSprite.x -= ((Olodumare.OlodumareThread.VELOCIDADE + velocidadeCorendo) * deltaTime);
            if(boTeclaDireita){
                alma.x -= ((velocidade + velocidadeCorendo) * deltaTime);
            }
        }

        //Ajusta a câmera se o Abejide esvier atras do Olodumare
        if(!boTeclaEsquerda && Olodumare.OlodumareThread.alma.x < janela.getWidth() * 0.15 &&
          alma.x < janela.getWidth() * direitaDistancia &&
          cameraSprite.x < 0 &&
          Collision.collided(DentroDaCamera, Olodumare.OlodumareThread.alma)){

            alma.x += ((Olodumare.OlodumareThread.VELOCIDADE) * deltaTime);
            cameraSprite.x += ((Olodumare.OlodumareThread.VELOCIDADE + velocidadeCorendo) * deltaTime);
            if(boTeclaEsquerda){
                alma.x += ((velocidade + velocidadeCorendo) * deltaTime);
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Abejide;

import static MetodosPrincipais.Colisao.olodumareBolaDeFogoDano;

import static Abejide.AbejideThread.alma;

import static MetodosPrincipais.DeltaTime.deltaTime;
import static MetodosPrincipais.Executando.cameraSprite;
import static MetodosPrincipais.Colisao.contadorDeHits;
import static MetodosPrincipais.Colisao.acressimoDeDanoHits;
import static MetodosPrincipais.Colisao.tempoZerarContador;
import static MetodosPrincipais.Executando.pausado;

import jplay.Animation;

/**
 *
 * @author Rodrigo Fernando da silva
 */
public class BolaDeFogo extends Thread{
    
    public Animation bolaDeFogo = new Animation("Imagens/Abejide/Projetil.png", 9, true);
    
    int velocidade = 600;
    double x;
    double y;
    boolean esquerda;
    boolean estaViva = false;
    boolean explosao;
    boolean especial;
    
    public BolaDeFogo(double bx, double by, boolean dir, boolean esp){
        x = bx;
        y = by - 18;
        esquerda = dir;
        especial = esp;
    }
    
    public void run(){
        explosao = false;

        if(!esquerda){
            bolaDeFogo.setSequence(0, 4);
            x += 50 + (-cameraSprite.x);
            bolaDeFogo.x = x;
            bolaDeFogo.y = y + 30;
        } else{
            bolaDeFogo.setSequence(4, 8);
            x -= 50 + cameraSprite.x;
            bolaDeFogo.x = x;
            bolaDeFogo.y = y + 30;
        }
        
        bolaDeFogo.setTotalDuration(500);
        bolaDeFogo.unhide();
    }
    
    public boolean EstaViva(){
        return estaViva;
    }
    
    public void PrintarAnimacao() throws InterruptedException{
        if(estaViva){
            if(!explosao && !pausado){
                if(!esquerda){
                    x += (velocidade * deltaTime);
                } else{
                    x -= (velocidade * deltaTime);
                }
            }
            bolaDeFogo.x = x + cameraSprite.x;
            
            //Se a bola de fogo colidir com o olodumare, é soltado uma explosão e olodumare
            //perde vida
            if(!explosao && bolaDeFogo.collided(Olodumare.OlodumareThread.alma)){
                bolaDeFogo = new Animation("Imagens/Abejide/Particulas/Explosao.png", 7);
                bolaDeFogo.setSequenceTime(0, 6, true, 600);
                bolaDeFogo.y = y - (bolaDeFogo.height / 4);
                if(!esquerda){
                    x = x - (bolaDeFogo.width / 4);
                } else{
                    x = x - (bolaDeFogo.width / 2);
                }
                explosao = true;
                if (especial) {
                    Olodumare.OlodumareThread.hpTemp -= (olodumareBolaDeFogoDano + acressimoDeDanoHits) / 2;
                } else {
                    Olodumare.OlodumareThread.hpTemp -= (olodumareBolaDeFogoDano + acressimoDeDanoHits);
                }
                contadorDeHits ++;
                tempoZerarContador = 0;
            }
            
            //Caso a animação de explosão esteja rodando e seu frame chegue no final, essa
            //thread da bola de fogo morre, tem vairas thread de bola de fogo
            if(explosao && bolaDeFogo.getCurrFrame() == bolaDeFogo.getFinalFrame() - 1){
                estaViva = false;
            }
            
            bolaDeFogo.x = x + cameraSprite.x;
            bolaDeFogo.draw();
            if (!pausado)
                bolaDeFogo.update();
            
            if (!bolaDeFogo.collided(MetodosPrincipais.Executando.DentroDaCamera)) {
                if(!esquerda && alma.x > Olodumare.OlodumareThread.alma.x){
                    estaViva = false;
                } else if(esquerda && alma.x < Olodumare.OlodumareThread.alma.x){
                    estaViva = false;
                }
            }
        }
    }
    
    public void DeixarViva(){
        estaViva = true;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Abejide;

import static Abejide.AbejideThread.alma;
import static MetodosPrincipais.Colisao.acressimoDeDanoHits;
import static MetodosPrincipais.Colisao.contadorDeHits;
import static MetodosPrincipais.Colisao.olodumareBolaDeFogoDano;
import static MetodosPrincipais.Colisao.tempoZerarContador;
import static MetodosPrincipais.DeltaTime.deltaTime;
import static MetodosPrincipais.Executando.cameraSprite;
import static MetodosPrincipais.Executando.pausado;
import jplay.Animation;

/**
 *
 * @author Rodrigo Ferndnao da Silva
 */
public class FaixaDeErnegia extends Thread{
   
    public Animation faixaDeErnegiaDireita;
    
    int velocidade = 600;
    int tempoDanoAtual = 3;
    int tempoDanoFinal = 3;
    double x;
    double y;
    boolean esquerda;
    boolean estaViva = false;
    boolean especial;
    
    public FaixaDeErnegia(double bx, double by, boolean dir, boolean esp){
        x = bx;
        y = by - 42;
        esquerda = dir;
        especial = esp;
        
        if (dir) {
            faixaDeErnegiaDireita = new Animation("Imagens/Abejide/Particulas/Especial02_Direita.png");
        } else {
            faixaDeErnegiaDireita = new Animation("Imagens/Abejide/Particulas/Especial02_Esquerda.png");
        }
    }
    
    public void run(){

        if(!esquerda){
            x += 50 + (-cameraSprite.x);
            faixaDeErnegiaDireita.x = x;
            faixaDeErnegiaDireita.y = y + 30;
        } else{
            x -= 50 + cameraSprite.x;
            faixaDeErnegiaDireita.x = x;
            faixaDeErnegiaDireita.y = y + 30;
        }
        
        faixaDeErnegiaDireita.setTotalDuration(500);
        faixaDeErnegiaDireita.unhide();
    }
    
    public boolean EstaViva(){
        return estaViva;
    }
    
    public void PrintarAnimacao() throws InterruptedException{
        if(estaViva){
            if(!pausado){
                if(!esquerda){
                    x += (velocidade * deltaTime);
                } else{
                    x -= (velocidade * deltaTime);
                }
            }
            if (tempoDanoAtual < tempoDanoFinal) {
                tempoDanoAtual ++;
            }
            //Se a bola de fogo colidir com o olodumare, é soltado uma explosão e olodumare
            //perde vida
            if(faixaDeErnegiaDireita.collided(Olodumare.OlodumareThread.alma) && tempoDanoAtual >= tempoDanoFinal){
                Olodumare.OlodumareThread.hpTemp -= (olodumareBolaDeFogoDano + acressimoDeDanoHits) / 2;
                
                contadorDeHits ++;
                tempoZerarContador = 0;
                tempoDanoAtual = 0;
            }
            
            faixaDeErnegiaDireita.x = x + cameraSprite.x;
            faixaDeErnegiaDireita.draw();
            
            if (!faixaDeErnegiaDireita.collided(MetodosPrincipais.Executando.DentroDaCamera)) {
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

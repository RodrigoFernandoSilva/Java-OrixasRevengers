/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Olodumare;

import Abejide.AbejideTeclado;
import static Abejide.AbejideThread.alma;

import static MetodosPrincipais.DeltaTime.deltaTime;
import static MetodosPrincipais.Main.janela;
import static MetodosPrincipais.Executando.DentroDaCamera;
import static MetodosPrincipais.Executando.chao;
import static MetodosPrincipais.Executando.cameraSprite;
import static MetodosPrincipais.DeltaTime.dormir;
import static MetodosPrincipais.ControladoDoLevel.velocidadeBolaDefogo;
import static MetodosPrincipais.Executando.executando;
import static MetodosPrincipais.Executando.pausado;
import static java.lang.Thread.sleep;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import jplay.Sprite;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class OlodumareBolaDeFogo extends Thread{
    
    double velocidade = 1.5 + velocidadeBolaDefogo;
    double velocidadeRotacionar = velocidade * 0.6;
    final int velocidadeAnimacao = 500;
    
    boolean estaViva = false;
    boolean bateuNoChao = false;
    boolean abejideNaEsquerda = false;
    boolean bateuNoAbejide = false;
    
    double x, y;
    double angulo;
    int i;
    int tempoComecar;
    int qualRotina;
    double precisao;
    //1: Faz uma parabola
    //2: Cai de cima para baixo
    //3: Vem das laterais
    
    public Sprite bolaDeFogo = new Sprite("Imagens/Olodumare/BolaDeFogo.png", 5);
    
    OlodumareBolaDeFogo(double px, double py, int qR, int tempoC, boolean aNaEsq, int ifor, int pre) {
        x = px - cameraSprite.x;
        y = py;
        i = ifor;
        tempoComecar = tempoC;
        qualRotina = qR;
        abejideNaEsquerda = aNaEsq;
        precisao = pre;
        bolaDeFogo.setSequenceTime(0, 4, true, velocidadeAnimacao);
    }
    
    Random gerador = new Random();
    
    @Override
    public void run() {
        
        try {
            sleep(tempoComecar);
        } catch (InterruptedException ex) {
            Logger.getLogger(OlodumareBolaDeFogo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        estaViva = true;
        precisao = (gerador.nextDouble() * (precisao * 2)) - precisao;
        
        switch(qualRotina){
            case 1:
                Rotina1();
                break;
            case 2:
                Rotina2();
                break;
            case 3:
                Rotina3();
                break;
            case 4:
                if (gerador.nextBoolean()) {
                    Rotina2();
                } else {
                    Rotina3();
                }
                break;
            case 5:
                i = gerador.nextInt(3);
                switch (i) {
                    case 0:
                        Rotina1();
                        break;
                    case 1:
                        Rotina2();
                        break;
                    default:
                        Rotina3();
                        break;
                }
                break;
        }
        
        if (bateuNoAbejide) {
            Explosao();
        }
    }
    
    private void Rotina1() {
        double[] xy;
        boolean vaiColidir = false;
        angulo = 270;
        while(estaViva){
            EspaPausado();
            
            if (!executando){
                estaViva = false;
            }
            
            if (!bateuNoChao) {
                xy = MoveSpriteRodando(angulo, velocidade);
                x += xy[0] * deltaTime;
                y += xy[1] * deltaTime;
                
                if (!vaiColidir){
                    if(abejideNaEsquerda) {
                        angulo -= velocidadeRotacionar;
                    } else {
                        angulo += velocidadeRotacionar;
                    }
                    vaiColidir = EstaAlinhado(bolaDeFogo.x, bolaDeFogo.y, alma.x + precisao, alma.y + (alma.height / 2));

                    if (angulo > 360) {
                        angulo -= 360;
                    } else if (angulo < 0) {
                        angulo += 360;
                    }
                }
            }
            
            BaterNoChao();
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumareBolaDeFogo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void Rotina2() {
        angulo = 270;
        
        while (true) {
            EspaPausado();
            
            y -= 90 * velocidade * deltaTime;
            
            if (bolaDeFogo.y <= -bolaDeFogo.height){
                break;
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumareBolaDeFogo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        y = -bolaDeFogo.height;
        x = (alma.x + (alma.width / 2));
        x -= (bolaDeFogo.width / 2) - precisao;
        x -= cameraSprite.x;
        
        angulo = 90;
        while(estaViva){
            EspaPausado();
            
            if (!bateuNoChao ) {
                y += 90 * velocidade * deltaTime;
            }
            
            BaterNoChao();
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumareBolaDeFogo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void Rotina3() {
        angulo = 270;
        
        while (true) {
            EspaPausado();
            
            y -= 90 * velocidade * deltaTime;
            
            if (bolaDeFogo.y <= -bolaDeFogo.height){
                break;
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumareBolaDeFogo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        y = alma.getFloor() - (alma.height / 2) - (bolaDeFogo.height / 2);
        y += 30;
        int qualLado = gerador.nextInt(2);
        if (qualLado == 0) {
            x = ((alma.x + 75) - cameraSprite.x) + cameraSprite.x;
            angulo = 0;
            x -= 200 * (i + 1);
        } else if (qualLado == 1) {
            x = (((alma.x + 75) - cameraSprite.x) + cameraSprite.x) + janela.getWidth();
            x += 200 * (i + 1);
            angulo = 180;
        }
       
        
        while(estaViva){
            EspaPausado();
            
            if (qualLado == 0) {
                x += 70 * velocidade * deltaTime;
                
                if (x > ((DentroDaCamera.x - cameraSprite.x) + janela.getWidth()+ bolaDeFogo.width)) {
                    estaViva = false;
                }
                
            } else if (qualLado == 1) {
                x -= 70 * velocidade * deltaTime;
                
                if (x + bolaDeFogo.width < (DentroDaCamera.x - cameraSprite.x)) {
                    estaViva = false;
                }
                
            }
            
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumareBolaDeFogo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void EspaPausado() {
        while (pausado) {
            try {
                sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void BaterNoChao() {
        if (!bateuNoChao && bolaDeFogo.y > chao.y + 20) {
            angulo = 0;
            bolaDeFogo = new Sprite("Imagens/Olodumare/BolaDeFogoExplosao.png", 8);
            bolaDeFogo.setSequenceTime(0, 7, true, velocidadeAnimacao + 200);
            y = alma.getFloor() - bolaDeFogo.height;
            x -= 20;
            bateuNoChao = true;
        }

        if (bateuNoChao && bolaDeFogo.getCurrFrame() == (bolaDeFogo.getFinalFrame() - 1)) {
            estaViva = false;
        }
    }
    
    private boolean EstaAlinhado(double meuX, double meuY, double objX, double objY){
        boolean vaiColidir = false;
        if(angulo <= 180){
            double forcaX, forcaY;
            double angPraColidir;
            double distanciaX, distanciaY;
            double diferenca;

            distanciaX = meuX - objX;
            distanciaY = (meuY - objY) * -1;

            if(distanciaX >= 0){
                diferenca = distanciaX + distanciaY;
            } else{
                distanciaX *= -1;
                diferenca = distanciaY + distanciaX;
            }

            forcaX = distanciaX / diferenca;
            forcaY = distanciaY / diferenca;
            
            if((meuX - objX) < 0){
                forcaX *= -1;
            }

            angPraColidir = (forcaX * 90) + 90;
            
            if(angulo > angPraColidir && !abejideNaEsquerda){
                vaiColidir = true;
                angulo = angPraColidir;
            } else if(angulo < angPraColidir && abejideNaEsquerda){
                vaiColidir = true;
                angulo = angPraColidir;
            }
        }
        
        return vaiColidir;
    }
    
    private static double[] MoveSpriteRodando(double angulo, double vel){
        double[] posicao = new double[2]; //0: x | 1:y
        double faltaPraBaixo, faltaPraCima;
        
        if(angulo >= 0 && angulo < 90){
                faltaPraBaixo = angulo - 90;
                faltaPraCima = angulo;

                faltaPraBaixo *= vel;
                faltaPraCima *= vel;
                
                posicao[0] -= faltaPraBaixo;
                posicao[1] += faltaPraCima;
            } else if(angulo >= 90 && angulo < 180){
                faltaPraBaixo = angulo - 180;
                faltaPraCima = angulo - 90;

                faltaPraBaixo *= vel;
                faltaPraCima *= vel;
                
                posicao[0] -= faltaPraCima;
                posicao[1] -= faltaPraBaixo;
            } else if(angulo >= 180 && angulo < 270){
                faltaPraBaixo = angulo - 270;
                faltaPraCima = angulo - 180;

                faltaPraBaixo *= vel;
                faltaPraCima *= vel;
                
                posicao[0] += faltaPraBaixo;
                posicao[1] -= faltaPraCima;
            } else if(angulo >= 270){
                faltaPraBaixo = angulo - 360;
                faltaPraCima = angulo - 270;

                faltaPraBaixo *= vel;
                faltaPraCima *= vel;
                
                posicao[0] += faltaPraCima;
                posicao[1] += faltaPraBaixo;
            }
        return posicao;
    }
    
    public Sprite PegarSprie() {
        return bolaDeFogo;
    }
    
    public boolean PegarEstaViva() {
        return estaViva;
    }
    
    public boolean PegarBateuNoChao() {
        return bateuNoChao;
    }
    
    public void PrintarSprite() {
        if (estaViva) {
            bolaDeFogo.setRotation((angulo * 1.575) / 90);
            bolaDeFogo.x = x + cameraSprite.x;
            bolaDeFogo.y = y;
            
            bolaDeFogo.draw();
            if (!pausado)
                bolaDeFogo.update();
        } else if (bateuNoAbejide) {
            if (!pausado)
                bolaDeFogo.draw();
            bolaDeFogo.update();
        }
    }
    
    private void Explosao() {
        bolaDeFogo = new Sprite("Imagens/Olodumare/Explosao.png", 7);
        bolaDeFogo.setSequenceTime(0, 6, false, velocidadeAnimacao);
        
        if ((x + cameraSprite.x) > alma.x) {
            x = x - (bolaDeFogo.width / 2);
        } else {
            x = x - (bolaDeFogo.width / 4);
        }
        y = y - (bolaDeFogo.width / 4);
        
        while (true) {
            
            bolaDeFogo.x = x + cameraSprite.x;
            bolaDeFogo.y = y;
            
            if (!bolaDeFogo.isPlaying()) {
                estaViva = false;
                break;
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumareBolaDeFogo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void MatarThread() {
        estaViva = false;
        bateuNoAbejide = true;
    }
}

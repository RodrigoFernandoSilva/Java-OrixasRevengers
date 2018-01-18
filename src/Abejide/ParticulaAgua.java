/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Abejide;

import static Abejide.AbejideTeclado.boTeclaEsquerda;
import static Abejide.AbejideTeclado.boTeclaDireita;
import static Abejide.AbejideThread.alma;
import static Abejide.AbejideThread.spriteSheetBaseDireita;
import static MetodosPrincipais.Executando.pausado;

import jplay.Animation;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class ParticulaAgua extends Thread{
    
    public Animation agua = new Animation("Imagens/Abejide/Particulas/Agua.png", 10);
    public Animation poca = new Animation("Imagens/Abejide/Particulas/Poca.png");
    public Animation pocaCollisao = new Animation("Imagens/Abejide/Particulas/PocaColisao.png");
    
    boolean foiPisada = false;
    boolean zerarFrame = false;
    
    double x = 0;
    
    @Override
    public void run(){
        agua.setSequence(0, 8);
        agua.setTotalDuration(500);
    }
    
    public ParticulaAgua(double varX, double varY){
        x = varX;
        pocaCollisao.y = (varY + 5) - pocaCollisao.height;
        agua.y = ((pocaCollisao.y - agua.height + pocaCollisao.height) - 2);
        poca.y = (pocaCollisao.y - poca.height + pocaCollisao.height);
    }
    
    public void PrintarAgua(double varX){
        agua.x = (varX + x) - ((agua.width) / 2) + (pocaCollisao.width / 2);
        pocaCollisao.x = varX + x;
        agua.draw();
        
        if(foiPisada){
            MudarFrame();
        }
        if((boTeclaEsquerda && (pocaCollisao.x + pocaCollisao.width / 2) < (alma.x + alma.width / 2) && 
          !foiPisada && !zerarFrame && pocaCollisao.collided(alma))&& (
           spriteSheetBaseDireita.getCurrFrame() == 11 || spriteSheetBaseDireita.getCurrFrame() == 15 ||
           spriteSheetBaseDireita.getCurrFrame() == 19 || spriteSheetBaseDireita.getCurrFrame() == 23)){
            foiPisada = true;
        } else if((boTeclaDireita && (pocaCollisao.x + pocaCollisao.width / 2) > (alma.x + alma.width / 2) && 
          !foiPisada && !zerarFrame && pocaCollisao.collided(alma))&& (
           spriteSheetBaseDireita.getCurrFrame() == 11 || spriteSheetBaseDireita.getCurrFrame() == 15 ||
           spriteSheetBaseDireita.getCurrFrame() == 19 || spriteSheetBaseDireita.getCurrFrame() == 23)){
            foiPisada = true;
        }
    }
    
    public void PrintarPoca(double varX){
        poca.x = (varX + x) - ((poca.width) / 2) + (pocaCollisao.width / 2);
        poca.draw();
        //pocaCollisao.draw();
    }
    
    private void MudarFrame(){
        if(agua.getCurrFrame() != agua.getFinalFrame()- 1 && !pausado){
            agua.update();
        } else if(agua.getCurrFrame() == agua.getFinalFrame() - 1){
            agua.setCurrFrame(0);
            zerarFrame = true;
            foiPisada = false;
            zerarFrame = false;
        }
    }
}

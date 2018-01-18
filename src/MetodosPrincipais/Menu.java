/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MetodosPrincipais;

import static MetodosPrincipais.PlanoDeFundo.solX;
import static MetodosPrincipais.PlanoDeFundo.luaX;
import static MetodosPrincipais.Main.ceu;
import static MetodosPrincipais.Main.montanhas;
import static MetodosPrincipais.Main.nuvem1;
import static MetodosPrincipais.Main.nuvem2;
import static MetodosPrincipais.Main.sol;
import static MetodosPrincipais.Main.lua;
import static MetodosPrincipais.PlanoDeFundo.tempoTransicao;
import static MetodosPrincipais.PlanoDeFundo.dia;
import static MetodosPrincipais.DeltaTime.dormirThread;
import static MetodosPrincipais.Executando.chao;

import static MetodosPrincipais.Main.janela;
import static MetodosPrincipais.Main.mouse;
import static MetodosPrincipais.Main.teclado;
import static MetodosPrincipais.Main.navegavel;

import static java.lang.Thread.sleep;

import jplay.Animation;
import jplay.Keyboard;

import java.awt.Point;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class Menu {
    
    public static boolean estaSobreButton = false;
    public static Point position = null;
    
    public static void Menu() throws InterruptedException{
        
        boolean menu = true;
        
        int distancia = 20;
        
        Animation orixasRevenge = new Animation("Imagens/Menu/OrixasRevenge.png", 4);
        orixasRevenge.setTotalDuration(500);
        orixasRevenge.x = ((janela.getWidth() - orixasRevenge.width) / 2);
        orixasRevenge.y = -55;
        
        Animation jogar = new Animation("Imagens/Menu/Menu_HUD.png", 11);
        jogar.y = janela.getHeight() / 2;
        jogar.x = ((janela.getWidth() - jogar.width) / 2);
        Animation controles = new Animation("Imagens/Menu/Menu_HUD.png", 11);
        controles.x = ((janela.getWidth() - controles.width) / 2);
        controles.y = jogar.y + jogar.height + distancia;
        controles.setCurrFrame(2);
        Animation sair = new Animation("Imagens/Menu/Menu_HUD.png", 11);
        sair.x = ((janela.getWidth() - sair.width) / 2);
        sair.y = controles.y + controles.height + distancia;
        sair.setCurrFrame(4);
        
        int indice = 0;
        boolean apertouEnter = false;
        boolean apertouSeta = false;
        
        while(menu){
            
            ceu.draw();
            montanhas.draw();
            nuvem1.draw();
            sol.draw();
            lua.draw();
            nuvem2.draw();
            
            orixasRevenge.draw();
            orixasRevenge.update();
            jogar.draw();
            controles.draw();
            sair.draw();
            
            DiaNoite();            
            
            janela.update();
            
            //Se o mouse ficar encima de um botão o cursor é coladado no lado dele, só que
            //se o usuário usar a seta do teclado para mover o cursor, ele é movido de acordo
            //com a tecla pressionada, porém se o mouse for movido novemente ele volta para
            //cima do botão que ele estava
            if(mouse.isOverObject(jogar) && !estaSobreButton){ //Boão jogar
                indice = 0;
                estaSobreButton = true;
                position = mouse.getPosition();
                jogar.setCurrFrame(1);
                controles.setCurrFrame(2);
                sair.setCurrFrame(4);
                apertouSeta = true;
            } else if(mouse.isOverObject(controles) && !estaSobreButton){ //Boão controles
                indice = 1;
                estaSobreButton = true;
                position = mouse.getPosition();
                jogar.setCurrFrame(0);
                controles.setCurrFrame(3);
                sair.setCurrFrame(4);
                apertouSeta = true;
            } else if(mouse.isOverObject(sair) && !estaSobreButton){ //Boão sair
                indice = 2;
                estaSobreButton = true;
                position = mouse.getPosition();
                jogar.setCurrFrame(0);
                controles.setCurrFrame(2);
                sair.setCurrFrame(5);
                apertouSeta = true;
            } else if(estaSobreButton && !mouse.isOverObject(sair) && !mouse.isOverObject(controles) && !mouse.isOverObject(jogar)){
                estaSobreButton = false;
            }
            
            if(mouse.getPosition() != position && estaSobreButton){
                estaSobreButton = false;
            }
            if (mouse.getPosition() != position && !mouse.isOverObject(sair) && !mouse.isOverObject(controles) && !mouse.isOverObject(jogar)) {
                jogar.setCurrFrame(0);
                controles.setCurrFrame(2);
                sair.setCurrFrame(4);
                apertouSeta = false;
            }
            if(teclado.keyDown(Keyboard.ENTER_KEY)){
                apertouEnter = true;
            }
            
            //Clique com o mouse
            if(mouse.isLeftButtonPressed() || apertouEnter){
                if(indice == 0 && (mouse.isOverObject(jogar) || (apertouEnter && apertouSeta))){ //Boão jogar
                    menu = false;
                    navegavel = 1;
                    if(dia){
                        solX = (chao.width * sol.x) / janela.getWidth();
                    } else{
                        luaX = (chao.width * lua.x) / janela.getWidth();
                    }
                } else if(indice == 1 && (mouse.isOverObject(controles) || (apertouEnter && apertouSeta))){ //Boão controles
                    navegavel = 2;
                    menu = false;
                } else if(indice == 2 && (mouse.isOverObject(sair) || (apertouEnter && apertouSeta))){ //Boão sair
                    janela.exit();
                    System.exit(0);
                } else {
                    apertouEnter = false;
                }
           }
            
            //Também sai com a tecla <Esc>
            if(teclado.keyDown(Keyboard.ESCAPE_KEY)){
                janela.exit();
                System.exit(0);
            } else if (teclado.keyDown(Keyboard.UP_KEY)){
                if (indice > 0){
                    indice --;
                }
                if (!apertouSeta) {
                    indice = 0;
                    apertouSeta = true;
                }
            } else if (teclado.keyDown(Keyboard.DOWN_KEY)){
                if (indice < 2) {
                    indice ++;
                }
                if (!apertouSeta) {
                    indice = 0;
                    apertouSeta = true;
                }
            }
            
            if(apertouSeta)switch (indice) {
                case 0:
                    jogar.setCurrFrame(1);
                    controles.setCurrFrame(2);
                    sair.setCurrFrame(4);
                    break;
                case 1:
                    jogar.setCurrFrame(0);
                    controles.setCurrFrame(3);
                    sair.setCurrFrame(4);
                    break;
                case 2:
                    jogar.setCurrFrame(0);
                    controles.setCurrFrame(2);
                    sair.setCurrFrame(5);
                    break;
            }
            
            sleep(dormirThread);
            
            position = mouse.getPosition();
        }
    }
    
    public static void DiaNoite(){
        
        if(nuvem2.getCurrFrame()!= (nuvem2.getFinalFrame()) - 1){
            ceu.update();
            montanhas.update();
            nuvem1.update();
            nuvem2.update();
        }
        
        //Muda de dia para noite
        if(sol.x > janela.getWidth() && dia){
            ceu.setSequenceTime(0, 6, true, tempoTransicao);
            montanhas.setSequenceTime(0, 6, true, tempoTransicao);
            nuvem1.setSequenceTime(0, 6, true, tempoTransicao);
            nuvem2.setSequenceTime(0, 6, true, tempoTransicao);
            dia = false;
            lua.x = -lua.width;
        } else if(lua.x > janela.getWidth() && !dia){
            ceu.setSequenceTime(6, 11, true, tempoTransicao);
            montanhas.setSequenceTime(6, 11, true, tempoTransicao);
            nuvem1.setSequenceTime(6, 11, true, tempoTransicao);
            nuvem2.setSequenceTime(6, 11, true, tempoTransicao);
            dia = true;
            sol.x = -sol.width;
        }
    }
}

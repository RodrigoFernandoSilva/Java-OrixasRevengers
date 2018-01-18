/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MetodosPrincipais;

import static MetodosPrincipais.Main.janelaDesativa;
import static MetodosPrincipais.Main.mouse;
import static MetodosPrincipais.Main.navegavel;
import static MetodosPrincipais.Main.janela;
import static MetodosPrincipais.Main.teclado;
import static MetodosPrincipais.Main.ceu;
import static MetodosPrincipais.Main.montanhas;
import static MetodosPrincipais.Main.nuvem1;
import static MetodosPrincipais.Main.nuvem2;
import static MetodosPrincipais.Main.sol;
import static MetodosPrincipais.Main.lua;

import java.awt.Point;
import java.awt.event.KeyEvent;

import jplay.GameImage;
import jplay.Animation;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class Controles {
    
    public static void Controles(){
        
        GameImage controles = new GameImage("Imagens/Controles/Controles.png");
        controles.height = janela.getHeight();
        controles.width = (janela.getWidth()* 600) / janela.getHeight();
        Animation voltar = new Animation("Imagens/Menu/Menu_HUD.png", 11);
                
        voltar.x = (janela.getWidth() - voltar.width) - 5;
        //voltar.y = (janela.getHeight()- voltar.height) - 5;
        voltar.y = 5;
        voltar.setCurrFrame(6);
        
        controles.x = janela.getWidth() / 2;
        controles.x -= controles.width / 2;
        
        controles.y = janela.getHeight() / 2;
        controles.y -= controles.height / 2;
        
        Point mouseUltimaPosicao = mouse.getPosition();
        
        while(true){
            
            ceu.draw();
            montanhas.draw();
            nuvem1.draw();
            sol.draw();
            lua.draw();
            nuvem2.draw();
            
            janelaDesativa.draw();
            
            controles.draw();
            voltar.draw();
            
            janela.update();
            
            if (teclado.keyDown(KeyEvent.VK_ENTER) || mouse.isLeftButtonPressed()) {
                if (voltar.getCurrFrame() == 7 || mouse.isOverObject(voltar)) {
                    navegavel = 0;
                    MetodosPrincipais.Menu.position = mouse.getPosition();
                    break;
                }
            } else if (teclado.keyDown(KeyEvent.VK_ESCAPE)) {
                navegavel = 0;
                MetodosPrincipais.Menu.position = mouse.getPosition();
                break;
            }
            
            if (teclado.keyDown(KeyEvent.VK_UP) || teclado.keyDown(KeyEvent.VK_DOWN)) {
                voltar.setCurrFrame(7);
                mouseUltimaPosicao = mouse.getPosition();
            }
            
            if (mouseUltimaPosicao != mouse.getPosition()) {
                if (mouse.isOverObject(voltar)) {
                    voltar.setCurrFrame(7);
                } else {
                    voltar.setCurrFrame(6);
                }
                mouseUltimaPosicao = mouse.getPosition();
            }
        }
    }
}

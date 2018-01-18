/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MetodosPrincipais;

import static MetodosPrincipais.DeltaTime.dormir;

import static MetodosPrincipais.Main.janela;
import static java.lang.Thread.sleep;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class LogoAnimado extends Thread{
    
    public void run(){
        
        janela.setTitle("Orixas Revenge");
        
        ImageIcon[] icon = new ImageIcon[5];
        icon[0] = new ImageIcon("Imagens/Logos/logo1.png");
        icon[1] = new ImageIcon("Imagens/Logos/logo2.png");
        icon[2] = new ImageIcon("Imagens/Logos/logo3.png");
        icon[3] = new ImageIcon("Imagens/Logos/logo4.png");
        
        int indice = 0;
        
        while(true){
            
            janela.setIconImage(icon[indice].getImage());
            
            try{
                sleep(dormir);
            } catch(InterruptedException ex){
                //Deu ruim
            }
            
            indice ++;
            indice %= 4;
        }
    }
}

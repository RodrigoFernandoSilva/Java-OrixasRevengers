/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MetodosPrincipais;

import Abejide.AbejideTeclado;
import static MetodosPrincipais.Main.ceu;
import static MetodosPrincipais.Main.montanhas;
import static MetodosPrincipais.Main.nuvem1;
import static MetodosPrincipais.Main.nuvem2;
import static MetodosPrincipais.Main.sol;
import static MetodosPrincipais.Main.lua;

import static MetodosPrincipais.DeltaTime.deltaTime;
import static MetodosPrincipais.DeltaTime.dormir;
import static MetodosPrincipais.DeltaTime.dormirThread;
import static MetodosPrincipais.Executando.pausado;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class PlanoDeFundo extends Thread{
    public static double luaX = -(lua.width);
    public static double solX = -(sol.width);
    
    public static int tempoTransicao = 1500;
    public static boolean dia = true;
    public static boolean menu = true;
    
    @Override
    public void run() {
        
        while(true){
            while (pausado) {
                try {
                    sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(dia && !menu){
                solX += (17 * deltaTime);
            } else if(!dia && !menu){
                luaX += (17 * deltaTime);
            }
            
            if(dia && menu){
                sol.x += 35 * deltaTime;
                
            } else if(!dia && menu){
                lua.x += 35 * deltaTime;
            }

            if((solX > ceu.width) || (luaX > ceu.width)){
                if(dia){
                    ceu.setSequenceTime(0, 6, true, tempoTransicao);
                    montanhas.setSequenceTime(0, 6, true, tempoTransicao);
                    nuvem1.setSequenceTime(0, 6, true, tempoTransicao);
                    nuvem2.setSequenceTime(0, 6, true, tempoTransicao);
                    dia = false;
                    solX = -(lua.width);
                } else{
                    ceu.setSequenceTime(6, 11, true, tempoTransicao);
                    montanhas.setSequenceTime(6, 11, true, tempoTransicao);
                    nuvem1.setSequenceTime(6, 11, true, tempoTransicao);
                    nuvem2.setSequenceTime(6, 11, true, tempoTransicao);
                    dia = true;
                    luaX = -(lua.width);
                }
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(PlanoDeFundo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

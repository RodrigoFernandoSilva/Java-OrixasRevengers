/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Olodumare;

import static MetodosPrincipais.Executando.executando;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class OlodumareEnvenenado extends Thread {
    public static int envenenado = 0;
    
    public void run(){
        while(executando){
            if(envenenado != 0){
                OlodumareThread.hpTemp -= 0.25;
                envenenado--;
                
                try {
                    sleep(250);
                } catch (InterruptedException ex) {
                    Logger.getLogger(OlodumareEnvenenado.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumareEnvenenado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

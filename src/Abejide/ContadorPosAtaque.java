/*
 * Essa thread serve para impedir que o Abejide ande logo após atacar, porque se não tiver isso,
 * se o jogador ficar atacando o combo das setas pode acabar soltando o son do personagem andando,
 * então tem um contador que faz é zerado após x tempo do ataque do Abejide terminar
 */

package Abejide;

import static MetodosPrincipais.Executando.executando;
import static Abejide.AbejideAnimacao.qualAtaque;
import static MetodosPrincipais.DeltaTime.dormir;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class ContadorPosAtaque extends Thread{
    public static boolean PodeAndar = true;
    
    public void run(){
        while(executando){
            if(qualAtaque != 0){
                PodeAndar = false;
                while(qualAtaque == 0){
                    try {
                        sleep(1);
                    } catch (InterruptedException ex) {
                       //Deu ruim
                    }
                }
                try {
                    sleep(500);
                } catch (InterruptedException ex) {
                   //Deu ruim
                }
                PodeAndar = true;
            }
            
            //Se não tiver isso a Thread não funciona
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
}

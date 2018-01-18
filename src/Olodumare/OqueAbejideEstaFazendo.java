/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Olodumare;

import static Olodumare.OlodumareThread.alma;

import static Abejide.AbejideTeclado.boEstaAtacando;
import static Abejide.AbejideTeclado.boTeclaBaixo;
import static Abejide.AbejideTeclado.boTeclaDireita;
import static Abejide.AbejideTeclado.boTeclaEsquerda;
import static Abejide.AbejideTeclado.apertouParaCorrer;
import static Abejide.AbejideAnimacao.qualAtaque;

import static MetodosPrincipais.Executando.executando;
import static MetodosPrincipais.DeltaTime.dormir;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class OqueAbejideEstaFazendo extends Thread{
    
    public static boolean ataqueFraco = false;
    
    public static boolean ataqueEspecialFraco = false;
    public static int qualAtaqueEspecialFraco = 0;
    
    public static boolean ataqueEspecial = false;
    public static int qualAtaqueEspecial = 0;
    
    public static boolean esquivando = false;
    public static boolean pulando = false;
    public static boolean seMovendo = false;
    public static boolean correndo = false;
    public static int distancia = 0;
    
    @Override
    public void run(){
        
        while(executando){
            //Pulando
            if(Abejide.AbejideThread.alma.getVelocityY() != 0 && !pulando){
                pulando = true;
            } else if(Abejide.AbejideThread.alma.getVelocityY() == 0 && pulando){
                pulando = false;
            }
            
            //Se movendo. tanto faz a direção
            if(!boEstaAtacando && boTeclaDireita && !boTeclaEsquerda&& !pulando){
                seMovendo = true;
            } else if(!boEstaAtacando && !boTeclaDireita && boTeclaEsquerda&& !pulando){
                seMovendo = true;
            } else if((boTeclaDireita && boTeclaEsquerda) || (boEstaAtacando || (!boTeclaDireita && !boTeclaEsquerda)) && (seMovendo)){
                seMovendo = false;
            }
            
            //Vê se esta correndo
            if(apertouParaCorrer && !correndo){
                correndo = true;
            } else if(!apertouParaCorrer && correndo){
                correndo = false;
            }
            
            //Distancia
            if(alma.x + (alma.width / 2) > Abejide.AbejideThread.alma.x + (Abejide.AbejideThread.alma.width / 2)){
                distancia = (int) ((alma.x + (alma.width / 2)) - (Abejide.AbejideThread.alma.x + (Abejide.AbejideThread.alma.width / 2)));
            } else{
                distancia =(int) ((Abejide.AbejideThread.alma.x + (Abejide.AbejideThread.alma.width / 2)) - (alma.x + (alma.width / 2)));
            }
            
            //Ataque fraco
            if(qualAtaque == 1 || qualAtaque == 2 || qualAtaque == 3 || qualAtaque == 7){
                ataqueFraco = true;
            } else{
                ataqueFraco = false;
            }
            
            //Ataque especial fraco
            if(qualAtaque == 4 || qualAtaque == 5 || qualAtaque == 6){
                ataqueEspecialFraco = true;
                qualAtaqueEspecialFraco = qualAtaque;
            } else{
                ataqueEspecialFraco = false;
                qualAtaqueEspecialFraco = 0;
            }
            
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(OqueAbejideEstaFazendo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}

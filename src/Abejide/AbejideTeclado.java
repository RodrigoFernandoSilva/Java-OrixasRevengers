/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Para adicionar uma nova tecla, é pressiona adicionar a variavel <boolean> dela, o contador
 * em <int> dela, e adicionar um <if> no metodo <TeclaNaoPressionada> e outro no metodo
 * <TeclaPressionada>, e também um na Thread <AbejideZerarTecladoBoolean>, e também na thead
 * <AbejideAnimacao>.
*/

package Abejide;

import static MetodosPrincipais.Executando.pausado;
import static MetodosPrincipais.Main.teclado;
import static MetodosPrincipais.Camera.velocidadeCorendo;
import static MetodosPrincipais.DeltaTime.dormir;

import static Abejide.AbejideThread.sm;
import static Abejide.AbejideThread.alma;
import static Abejide.AbejideThread.mp;
import static Abejide.AbejideThread.poeira;
import static Abejide.AbejideThread.poeiraX;
import static Abejide.AbejideThread.mpCarregando;
import static Abejide.AbejideThread.mpCarregaPraAtacar;
import static Abejide.AbejideAnimacao.jaAtaqueEspecialForte;
import static MetodosPrincipais.ControladoDoLevel.abejideCarregaMP;

import static MetodosPrincipais.Executando.cameraSprite;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import jplay.Keyboard;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class AbejideTeclado extends Thread{
    //Salva quantas vezes a tecla foi pressionada
    public static int teclaEsquerda = 0;
    public static int teclaDireita = 0;
    public static int teclaBaixo = 0;
    
    public static boolean apertouParaCorrer = false;
    
    //Controle para que seja reconhecido o pressionamento da tecla apenas uma vez por click,
    //ou seja, só adiciona um ao contaador de tecla pressionada se a sua váriavel for igual a false,
    //porque se não fizer isso, como a tecla vai esta sendo sempre pressionada, a variável do
    //Contador vai ficar mais de 8000
    public static boolean boTeclaEsquerda = false;
    public static boolean boTeclaDireita = false;
    public static boolean boTeclaBaixo = false;
    //Obs: Não usada no sistema de ataque, tipo se a tecla esquerda tiver pressionada mais z,
    //não foi usada porque nessa Thread tem um sleep entre o reconhecimento da tecla pressionada
    //e a alteração da variável para true, e nesse meio periodo o jogador pode ter apertado
    //alguma seta, mais esse boolean é usado no sistema de movimentação do personagem e da câmera
    
    
    //Variável que fala se o jogador esta atacando ou não
    public static boolean boEstaAtacando = false;
    
    public static final double VELOCIDADE_CORRENDO= 40;
    
    public static boolean poeiraSolta = false;
    
    public static double poeiraUltimaPosicao;
    
    //Variaveis que controla o duplo clique para os combos e o ataque, somente adiciona um
    //ao contador
    boolean teclaDireitaSolta = true;
    AbejideDuploClique teclaDireitaThread = new AbejideDuploClique("teclaDireita");
    boolean teclaBaixoSolta = true;
    AbejideDuploClique teclaBaixoThread = new AbejideDuploClique("teclaBaixo");
    boolean teclaEsquerdaSolta = true;
    AbejideDuploClique teclaEsquerdaThread = new AbejideDuploClique("teclaEsquerda");
    
    @Override
    public void run() {
        
        //Loop do jogo, emquando o jogador não sair da batalha o jogo ta rodando
        while(MetodosPrincipais.Executando.executando){
            
            while (jaAtaqueEspecialForte) {
                try {
                    sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            while (pausado) {
                try {
                    sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            DuploClique();
            
            if(teclado.keyDown(KeyEvent.VK_Z) && mpCarregando < mp){
                mpCarregando += mpCarregaPraAtacar + abejideCarregaMP;
            } else if (!teclado.keyDown(KeyEvent.VK_Z) && jaAtaqueEspecialForte) {
                mpCarregando = 0;
            }
            
            //Sistema da particula de poeira quando Abejide corre
            if(teclaDireita == 2 && !poeiraSolta && !boEstaAtacando){
                poeiraX = (-cameraSprite.x) + alma.x - 40;
                poeira.y = alma.getFloor() - (alma.getFloor() - alma.y) + 45;
                poeira.setSequenceTime(0, 7, true, 600);
                poeiraSolta = true;
            } else if(teclaEsquerda == 2 && !poeiraSolta && !boEstaAtacando){
                poeiraX = (-cameraSprite.x) + alma.x - 80;
                poeira.y = alma.getFloor() - (alma.getFloor() - alma.y) + 45;
                poeira.setSequenceTime(7, 14, true, 600);
                poeiraSolta = true;
            }
            if(teclaEsquerda!= 2 && teclaDireita!= 2 && poeiraSolta && poeira.getCurrFrame() == poeira.getFinalFrame() - 1){
                poeiraSolta = false;
                poeiraUltimaPosicao = poeiraX;

            }
            
            TeclaNaoPressionada();
            
            //Tem o TryCatch porque nesse metodo tem um sleep
            try {
                TeclaPressionada();
            } catch (InterruptedException ex) {
                Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                TeclaFoiSolta();
            } catch (InterruptedException ex) {
                Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Sistema de correr
            if(teclaEsquerda == 2 || teclaDireita == 2){
               apertouParaCorrer = true;
            } else if((velocidadeCorendo != 0 && !boTeclaDireita && !boTeclaEsquerda) || sm < 0 || boEstaAtacando){
                velocidadeCorendo = 0;
                apertouParaCorrer = false;
            }
            if(apertouParaCorrer){
                velocidadeCorendo = VELOCIDADE_CORRENDO + (sm * 1);
            }
            
            if(boEstaAtacando){
                teclaBaixo = 0;
                teclaDireita = 0;
                teclaEsquerda = 0;
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Vê se algumas teclas não então sendo pressionadas, se não estiver sua variável boolean
    //vai ser zerada (false), tecla z separada em outra Thread
    private void TeclaNaoPressionada() {
        if(!teclado.keyDown(Keyboard.LEFT_KEY)){
            boTeclaEsquerda = false;
        }
        if(!teclado.keyDown(Keyboard.RIGHT_KEY)){
            boTeclaDireita = false;
        }
        if(!teclado.keyDown(Keyboard.DOWN_KEY)){
            boTeclaBaixo = false;
        }
    }
    
    //Vê se a tela foi pressionada, se sim sua boolean vai ser true e além disso, a tecla só porde
    //ser pressionada no máximo duaz vezes, tecla z separada em outra Thread
    private void TeclaPressionada() throws InterruptedException {
        int dormir = 150;
        if(teclado.keyDown(Keyboard.LEFT_KEY) && boTeclaEsquerda == false &&
           !boEstaAtacando){
            sleep(dormir);
            boTeclaEsquerda = true;
        }
        if(teclado.keyDown(Keyboard.RIGHT_KEY) && boTeclaDireita == false &&
           !boEstaAtacando){
            sleep(dormir);
            boTeclaDireita = true;
        }
        if(teclado.keyDown(Keyboard.DOWN_KEY) && boTeclaBaixo == false &&
           !boEstaAtacando){
            sleep(dormir);
            boTeclaBaixo = true;
        }
    }
    
    private void TeclaFoiSolta() throws InterruptedException {
        if(!teclado.keyDown(Keyboard.LEFT_KEY) && teclaEsquerda == 2){
            sleep(100);
            teclaEsquerda = 0;
        }
        if(!teclado.keyDown(Keyboard.RIGHT_KEY) && teclaDireita == 2){
            sleep(100);
            teclaDireita = 0;
        }
        if(!teclado.keyDown(Keyboard.DOWN_KEY) && teclaBaixo == 2){
            sleep(100);
            teclaBaixo = 0;
        }
    }
    
    private void DuploClique(){
        //Tecla Direita
        if(teclaDireitaSolta && boTeclaDireita){
            teclaDireitaThread = new AbejideDuploClique("teclaDireita");
            teclaDireitaThread.start();
            teclaDireita ++;
            teclaDireitaSolta = false;
        }
        if(!boTeclaDireita && !teclaDireitaSolta){
            teclaDireitaSolta = true;

            if(teclaDireita >= 2){
                teclaDireita = 0;
                teclaDireitaThread.MatarThread();
            }
        }
        
        //Tecla Esquerda
        if(teclaEsquerdaSolta && boTeclaEsquerda){
            teclaEsquerdaThread = new AbejideDuploClique("teclaEsquerda");
            teclaEsquerdaThread.start();
            teclaEsquerda ++;
            teclaEsquerdaSolta = false;
        }
        if(!boTeclaEsquerda && !teclaEsquerdaSolta){
            teclaEsquerdaSolta = true;

            if(teclaEsquerda >= 2){
                teclaEsquerda = 0;
                teclaEsquerdaThread.MatarThread();
            }
        }
        
        //Tecla Baixo
        if(teclaBaixoSolta && boTeclaBaixo){
            teclaBaixoThread = new AbejideDuploClique("teclaBaixo");
            teclaBaixoThread.start();
            teclaBaixo ++;
            teclaBaixoSolta = false;
        }
        if(!boTeclaBaixo && !teclaBaixoSolta){
            teclaBaixoSolta = true;

            if(teclaBaixo >= 2){
                teclaBaixo = 0;
                teclaBaixoThread.MatarThread();
            }
        }
    }
}

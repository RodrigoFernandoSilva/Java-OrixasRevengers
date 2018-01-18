/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Abejide;

import static MetodosPrincipais.DeltaTime.dormir;
import static MetodosPrincipais.Camera.direitaDistancia;
import static MetodosPrincipais.Camera.esquerdaDistancia;
import static MetodosPrincipais.Camera.velocidadeCorendo;
import static MetodosPrincipais.Main.teclado;
import static MetodosPrincipais.Main.janela;
import static MetodosPrincipais.Executando.cameraSprite;
import static MetodosPrincipais.Executando.chao;
import static MetodosPrincipais.Executando.barraGrade;
import static MetodosPrincipais.Executando.barraHP;
import static MetodosPrincipais.Executando.barraMP;
import static MetodosPrincipais.Executando.barraMPcarregando;
import static MetodosPrincipais.Executando.barraSM;
import static MetodosPrincipais.Executando.pausado;
import MetodosPrincipais.Executando;

//Importa as variáveis do teclado, quemostra quantas vezes a tecla foi pressionada
import static Abejide.AbejideTeclado.boTeclaEsquerda;
import static Abejide.AbejideTeclado.boTeclaDireita;
import static Abejide.AbejideTeclado.boEstaAtacando;
import static Abejide.ContadorPosAtaque.PodeAndar;
import static Abejide.AbejideThread.sm;
import static Abejide.AbejideAnimacao.jaRodou;
import static Abejide.AbejideAnimacao.direcaoRodou;
import static Abejide.AbejideAnimacao.jaAtaqueEspecialForte;
import static MetodosPrincipais.ControladoDoLevel.abejideRecuperaHP;
import static MetodosPrincipais.ControladoDoLevel.abejideRecuperaSM;

import jplay.GameImage;
import jplay.Sprite;
import jplay.Animation;;
import jplay.Keyboard;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class AbejideThread extends Thread {
    public static GameImage caixaDeColisao = new GameImage("Imagens/Abejide/CaixaDeColisao.png");
    
    public static Sprite alma;
    
    //Sprites sheet do Abejide olhando para a direita
    public static Animation spriteSheetBaseDireita;
    public static Animation spriteSheetAtaqueFracoDireita;
    public static Animation spriteSheetVenenoDireita;
    public static Animation spriteSheetVenenoEsquerda;
    public static Animation spriteSheetRolandoDireita;
    public static Animation spriteSheetRolandoEsquerda;
    //Sprites sheet do Abejide olhando para a esquerda
    public static Animation spriteSheetBaseEsquerda;
    public static Animation spriteSheetAtaqueFracoEsquerda;
    public static Animation poeira;
    public static double poeiraX = 0;
    
    //Variáveis quenão existe na classe personagem
    //Ernegia (estamina)
    public static double sm  = 100;
    public static double hp = 100;
    public static double mp = 100;
    public static int level = 0;
    public static double mpCarregando = 0;
    public static double perderSmAtacando = 5f;
    
    public static boolean jaPulou = false;
    
    static final int AJUSTAR_NO_CHAO = 66;
    
    public static BolaDeFogo[] bolaDeFogo;
    public static BolaDeFogo[] bolaDeFogoEspecial01 = new BolaDeFogo[10];
    public static BolaDeFogo[] bolaDeFogoEspecial01_0 = new BolaDeFogo[10];
    public static BolaDeFogo[] bolaDeFogoEspecial01_1 = new BolaDeFogo[10];
    
    public static FaixaDeErnegia faixaDeErnegia[] = new FaixaDeErnegia[3];
    
    public static ParticulaAgua[] particulaAgua;
    
    public static double ajustarCaixaDeColisao = 0;
    
    public static double mpCarregaPraAtacar = 0.6f;
    
    final double FORCA_PULO = 3.7f;
    final double PERDER_SM_CORRENDO = 0.1f;
    final double PERDER_SM_PULANDO = 4f;
    final double RECUPERAR_SM = 0.04f;
    final double RECUOERAR_SM_PARADO = 0.04f;
    final double RECUPERAR_HP = 0.02f;
    final static double PERDER_SM_ROLANDO = 4.5f;
    static double perderSmRolando = PERDER_SM_ROLANDO;
    
    public static double velocidaeRodouPassar = 0;
    
    @Override
    public void run(){
        
        for (int i = 0; i < bolaDeFogoEspecial01.length; i ++) {
            bolaDeFogoEspecial01[i] = new BolaDeFogo(0, 0, false, false);
            bolaDeFogoEspecial01_0[i] = new BolaDeFogo(0, 0, false, false);
            bolaDeFogoEspecial01_1[i] = new BolaDeFogo(0, 0, false, false);
        }
        
        for (int i = 0; i < faixaDeErnegia.length; i ++) {
            faixaDeErnegia[i] = new FaixaDeErnegia(0, 0, false, false);
        }
        
        double perderSmCorrendo;
        double perderSmPulando;
        double recuperarSM;
        double recuperarSmParado;
        double recuperarHp;
        double velocidadeRodou = 150;
        
        double velocidade;
        //Carrega todas as imagens do Abejide
        alma = new Sprite("Imagens/Abejide/Alma.png");
        spriteSheetBaseDireita = new Animation("Imagens/Abejide/Direita/Bases.png", 27, true);
        spriteSheetAtaqueFracoDireita = new Animation("Imagens/Abejide/Direita/Ataques/AtaquesFraco.png", 21);
        spriteSheetVenenoDireita = new Animation("Imagens/Abejide/Direita/AtaqueEspecial/AtaqueVeveno.png", 19);
        spriteSheetBaseEsquerda = new Animation("Imagens/Abejide/Esquerda/Bases.png", 27, true);
        spriteSheetAtaqueFracoEsquerda = new Animation("Imagens/Abejide/Esquerda/Ataques/AtaquesFraco.png", 21);
        spriteSheetVenenoEsquerda = new Animation("Imagens/Abejide/Esquerda/AtaqueEspecial/AtaqueVeveno.png", 19);
        spriteSheetRolandoDireita = new Animation("Imagens/Abejide/Direita/Rolando.png", 9);
        spriteSheetRolandoEsquerda = new Animation("Imagens/Abejide/Esquerda/Rolando.png", 9);
        poeira = new Animation("Imagens/Abejide/Particulas/Poeira.png", 15);
        poeira.setSequenceTime(0, 7, true, 600);
        poeira.x = alma.x;
        poeira.y = alma.y;
        
        //Inicia o personagem no meio da tela
        alma.setX(janela.getWidth() * 0.15);
        alma.setY(Executando.chao.y - alma.height + AJUSTAR_NO_CHAO);
        //Inicia os outros atributos do Abejide
        alma.setFloor(((int)(Executando.chao.y)) + AJUSTAR_NO_CHAO);
        alma.setJumpVelocity(FORCA_PULO);
        
        //Thread de leitura do teclado
        AbejideTeclado abejideTeclado = new AbejideTeclado();
        abejideTeclado.start();
        //Thread para controlar a animação do Abejide
        AbejideAnimacao abejideAnimacao = new AbejideAnimacao();
        abejideAnimacao.start();
        //Thread para controlar o som
        AbejideSons abejideSons = new AbejideSons();
        abejideSons.start();
        //Thread da bola de fogo
        bolaDeFogo = new BolaDeFogo[5];
        for(int i = 0; i < bolaDeFogo.length; i ++){
            bolaDeFogo[i] = new BolaDeFogo(0, 0, false, false);
        }
        
        particulaAgua = new ParticulaAgua[3];
        particulaAgua[0] = new ParticulaAgua(300, (chao.y + 75));
        particulaAgua[1] = new ParticulaAgua(900, (chao.y + 75));
        particulaAgua[2] = new ParticulaAgua(1800, (chao.y + 75));
        for(int i = 0; i < particulaAgua.length; i ++){
            particulaAgua[i].start();
        }
        
        int ajusteY = 133;
        int ajusteX = 10;
        //Soma mais uma para dizer que uma thread de todas ja terminou de carregar as imagens
        Executando.threadsCarregadas ++;
        while(Executando.executando){
            perderSmCorrendo = PERDER_SM_CORRENDO;
            perderSmPulando = PERDER_SM_PULANDO;
            recuperarSM = RECUPERAR_SM;
            recuperarSmParado = RECUOERAR_SM_PARADO;
            recuperarHp = RECUPERAR_HP;
            
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
            
            /*if(teclado.keyDown(Keyboard.ENTER_KEY)){
                hp = 100;
                mp = 100;
                sm = 100;
                if (boTeclaDireita || boTeclaEsquerda) {
                    Olodumare.OlodumareThread.hpTemp -= 500;
                }
            }*/
            
            //Coloca a velocidade da camera no player
            velocidade = MetodosPrincipais.Camera.velocidade;
            
            //Sistema de recuperar HP com o passar do tempo;
            if(hp < 100){
                hp += recuperarHp + abejideRecuperaHP;
            }
            
            //Se o personagem estiver correndo e a estamina acabar, ela é zerada
            if(velocidadeCorendo !=0 && sm <= 0){
                velocidadeCorendo = 0;
            }
            //Se o personagem estiver correndo e a estamina não estiver acabado, ela
            //é diminuida
            if(velocidadeCorendo != 0 && sm > 0){
                sm -= perderSmCorrendo;
            //Se o personagem não estiver toda a estamina pre-enchida ela é recuperada, caso
            //o if acima não tiver entrado
            } else if(sm < 100){
                sm += recuperarSM + abejideRecuperaSM;
                //Se ele tiver parado recupera mais rapido
                if(!boTeclaDireita && ! boTeclaEsquerda){
                    sm += recuperarSmParado;
                }
            }
            
            //Muda a tamanho das barras do HUD.
            //É tirado o pitoco que tem depois da barra no (<248 - x>, no inicio da conta), e o
            //do inicio no (< + x> | ta no final da conta)
            barraHP.width = (int) (((barraGrade.width - 14) * hp) / 100);
            barraMP.width = (int) (((barraGrade.width - 53) * mp) / 100);
  barraMPcarregando.width = (int) (((barraGrade.width - 53) * mpCarregando) / 100);
            barraSM.width = (int) (((barraGrade.width - 53) * sm) / 100);
            
            //Coloca as sprite sheet em cima da alma do Abejide
            spriteSheetBaseDireita.x = alma.getX() - 150 - ajusteX;
            spriteSheetBaseDireita.y = alma.getY() - ajusteY;
            
            spriteSheetAtaqueFracoDireita.x = alma.getX() - 150;
            spriteSheetAtaqueFracoDireita.y = alma.getY() - ajusteY;
            
            spriteSheetBaseEsquerda.x = alma.getX() - 138 - ajusteX;
            spriteSheetBaseEsquerda.y = alma.getY() - ajusteY;
            
            spriteSheetAtaqueFracoEsquerda.x = alma.getX() - 138;
            spriteSheetAtaqueFracoEsquerda.y = alma.getY() - ajusteY;
            
            spriteSheetVenenoDireita.x = alma.getX() - 138 - ajusteX;
            spriteSheetVenenoDireita.y = alma.getY() - ajusteY;
            
            spriteSheetVenenoEsquerda.x = alma.getX() - 138 - ajusteX;
            spriteSheetVenenoEsquerda.y = alma.getY() - ajusteY;
            
            spriteSheetRolandoEsquerda.x = alma.getX() - 138 - ajusteX;
            spriteSheetRolandoEsquerda.y = alma.getY() - ajusteY;
            
            spriteSheetRolandoDireita.x = alma.getX() - 138 - ajusteX;
            spriteSheetRolandoDireita.y = alma.getY() - ajusteY;
            
            spriteSheetRolandoEsquerda.x = alma.getX() - 138 - ajusteX;
            spriteSheetRolandoEsquerda.y = alma.getY() - ajusteY;
            
            
            caixaDeColisao.x = ((alma.x + (alma.width / 2)) - (caixaDeColisao.width / 2)) + ajustarCaixaDeColisao;
            caixaDeColisao.y = alma.y;
            
            //Ajusta a câmera a velocidade do personagem quando ele estiver rodando no chão
            if (jaRodou) {
                velocidaeRodouPassar = ((velocidade + velocidadeRodou) * MetodosPrincipais.DeltaTime.deltaTime) * direcaoRodou;
                if (direcaoRodou == 1) {
                    if(alma.x< janela.getWidth() * direitaDistancia){
                        alma.x += velocidaeRodouPassar;
                    }  else if(cameraSprite.x <= -(chao.width - janela.getWidth() - 0.5)){
                        alma.x += velocidaeRodouPassar;
                    }
                } else {
                    if(alma.x > janela.getWidth() * esquerdaDistancia){
                        alma.x += velocidaeRodouPassar;
                    } else if(cameraSprite.x >= -0.5){
                        alma.x += velocidaeRodouPassar;
                    }
                }
            }
            
            //Move o personagem com as setas, se o mesmo não estiver atacando
            if(PodeAndar && !jaRodou){
                if(boTeclaEsquerda && boEstaAtacando == false && alma.getX() > 0){

                    if(alma.x > janela.getWidth() * esquerdaDistancia){
                        alma.x -= ((velocidade + velocidadeCorendo) * MetodosPrincipais.DeltaTime.deltaTime);
                    } else if(cameraSprite.x >= -0.5){
                        alma.x -= ((velocidade + velocidadeCorendo) * MetodosPrincipais.DeltaTime.deltaTime);
                    }
                }
                if(boTeclaDireita && boEstaAtacando == false && alma.getX() < (janela.getWidth() - alma.width)){
                    if(alma.x< janela.getWidth() * direitaDistancia){
                        alma.x += ((velocidade + velocidadeCorendo) * MetodosPrincipais.DeltaTime.deltaTime);
                    }  else if(cameraSprite.x <= -(chao.width - janela.getWidth() - 0.5)){
                        alma.x += ((velocidade + velocidadeCorendo) * MetodosPrincipais.DeltaTime.deltaTime);
                    }
                }
            }
            
            //Se o personagem estiver pulando e atacando, esses if faz ele se mover no ar caso
            // o esmo esteja pressionando a tecla de movimento
            if(boEstaAtacando == true && alma.y < (alma.getFloor() - alma.height)){
                if(boTeclaDireita){
                    alma.x += ((velocidade + velocidadeCorendo) * MetodosPrincipais.DeltaTime.deltaTime);
                }
                if(boTeclaEsquerda){
                    alma.x -= ((velocidade + velocidadeCorendo) * MetodosPrincipais.DeltaTime.deltaTime);
                }
            }
            
            if(teclado.keyDown(Keyboard.UP_KEY) && jaPulou == false && alma.y != (alma.getFloor() - alma.height)){
                jaPulou = true;
                sm -= perderSmPulando;
            } else if(alma.y == (alma.getFloor() - alma.height)){
                jaPulou = false;
            }
            
            //Faz o personagem pular se ele não estiver atacando
            if(!boEstaAtacando && !jaRodou){
                alma.jump(Keyboard.UP_KEY);
            } else{
                alma.fall();
            }
            
            //Impedi que o mp do Abejide seja maior que 100
            if(mp > 100){
                mp = 100;
            }
            
            try{
                sleep(dormir);
            } catch(InterruptedException ex){
                //Deu ruim!!!
            }
        }
        
    }
}

/*
 * O que tem aqui em termos de execução:
 *  Diminuir a barra de estama quando estiver correndo;
 *  Recuperar a barra de estamina, quando o personagem estiver parado;
 *  Atualizar informações do HUD, (HP, MP SM);
 *  Movimentar o sprite do Abejide, (alma);
 *  Movimentar o alma do Abejide quando ele estiver pulando e atacando;
 *  Colocar os sprite sheet do Abejide encima da alma;
 *  Sistema de fazer o Abejide pular;
 *  Sistema de perder estamina quando pular;
*/
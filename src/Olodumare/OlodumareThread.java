/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Olodumare;

import Abejide.AbejideTeclado;
import Abejide.AbejideThread;
import static MetodosPrincipais.ControladoDoLevel.*;

import static MetodosPrincipais.Executando.executando;
import static MetodosPrincipais.Executando.threadsCarregadas;
import static MetodosPrincipais.Main.janela;
import static MetodosPrincipais.Executando.chao;
import static MetodosPrincipais.Executando.pausado;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import jplay.Sprite;
import jplay.Animation;
import jplay.GameImage;

/**
 *
 * @author homework
 */
public class OlodumareThread extends Thread{
    public static Sprite alma;
    public static Animation spriteSheetBaseDireita;
    public static Animation spriteSheetAtaqueFracoDireita;
    
    public static Animation spriteSheetBaseEsquerda;
    public static Animation spriteSheetAtaqueFracoEsquerda;
    
    public static OlodumareBolaDeFogo[] olodumareBolaDeFogo;
    public static int olodumareBolaDeFogoMorta = 0;
    
    public static GameImage olodumareBarraGrade;
    public static Animation[] olodumareBarraHP = new Animation[5];
    public static int olodumareNumeroDeBolaDeFogo;
    public static int olodumareBolaDeFogoCriadas = 0;
    public static OlodumarePentagramaBola olodumarePentagramaBola = new OlodumarePentagramaBola();
    public static OlodumarePedraDoChao olodumarePedraDoChao = new OlodumarePedraDoChao(0, 0);
    public static OlodumarePedraGigante olodumarePedraGigante = new OlodumarePedraGigante(0, 0);
    
    public static double x;
    
    public static final double VELOCIDADE = 30;
    
    public static Font olodumareFonteTTF;
    public static Color olodumareFonteCor;
    
    static final int AJUSTAR_NO_CHAO = 76;
    static final int TEMPO_PARADO = 1500;
    
    public static int ajusteX = 0;
    public static boolean mostrandoEsquerda = true;
    
    public static double hpTotal;
    public static double hpTemp;
    
    static final int THREAD_TAMANO_ATAQUE = 7;
    public static int qualThreadAtaque = 0;
    //0: nenhum ataque
    //1: OlodumareIA01 - parado e/ou Teketransporte
    //2: OlodumareIA02 - Se move na tela
    
    public static boolean estaBolaDeFogo = false;
    public static boolean estaPentagramaBola= false;
    public static boolean estaPedraDoChao = false;
    public static boolean estaPedraGigante = false;
    public static boolean estaThreadIA01= false;
    public static boolean estaThreadIA02= false;
    
    
    @Override
    public void run() {
        boolean animacaoDeAtaque = false;
        
        Random gerador = new Random();
        
        //Cria a cor do texto e a fonte para a barra de vida do Olodumare
        File pasta = new File("Fontes/fonte.ttf");
        FileInputStream pastaFonte = null;
        try {
            pastaFonte = new FileInputStream(pasta);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OlodumareThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            olodumareFonteTTF = Font.createFont(Font.TRUETYPE_FONT, pastaFonte).deriveFont(Font.PLAIN, 12);
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(OlodumareThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        olodumareFonteCor = new Color(255, 255, 255);
        
        //Carrega as imagens para as variáveis
        olodumareBarraGrade = new GameImage("Imagens/Olodumare/HUD/BarraGrade.png");
        olodumareBarraGrade.x = (janela.getWidth()  - olodumareBarraGrade.width) / 2;
        olodumareBarraGrade.y =  janela.getHeight() - olodumareBarraGrade.height - 5;
        
        olodumareBarraHP[0] = new Animation("Imagens/Olodumare/HUD/HpAzul.png");
        olodumareBarraHP[1] = new Animation("Imagens/Olodumare/HUD/HpLaranja.png");
        olodumareBarraHP[2] = new Animation("Imagens/Olodumare/HUD/HpCiano.png");
        olodumareBarraHP[3] = new Animation("Imagens/Olodumare/HUD/HpLilas.png");
        olodumareBarraHP[4] = new Animation("Imagens/Olodumare/HUD/HpVerde.png");
        
        //Coloca as barra de hp no lugar certo na tela
        for (Animation olodumareBarraHP1 : olodumareBarraHP) {
            olodumareBarraHP1.x = olodumareBarraGrade.x + 18;
            olodumareBarraHP1.y = olodumareBarraGrade.y;
        }
        
        alma = new Sprite("Imagens/Olodumare/alma.png");
        spriteSheetBaseDireita = new Animation("Imagens/Olodumare/Direita/Base.png", 30);
        spriteSheetBaseEsquerda = new Animation("Imagens/Olodumare/Esquerda/Base.png", 30);
        
        x = (janela.getWidth() * 0.8) + 226;
        alma.setX(x);
        alma.setY((chao.y - alma.height) + AJUSTAR_NO_CHAO);
        
        hpTotal = 10000;
        hpTemp = 100;
        hpTotal -= hpTemp;
        
        spriteSheetBaseDireita.setSequenceTime(0, 8, true, TEMPO_PARADO);
        spriteSheetBaseEsquerda.setSequenceTime(0, 8, true, TEMPO_PARADO);
        
        //Cria todas as Threads do Olodumare
        OlodumareEnvenenado olodumareEnvenenado = new OlodumareEnvenenado();
        olodumareEnvenenado.start();
        OqueAbejideEstaFazendo oqueAbejideEstaFazendo = new OqueAbejideEstaFazendo();
        oqueAbejideEstaFazendo.start();
        OlodumareAnimacao olodumareAnimacao = new OlodumareAnimacao();
        olodumareAnimacao.start();
        
        //Cria as Thread para não da erro no executar quando ve se elas estão vivas
        olodumareBolaDeFogo = new OlodumareBolaDeFogo[500];
        
        spriteSheetBaseDireita.hide();
        
        threadsCarregadas ++;
        int i;
        int qualBolaDefogo;
        
        //Espera a thread do Abejide ter inicializado, para não correr o risco de IA do
        //Olodumare tentar acessar um sprite do Abejide que ainda não foi carregado
        while(threadsCarregadas < 2){
            System.out.print(""); //Sem isso o programa trava no while, não sei porque
            //Quando a thread carrega a imangen, logo abaixo dela é adicionado um a variavel do while
        }
        
        try {
            sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(OlodumareThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(executando){
            while (pausado) {
                try {
                    sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AbejideTeclado.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if(qualThreadAtaque == 0){
                if (gerador.nextInt() > chanceAndarAtacar) {
                    qualThreadAtaque = gerador.nextInt(5) + 3;
                    while (true) {
                        if (qualThreadAtaque == 4 && !podeAtaque4) {
                            qualThreadAtaque = gerador.nextInt(5) + 3;
                        } else if (qualThreadAtaque == 5 && !podeAtaque5) {
                            qualThreadAtaque = gerador.nextInt(5) + 3;
                        } else if (qualThreadAtaque == 6 && !podeAtaque6) {
                            qualThreadAtaque = gerador.nextInt(5) + 3;
                        } else if (qualThreadAtaque == 7 && !podeAtaque7) {
                            qualThreadAtaque = gerador.nextInt(5) + 3;
                        } else {
                            break;
                        }
                    }
                } else {
                    qualThreadAtaque = gerador.nextInt(2) + 1;
                }
                //qualThreadAtaque = 6;
                switch (qualThreadAtaque) {
                    case 1: //Teletransporta e fica parada
                        OlodumareIA01 olodumareIA01 = new OlodumareIA01(50);
                        olodumareIA01.start();
                        estaThreadIA01 = true;
                        break;
                    case 2: //Anda pela tela
                        OlodumareIA02 olodumareIA02 = new OlodumareIA02(520);
                        olodumareIA02.start();
                        estaThreadIA02 = true;
                        break;
                    case 3: //Lança bola de fogo de diferentes formas
                        if (!estaBolaDeFogo) {
                            estaBolaDeFogo = true;
                            olodumareBolaDeFogoCriadas = 0;
                            olodumareNumeroDeBolaDeFogo = gerador.nextInt(12 - 2) + 1;
                            qualBolaDefogo = gerador.nextInt(5) + 1;
                            
                            if ("Esquerda".equals(EsquerdaOuDireita())) { //Em relação a Abejide
                                mostrandoEsquerda = true;
                                spriteSheetBaseDireita.hide();
                                spriteSheetBaseEsquerda.unhide();
                            } else {
                                mostrandoEsquerda = false;
                                spriteSheetBaseDireita.unhide();
                                spriteSheetBaseEsquerda.hide();
                            }
                            
                            while (true) {
                                if (qualBolaDefogo == 3 && !podeAtaque3_3) {
                                    qualBolaDefogo = gerador.nextInt(5) + 1;
                                } else if (qualBolaDefogo == 4 && !podeAtaque3_4) {
                                    qualBolaDefogo = gerador.nextInt(5) + 1;
                                } else if (qualBolaDefogo == 5 && !podeAtaque3_5) {
                                    qualBolaDefogo = gerador.nextInt(5) + 1;
                                } else {
                                    break;
                                }
                            }
                            for (i = 0; i < olodumareNumeroDeBolaDeFogo; i ++) {
                                if (mostrandoEsquerda) {
                                    olodumareBolaDeFogo[i] = new OlodumareBolaDeFogo(alma.x - 80, alma.y + 42, qualBolaDefogo, (i * 1000), true, i, bolaDefogoPrecisao);
                                } else {
                                    olodumareBolaDeFogo[i] = new OlodumareBolaDeFogo(alma.x + 84, alma.y + 42, qualBolaDefogo, (i * 1000), false, i, bolaDefogoPrecisao);
                                }
                                olodumareBolaDeFogo[i].start();
                                olodumareBolaDeFogoCriadas ++;
                            }
                        } else {
                            qualThreadAtaque = 0;
                        }
                        break;
                        
                    case 4: //Pentagrama, tem os espinhos e a chuva de bola de fogo
                        if (!estaPentagramaBola && !animacaoDeAtaque){
                                animacaoDeAtaque = true;
                                spriteSheetBaseEsquerda.setSequenceTime(10, 18, true, TEMPO_PARADO + 200);
                                spriteSheetBaseDireita.setSequenceTime(10, 18, true, TEMPO_PARADO + 200);
                        } else if (!animacaoDeAtaque) {
                            qualThreadAtaque = 0;
                        }
                        break;
                    case 5: //Sobe pedras do chão
                        animacaoDeAtaque = true;
                        spriteSheetBaseEsquerda.setSequenceTime(10, 18, true, TEMPO_PARADO + 200);
                        spriteSheetBaseDireita.setSequenceTime(10, 18, true, TEMPO_PARADO + 200);
                        break;
                    case 6: //Faz uma linha de bola de fogo
                        estaBolaDeFogo = true;
                        olodumareBolaDeFogoCriadas = 0;
                        olodumareNumeroDeBolaDeFogo = gerador.nextInt(100 - 2) + 1;
                        if ("Esquerda".equals(EsquerdaOuDireita())) { //Em relação a Abejide
                            mostrandoEsquerda = true;
                            spriteSheetBaseDireita.hide();
                            spriteSheetBaseEsquerda.unhide();
                        } else {
                            mostrandoEsquerda = false;
                            spriteSheetBaseDireita.unhide();
                            spriteSheetBaseEsquerda.hide();
                        }
                        for (i = 0; i < olodumareNumeroDeBolaDeFogo; i ++) {
                            if (mostrandoEsquerda) {
                                olodumareBolaDeFogo[i] = new OlodumareBolaDeFogo(alma.x - 80, alma.y + 42, 2, (i * 100), true, i, 10);
                            } else {
                                olodumareBolaDeFogo[i] = new OlodumareBolaDeFogo(alma.x + 84, alma.y + 42, 2, (i * 100), false, i, 10);
                            }
                            olodumareBolaDeFogo[i].start();
                            olodumareBolaDeFogoCriadas ++;
                        }
                        break;
                    case 7: //Pedra gigante
                        animacaoDeAtaque = true;
                        spriteSheetBaseEsquerda.setSequenceTime(10, 18, true, TEMPO_PARADO + 200);
                        spriteSheetBaseDireita.setSequenceTime(10, 18, true, TEMPO_PARADO + 200);
                        break;
                    default:
                        qualThreadAtaque = 0;
                }
            }
            
            if (mostrandoEsquerda) {
                ajusteX = 10;
            } else {
                ajusteX = -10;
            }
            
            //Solta a thread de ataque quando a animação chegar no frame certo
            if (!estaPedraGigante && qualThreadAtaque == 7) {
                if (spriteSheetBaseEsquerda.getCurrFrame() >= 10) {
                    if ("Esquerda".equals(EsquerdaOuDireita())) { //Em relação a Abejide
                        olodumarePedraGigante = new OlodumarePedraGigante(x, 1);
                    } else {
                        olodumarePedraGigante = new OlodumarePedraGigante(x, -1);
                    }
                    olodumarePedraGigante.start();
                    estaPedraGigante = true;
                }
            }
            
            if (!estaPedraDoChao && qualThreadAtaque == 5) {
                if (spriteSheetBaseEsquerda.getCurrFrame() >= 15) {
                    if ("Esquerda".equals(EsquerdaOuDireita())) { //Em relação a Abejide
                        olodumarePedraDoChao = new OlodumarePedraDoChao(alma.x, 1);
                    } else {
                        olodumarePedraDoChao = new OlodumarePedraDoChao(alma.x, -1);
                    }
                    olodumarePedraDoChao.start();
                    estaPedraDoChao= true;
                }
            }
            
            if (!estaPentagramaBola && qualThreadAtaque == 4) {
                if (spriteSheetBaseEsquerda.getCurrFrame() >= 13) {
                    olodumarePentagramaBola = new OlodumarePentagramaBola();
                    olodumarePentagramaBola.start();
                    estaPentagramaBola = true;
                }
            }
            
            //Volta para a animação do olodumare respirando quand oa animação de ataque acabar
            if (!estaPentagramaBola && animacaoDeAtaque && spriteSheetBaseEsquerda.getCurrFrame() >= (spriteSheetBaseEsquerda.getFinalFrame() - 1)) {
                spriteSheetBaseEsquerda.setSequenceTime(0, 8, true, TEMPO_PARADO);
                spriteSheetBaseDireita.setSequenceTime(0, 8, true, TEMPO_PARADO);
                animacaoDeAtaque = false;
            }
            if (estaPentagramaBola && animacaoDeAtaque && spriteSheetBaseEsquerda.getCurrFrame() >= (spriteSheetBaseEsquerda.getFinalFrame() - 1)) {
                spriteSheetBaseEsquerda.setSequenceTime(0, 8, true, TEMPO_PARADO);
                spriteSheetBaseDireita.setSequenceTime(0, 8, true, TEMPO_PARADO);
                animacaoDeAtaque = false;
                qualThreadAtaque = 0;
            }
            
            //Zera a variável de ataque quando todas as bolas de fogo forem criadas
            if (estaBolaDeFogo) {
                for (i = 0; i < olodumareNumeroDeBolaDeFogo; i ++) {
                    if (olodumareBolaDeFogo[i].isAlive()) {
                        break;
                    }
                }
                
                if (i >= olodumareNumeroDeBolaDeFogo) {
                    qualThreadAtaque = 0;
                }
            }
            
            //Zera a variável que controla a bola de fogo, é zerada quando todas as bolas de
            //fogo tiverem ja sido destruidas
            olodumareBolaDeFogoMorta = 0;
            for (i = 0; i < olodumareNumeroDeBolaDeFogo; i ++) {
                if (!olodumareBolaDeFogo[i].isAlive()) {
                    olodumareBolaDeFogoMorta += 1;
                }
            }
            if (olodumareBolaDeFogoMorta == olodumareNumeroDeBolaDeFogo) {
                estaBolaDeFogo = false;
            }
            
            spriteSheetBaseDireita.x = alma.getX() - ((spriteSheetBaseDireita.width - alma.width) / 2) - ajusteX;
            spriteSheetBaseDireita.y = alma.getY() - 20;
            spriteSheetBaseEsquerda.x = alma.getX() - ((spriteSheetBaseEsquerda.width - alma.width) / 2) - ajusteX;
            spriteSheetBaseEsquerda.y = alma.getY() - 20;
            
            ColocarHpNaBarra();
            
            try {
                sleep(MetodosPrincipais.DeltaTime.dormirThread);
            } catch (InterruptedException ex) {
                Logger.getLogger(OlodumareThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    void ColocarHpNaBarra(){
        if(hpTemp <= 0){
            //Move a primeira barra de hp para a ultima posição do vetor
            olodumareBarraHP[0].width = olodumareBarraGrade.width;
            Animation temp = olodumareBarraHP[0];
            
            for(int i = 0; i < olodumareBarraHP.length - 1; i ++){
                olodumareBarraHP[i] = olodumareBarraHP[i + 1];
            }
            
            olodumareBarraHP[olodumareBarraHP.length - 1] = temp;
            
            //Soma o o hp porque se a vida temporaria do Olodumare for manor que 0, quer dizer
            //que ele levou um dano que tirou toda aquela barra e uma parte da próxima, então
            //por isso que é somado, para atribuir o dano amais na próxima barra
            hpTotal += hpTemp;
            //Se o hp tatal for maior a 100, que dizer que ainda vai vir outra barra depois
            if(hpTotal > 100){
                hpTemp = 100;
                hpTotal -= hpTemp;
            //se não for, é atribuido oa hp total ao hp temporario e dpois o total é zerado,
            //também é zerado todas as barras de hp coloridas, deixando apenas a primeira bara
            } else{
                hpTemp = hpTotal;
                hpTotal = 0;
                for(int i = 1; i < olodumareBarraHP.length; i ++){
                    olodumareBarraHP[i].width = 0;
                }
            }
        }
        
        //Coloca o hp temporario na barra de "Animation"
        olodumareBarraHP[0].width = (int) (((516 - 36) * hpTemp) / 100);
    }
    
    public String EsquerdaOuDireita(){
        if(AbejideThread.alma.x + (AbejideThread.alma.width / 2) > alma.x + (alma.width / 2)){
            return "Direita";
        } else{
            return "Esquerda";
        }
    }
}
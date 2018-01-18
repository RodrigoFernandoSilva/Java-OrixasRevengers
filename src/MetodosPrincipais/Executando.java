/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MetodosPrincipais;

import static Abejide.AbejideTeclado.poeiraUltimaPosicao;
import static Abejide.AbejideThread.particulaAgua;
import static Abejide.AbejideThread.spriteSheetBaseDireita;
import static Abejide.AbejideThread.spriteSheetAtaqueFracoDireita;
import static Abejide.AbejideThread.spriteSheetBaseEsquerda;
import static Abejide.AbejideThread.spriteSheetAtaqueFracoEsquerda;
import static Abejide.AbejideThread.spriteSheetVenenoDireita;
import static Abejide.AbejideThread.spriteSheetVenenoEsquerda;
import static Abejide.AbejideThread.spriteSheetRolandoDireita;
import static Abejide.AbejideThread.spriteSheetRolandoEsquerda;
import static Abejide.AbejideTeclado.teclaEsquerda;
import static Abejide.AbejideTeclado.teclaDireita;
import static Abejide.AbejideTeclado.teclaBaixo;
import static Abejide.AbejideThread.bolaDeFogo;
import static Abejide.AbejideThread.poeira;
import static Abejide.AbejideThread.poeiraX;
import static Abejide.AbejideThread.level;
import static Abejide.AbejideThread.bolaDeFogoEspecial01;
import static Abejide.AbejideThread.bolaDeFogoEspecial01_0;
import static Abejide.AbejideThread.bolaDeFogoEspecial01_1;
import static Abejide.AbejideThread.caixaDeColisao;
import static Abejide.AbejideThread.faixaDeErnegia;
import Abejide.AbejideThread;
import static Abejide.AbejideThread.hp;

import static MetodosPrincipais.Main.janelaDesativa;
import static MetodosPrincipais.Main.ceu;
import static MetodosPrincipais.Main.montanhas;
import static MetodosPrincipais.Main.sol;
import static MetodosPrincipais.Main.lua;
import static MetodosPrincipais.Main.nuvem1;
import static MetodosPrincipais.Main.nuvem2;
import static MetodosPrincipais.Main.janela;
import static MetodosPrincipais.Main.teclado;
import static MetodosPrincipais.DeltaTime.dormir;

import static Olodumare.OlodumareThread.olodumareBarraGrade;
import static Olodumare.OlodumareThread.olodumareBarraHP;
import static Olodumare.OlodumareThread.olodumareFonteTTF;
import static Olodumare.OlodumareThread.olodumareFonteCor;
import static Olodumare.OlodumareThread.olodumareBolaDeFogo;
import static Olodumare.OlodumareThread.olodumareBolaDeFogoCriadas;
import static Olodumare.OlodumareThread.olodumarePentagramaBola;
import static Olodumare.OlodumareThread.olodumarePedraDoChao;
import static Olodumare.OlodumareThread.olodumarePedraGigante;
import static Olodumare.OlodumareThread.hpTemp;
import static Olodumare.OlodumareThread.hpTotal;

import static MetodosPrincipais.Colisao.contadorDeHits;
import static MetodosPrincipais.Colisao.contadorAtaque;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Thread.sleep;


import jplay.Keyboard;
import jplay.Sprite;
import jplay.GameImage;

import jplay.Sound;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class Executando{
    
    public static GameImage finDeJogo = new GameImage("Imagens/Logos/FinDeJogo.png");
    
    public static GameImage chao = new Sprite("Imagens/Cenario/Chao.png");;
    public static Sprite cameraSprite = new Sprite("Imagens/HUD/Camera.png");;
    
    public static GameImage DentroDaCamera = new GameImage("Imagens/HUD/DentroDaCamera.png");
    public static GameImage planoFundo0;
    public static GameImage planoFundo1;
    public static GameImage planoFundo2;
    public static GameImage planoFundo3;
    public static GameImage barraGrade;
    public static GameImage barraLevel;
    public static Sprite barraHP;
    public static Sprite barraMP;
    public static Sprite barraMPcarregando;
    public static Sprite barraSM;
    //Serve para o while esperar até todas as threads carregarem as imagens, porque se não
    //Carregarem da erro
    public static int threadsCarregadas = 0;
    //Loop do jogo
    public static boolean executando = true;
    public static boolean pausado = false;
    
    static Font fonteTTF;
    
    public static void Executando() throws FileNotFoundException, FontFormatException, IOException, InterruptedException {
        Font fonteTTFhit;
        
        GameImage level_Direita = new GameImage("Imagens/HUD/Level_Direita.png");
        GameImage level_Esquerda = new GameImage("Imagens/HUD/Level_Esquerda.png");
        
        level_Direita.height = janela.getHeight();
        level_Direita.width = (janela.getWidth()* 600) / janela.getHeight();
        level_Direita.x = janela.getWidth() / 2;
        level_Direita.x -= level_Direita.width / 2;
        
        level_Esquerda.height = janela.getHeight();
        level_Esquerda.width = (janela.getWidth()* 600) / janela.getHeight();
        level_Esquerda.y = janela.getHeight() / 2;
        level_Esquerda.y -= level_Esquerda.height / 2;
        
        barraLevel = new GameImage("Imagens/HUD/Level.png");
        
        DentroDaCamera.width = janela.getWidth();
        cameraSprite.x = -230;
        
        File pasta = new File("Fontes/fonte.ttf");
        FileInputStream pastaFonte = new FileInputStream(pasta);
        fonteTTF = Font.createFont(Font.TRUETYPE_FONT, pastaFonte).deriveFont(Font.PLAIN, 24);
        
        File pastahit = new File("Fontes/fonte.ttf");
        FileInputStream pastaFontehit = new FileInputStream(pastahit);
        fonteTTFhit = Font.createFont(Font.TRUETYPE_FONT, pastaFontehit).deriveFont(Font.PLAIN, 74);
        //Efeito sonoro de vento
        Sound vento = new Sound("Audios/Vento.wav");
        vento.setRepeat(true);
        vento.setVolume(6f);

        //Carrega as imagens de fundo e seta sua posições se necessario
        planoFundo0 = new GameImage("Imagens/Cenario/planoFundo0.png");
        planoFundo0.y = janela.getHeight() - planoFundo0.height;
        planoFundo1 = new GameImage("Imagens/Cenario/planoFundo1.png");
        planoFundo1.y = janela.getHeight() - planoFundo1.height;
        planoFundo2 = new GameImage("Imagens/Cenario/planoFundo2.png");
        planoFundo2.y = janela.getHeight() - planoFundo2.height;
        planoFundo3 = new GameImage("Imagens/Cenario/planoFundo3.png");
        planoFundo3.y = janela.getHeight() - planoFundo3.height;
        chao.y = janela.getHeight() - chao.height;
        
        //Carrega as imagens do HUD e coloca elas no lugar certo
        barraGrade = new GameImage("Imagens/HUD/BarraGrades.png");
        barraGrade.x = 5;
        barraGrade.y = 5;
        barraHP = new Sprite("Imagens/HUD/BarraHP.png");
        barraHP.x = barraGrade.x + 7;
        barraHP.y = barraGrade.y;
        barraMP = new Sprite("Imagens/HUD/BarraMP.png");
        barraMP.x = barraGrade.x + 26;
        barraMP.y = barraGrade.y;
        barraMPcarregando = new Sprite("Imagens/HUD/BarraMPcarregando.png");
        barraMPcarregando.x = barraGrade.x + 26;
        barraMPcarregando.y = barraGrade.y;
        barraSM = new Sprite("Imagens/HUD/BarraSM.png");
        barraSM.x = barraGrade.x + 26;
        barraSM.y = barraGrade.y;
        barraLevel.y = barraGrade.y + barraGrade.height;
        barraLevel.x = barraGrade.x;
        //Obs: Se for mudar o tamanho da barra, tem que mudar algums valores na classe Abejide,
        //onde é pegado o hp do personagem e colocado na barra, isso vale para o MP e SM
        
        /*
        Inicializa todas as Threads que são:
        O jogador, Abejide protagonista
        */
        
        AbejideThread abejideThread = new AbejideThread();
        abejideThread.start();
        
        Olodumare.OlodumareThread olodumareThread = new Olodumare.OlodumareThread();
        olodumareThread.start();
        
        //Espera todas as threads carregarem pelomenos a imagens
        while(threadsCarregadas < 2){
            System.out.print(""); //Sem isso o programa trava no while, não sei porque
            //Quando a thread carrega a imangen, logo abaixo dela é adicionado um a variavel do while
        }
        
        //Thread da câmera do jogo
        Camera camera = new Camera();
        camera.start();
        
        //Thread que controla a colisão e o dano dela
        Colisao colisao = new Colisao();
        colisao.start();
        
        //Thread que controla o level do Abejide e a dificuldade do jogo
        ControladoDoLevel controladoDoLevel = new ControladoDoLevel();
        controladoDoLevel.start();
        
        //Da play em todos os sons de fundo
        //vento.play();
        
        int i; //Usado em laço de repetição
        
        Thread.sleep(200);
        //Loop do jogo em si
        while(executando){
            
            //Desenha na tela o plano de fundo
            ceu.draw();
            nuvem1.draw();
            sol.draw();
            lua.draw();
            nuvem2.draw();
            montanhas.draw();
            planoFundo3.draw();
            planoFundo2.draw();
            planoFundo1.draw();
            planoFundo0.draw();
            chao.draw();
            
            //Ataque do pentagrama
            if (olodumarePentagramaBola.isAlive()) {
                olodumarePentagramaBola.PrintarPentagrama();
            }
            
            for(i = 0; i < particulaAgua.length; i ++){
                particulaAgua[i].PrintarPoca(cameraSprite.x);
            }
            
            //Printa tudo do Olodumare na tela
            //Olodumare.OlodumareThread.alma.draw();
            Olodumare.OlodumareThread.spriteSheetBaseDireita.draw();
            Olodumare.OlodumareThread.spriteSheetBaseEsquerda.draw();
            
            if (!pausado) {
                Olodumare.OlodumareThread.spriteSheetBaseDireita.update();
                Olodumare.OlodumareThread.spriteSheetBaseEsquerda.update();
            }
            //Printa tudo referente ao Abejide
            //Abejide.AbejideThread.alma.draw();
            //caixaDeColisao.draw();
            spriteSheetBaseDireita.draw();
            spriteSheetAtaqueFracoDireita.draw();
            spriteSheetBaseEsquerda.draw();
            spriteSheetAtaqueFracoEsquerda.draw();
            spriteSheetVenenoDireita.draw();
            spriteSheetVenenoEsquerda.draw();
            spriteSheetRolandoDireita.draw();
            spriteSheetRolandoEsquerda.draw();
            
            //Atualiza as animações dos personagens
            if (!pausado){
                spriteSheetBaseDireita.update();
                spriteSheetAtaqueFracoDireita.update();
                spriteSheetBaseEsquerda.update();
                spriteSheetAtaqueFracoEsquerda.update();
                spriteSheetVenenoDireita.update();
                spriteSheetVenenoEsquerda.update();
                spriteSheetRolandoDireita.update();
                spriteSheetRolandoEsquerda.update();
            }
            
            for(i = 0; i < particulaAgua.length; i ++){
                particulaAgua[i].PrintarAgua(cameraSprite.x);
            }
            
            //Printa a poeira, se ela estiver ativa e o personagem tiver correndo
            if(poeira.getCurrFrame() != poeira.getFinalFrame() - 1 &&
               Abejide.AbejideTeclado.poeiraSolta && poeiraUltimaPosicao != poeiraX){
                poeira.draw();
                poeira.update();
            }
            
            //Printa a bola de fogo, se ela estiver viva, a desisão fica na propria thread
            for(i = 0; i < bolaDeFogo.length; i ++){
                bolaDeFogo[i].PrintarAnimacao();
            }
            
            //Printa a bola de fogo do ataque especial, se ela estiver viva, a desisão fica na
            //propria thread
            for(i = 0; i < bolaDeFogoEspecial01.length; i ++){
                bolaDeFogoEspecial01[i].PrintarAnimacao();
            }
            for(i = 0; i < bolaDeFogoEspecial01_0.length; i ++){
                bolaDeFogoEspecial01_0[i].PrintarAnimacao();
            }
            for(i = 0; i < bolaDeFogoEspecial01_1.length; i ++){
                bolaDeFogoEspecial01_1[i].PrintarAnimacao();
            }
            for(i = 0; i < faixaDeErnegia.length; i ++){
                faixaDeErnegia[i].PrintarAnimacao();
            }
            
            //Printa a bola de fogo do Olodumare se ela estiver sido jogada
            for(i = 0; i < olodumareBolaDeFogoCriadas; i ++){
                if (olodumareBolaDeFogo[i].isAlive()) {
                    olodumareBolaDeFogo[i].PrintarSprite();
                }
            }
            if (olodumarePedraDoChao.isAlive()) {
                olodumarePedraDoChao.PrintarPedra();
            }
            
            if (olodumarePentagramaBola.isAlive()) {
                olodumarePentagramaBola.PrintarBolaDeFogo();
            }
            
            if (olodumarePedraGigante.isAlive()) {
                olodumarePedraGigante.PrintarPedraGigante();
            }
            
            if(ceu.getCurrFrame()!= (ceu.getFinalFrame()) - 1){
                ceu.update();
                montanhas.update();
                nuvem1.update();
                nuvem2.update();
            }
            
            barraLevel.draw();
            barraHP.draw();
            barraMP.draw();
            barraMPcarregando.draw();
            barraSM.draw();
            barraGrade.draw();
            
            //Printa só a primeira barra do olodumare e a que esta atras dela, as outras não são
            //necessarias printas, pois sempre que a primeira barra acaba, o vetor é todo movido
            //para frente e, a primeira barra vai para a ultima posição
            for(i = 1; i >=0 ; i--){
                olodumareBarraHP[i].draw();
            }
            olodumareBarraGrade.draw();
            
            //cameraSprite.draw();
            //DentroDaCamera.draw();
            
            janela.drawText("Lv." + level, (int) barraLevel.x + 102, 122, new Color(0, 0, 0), fonteTTF);
            
            i = (int) ((Olodumare.OlodumareThread.hpTotal +
                        Olodumare.OlodumareThread.hpTemp) / 100);
            janela.drawText(i + "x",((int) olodumareBarraGrade.x) + 27,
                           (int) olodumareBarraGrade.y + 37, olodumareFonteCor, olodumareFonteTTF);
            
            if (contadorDeHits > 0){
                janela.drawText("" + contadorDeHits + "x", 20, (janela.getHeight() / 2), new Color(255, 255, 255), fonteTTFhit);
            }
            //((int) olodumareBarraGrade.x - (9 * String.valueOf(i).length())) -- Alinhado a direita
            //PrintarIAolodumare();
            
            if (pausado) {
                janelaDesativa.draw();
                if ("Esquerda".equals(EsquerdaOuDireitaStatic())) {
                    level_Direita.draw();
                } else {
                    level_Esquerda.draw();
                }
                
            }
            
            if (hp <= 0 || (hpTemp + hpTotal) <= 0) {
                executando = false;
            }
            
            //Atualiza a janela de verdae, se não fizer isso as imagens desenhadas não são
            //mostradas, ou nocaso redenrizadas ja variavel windows
            janela.update();
            
            //Se apertar o <ESC> o programa sai e fecha tudo
            if(teclado.keyDown(KeyEvent.VK_P)){
                pausado = !pausado;
            }
            
            if (teclado.keyDown(Keyboard.ESCAPE_KEY)) {
                executando = false;
            }
            
            try{
                sleep(dormir);
            } catch(InterruptedException ex){
                //Deu ruim
            }
        }
        
        finDeJogo.height = janela.getHeight();
        finDeJogo.width = janela.getWidth();
        
        while (true){
            
            finDeJogo.draw();
            janela.update();
            
            if (teclado.keyDown(Keyboard.ESCAPE_KEY)) {
                janela.exit();
                System.exit(0);
            }
        }
    }
    
    private static void PrintarIAolodumare(){
        janela.drawText("Esquerda: " + teclaEsquerda + " | Direita: " + teclaDireita +
                " | Baixo: " + teclaBaixo + " | fps: " + dormir + " | ", 2, 150, new Color(255, 255, 255), fonteTTF);
        janela.drawText("contadorDeHits: " + contadorDeHits + "x", 300, 300, new Color(255, 255, 255), fonteTTF);
            
            
        int y = 170;
        janela.drawText("ataqueFraco: " + Olodumare.OqueAbejideEstaFazendo.ataqueFraco , 10, y, new Color(255, 255, 255), fonteTTF);
        y += 30;
        janela.drawText("ataqueEspecialFraco: " + Olodumare.OqueAbejideEstaFazendo.ataqueEspecialFraco +
                        " - qualAtaqueEspecialFraco: " + Olodumare.OqueAbejideEstaFazendo.qualAtaqueEspecialFraco  , 10, y, new Color(255, 255, 255), fonteTTF);
        y += 30;
        janela.drawText("ataqueEspecial: " + Olodumare.OqueAbejideEstaFazendo.ataqueEspecial +
                        " - qualAtaqueEspecial: " + Olodumare.OqueAbejideEstaFazendo.qualAtaqueEspecial  , 10, y, new Color(255, 0, 0), fonteTTF);
        y += 30;
        janela.drawText("esquivando: " + Olodumare.OqueAbejideEstaFazendo.esquivando , 10, y, new Color(255, 0, 0), fonteTTF);
        y += 30;
        janela.drawText("pulando: " + Olodumare.OqueAbejideEstaFazendo.pulando , 10, y, new Color(255, 255, 255), fonteTTF);
        y += 30;
        janela.drawText("seMovendo: " + Olodumare.OqueAbejideEstaFazendo.seMovendo , 10, y, new Color(255, 255, 255), fonteTTF);
        y += 30;
        janela.drawText("correndo: " + Olodumare.OqueAbejideEstaFazendo.correndo , 10, y, new Color(255, 255, 255), fonteTTF);
        y += 30;
        janela.drawText("distancia: " + Olodumare.OqueAbejideEstaFazendo.distancia , 10, y, new Color(255, 255, 255), fonteTTF);
        y += 30;
        janela.drawText("(" + contadorAtaque[0] + ")" + "(" + contadorAtaque[1] + ")" +
                        "(" + contadorAtaque[2] + ")" + "(" + contadorAtaque[3] + ")" , 10, y, new Color(255, 255, 255), fonteTTF);
        
        
        y = 210;
        janela.drawText("qualThreadAtaque: " + Olodumare.OlodumareThread.qualThreadAtaque , 500, y, new Color(255, 255, 255), fonteTTF);
        y += 30;
        janela.drawText("estaThreadIA01: " + Olodumare.OlodumareThread.estaThreadIA01 , 500, y, new Color(255, 255, 255), fonteTTF);
        y += 30;
        janela.drawText("estaThreadIA02: " + Olodumare.OlodumareThread.estaThreadIA02 , 500, y, new Color(255, 255, 255), fonteTTF);
        y += 30;
        janela.drawText("estaBolaDeFogo: " + Olodumare.OlodumareThread.estaBolaDeFogo , 500, y, new Color(255, 255, 255), fonteTTF);
        y += 30;
        janela.drawText("estaPentagramaBola: " + Olodumare.OlodumareThread.estaPentagramaBola , 500, y, new Color(255, 255, 255), fonteTTF);
        y += 30;
        janela.drawText("estaPedraDoChao: " + Olodumare.OlodumareThread.estaPedraDoChao , 500, y, new Color(255, 255, 255), fonteTTF);
        y += 30;
        janela.drawText("estaPedraGigante: " + Olodumare.OlodumareThread.estaPedraGigante , 500, y, new Color(255, 255, 255), fonteTTF);
        y += 30;
    }
    
    public static String EsquerdaOuDireitaStatic(){
        if(AbejideThread.alma.x + (AbejideThread.alma.width / 2) > Olodumare.OlodumareThread.alma.x + (Olodumare.OlodumareThread.alma.width / 2)){
            return "Direita";
        } else{
            return "Esquerda";
        }
    }
}

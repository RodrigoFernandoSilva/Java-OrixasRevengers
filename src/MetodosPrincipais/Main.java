/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MetodosPrincipais;

import static MetodosPrincipais.PlanoDeFundo.tempoTransicao;

import static jplay.InputBase.DETECT_INITIAL_PRESS_ONLY;
import static jplay.InputBase.DETECT_EVERY_PRESS;

import java.awt.FontFormatException;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;
import jplay.Animation;
import jplay.GameImage;

import jplay.Window;
import jplay.Keyboard;
import jplay.Mouse;
import jplay.Sound;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class Main {
    public static Animation ceu;
    public static Animation montanhas;
    public static Animation nuvem1;
    public static Animation nuvem2;
    public static GameImage sol;
    public static GameImage lua;
    public static GameImage janelaDesativa = new GameImage("Imagens/Controles/JanelaDesativa.png");
    public static final int FPS_MAXIMO = 60;
    public static final int TEMPO_OTIMO = 1000000000 / FPS_MAXIMO;
    public static final int DORMIR = 10;
    public static final DecimalFormat MASCARA_DECIMAL = new DecimalFormat("0.0000");
    public static Window janela;
    public static Mouse mouse;
    public static Keyboard teclado;
    
    public static int navegavel = 0;
    
    public static void main(String[] args) throws FontFormatException, IOException, FileNotFoundException, InterruptedException {
        GameImage telaInicial = new GameImage("Imagens/TelaInicial.png");
        boolean dimencaoMenor = false;
        janela = new Window(400, 400);
        mouse = janela.getMouse();
        teclado = janela.getKeyboard();
        teclado.addKey(KeyEvent.VK_1);
        teclado.addKey(KeyEvent.VK_2);
        GameImage _800x600 = new GameImage("Imagens/Menu/800x600.png");
        _800x600.x = janela.getWidth() / 2;
        _800x600.x -= (_800x600.width / 2) + 20;
        _800x600.y = 200;
        GameImage _1280X720 = new GameImage("Imagens/Menu/1280x720.png");
        _1280X720.x = janela.getWidth() / 2;
        _1280X720.x -= (_1280X720.width / 2) + 20;
        _1280X720.y = 300;
        
        LogoAnimado logoAnimado = new LogoAnimado();
        logoAnimado.start();
        
        while(true) {
            telaInicial.draw();
            _800x600.draw();
            _1280X720.draw();
            
            janela.update();
            
            if (teclado.keyDown(Keyboard.ESCAPE_KEY)) {
                janela.exit();
                System.exit(0);
            } else if (teclado.keyDown(KeyEvent.VK_1)) {
                dimencaoMenor = true;
                break;
            } else if (teclado.keyDown(KeyEvent.VK_2)) {
                break;
            }
            
            if (mouse.isLeftButtonPressed()) {
                if (mouse.isOverObject(_800x600)) {
                    dimencaoMenor = true;
                    break;
                } else if (mouse.isOverObject(_1280X720)) {
                    break;
                }
            }
            
            Thread.sleep(DORMIR);
            
            if (1 == 2) {
                break;
            }
        }
        
        //Carrega as imagens do ceu
        ceu = new Animation("Imagens/Cenario/PlanoDeFundo/Ceu.png", 12);
        montanhas = new Animation("Imagens/Cenario/PlanoDeFundo/Montanhas.png", 12);
        nuvem1 = new Animation("Imagens/Cenario/PlanoDeFundo/Nuvem1.png", 12);
        nuvem2 = new Animation("Imagens/Cenario/PlanoDeFundo/Nuvem2.png", 12);
        sol = new GameImage("Imagens/Cenario/PlanoDeFundo/Sol.png");
        lua = new GameImage("Imagens/Cenario/PlanoDeFundo/Lua.png");
        ceu.setSequenceTime(6, 11, true, tempoTransicao);
        montanhas.setSequenceTime(6, 11, true, tempoTransicao);
        nuvem1.setSequenceTime(6, 11, true, tempoTransicao);
        nuvem2.setSequenceTime(6, 11, true, tempoTransicao);
        sol.x = -sol.width;
        lua.x = -lua.width ;
        PlanoDeFundo planoDeFundo = new PlanoDeFundo();
        planoDeFundo.start();
        
        DeltaTime deltatime = new DeltaTime();
        deltatime.start();
        janela = new Window(800, 600);        
        mouse = janela.getMouse();
        
        if (!dimencaoMenor)
            janela.setSize(1280, 720);
        
        janela.setFullScreen();
        teclado = janela.getKeyboard(); //Le o teclado da janela criada
        //Adiciona as telcas na variável teclado, para que elas sejam reconhecias
        teclado.addKey(KeyEvent.VK_LEFT, DETECT_EVERY_PRESS);
        teclado.addKey(KeyEvent.VK_RIGHT, DETECT_EVERY_PRESS);
        teclado.addKey(KeyEvent.VK_Z, DETECT_EVERY_PRESS);
        teclado.addKey(KeyEvent.VK_X, DETECT_EVERY_PRESS);
        teclado.addKey(KeyEvent.VK_D, DETECT_EVERY_PRESS);
        teclado.addKey(KeyEvent.VK_A, DETECT_EVERY_PRESS);
        teclado.addKey(KeyEvent.VK_P, DETECT_INITIAL_PRESS_ONLY);
        
        ceu.y = janela.getHeight() - ceu.height;
        montanhas.y = janela.getHeight() - montanhas.height;
        nuvem1.y = janela.getHeight() - nuvem1.height;
        nuvem2.y = janela.getHeight() - nuvem2.height;
        
        navegavel = 0;
        
        //Musica
        Sound musica = new Sound("Audios/Musica.wav");
        musica.setRepeat(true);
        musica.setVolume(-6f);
        musica.play();
        
        janelaDesativa.height = janela.getHeight();
        janelaDesativa.width = janela.getWidth();
        
        //Tira isso depois seu burro!!!
        //Executando.Executando();
        while(true){
            switch(navegavel){
                case 0:
                    teclado.addKey(KeyEvent.VK_UP, DETECT_INITIAL_PRESS_ONLY);
                    teclado.addKey(KeyEvent.VK_DOWN, DETECT_INITIAL_PRESS_ONLY);
                    Menu.Menu();
                break;
                case 2:
                    Controles.Controles();
                    break;
                case 1:
                    MetodosPrincipais.PlanoDeFundo.menu = false;
                    teclado.addKey(KeyEvent.VK_UP, DETECT_EVERY_PRESS);
                    teclado.addKey(KeyEvent.VK_DOWN, DETECT_EVERY_PRESS);
                    Executando.Executando();
                break;
            }
        }
    }
}

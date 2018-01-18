/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MetodosPrincipais;

import static Abejide.AbejideThread.level;
import static Olodumare.OlodumareThread.hpTotal;
import static Olodumare.OlodumareThread.hpTemp;

import static MetodosPrincipais.Executando.executando;
import static MetodosPrincipais.DeltaTime.dormir;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rodrigo Fernando da Silva
 */
public class ControladoDoLevel extends Thread {
    
    public static boolean podeAtaque3_3 = false; //Bola de fogo lado
    public static boolean podeAtaque3_4 = false; //Bola de fogo cima e lado
    public static boolean podeAtaque3_5 = false; //Bola de fogo cima, lado, rodando
    public static boolean podeAtaque4 = false; //Pentagrama
    public static boolean podeAtaque5 = false; //Sobe pedra do chão
    public static boolean podeAtaque6   = false; //Linha de bola de fogo encima do abejode
    public static boolean podeAtaque7   = false; //Pedra gigante
    public static int chanceAndarAtacar = 70; //É em porcentagem, pq sorteia um random até 100, quanto menor mais chance de atacar
    public static int bolaDefogoPrecisao = 200;
    public static double velocidadeBolaDefogo = 0; //Soma da vecolidade delas, quanto maior mais rapido
    public static int tempoIniciarPentagrama = 0; //Subtrai do tempo que esta lá
    public static int numeroDePedras = 5; //Quantas pedras vai ser criada no ataque de pedras que sobem do chão
    public static int pedrasDoChaoTempo = 0; //Subtrai do tempo que esta lá
    public static int pedrasDoChaoTempoCriarNova = 13; //É feito uma regra de três com base na variável acima
    public static int distanciaPedraGigante = 0; //Soma a distancia que esta lá, quanto maior mais longe a pedra gigante vai, é um temporizado que é somado a todo tempo
    public static int pedraGiganteVelocidade = 0; //Soma para dar uma velocidade maior
    public static double danoAtaqueOlodumare = 0; //Quanto mais de ataque Olodumare vai dar no Abejide
    public static int tempoParado = 0; //Subtrai do tempo que vai ficar parado
    public static double abejideRecuperaHP = 0.0;
    public static double abejideRecuperaSM = 0.0;
    public static double abejideGanhaMP = 0.0;
    public static double abejideCarregaMP = 0.0;
    
    double olodumareHP;
    int ultimoLevel = 0;
    
    @Override
    public void run () {
        
        while (executando) {
            
            olodumareHP = (hpTotal + hpTemp) / 100;
            
            ControleLevel();
            
            if (ultimoLevel != level) {
                ControlaDificuldade();
            }
            
            try {
                sleep(dormir);
            } catch (InterruptedException ex) {
                Logger.getLogger(ControladoDoLevel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void ControleLevel() {
        if (olodumareHP <= 98 && level == 0) {
            level = 1;
        }
        if (olodumareHP <= 91 && level == 1) {
            level = 2;
        }
        if (olodumareHP <= 83 && level == 2) {
            level = 3;
        }
        if (olodumareHP <= 77 && level == 3) {
            level = 4;
        }
        if (olodumareHP <= 69 && level == 4) {
            level = 5;
        }
        if (olodumareHP <= 62 && level == 5) {
            level = 6;
        }
        if (olodumareHP <= 56 && level == 6) {
            level = 7;
        }
        if (olodumareHP <= 49 && level == 7) {
            level = 8;
        }
        if (olodumareHP <= 42 && level == 8) {
            level = 9;
        }
        if (olodumareHP <= 39 && level == 9) {
            level = 10;
        }
        if (olodumareHP <= 30 && level == 10) {
            level = 11;
        }
        if (olodumareHP <= 23 && level == 11) {
            level = 12;
        }
        if (olodumareHP <= 15 && level == 12) {
            level = 13;
        }
    }
    
    private void ControlaDificuldade() {
        switch (level) {
            case 1:
                podeAtaque3_3 = true;
                velocidadeBolaDefogo = 0.5;
                bolaDefogoPrecisao = 170;
                danoAtaqueOlodumare = 1;
                tempoParado = 8;
                break;
            case 2:
                podeAtaque3_4 = true;
                chanceAndarAtacar = 60;
                abejideGanhaMP = 0.1;
                abejideRecuperaHP = 0.01f;
                abejideRecuperaSM = 0.01f;
                abejideCarregaMP = 0.01f;
                break;
            case 3:
                podeAtaque3_4 = true;
                bolaDefogoPrecisao = 140;
                tempoParado = 15;
                break;
            case 4:
                podeAtaque3_5 = true;
                velocidadeBolaDefogo = 1;
                danoAtaqueOlodumare = 2;
                tempoParado = 24;
                abejideGanhaMP = 0.3;
                abejideRecuperaHP = 0.015f;
                abejideRecuperaSM = 0.02f;
                abejideCarregaMP = 0.015f;
                break;
            case 5:
                podeAtaque4 = true;
                bolaDefogoPrecisao = 100;
                tempoParado = 30;
                break;
            case 6:
                podeAtaque5 = true;
                chanceAndarAtacar = 55;
                bolaDefogoPrecisao = 90;
                tempoIniciarPentagrama = 10;
                tempoParado = 39;
                abejideGanhaMP = 0.5;
                abejideRecuperaHP = 0.018f;
                abejideRecuperaSM = 0.04f;
                abejideCarregaMP = 0.018f;
                break;
            case 7:
                podeAtaque7 = true;
                velocidadeBolaDefogo = 1.5;
                pedrasDoChaoTempo = 100;
                pedrasDoChaoTempoCriarNova = ((1500 - pedrasDoChaoTempo) * 13) / 1500;
                distanciaPedraGigante = 25;
                tempoIniciarPentagrama = 30;
                danoAtaqueOlodumare = 3;
                tempoParado = 47;
                break;
            case 8:
                bolaDefogoPrecisao = 50;
                distanciaPedraGigante = 50;
                pedraGiganteVelocidade = 10;
                tempoIniciarPentagrama = 50;
                tempoParado = 59;
                break;
            case 9:
                chanceAndarAtacar = 50;
                numeroDePedras = 6;
                pedrasDoChaoTempo = 200;
                pedrasDoChaoTempoCriarNova = ((1500 - pedrasDoChaoTempo) * 13) / 1500;
                distanciaPedraGigante = 80;
                tempoIniciarPentagrama = 70;
                danoAtaqueOlodumare = 4;
                tempoParado = 68;
                abejideGanhaMP = 0.6;
                abejideRecuperaHP = 0.01f;
                abejideRecuperaSM = 0.06f;
                abejideCarregaMP = 0.02f;
                break;
            case 10:
                bolaDefogoPrecisao = 45;
                pedrasDoChaoTempo = 300;
                pedrasDoChaoTempoCriarNova = ((1500 - pedrasDoChaoTempo) * 13) / 1500;
                distanciaPedraGigante = 120;
                pedraGiganteVelocidade = 25;
                tempoIniciarPentagrama = 90;
                danoAtaqueOlodumare = 5;
                tempoParado = 78;
                break;
            case 11:
                velocidadeBolaDefogo = 2;
                bolaDefogoPrecisao = 30;
                numeroDePedras = 7;
                pedrasDoChaoTempo = 380;
                pedrasDoChaoTempoCriarNova = ((1500 - pedrasDoChaoTempo) * 13) / 1500;
                pedraGiganteVelocidade = 40;
                distanciaPedraGigante = 150;
                tempoIniciarPentagrama = 110;
                tempoParado = 86;
                abejideGanhaMP = 0.9;
                abejideRecuperaHP = 0.02F;
                abejideRecuperaSM = 0.08f;
                abejideCarregaMP = 0.03f;
                break;
            case 12:
                chanceAndarAtacar = 45;
                bolaDefogoPrecisao = 20;
                podeAtaque6 = true;
                pedrasDoChaoTempo = 500;
                pedrasDoChaoTempoCriarNova = ((1500 - pedrasDoChaoTempo) * 13) / 1500;
                distanciaPedraGigante = 180;
                tempoIniciarPentagrama = 130;
                danoAtaqueOlodumare = 6;
                tempoParado = 92;
            case 13:
                velocidadeBolaDefogo = 2.5;
                bolaDefogoPrecisao = 10;
                distanciaPedraGigante = 200;
                tempoIniciarPentagrama = 150;
                danoAtaqueOlodumare = 7;
                tempoParado = 100;
                abejideGanhaMP = 1;
                abejideRecuperaHP = 0.03F;
                abejideRecuperaSM = 0.07f;
                abejideCarregaMP = 0.04f;
                break;
            default:
                break;
        }
        
        ultimoLevel = level;
    }
}

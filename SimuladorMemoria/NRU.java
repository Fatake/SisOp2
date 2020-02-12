package nru;
import java.util.Random;
/*
 * ALGORITMO DE REEMPLAZO NRU
 * Este algoritmo se guía con los bits de referencia 
 * y modificacion.
 * Entrada;N
 * Salida: 
 * Inicio---
 * Todas la paginas no estan referenciadas ni modificadas
 * Se elige una pagina de acuerdo a un tiempo X (Utilizar Random)
 * Se accede a la pagina
 * Se modifica a 1 el bit de referencia o el de modificación 
 * El SO apaga todos los bits de referencia para saber cuales han sido ocupados
 * Se vuelve a hacer uso del avantum de tiempo para solicitar una nueva pagina
 * En caso de que la pagina no esté presente provocara un fallo de pagina (page fault)
 * Se aplica el algoritmo NRU utilizandi las 4 categorias
 * Se desaloja una página de la categoría más baja 
 * (una buena opción son las páginas que estan ubicadas en la categoria 0)
 */
public class NRU {

    public static void replacePage ( Vector mem , int virtPageNum , int replacePageNum , ControlPanel controlPanel ) {
        /* NRU algoritmo */
        int numeroPaginas = mem.size();//Cantidad de paginas
        int oldestPage = 0 ;
        boolean eliminada = false;
        for (int i=0; i<numeroPaginas ;i++){//Para cada pagina
            /*busca una pagina segun el algoritmo
            que cumpla con la tabla de prioridades de abajo

            R   M
            0   0  mas prioridad
            0   1
            1   0
            1   1  menos prioridad
            */
        Page pagina = (Page) mem.get(i);
        //Busca Para cada Pagina
        if( pagina.R == 0 && pagina.M == 0){//Prioridad mas alta
            oldestPage = i;
            break;
        }
        if( pagina.R == 0 && pagina.M == 1){
            oldestPage = i;
            break;
        }
        if( pagina.R == 1 && pagina.M == 0){
            oldestPage = i;
            break;
        }
        if( pagina.R == 1 && pagina.M == 1){//Prioridad mas Baja
            oldestPage = i;
            break;
        }
        }

        Page page = ( Page ) mem.elementAt( replacePageNum );
        Page nextpage = ( Page ) mem.elementAt( oldestPage );

        controlPanel.removePhysicalPage( replacePageNum );
        System.out.println("Cambiando a: "+oldestPage);
        page.physical = nextpage.physical; 

        controlPanel.addPhysicalPage( nextpage.physical , replacePageNum );

        page.inMemTime = 0;
        page.lastTouchTime = 0;
        page.R = 0;
        page.M = 1;
        page.physical = -1;
    }
    
}

/* 
    Está en este archivo, específicamente la función replacePage
    a la que llamará MemoryManagement cuando haya un error de página.
    Los usuarios de este programa deben reescribir PageFault para 
    implementar el algoritmo de reemplazo de página.
*/

  // This PageFault file is an example of the FIFO Page Replacement 
  // Algorithm as described in the Memory Management section.

import java.util.*;


public class PageFault {//Algoritmo de Reemplazo de pagina 
  /**
    * Se llama a este método cada vez que
    * se necesita reemplazar una página.
    * <p>
    * 
    * El algoritmo de reemplazo de página incluido
    * con el simulador es FIFO (primero en entrar, primero en salir).
    * Se debe usar un bucle while o for para buscar en los
    * contenidos de la memoria actual una página de reemplazo de candidato.
    * En el caso de FIFO, el bucle while se usa para encontrar
    * la página adecuada y al mismo tiempo asegurarse
    * de que no se excede virtPageNum.
    * <pre>
    *   Página de la página = (Página) mem.elementAt (más antiguaPágina)
    * </pre>
    * 
    * Esta línea trae el contenido de la página en la página
    * más antigua (un entero especificado) del vector mem al objeto de la página.
    * A continuación, recupere el contenido de la página de destino,
    * reemplace PageNum.
    * Establezca la dirección de memoria física de la página
    * que se agregará igual a la página que se eliminará.
    * <pre>
    *   controlPanel.removePhysicalPage (más antiguoPágina)
    * </pre>
    * 
    * Una vez que una página se elimina de la memoria,
    * también debe reflejarse gráficamente. Esta línea lo hace eliminando
    * la página física en el valor de página más antiguo.
    * La página que se agregará a la memoria también debe mostrarse a través de
    * la llamada a la función addPhysicalPage.
    * También se debe recordar restablecer los valores
    * de la página que acaba de eliminarse de la memoria.
    * 
    * @param mem es el vector que contiene el contenido
    * de las páginas en la memoria que se simula. Se debe buscar en
    * mem para encontrar la página adecuada 
    * para eliminar y modificar para reflejar cualquier cambio.
    *   
    * @param virtPageNum es el número de páginas
    * virtuales en el simulador (establecido en Kernel.java).
    *   
    * @param replacePageNum es la página
    * solicitada que causó la falla de la página.
    * 
    * @param controlPanel representa el elemento
    * gráfico del simulador y permite modificar la visualización actual.
   */
  public static void replacePage ( Vector mem , int virtPageNum , int replacePageNum , ControlPanel controlPanel ) 
  {
    int count = 0;
    int oldestPage = -1;
    int oldestTime = 0;
    int firstPage = -1;
    int map_count = 0;
    boolean mapped = false;

    while ( ! (mapped) || count != virtPageNum ) {//Algoritmo FIFO
      Page page = ( Page ) mem.elementAt( count );
      if ( page.physical != -1 ) {
        if (firstPage == -1) {
          firstPage = count;
        }
        if (page.inMemTime > oldestTime) {
          oldestTime = page.inMemTime;
          oldestPage = count;
          mapped = true;
        }
      }
      count++;
      if ( count == virtPageNum ) {
        mapped = true;
      }
    }
    if (oldestPage == -1) {
      oldestPage = firstPage;
    }
    Page page = ( Page ) mem.elementAt( oldestPage );
    Page nextpage = ( Page ) mem.elementAt( replacePageNum );
    controlPanel.removePhysicalPage( oldestPage );
    nextpage.physical = page.physical;
    controlPanel.addPhysicalPage( nextpage.physical , replacePageNum );
    page.inMemTime = 0;
    page.lastTouchTime = 0;
    page.R = 0;
    page.M = 0;
    page.physical = -1;
  }
}

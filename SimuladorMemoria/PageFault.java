import java.util.*;
/**
 * Cuando hay error en la pagina 
 */
public class PageFault {
  public static void replacePage ( Vector mem , int virtPageNum , int replacePageNum , ControlPanel controlPanel ) 
  {
    int count = 0;
    int oldestPage = -1;
    int oldestTime = 0;
    int firstPage = -1;
    int map_count = 0;
    boolean encontrado = false;

    //Mientras no se a encontrado
    while ( ! (encontrado) || count != virtPageNum ) {
      Page page = ( Page ) mem.elementAt( count );
      if ( page.physical != -1 ) {//Si es pagina virtual
        if (firstPage == -1) {
          firstPage = count;
        }
        if( page.R == 0 && page.M == 0){//Prioridad mas alta
          oldestPage = count;
          encontrado = true;
        } else if( page.R == 0 && page.M == 1){
          oldestPage = count;
          encontrado = true;
        }
      }
      count++;
      if ( count == virtPageNum ) {
        encontrado = true;
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

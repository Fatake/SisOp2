import java.lang.Thread;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class Kernel extends Thread{
  // The number of virtual pages must be fixed at 63 due to
  // dependencies in the GUI
  private static int virtPageNum = 63;
  int cont=0;
  public ArrayList<String> Resultados = new ArrayList<String>();//ARRAY PARA INGRESAR LOS RESULTADOS DE SEGMENTACION
  private String output = null;
  private static final String lineSeparator = System.getProperty("line.separator");
  private String command_file;
  private String config_file;
  public ControlPanel controlPanel ;
  static private Vector memVector = new Vector();
  static private Vector instructVector = new Vector();
  private boolean doStdoutLog = false;
  private boolean doFileLog = false;
  public int runs;
  public int runcycles;
  static public long block = (int) Math.pow(2,12);
  //public long block1 = (int) Math.pow(2,14);////
  public static byte addressradix = 10;
  ///
  static public long seg1 = 49151;//Valor decimal del limite de pagina 0-2
  static public long seg2=147455;//3-8
  static public long seg3=262143;//9-15
  static public long seg4=311295;//16-18 
  static public long seg5=524287;//19-31
  //////

  public void init( String commands , String config )  {
    File f = new File( commands );
    command_file = commands; 
    config_file = config;
    String line;
    String tmp = null;
    String command = "";
    byte R = 0;
    byte M = 0;
    int i = 0;
    int j = 0;
    int id = 0;
    int physical = 0;
    int physical_count = 0;
    int inMemTime = 0;
    int lastTouchTime = 0;
    int map_count = 0;
    double power = 14;
    long high = 0;
    long low = 0;
    long addr = 0;
    long pagina = 0;
    long address_limit = (block * virtPageNum+1)-1;
  
  
    if ( config != null )
    {
      f = new File ( config );
      try 
      {
        DataInputStream in = new DataInputStream(new FileInputStream(f));
        while ((line = in.readLine()) != null) 
        {
          if (line.startsWith("numpages")) 
          { 
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) 
            {
              tmp = st.nextToken();
              virtPageNum = Common.s2i(st.nextToken()) - 1;
              if ( virtPageNum < 2 || virtPageNum > 63 )
              {
                System.out.println("MemoryManagement: numpages out of bounds.");
                System.exit(-1);
              }
              address_limit = (block * virtPageNum+1)-1;
            }
          }
        }
        in.close();
      } catch (IOException e) { /* Handle exceptions */ }
      for (i = 0; i <= virtPageNum; i++) 
      {
        high = (block * (i + 1))-1;
        low = block * i;
        memVector.addElement(new Page(i, -1, R, M, 0, 0, high, low));
      }
      try 
      {
        DataInputStream in = new DataInputStream(new FileInputStream(f));
        while ((line = in.readLine()) != null) 

        {
          if (line.startsWith("memset")) 
          { 
            StringTokenizer st = new StringTokenizer(line);
            st.nextToken();
            while (st.hasMoreTokens()) 
            { 
              id = Common.s2i(st.nextToken());
              tmp = st.nextToken();
              if (tmp.startsWith("x")) 
              {
                physical = -1;
              } 
              else 
              {
                physical = Common.s2i(tmp);
              }
              if ((0 > id || id > virtPageNum) || (-1 > physical || physical > ((virtPageNum - 1) / 2)))
              {
                System.out.println("MemoryManagement: Invalid page value in " + config);
                System.exit(-1);
              }
              R = Common.s2b(st.nextToken());
              if (R < 0 || R > 1)
              {
                System.out.println("MemoryManagement: Invalid R value in " + config);
                System.exit(-1);
              }
              M = Common.s2b(st.nextToken());
              if (M < 0 || M > 1)
              {
                 System.out.println("MemoryManagement: Invalid M value in " + config);
                 System.exit(-1);
              }
              inMemTime = Common.s2i(st.nextToken());
              if (inMemTime < 0)
              {
                System.out.println("MemoryManagement: Invalid inMemTime in " + config);
                System.exit(-1);
              }
              lastTouchTime = Common.s2i(st.nextToken());
              if (lastTouchTime < 0)
              {
                System.out.println("MemoryManagement: Invalid lastTouchTime in " + config);
                System.exit(-1);
              }
              Page page = (Page) memVector.elementAt(id);
              page.physical = physical;
              page.R = R;
              page.M = M;
              page.inMemTime = inMemTime;
              page.lastTouchTime = lastTouchTime;
            }
          }
          if (line.startsWith("enable_logging")) 
          { 
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) 
            {
              if ( st.nextToken().startsWith( "true" ) )
              {
                doStdoutLog = true;
              }              
            }
          }
          if (line.startsWith("log_file")) 
          { 
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) 
            {
              tmp = st.nextToken();
            }
            if ( tmp.startsWith( "log_file" ) )
            {
              doFileLog = false;
              output = "tracefile";
            }              
            else
            {
              doFileLog = true;
              doStdoutLog = false;
              output = tmp;
            }
          }
          if (line.startsWith("pagesize")) 
          { 
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) 
            {
              tmp = st.nextToken();
              tmp = st.nextToken();
              if ( tmp.startsWith( "power" ) )
              {
                power = (double) Integer.parseInt(st.nextToken());
                block = (int) Math.pow(2,power);
              }
              else
              {
                block = Long.parseLong(tmp,10);             
              }
              address_limit = (block * virtPageNum+1)-1;
            }
            if ( block < 64 || block > Math.pow(2,26))
            {
              System.out.println("MemoryManagement: pagesize is out of bounds");
              System.exit(-1);
            }
            for (i = 0; i <= virtPageNum; i++) 
            {
              Page page = (Page) memVector.elementAt(i);
              page.high = (block * (i + 1))-1;
              page.low = block * i;
            }
          }
          if (line.startsWith("addressradix")) 
          { 
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) 
            {
              tmp = st.nextToken();
              tmp = st.nextToken();
              addressradix = Byte.parseByte(tmp);
              if ( addressradix < 0 || addressradix > 20 )
              {
                System.out.println("MemoryManagement: addressradix out of bounds.");
                System.exit(-1);
              }
            }
          }
        }
        in.close();
      } catch (IOException e) { /* Handle exceptions */ }
    }
    f = new File ( commands );
    try 
    {
      DataInputStream in = new DataInputStream(new FileInputStream(f));
      while ((line = in.readLine()) != null) 
      {
        if (line.startsWith("READ") || line.startsWith("WRITE")) 
        {
          if (line.startsWith("READ")) 
          {
            command = "READ";
          }
          if (line.startsWith("WRITE")) 
          {
            command = "WRITE";
          }
          StringTokenizer st = new StringTokenizer(line);
          tmp = st.nextToken();
          tmp = st.nextToken();
          if (tmp.startsWith("random")) 
          {
            instructVector.addElement(new Instruction(command,Common.randomLong( address_limit )));
          } 
          else 
          { 
            if ( tmp.startsWith( "bin" ) )
            {
              addr = Long.parseLong(st.nextToken(),2);             
            }
            else if ( tmp.startsWith( "oct" ) )
            {
              addr = Long.parseLong(st.nextToken(),8);
            }
            else if ( tmp.startsWith( "hex" ) )
            {
              addr = Long.parseLong(st.nextToken(),16);
              pagina= Long.parseLong(st.nextToken(),16);
              
              if (addr >= 0 && addr <= seg1)
              {
                if(pagina <= seg1){
				this.Resultados.add("S1 " + "P"+ pagina/block);
                //JOptionPane.showMessageDialog(null, "S1 " + "Pagina:    "+ pagina/block);
                instructVector.addElement(new Instruction(command,addr));
                }
                else{
                  this.Resultados.add("Error, la pagina no esta dentro del segmento 1.");
                  //JOptionPane.showMessageDialog(null, "Error, la pagina no esta dentro del segmento 1.");
                }
              }
              else {
                if (addr >= seg1+1 && addr <=seg2)
                {
                  if (pagina <= seg2){
                this.Resultados.add("S2 " + "P"+ pagina/block);
                    //JOptionPane.showMessageDialog(null, "S2 " + "Pagina:    "+ pagina/block);
                    instructVector.addElement(new Instruction(command,addr));
                }
                else{
					this.Resultados.add("Error, la pagina no esta dentro del segmento 2.");
                //  JOptionPane.showMessageDialog(null, "Error, la pagina no esta dentro del segmento 2.");
                }
              }
              else 
              {
                if (addr >= seg2+1 && addr <=seg3)
                {
                  if (pagina <= seg3){
					  this.Resultados.add("S3 " + "P"+ pagina/block);
                    //JOptionPane.showMessageDialog(null, "S3 " + "Pagina:    "+ pagina/block);
                    instructVector.addElement(new Instruction(command,addr));
                }
                else{
                  this.Resultados.add("Error, la pagina no esta dentro del segmento 3.");
                  //JOptionPane.showMessageDialog(null, "Error, la pagina no esta dentro del segmento 3.");
                }
              }
              else
              {
                if(addr >= seg3+1 && addr <=seg4)
                {
                  if(pagina <= seg4){
					  this.Resultados.add("S4 " + "P"+ pagina/block);
                    //JOptionPane.showMessageDialog(null, "S4 " + "Pagina:    "+ pagina/block);
                    instructVector.addElement(new Instruction(command,addr));
                }
                else{
					this.Resultados.add("Error, la pagina no esta dentro del segmento 4.");
                  //JOptionPane.showMessageDialog(null, "Error, la pagina no esta dentro del segmento 4.");
                }
              }
              else
              {
                if (addr >= seg4+1 && addr <=seg5)
                {
                  if (pagina <= seg5){
					  this.Resultados.add("S5 " + "P"+ pagina/block);
                    //JOptionPane.showMessageDialog(null, "S5 " + "Pagina:    "+ pagina/block);
                    instructVector.addElement(new Instruction(command,addr));
                }
                else{
					this.Resultados.add("Error, la pagina no esta dentro del segmento 5.");
                  //JOptionPane.showMessageDialog(null, "Error, la pagina no esta dentro del segmento 5.");
                }
              }
                    }
                  }
                }
              }
            }
            else
            {
              addr = Long.parseLong(tmp);
            }
            if (0 > addr || addr > address_limit)
            {
          
              System.out.println("MemoryManagement: " + addr + ", Address out of range in " + commands);
              System.exit(-1);
            }
            //////////////////
            if (0 > pagina || pagina > address_limit)
            {
              System.out.println("MemoryManagement: " + pagina + ", Address out of range in " + commands);
              System.exit(-1);
            }

            /////////////////////
            //instructVector.addElement(new Instruction(command,addr));
          } 
        }
      }
      in.close();
    } catch (IOException e) { /* Handle exceptions */ }
    runcycles = instructVector.size();
    if ( runcycles < 1 )
    {
      System.out.println("MemoryManagement: no instructions present for execution.");
      System.exit(-1);
    }
    if ( doFileLog )
    {
      File trace = new File(output);
      trace.delete();
    }
    runs = 0;
    for (i = 0; i < virtPageNum; i++) 
    {
      Page page = (Page) memVector.elementAt(i);
      if ( page.physical != -1 )
      {
        map_count++;
      }
      for (j = 0; j < virtPageNum; j++) 
      {
        Page tmp_page = (Page) memVector.elementAt(j);
        if (tmp_page.physical == page.physical && page.physical >= 0)
        {
          physical_count++;
        }
      }
      if (physical_count > 1)
      {
        System.out.println("MemoryManagement: Duplicate physical page's in " + config);
        System.exit(-1);
      }
      physical_count = 0;
    }
    if ( map_count < ( virtPageNum +1 ) / 2 )
    {
      for (i = 0; i < virtPageNum; i++) 
      {
        Page page = (Page) memVector.elementAt(i);
        if ( page.physical == -1 && map_count < ( virtPageNum + 1 ) / 2 )
        {
          page.physical = i;
          map_count++;
        }
      }
    }
    for (i = 0; i < virtPageNum; i++) 
    {
      Page page = (Page) memVector.elementAt(i);
      if (page.physical == -1) 
      {
        controlPanel.removePhysicalPage( i );
      } 
      else
      {
        controlPanel.addPhysicalPage( i , page.physical );
      }
    }
    for (i = 0; i < instructVector.size(); i++) 
    {
      high = block * virtPageNum;
      Instruction instruct = ( Instruction ) instructVector.elementAt( i );
      if ( instruct.addr < 0 || instruct.addr > high )
      {
        System.out.println("MemoryManagement: Instruction (" + instruct.inst + " " + instruct.addr + ") out of bounds.");
        System.exit(-1);
      }
    }
  } 

  public void setControlPanel(ControlPanel newControlPanel) 
  {
    controlPanel = newControlPanel ;
  }

  public void getPage(int pageNum) 
  {
    Page page = ( Page ) memVector.elementAt( pageNum );
    controlPanel.paintPage( page );
  }

  private void printLogFile(String message)
  {
    String line;
    String temp = "";

    File trace = new File(output);
    if (trace.exists()) 
    {
      try 
      {
        DataInputStream in = new DataInputStream( new FileInputStream( output ) );
        while ((line = in.readLine()) != null) {
          temp = temp + line + lineSeparator;
        }
        in.close();
      }
      catch ( IOException e ) 
      {
        /* Do nothing */
      }
    }
    try 
    {
      PrintStream out = new PrintStream( new FileOutputStream( output ) );
      out.print( temp );
      out.print( message );
      out.close();
    } 
    catch (IOException e) 
    {
      /* Do nothing */ 
    }
  }

  public void run()
  {
    step();
   
    while (runs != runcycles) 
    {
      try 
      {
        Thread.sleep(1000);
      } 
      catch(InterruptedException e) 
      {  
        /* Do nothing */ 
      }
      step();
     
    }  
  }

  public void step(){
    int i = 0;

    Instruction instruct = ( Instruction ) instructVector.elementAt( runs );
    controlPanel.instructionValueLabel.setText( instruct.inst );
    controlPanel.addressValueLabel.setText( Long.toString( instruct.addr , addressradix ) );
    controlPanel.resultados.setText(controlPanel.resultados.getText()+"\n"+Resultados.get(cont));
    cont++;
    getPage( Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) );
    if ( controlPanel.pageFaultValueLabel.getText() == "YES" ) {
      controlPanel.pageFaultValueLabel.setText( "NO" );
    }
    if ( instruct.inst.startsWith( "READ" ) ) {//Si va a leer
      //Genera pafallo de pagina
      Page page = ( Page ) memVector.elementAt( 
        Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) );
      if ( page.physical == -1 ) {
        if ( doFileLog ){
          printLogFile( "READ " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
        }
        if ( doStdoutLog ){
          System.out.println( "READ " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
        }
        PageFault.replacePage( memVector , virtPageNum , Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) , controlPanel );
        controlPanel.resultados.setText("\n Error de Segmento");
        controlPanel.pageFaultValueLabel.setText( "Cambiada" );
      } 
      else {
        page.R = 1;
        page.lastTouchTime = 0;   
        if ( doFileLog )
        {
          printLogFile( "READ " + Long.toString( instruct.addr , addressradix ) + " ... okay" );
        }
        if ( doStdoutLog )
        {
          System.out.println( "READ " + Long.toString( instruct.addr , addressradix ) + " ... okay" );
        }
      }
    }
    if ( instruct.inst.startsWith( "WRITE" ) ) 
    {
      Page page = ( Page ) memVector.elementAt( Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) );
      if ( page.physical == -1 ) 
      {
        if ( doFileLog )
        {
          printLogFile( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
        }
        if ( doStdoutLog )
        {
           System.out.println( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... page fault" );
        }
        PageFault.replacePage( memVector , virtPageNum , Virtual2Physical.pageNum( instruct.addr , virtPageNum , block ) , controlPanel );
        controlPanel.pageFaultValueLabel.setText( "Cambiada" );
        controlPanel.resultados.setText("\n Error de Segmento");
      } 
      else 
      {
        page.M = 1;
        page.lastTouchTime = 0;
        if ( doFileLog )
        {
          printLogFile( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... okay" );
        }
        if ( doStdoutLog )
        {
          System.out.println( "WRITE " + Long.toString(instruct.addr , addressradix) + " ... okay" );
        }
      }
    }
    for ( i = 0; i < virtPageNum; i++ ) 
    {
      Page page = ( Page ) memVector.elementAt( i );
      if ( page.R == 1 && page.lastTouchTime == 250 ) 
      {
        page.R = 0;
      }
      if ( page.physical != -1 ) 
      {
        page.inMemTime = page.inMemTime + 10;
        page.lastTouchTime = page.lastTouchTime + 10;
      }
    }
    runs++;
    controlPanel.timeValueLabel.setText( Integer.toString( runs*10 ) + " (ns)" );
    
  }

  public void reset() {
    memVector.removeAllElements();
    instructVector.removeAllElements();
    controlPanel.resultados.setText("");
    controlPanel.statusValueLabel.setText( "STOP" ) ;
    controlPanel.timeValueLabel.setText( "0" ) ;
    controlPanel.instructionValueLabel.setText( "NONE" ) ;
    controlPanel.addressValueLabel.setText( "NULL" ) ;
    controlPanel.pageFaultValueLabel.setText( "NO" ) ;
    controlPanel.virtualPageValueLabel.setText( "x" ) ;
    controlPanel.physicalPageValueLabel.setText( "0" ) ;
    controlPanel.RValueLabel.setText( "0" ) ;
    controlPanel.MValueLabel.setText( "0" ) ;
    controlPanel.inMemTimeValueLabel.setText( "0" ) ;
    controlPanel.lastTouchTimeValueLabel.setText( "0" ) ;
    controlPanel.lowValueLabel.setText( "0" ) ;
    controlPanel.highValueLabel.setText( "0" ) ;
    init( command_file , config_file );
  }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nru;

public class Page 
{
  public int id;
  public int physical;
  public byte R;
  public byte M;
  public int inMemTime;
  public int lastTouchTime;
  public long high;
  public long low;

  public Page( int id, int physical, byte R, byte M, int inMemTime, int lastTouchTime, long high, long low ) 
  {
    this.id = id;
    this.physical = physical;
    this.R = R;
    this.M = M;
    this.inMemTime = inMemTime;
    this.lastTouchTime = lastTouchTime;
    this.high = high;
    this.low = low;
  } 	

    Page() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

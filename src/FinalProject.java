/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * FinalProject.java  05/07/10
 *
 * @author - Jane Doe
 * @author - Period n
 * @author - Id nnnnnnn
 *
 * @author - I received help from ...
 *
 */

import javax.swing.*;
import java.awt.event.*;

public class FinalProject implements MouseListener,MouseMotionListener
{
	public static JFrame frame;
    public static Display display;
    public static Atom firstAtom; // first atom in bond
   /* public static boolean drawingBond;
    public static int bondX;
    public static int bondY;*/
    //public static BondDrawer drawer;

    public FinalProject(){
    	int totalAtoms, totalPlayers; // Get first two from user

        //drawingBond = false;

       // drawer = new BondDrawer();

    	frame = new JFrame("Unnamed Chemistry Game - by Nick, Nate, and Brendan");
		frame.setSize(950,750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		do{
			totalPlayers = Integer.parseInt(JOptionPane.showInputDialog("Total players"));
		}while(totalPlayers<1);

        do{
        	totalAtoms = Integer.parseInt(JOptionPane.showInputDialog("Total atoms"));
        }while(totalAtoms<2);

        display = new Display(totalPlayers,totalAtoms);

        JScrollPane scroll = new JScrollPane(display);


        frame.add(scroll);
		frame.setVisible(true);
        frame.addMouseListener(this);
         frame.addMouseMotionListener(this);
         display.addMouseMotionListener(this);
         display.addMouseListener(this);


		while(display.canBondVar){

			// Render molecules and current player

            display.redraw();

			frame.repaint();


			// Get user input; require that oxygen is bonded to at least one carbon (so don't have weird O-O-O) or hydrogen

	}
// says player 0 instead of player 2 (currentPlayer=0)
	JOptionPane.showMessageDialog(display,"Player "+((display.currentPlayer+totalPlayers-1) % totalPlayers + 1)+" won!");
	
	// MODIFY TO SUPPORT "When players creates last bond, player is asked to name [randomly 1 or all types] of molecules drawn.
 //if player cannot name, the game continues"

		// Say that currentPlayer won?  At which point do we ask to name molecule?  AHAHA!
		// Ask currentPlayer to name all molecules.  If currentPlayer can't, then add more atoms or break bonds or something, the game continues


    }

   public void mousePressed(MouseEvent e) {
        //JOptionPane.showInputDialog("clicked");
/*
       for(Atom a : display.level){
           if(e.getX() >= a.x && e.getX() <= a.x+30 && e.getY() >= a.y && e.getY() <= a.y+30){
               display.drawingBond = true;
             //  JOptionPane.showInputDialog("success");
               display.bondX0=e.getX();
               display.bondY0=e.getY();
               firstAtom = a;
               break;
           }
          // System.out.println("mx,my,x,y"+e.getX()+","+e.getY()+","+a.x+","+a.y+",");
       }*/

    }

    public void mouseReleased(MouseEvent e) {
       /*display.drawingBond = false;

       for(Atom a : display.level){
           if(e.getX() >= a.x && e.getX() <= a.x+30 && e.getY() >= a.y && e.getX() <= a.y+30){
               display.currentPlayer=(display.currentPlayer+1)%display.totalPlayers; // Only end turn if actually landed on a bond-give users another chance
               if((firstAtom.bondedAtoms).size()<firstAtom.bonds){ // if player is n00b and doesn't know proper bond amount, does not get a second chance; currenyPlayer remains updated
                    firstAtom.makeBond(a); // It is not necessary to make a bond to firstAtom; only firstAtom needs to bond to A?
                    a.makeBond(firstAtom); // well, just in case...
               }
               break;
           }
       }*/
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

     public void mouseDragged(MouseEvent e) {

    }

      public void mouseMoved(MouseEvent e) {
       // System.out.print("moving");
          // is it possible to have mouseMoved while holding down mouse? seemingly not :/
          if(display.drawingBond==true){ // this if condition doesn't really matter...Display class already checks this
            display.bondX1 = e.getX();
            display.bondY1 = e.getY();
           // System.out.println("movvv"+display.bondX1+"+"+display.bondY1);
        }
         // System.out.println("movvv"+display.bondX1+"+"+display.bondY1);

    }

    public void mouseClicked(MouseEvent e) {
         // Only create bond if requirements for both atoms are satified; both makeBonds succesful
         for(Atom a : display.level){
           if(e.getX() >= a.x && e.getX() <= a.x+30 && e.getY() >= a.y && e.getY() <= a.y+30){
              if(display.drawingBond==false){
             //  JOptionPane.showInputDialog("success");
               display.bondX0=e.getX();
               display.bondY0=e.getY();
               firstAtom = a;

              }
              else{

                 //  display.currentPlayer=(display.currentPlayer+1)%display.totalPlayers; // Only end turn if actually landed on a bond-give users another chance
               //if((firstAtom.bondedAtoms).size()<firstAtom.bonds){ // if player is n00b and doesn't know proper bond amount, does not get a second chance; currenyPlayer remains updated
                     // PUT TOO MANY BONDS CHECK IN makeBond!
                    // javax.swing.JOptionPane.showInputDialog("trying to bond");

                    if(firstAtom.validBond(a) && a.validBond(firstAtom) && (!Atom.circularBond(firstAtom,firstAtom,a) || a.repeats(firstAtom)>0)){
                    	// Note that only need to test for circularBonds with one atom
                    //javax.swing.JOptionPane.showInputDialog("bond");
                    display.currentPlayer=(display.currentPlayer+1)%display.totalPlayers;
                    	firstAtom.makeBond(a); // It is not necessary to make a bond to firstAtom; only firstAtom needs to bond to A?
                   	a.makeBond(firstAtom);
                   	display.canBond();
                   	 // well, just in case...DO THIS IN CASE H IS FIRST ATOM, C IS,,,
                    }
                // If don't do this, then when rendering, C is rendered by itself, marked as rendered, 
                //}
              }
              display.drawingBond = !display.drawingBond;
             break;
           }
          // System.out.println("mx,my,x,y"+e.getX()+","+e.getY()+","+a.x+","+a.y+",");
       }

    }
	public static void main(String[] args)
	{
		FinalProject game = new FinalProject();
	}
}
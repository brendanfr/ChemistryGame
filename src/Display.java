/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * @(#)Display.java
 *
 *
 * @author
 * @version 1.00 2010/5/16
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

// This class renders information...and I ended up making it so that it holds esesntial game data like totalAtoms too


public class Display extends JPanel {

        public int bondX0; // x,y coordinates at beginnning of bond
        public int bondY0;
        public int bondX1; // x,y coordinates at beginnning of bond
        public int bondY1;
         boolean canBondVar;

        public boolean drawingBond;

       // public boolean isRoot;

	public int totalPlayers;
	public int currentPlayer;

	public ArrayList<Atom> level;

    public Display(int totalPlayers, int totalAtoms) {
    	drawingBond = false;
    	canBondVar = true;
        this.totalPlayers = totalPlayers;
        currentPlayer = 0;
    	level = new ArrayList<Atom>();
    	if(totalPlayers==9000){
    		// Requested ethanol glitch/feature; the code shows as a good demonstration of how the Atom class and bond formation works
    		// Note that when two atoms are bonded, they aren't added to each other's arraylist like normal
    		Atom c1 = new Atom(4);
    		c1.bondedAtoms.add(new Atom(1));
    		c1.bondedAtoms.add(new Atom(1));
    		c1.bondedAtoms.add(new Atom(1));
    		
    		Atom c2 = new Atom(4);
    		
    		
    		Atom o = new Atom(2);
    		o.bondedAtoms.add(new Atom(1));
    		
    		c2.bondedAtoms.add(o); // Remember; the rendering code tries to draw the first item opposite
    		c2.bondedAtoms.add(new Atom(1));
    		c2.bondedAtoms.add(new Atom(1));
    		
    		c1.bondedAtoms.add(c2);
    		level.add(c1);
    		canBondVar=false;
    	}
    	else
  			generateLevel(totalAtoms);

    	// Test level - test more complex things, 3 or so carbon chains?
    	/*
    	Atom c1 = new Atom(4);

        Atom c2 = new Atom(4);
        (c2.bondedAtoms).add(new Atom(1));
        (c2.bondedAtoms).add(new Atom(1));
        (c2.bondedAtoms).add(c1);

    	(c1.bondedAtoms).add(c2);
    	(c1.bondedAtoms).add(new Atom(1));
    	(c1.bondedAtoms).add(new Atom(1));
        (c1.bondedAtoms).add(new Atom(1));

    	level.add(c1);

        Atom c3 = new Atom(4);
        (c3.bondedAtoms).add(new Atom(1));
        level.add(c3);*/
    }

    public void redraw()
    {

    }

    public void paintComponent(Graphics g){ // @Override
    	Graphics2D g2 = (Graphics2D)g;

    	g2.setFont(new Font("sansserif", Font.BOLD, 24));

        //System.out.println("x0,y0,x1,y1:"+bondX0+","+bondY0+","+bondX1+","+bondY1);
		String test = (drawingBond ? "true" : "false");
		//System.out.println("RENDERING"+test);
		g2.setColor(Color.BLACK);

        if(drawingBond)
            g2.drawLine(bondX0,bondY0,bondX1,bondY1);

         renderMolecules(g2);
    	// Render one molecule per row on a grid (90 degree angles between everything, even O?)

    	// Recursively render molecules


    	// Draw current player #

		g2.drawString("Player "+(currentPlayer+1),700,20);

    }

    public void generateLevel(int totalAtoms) {
    	while(level.size()<totalAtoms){
    		switch(level.size() % 6){
    			case 0: // Start with a carbon
    			case 3:
    				level.add(new Atom(4)); // Carbon
    				break;
    			case 1:
    			case 2:
    			case 5:
    				level.add(new Atom(1)); // Hydrogen
    				break;
    			case 4:
    				level.add(new Atom(2)); // Oxygen
    		}
    	}
    }

    public synchronized void canBond(){
    	// All bonds are used up if and only if there exist two distinct atoms that have bond postions open
    	// Remember 2 Os double bonded to each other and H by itself
    	/*
    	int potentialBonds = 0;
    	for(Atom a : level){
    		if(a.bondedAtoms.size() != a.bonds) potentialBonds++; // Could break loop early as soon as find out that potentialBonds>1, I guess
    	}
    	return potentialBonds > 1;
    	*/
    	// More complicated than above, just do this long thing
	//	synchronized (level){


    	int potentialBonds = 0;
    	for(Atom a : level){
    		for(Atom b : level){
    			if(b.validBond(a) && a.validBond(b) && (!Atom.circularBond(b,b,a) || a.repeats(b)>0)) potentialBonds++; // Could break loop early as soon as find out that potentialBonds>1, I guess
    		}
    	}
    	canBondVar = potentialBonds > 1;
    	//}
		
		// return true;
    }

    public void renderMolecules(Graphics2D g2){
    	// TODO: render longest chain horizontally?
    	// Each atom is 30 x30, separated by bonds of length 100 to avoid (invisible) overlapping weirdness

        int j=0; // Lone atom height
        int k=0; // Bonded atom y

        for(int i=0;i<level.size();i++){
            if(level.get(i).rendered==false){
                //(level.get(i)).rendered = false;
                (level.get(i)).direction = 1; // Set initial direction; east, so goes horizontally right
                if(level.get(i).bondedAtoms.size()==0){ // Draw lone atoms in a certain section
                //	System.out.print("atom");
                	renderAtom(g2,level.get(i),100,50+50*j,true);
                	j++;
                }
                else{
                	//System.out.println("molc");
                	renderAtom(g2,level.get(i),400,150+150*k,true);
                	k++;
                }
            }
        }

		//renderAtom(g2,level.get(0),400,200+150*0);
        // Reset rendering status

        for(Atom a : level){
            a.rendered=false;
        }

    }

    private int findInLevel(Atom a){
    	for(int i=0;i<level.size();i++){
    		if(level.get(i)==a)
    			return i;
    	}
    	return -1;
    }

    //inefficient whatever

    public void renderAtom(Graphics2D g2,Atom a, int x, int y,boolean isRoot){
    	//int amountRendered = 0; // Amount subatoms rendered for current atom; if remains 0 after loop, then no more to draw
    	//boolean renderedSomething = false; // If this remains false after loop, know that no more to draw, render everything backwards

    	// Determine default atom's direction by parent atom's dir; ex if parent dir=east, child should try to do east

        a.x=x;
        a.y=y;

        // Draw C, H, or O letter

        if(a.bonds==4){
        	g2.setColor(Color.BLACK);
        	g2.fillOval(x,y,30,30);
        	g2.setColor(Color.BLUE);
        	g2.drawString("C",x+7,y+24);
        }
        else if(a.bonds==2){
        	g2.setColor(Color.WHITE);
        	g2.fillOval(x,y,30,30);
        	g2.setColor(Color.BLUE);
        	g2.drawString("O",x+7,y+24);
        }
        else{
        	g2.setColor(Color.ORANGE);
        	g2.fillOval(x,y,30,30);
        	g2.setColor(Color.BLUE);
        	g2.drawString("H",x+7,y+24);
        }

	ArrayList<Integer> directions = new ArrayList<Integer>();
	if(isRoot)directions.add(2); // Can only go in opposite direction of parent if root, else drawing on top of molecule
	directions.add(0);
	directions.add(1);
	directions.add(3);

	//int[] directions = {0,1,3,4}; // Directions should try to use, in priority order left to right
	// Relative to thing; this is thing added

    //System.out.print(";" + a.bondedAtoms.size()+","+findInLevel(a));
	a.rendered = true; // Is it necessary to have this line here? If an atom with no parents isn't marked as not rendered...well, depending if call a.makeBond(firstAtom) besides firstAtom.makeBond(a)

	// Does a.rendered=true affect atom stored in bondedAtoms array?
        //System.out.println("x,y"+x+","+y);
//System.out.println(a.direction);
		int bondedAtomsRendered = 0;
    	for(int i=0;i<(a.bondedAtoms).size();i++){
    		//System.out.println("I"+i);
    //			System.out.print("i"+i+"ei");
            if(((a.bondedAtoms).get(i)).rendered == false){

                          // System.out.print(" not rendered");
    			// if i==0 keep default dir, make more efficient
                          // Have this series of if-else-if separate from next cause

                // If 1 molecule, put straight; if 2, put on each side? (what if c and h), if three, fill, try to put carbon straigh

                // Inefficient

				//System.out.print("nr"); // not rendered

				// Note that even though a molecule may not be rendered, i is still incremented.
				// Instead of using i, use amount of molecules rendered within loop
                // IS POSSIBLE TO GET COLOR AT LOCATION ON SCREEN? If something drawn in place, don't do; default is straight
                /*
                if(bondedAtomsRendered==1){
    				if(i==bondedAtomsRendered+1) ((a.bondedAtoms).get(i)).direction = (a.direction+2) % 4;

    				else ((a.bondedAtoms).get(i)).direction = (a.direction+2) % 4; // opposite
                }
    			else if(bondedAtomsRendered==2)
    				((a.bondedAtoms).get(i)).direction = (a.direction+1) % 4;
    			else if(bondedAtomsRendered==3)
    				((a.bondedAtoms).get(i)).direction = (a.direction+3) % 4;
                        else // i==0
                             ((a.bondedAtoms).get(i)).direction = a.direction; // Try to render STRAIGHT carbon trains, carbon is always first in bonded list
*/
				bondedAtomsRendered++;
				// Can't draw on top of parent; WAIT NEW isRoot VAR TAKES CARE OF THIS
				/*
				if(i==bondedAtomsRendered+1){
					System.out.println("PARENT");
					directions.remove(directions.indexOf(new Integer(i-1)));
				}*/

				// Find optimal direction
				((a.bondedAtoms).get(i)).direction=(a.direction+directions.remove(0)) % 4;


				/*
				for(int j=0;j<directions.length;j++){
					if()
				}*/

                       // System.out.print(" dir="+((a.bondedAtoms).get(i)).direction);

                        // This assumes that can manipulate same memory location
                        //((a.bondedAtoms).get(i)).rendered = true; // Since atoms contain references to each other in bondedAtoms array, don't count twice

				g2.setColor(Color.BLACK);

				int r = a.repeats((a.bondedAtoms).get(i));
//System.out.println("r"+r);
    			if(((a.bondedAtoms).get(i)).direction==0){ // north
    			//	g2.drawLine(x+15,y+15,x+15,y-25);
    				for(int j = 1; j<=r; j++){
    					// All of this type casting looks weird, but I think it's the nicest way
    					g2.drawLine((int)(x+15-9+18*(double)j/(double)(r+1)),y+15,(int)(x+15-9+18*(double)j/(double)(r+1)),y-25);
    				}
                            renderAtom(g2,(a.bondedAtoms).get(i),x,y-40,false);
    			}
    			else if(((a.bondedAtoms).get(i)).direction==1){
    				//g2.drawLine(x+15,y+15,x+55,y+15);

    				//javax.swing.JOptionPane.showInputDialog("repeats"+r);
    				for(double j = 1; j<=r; j++){
    					g2.drawLine(x+15,(int)(y+15-9+18*(double)j/(double)(r+1)),x+55,(int)(y+15-9+18*(double)j/(double)(r+1)));

    				}

                            renderAtom(g2,(a.bondedAtoms).get(i),x+40,y,false);
    			}
    			else if(((a.bondedAtoms).get(i)).direction==2){
    				//g2.drawLine(x+15,y+15,x+15,y+55); // (y+15)+40
				for(int j = 1; j<=r; j++){
    					// All of this type casting looks weird, but I think it's the nicest way
    					g2.drawLine((int)(x+15-9+18*(double)j/(double)(r+1)),y+15,(int)(x+15-9+18*(double)j/(double)(r+1)),y+55);
    				}
                            renderAtom(g2,(a.bondedAtoms).get(i),x,y+40,false);
    			}
    			else{
    			//else if(((a.bondedAtoms).get(i)).direction==3){
    				//g2.drawLine(x+15,y+15,x-25,y+15);
				for(double j = 1; j<=r; j++){
    					g2.drawLine(x+15,(int)(y+15-9+18*(double)j/(double)(r+1)),x-25,(int)(y+15-9+18*(double)j/(double)(r+1)));

    				}
                            renderAtom(g2,(a.bondedAtoms).get(i),x-40,y,false);
    			}


    		}
    	}

    	// Render molecules
    	//g2.drawOval(400,300,30,30);
    }

    /*public void renderBonds(Atom a, int i, int xf, int yf, int dir){ // Draws a single, double, or triple-bond between parent atom A and its child at bondedAtoms.get(i)
    	// Bonds length 12 apart (12 div by 2, 3, far enough)
    	int r = a.repeats(i);

    	for(int j = 0; j<r; j++){
    		g2.drawLine(x+15,y+15,xf,yf);
    	}
    }*/

    //public ArrayList<Atom> longestCarbon
}
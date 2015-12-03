/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Atom.java  5/7/2010
 *
 * @author - BFR, superhacker
 * @author - Period n
 * @author - Id nnnnnnn
 *
 * @author - I received help from ...
 *
 */

// Distinguish between isomers?  Standard way to draw
// Since 2D, not an issue

/* TODO
 *
 * getLongestCarbonChain
 *MAke sure rules for generated atoms make sense; don't make people start off with just Os...
 *First is already C?
 *You can't choose any random place to make connections; valid sensical-looking think is rendered
 *at beginning of each turn, then given option where to put stuff within valid locations (can tab through)


If do Nick's interpretation, have weird issues with bonds going across screen and looking weird
Strategy for Nick's more interesting?
rearrange at end; find largest carbon change; etc. or have "rearrange" button to rearrange when user desires
keep track of things on screen, multiple molecules, eg. rearrange H2 and something really complicated,
need know where to draw.  draw from top to bottom, but



naming molecule

generateLevel

different arrangements of atoms for levels; generate levels

If do mine, can always insure have single molecule that makes sense

make abundance of hydrogens

there are multiple ways to come up with a molecule given a set of bonds (ex change subatom bond rendering order, left to right vs right to left)

name each molecule

part of strategy is not to make weird molecules that can't name; knowing the names of more molecules is an advantage

make molecule class?

ISSUES:
#1 Overlapping atoms (atoms fixed in such a way that must be drawn on top of each other), modify Display and bonding routie, know where to click
Have "layers" that use can switch between? Not very user-friendly...
#2 Diagonal bonds.  Have a bunch of atoms, only atoms left without bonds are two Carbons fixed diagonally from each other, can't bond in real life
diagonal bonds are a subset of "circular" bonds. don't allow these bonds
#3 Molecules go off screen
#4 Randomly can't have firstAtom by lone on left, bond to one on right
#5 Multiple-bonds stopped working
#6 Don't use just 90 degree angles
#7 Atoms being rendered on top of each other, when shouldn't actually, even with current rendering design
#8 Atom marked as bonded, not isn't rendered as being bonded to atom that choose; drawn as lone molecule
C-H;C-C (left atom right atom, these are bonds formed) causes problems ONLY WITH NICK'S SORTING CODE
Why is renderAtom accessing lone atoms when drawing MOLECULE!
make carbon-carbon bonds larger than h-h or c-h bonds

RULES:
> No cyclo- compounds, or compounds with multiple "paths" from one atom to another
> No compounds that lead to overlapping molecules
Trace of program:

1. Generate arrangement of atoms
2. User has a turn
2i.

Brendan:
- Render molecule

Nate:

Nick:



---

Arraylist of Atoms, ArrayLst of b onds, bond points to atoms

ONly tab to things that are bondable (bondedAtoms.size()==bonds).

make a grid when generate Level, atoms must always exist on grid

molecules top to bottom on grid according to order in a

lone atoms with no bonds are rendered on same row, though

 --
 *
 * when minimize screen, rendered atoms disappear WTF?

OBJECT'S NOT DI.

When players creates last bond, player is asked to name [randomly 1 or all types] of molecules drawn.
if player cannot name, the game continues

right now only single-bonding between atoms.  allow double, triple bonds? issues with rendering multiple bonds

implement 3-Dish engine, account for
C-CC
| |
C-C .. sorted bondedAtoms takes care of this for certain cases

create "stack"? can sort through atoms in stack?

// automatically

*/

import java.util.ArrayList;

public class Atom
{
	public ArrayList<Atom> bondedAtoms; // Other atoms it bonds to; bonds.length = 2 => O, etc.
	//private enum AtomType {HYDROGEN=1,OXYGEN=2,CARBON=4};
	public int bonds; // Maximum bonds; how the atom is identified
	public boolean rendered; // True if already rendered for current screen update, but always gets reset to false after
    public int direction; // Relative to parent molecule, which direction is facing; 0=north, 1=east, ...
    public int x;
    public int y;
   
    public Atom(int bonds)
    {/*
*/
    	this.bonds = bonds; // Is this how one is supposed to deal with same names
    	rendered = false;
    	bondedAtoms = new ArrayList<Atom>();
    }
	public void makeBond(Atom a)
	{
		// REODER SO THAT CARBONS ARE ALWAYS FIRST, THEN IN RENDERATOM ALWAYS DO DIRECTION STRAIGHT LEFT RIGHT!!!!!!!!!!!!!!!!!

		//javax.swing.JOptionPane.showInputDialog("attempting to bond"+bondedAtoms.size());

		//if(bondedAtoms.size()<bonds && a!=this && repeats(a)<=3){ // Yes, comparing object memory locations...
			//javax.swing.JOptionPane.showInputDialog("success");
			// So, H is succesfully being bonded to C...just not getting rendered as bonded
			for(int i=0;i<=bondedAtoms.size();i++){
				if(i==bondedAtoms.size() || a.bonds>bondedAtoms.get(i).bonds){ // It's a good thing Java does lazy || eval..I think..
					bondedAtoms.add(i,a);
					break;
				}
			}/*
			System.out.println();
			for(int i=0;i<bondedAtoms.size();i++){
				System.out.print(bondedAtoms.get(i).bonds);
			}*/
			// Modified version of Nick's code; not descending order and weird lines changed
			/* DON'T NEED TO SORT ENTIRE LIST EACH TIME, JUST INSERT LAST..
			 int index = 0;
     int indexofswitch=0;
     int max;
     Atom holder;
     for (int i=0; i<bondedAtoms.size(); i++)
     {
		max=bondedAtoms.get(index+1).bonds;
      for (int j=index+1; j<bondedAtoms.size(); j++)
      {

       if(bondedAtoms.get(j).bonds>max)
       {
        max=bondedAtoms.get(j).bonds;
        indexofswitch=j;
       }
      }
      holder=bondedAtoms.get(index);
      bondedAtoms.set(indexofswitch,bondedAtoms.get(index));
      bondedAtoms.set(index,holder);
      index++;
     }
/*
	 int index = 0;
     int indexofswitch=0;
     int min;
     Atom holder;
     for (int i=0; i<bondedAtoms.size(); i++)
     {

      for (int j=index; j<bondedAtoms.size(); j++)
      {
       min=bondedAtoms.get(j).bonds; // THESE TWO LINES DO NOT MAKE SENES TO MEEEE
       if(bondedAtoms.get(j).bonds<min)
       {
        min=bondedAtoms.get(j).bonds;
        indexofswitch=j;
       }
      }
      holder=bondedAtoms.get(index);
      bondedAtoms.set(indexofswitch,bondedAtoms.get(index));
      bondedAtoms.set(index,holder);
      index++;
     }*/
		//}
	}

	public boolean validBond(Atom a)
	{ // validBond must be satisfied for both parent and child atoms in order for bond between to be created
	// DON'T ALLOW QUADRUPLE BOND BETWEEN TWO CARBONS!!!!!!!!!!!
	//	return bondedAtoms.size()<bonds && a!=this && (repeats(a)<=2 || (repeats(a)==3 && a;
	
		return bondedAtoms.size()<bonds && a!=this && repeats(a)<3;
		
	}



	public int repeats(Atom a)
	{
		// Returns the number of times a is repeated in bondedAtoms; used for dealing with variable # of bonds between 2 atoms
		synchronized (bondedAtoms){
		int times = 0;
		for(Atom t : bondedAtoms){
			if(t==a) // Yes, comparing object memory locations again
				times++;
		}
		return times;
		}
	}

	public static boolean circularBond(Atom parent, Atom a1, Atom a2){ // p is an atom that could potentially be bonded to this, but p can't already be connected to this in any way
		// See if Atom a2 is somehow connected to a1
		// We do not want "circular" bonds (no benzene?), circular things not supported by rendering engine anyway
		// Don't have to test for "this" itself; only bondedAtoms.  Multiple bonds between two atoms are allowed haha.
		
		// Don't want to check parent when going through children (ex c1 parent of c2, start at c1, recurs to go to c2, c2 checks c1 NONO)
		// ^ Cause note that Atom A: bondedAtoms, bondedAtoms changes for each call?
		/*
		for(Atom a : bondedAtoms){
			if(a==p){ //Yes, comparing object memory locations again
				//javax.swing.JOptionPane.showInputDialog("circular");
				return true;
			}
			return circularBond(a);
		}*/
		for(Atom a : a1.bondedAtoms){
			if(a!=parent){ // Don't count parent twice; infinite
			
			if(a==a2){ //Yes, comparing object memory locations again
				//javax.swing.JOptionPane.showInputDialog("circular");
				return true;
			}
			return circularBond(a1,a,a2);
			}
		}
		return false;
	}

}
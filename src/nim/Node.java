package nim;

public class Node
{
	   int nFish; // Number of matches remaining after a move to this Node
	                 // from the parent Node.
	   char player;  // Game configuration from which player (A - player A, B -
	                 // player B) makes a move.
	   Node left;    // Link to left child Node -- a move is made to left Node
	                 // when 1 match is taken. (This link is only null when the
	                 // current Node is a leaf.)
	   Node center;  // Link to center child Node -- a move is made to this Node
	                 // when 2 matches are taken. (This link may be null, even if
	                 // the current Node is not a leaf.)
	   Node right;   // Link to right child Node -- a move is made to this Node
	                 // when three matches are taken. (This link may be null,
	                 // even if the current Node is not a leaf.)
}
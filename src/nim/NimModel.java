// NimModel.java
// Code modifications made by : Drew Murphy
package nim;

public class NimModel {

    public static final char PLAYER_A = 'A';
    public static final char PLAYER_B = 'B';
    // Game tree root node reference. Modified during game play; reset if user
    // chooses to play again.
    private Node tempRoot;
    private Node gameTreeRoot;
    private int numFish, numComputer, numHuman;

    public NimModel(int nfish) {
        gameTreeRoot = buildGameTree(nfish, PLAYER_A);
        numFish = nfish;
        numComputer = 0;
        numHuman = 0;
        newGame();
    }

    public boolean gameOver() {
        return (tempRoot.nFish == 0);
    }

    public int getNumFish() {
        return numFish;
    }
    
    public int getNumCom() {
    	return numComputer;
    }
    
    public int getNumHuman() {
    	return numHuman;
    }

    public void playTurn(int fishToTake) {
        switch (fishToTake) {
            case 1:
                tempRoot = tempRoot.left;
                numFish--;
                numHuman++;
                break;

            case 2:
                tempRoot = tempRoot.center;
                numFish = numFish - 2;
                numHuman = numHuman + 2;
                break;

            case 3:
                tempRoot = tempRoot.right;
                numFish = numFish - 3;
                numHuman = numHuman + 3;
        }

    }

    final public void newGame() {
        tempRoot = gameTreeRoot;
        numComputer = 0;
        numHuman = 0;
    }

    final public Node buildGameTree(int nfish, char player) {
        Node n = new Node();
        n.nFish = nfish;
        n.player = player;

        if (nfish >= 1) {
            n.left = buildGameTree(nfish - 1, (player == PLAYER_A) ? PLAYER_B : PLAYER_A);
        }
        if (nfish >= 2) {
            n.center = buildGameTree(nfish - 2, (player == PLAYER_A) ? PLAYER_B : PLAYER_A);
        }
        if (nfish >= 3) {
            n.right = buildGameTree(nfish - 3, (player == PLAYER_A) ? PLAYER_B : PLAYER_A);
        }

        return n;
    }

    public int computeMinimax(Node n) {
        int ans;

        if (n.nFish == 0) {
            return (n.player == PLAYER_A) ? 1 : -1;
        } else if (n.player == PLAYER_A) {
            ans = Math.max(-1, computeMinimax(n.left));
            if (n.center != null) {
                ans = Math.max(ans, computeMinimax(n.center));
                if (n.right != null) {
                    ans = Math.max(ans, computeMinimax(n.right));
                }
            }
        } else {
            ans = Math.min(1, computeMinimax(n.left));
            if (n.center != null) {
                ans = Math.min(ans, computeMinimax(n.center));
                if (n.right != null) {
                    ans = Math.min(ans, computeMinimax(n.right));
                }
            }
        }
        return ans;
    }

    public int takeBestMove() {
        // Use the minimax algorithm to determine if the
        // computer player's optimal move is the child node left
        // of the current root node, the child node below the
        // current root node, or the child node right of the
        // current root node.

        int v1 = computeMinimax(tempRoot.left);
        int v2 = (tempRoot.center != null)
                ? computeMinimax(tempRoot.center) : 2;
        int v3 = (tempRoot.right != null)
                ? computeMinimax(tempRoot.right) : 2;

        int takenFish;
        if (v1 < v2 && v1 < v3) {
            takenFish = 1;
            tempRoot = tempRoot.left;
        } else if (v2 < v1 && v2 < v3) {
            takenFish = 2;
            tempRoot = tempRoot.center;
        } else if (v3 < v1 && v3 < v2) {
            takenFish = 3;
            tempRoot = tempRoot.right;
        } else {
            takenFish = (int) (Math.random() * 3) + 1;
            switch (takenFish) {
                case 1:
                    tempRoot = tempRoot.left;
                    break;

                case 2:
                    tempRoot = tempRoot.center;
                    break;

                case 3:
                    tempRoot = tempRoot.right;
            }
        }
        
        numFish = numFish - takenFish;
        numComputer = numComputer + takenFish;
        return takenFish;
    }
    
    public void rebuild(int n) {
    	gameTreeRoot = buildGameTree(n, PLAYER_A);
    	numFish = n;
    }
}

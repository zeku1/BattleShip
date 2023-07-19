import java.util.Random;
import java.util.Scanner;

public class BattleShips {
  static Scanner s = new Scanner(System.in);// accept input from the user
  static Random r = new Random();// accept input from the computer

  public static void main(String[] args) {

    // STEP 1
    // Array for the ocean map
    String grid[][] = new String[10][10];
    // Initialized 2D array
    for(int i = 0;i < 10;i++){// first loop for the y axis
      for(int x = 0;x < 10; x++){// second loop for the x axis
        grid[i][x] = " ";
      }
    }

    //------------ Header of the game
    System.out.println("**** Welcome to Battle Ships game ****");

    System.out.println("Right now, the sea is empty.");
    //-----------------------------

    oceanMap(grid);// method to illustrat the ocean map

    // STEP 2
    // Array for the player shhips
    int pShips[][] = new int[5][2];
    System.out.println("Deploy your ships");
    // initializing array
    pShips = playerShips(grid,pShips);

    oceanMap(grid);

    // STEP 3
    // Array for the computer ships
    int cShips[][] = new int[5][2];
    System.out.println("Computer is deploying ships");
    // initializing array
    cShips = computerShips(grid,cShips);

    // STEP 4
    boolean over = false;// to determine if the game is over
    int j = 0;
    int x, y;// players input coordinates
    int comX,comY;// compuers input coordinates
    int Score1 = 0;// check if computers ship is hit
    int Score2 = 0;// check if computers ship is hit
    int all = 0;
    int missed[][] = new int[100][2];// store compters previous input

    while(!over){
      // Player's turn
      System.out.println("YOUR TURN");

      boolean pOkay = false;
      while(!pOkay) {
        System.out.print("Enter X coordinate: ");
        x = s.nextInt();
        System.out.print("Enter Y coordinate: ");
        y = s.nextInt();
        if (!outOfBounds(x, y)) {
          Score1 = playerTurn(x, y, grid, cShips, pShips);
          pOkay = true;
        }else {
          System.out.println("Coordinates is out of bounds!"
            + "\nEnter another coordinates");
        }
      }

      //Computers turn
      System.out.println("COMPUTER'S TURN");
      boolean Cokay = false;

      while(!Cokay) {
        comX = r.nextInt(10);
        comY = r.nextInt(10);

        for(int i = 0; i < 23; i++){
          if(comY == missed[i][0] && comX == missed[i][1]){
            Cokay = false;
            break;
          }else {
            Score2 = computerTurn(comX, comY, grid, cShips, pShips);
            Cokay = true;
            break;
          }
        }
        missed[j][0] = comY;
        missed[j][1] = comX;
        j++;
      }


      oceanMap(grid);

      all = all + Score1 + Score2;

      over = shipCount(grid, all, cShips);// check if there is still ship left


    }


  }

  private static boolean outOfBounds(int x,int y){
    boolean resubmit = false;
    if(x>10 && y>10){
      resubmit = true;
    }
    return resubmit;
  }

  private static boolean shipCount(String[][] grid, int all , int[][] cShips) {
    int player=0;
    int computer = 5;
    boolean over = false;
    for(int i = 0;i < 10;i++){
      for(int x = 0;x < 10; x++){
        if(grid[i][x] == "@"){
          player += 1;
        }
      }
    }
    computer = computer - all;
    System.out.println("Your ships: " + player + " | Computer Ships: " + computer);

    if(player == 0){
      System.out.println("You loose the battle. Better luck next time:(");
      for(int i = 0;i < 5;i++){
        for(int x = 0;x < 2; x++){
          int xx = cShips[i][1];
          int yy = cShips[i][0];
          if(grid[yy][xx] == " "){
            grid[yy][xx] = "0";
          }
        }
      }
      oceanMap(grid);
      over = true;
    }else if(computer == 0){
      System.out.println("Hooray! You win the battle :)");
      over = true;
    }else{
      over = false;
    }

    return over;

  }


  private static int computerTurn(int x, int y, String[][] grid, int[][] cShips, int[][] pShips ){

    int score =0;
    boolean shot = false;// if hit player's ship
    boolean suicide = false;// if hit players ship

    for(int i = 0; i < 5; i++){// create loop to check if it match the coordinates of the player's ship
      int cy = pShips[i][0];
      int cx = pShips[i][1];
      if(x == cx && y == cy){
        shot = true;
        break;
      }else{
        shot = false;
      }
    }
    for(int i = 0; i < 5; i++){// create loop to check if it match the coordinates of the comuter's ship
      int py = cShips[i][0];
      int px = cShips[i][1];
      if(x == px && y == py){
        suicide = true;
        break;
      }else{
        suicide = false;
      }
    }

    // (!) + message -> if computer's ship is hit
    // (x) + message -> if player's ship is hit
    if(grid[y][x] == "!"){
      System.out.println("Computer missed");
    }else if(grid[y][x] == "x"){
      System.out.println("Computer missed");
    }else{
      if(shot){
        grid[y][x] = "x";
        System.out.println("The Computer sunk one of your ships!");

      }else if(suicide){
        grid[y][x] = "!";
        System.out.println("The Computer sunk one of its own ships");
        score = 1;
      }else{
        System.out.println("Computer missed");
      }
    }

    return score;
  }

  private static int playerTurn(int x,int y, String[][] grid, int[][] cShips,int[][] pShips ) {
    int score =0;
    boolean shot = false;// if hit computer's ship
    boolean suicide = false;// if hit players ship

    for(int i = 0; i < 5; i++){// create loop to check if it match the coordinates of the comuter's ship
      int cy = cShips[i][0];
      int cx = cShips[i][1];
      if(x == cx && y == cy){
        shot = true;
        break;
      }else{
        shot = false;
      }
    }
    for(int i = 0; i < 5; i++){// create loop to check if it match the coordinates of the player's ship
      int py = pShips[i][0];
      int px = pShips[i][1];
      if(x == px && y == py){
        suicide = true;
        break;
      }else{
        suicide = false;
      }
    }

    // (!) + message -> if computer's ship is hit
    // (x) + message -> if player's ship is hit
    // (-) + message -> if player misses
    if(grid[y][x] == "!"){
      System.out.println("Sorry, you missed");
    }else if(grid[y][x] == "x"){
      System.out.println("Sorry, you missed");
    }else{
      if(shot){
        grid[y][x] = "!";
        System.out.println("BOOM! You sunk the ship");
        score = 1;
      }else if(suicide){
        grid[y][x] = "x";
        System.out.println("Oh no, you sunk your own ship :(");

      }else{
        System.out.println("Sorry, you missed");
        grid[y][x] = "-";
      }
    }
    return score;
  }


  private static int[][] computerShips(String[][] grid, int[][] cShips) {
    boolean cont = false;
    int j = 0;// check number of ships
    int x,y;// x and y coordinates of the ships

    while(!cont){
      //----------Getting coordinates from the computer-----------------//
      x = r.nextInt(10);// limit to 0-9 numbers that computer guesses
      y = r.nextInt(10);

      if(grid[y][x]==" "){
        cShips[j][0] = y;//store y coordinates
        cShips[j][1] = x;//store x coordinates
        System.out.println((j+1)+". ship DEPLOYED");// alert player the ships are deployed
        j++;
      }

      if(j==5){
        cont = true;
      }

    }



    return cShips;
  }

  private static int[][] playerShips(String[][] grid , int[][] pShips) {
    boolean cont = false;
    int j = 0;// to how many ships is deployed
    int x,y; // the x and y coordinates of the ships

    while(!cont){
      //----------Getting coordinates from the player-----------------//
      System.out.print("Enter X coordinate for your "+(j+1)+ ". ship: ");
      x = s.nextInt();
      System.out.print("Enter Y coordinate for your "+(j+1)+ ". ship: ");
      y = s.nextInt();

      if(x < 10 && y < 10){// checks if the input is out of bounds
        if(grid[y][x]==" "){// check if there's already a ship in the coordinates
          grid[y][x] = "@"; // to illustrate the ship
          pShips[j][0] = y; // store y coordinates
          pShips[j][1] = x; // store x coordinates
          j++;
        }else{
          System.out.println("Position already taken."
            + "\n Choose other position");

        }
      }else{
        System.out.println("Out of bounds!"
          + "\n Stay inside the perimeter!");
      }

      if(j==5){// check if the all five ships deployed
        cont = true;
      }

    }


    return pShips;
  }

  private static void oceanMap(String[][] grid) {

    System.out.print("\n " +
      "  0123456789");
    for(int i = 0; i < 10;i++){
      System.out.println();
      System.out.print(i+" |");
      for(int x = 0; x < 10; x++){
        System.out.print(grid[i][x]);
      }
      System.out.print("|" +i);
    }
    System.out.println("\n " +
      "  0123456789");
    System.out.println();

  }
}

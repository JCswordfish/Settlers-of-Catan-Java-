import javax.swing.*;
import java.awt.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;



import java.io.*;
import java.net.*;
import java.util.*;


import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;






public class ClientScreen extends JPanel implements ActionListener, MouseListener, KeyListener{
    
	private String hostName;
    private static final int trueHeight = 145; // base of 30 30 120
	private static final double cos60 = Math.cos(3.14159265/3);
	private static final double sin60 = Math.sin(3.14159265/3);
	private static final int settleX = 152;
	private static final int settleY = 183;
    private static final int[] portsX = {176, 305, 559, 778, 899, 775, 563, 301, 176};
    private static final int[] portsY = {352, 141, 137, 261, 490, 709, 829, 832, 609};


    
    
	int portNumber;
	ServerSocket server;
	Socket socket;
	ObjectOutputStream out;
	Color ownColor;
	ObjectInputStream in;
	private JButton startButton;
	String username;
	boolean settling,roading, firstClick, shop, citing, robbing, robbing2;
	int clickR, clickC;

	private int index, recipient;
    int toDiscard;

	private JTextField usernameField;
	private Game game;
	private boolean started;
    private boolean trading;
	private Color[] indexToColor;

    int roadsFromCard,resourcesFromCard;

    private int[] tradeResources1;
    private int[] tradeResources2;

    private int bankS1;
    private int bankS2;
    private int toTradeBank;

    private int freeRoads, freeResources;

	BufferedImage oreImage;
    BufferedImage brickImage;
    BufferedImage wheatImage;
    BufferedImage sheepImage;
    BufferedImage woodImage;
    BufferedImage desertImage;
    BufferedImage backgroundImage;

    BufferedImage yellowHouseImage;
    BufferedImage greenHouseImage;
    BufferedImage blueHouseImage;
    BufferedImage redHouseImage;
	BufferedImage shopImage;

    BufferedImage oreCardImage;
    BufferedImage brickCardImage;
    BufferedImage wheatCardImage;
    BufferedImage sheepCardImage;
    BufferedImage woodCardImage;

    BufferedImage yellowCityImage;
    BufferedImage greenCityImage;
    BufferedImage blueCityImage;
    BufferedImage redCityImage;


    BufferedImage knightCardImage;
    BufferedImage victoryPointCardImage;
    BufferedImage twoRoadsCardImage;
    BufferedImage twoFreeCardImage;
    BufferedImage monopolyCardImage;

    boolean monopoly;

    BufferedImage robberImage;

    int tradesToDisplay;


	public ClientScreen(){


		startButton = new JButton();
		startButton.setFont(new Font("Arial", Font.BOLD, 20));
		startButton.setHorizontalAlignment(SwingConstants.CENTER);
		startButton.setBounds(555, 85, 200, 30);
		startButton.setText("Start");
		this.add(startButton);
		startButton.addActionListener(this);
		startButton.setVisible(true);
		
		usernameField = new JTextField();
		usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
		usernameField.setHorizontalAlignment(SwingConstants.LEFT);
		usernameField.setBounds(557, 13, 200, 30);
		usernameField.setText("Username");
		this.add(usernameField);

		indexToColor = new Color[] {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};

		started = false;

		this.setLayout(null);
		this.setFocusable(true);
		addKeyListener(this);
        addMouseListener(this);

		try{
			oreImage = ImageIO.read(new File("ore.png"));
			brickImage = ImageIO.read(new File("brick.png"));
			wheatImage = ImageIO.read(new File("wheat.png"));
			sheepImage = ImageIO.read(new File("sheep.png"));
			woodImage = ImageIO.read(new File("wood.png"));
			desertImage = ImageIO.read(new File("desert.png"));
			backgroundImage = ImageIO.read(new File("Settlers of Catan Board Design.png"));

			redHouseImage = ImageIO.read(new File("redHouse.png"));
			yellowHouseImage = ImageIO.read(new File("yellowHouse.png"));
			greenHouseImage = ImageIO.read(new File("greenHouse.png"));
			blueHouseImage = ImageIO.read(new File("blueHouse.png"));

            redCityImage = ImageIO.read(new File("redCity.png"));
            yellowCityImage = ImageIO.read(new File("yellowCity.png"));
            greenCityImage = ImageIO.read(new File("greenCity.png"));
            blueCityImage = ImageIO.read(new File("blueCity.png"));

			shopImage = ImageIO.read(new File("shopImage.png"));

            oreCardImage = ImageIO.read(new File("oreCard.png"));
            brickCardImage = ImageIO.read(new File("brickCard.png"));
            wheatCardImage = ImageIO.read(new File("wheatCard.png"));
            sheepCardImage = ImageIO.read(new File("sheepCard.png"));
            woodCardImage = ImageIO.read(new File("woodCard.png"));

            robberImage = ImageIO.read(new File("robber.png"));

            knightCardImage = ImageIO.read(new File("knightCard.png"));
            victoryPointCardImage = ImageIO.read(new File("victoryPointCard.png"));
            twoRoadsCardImage = ImageIO.read(new File("twoRoadsCard.png"));
            twoFreeCardImage = ImageIO.read(new File("twoFreeCard.png"));
            monopolyCardImage = ImageIO.read(new File("monopolyCard.png"));

		} catch (IOException ex){
			System.out.println(ex);
		}
		settling = true;
		roading = false;
        robbing = false;
        citing = false;
		shop = false;
        robbing2 = false;
		firstClick = true;
        monopoly = false;
        toDiscard = 0;
        tradesToDisplay = 0;

        recipient = -1;
        bankS1 = -1;
        bankS2 = -1;
        toTradeBank = 4;

        freeResources = 0;
        freeRoads = 0;

    }
	
    
    
    


    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(started){
			if(game.getWinner() != -1){ // game done
				g.setColor(Color.BLACK);
				g.fillRect(0,0,1524,1080);
                g.setColor(indexToColor[game.getWinner()]);
				g.setFont(new Font("Arial", Font.BOLD, 40));
                g.drawString("Player " + game.getWinner() + " has won!",100,600);
                g.drawString("Press R to restart",100,1000);
                return;
            } else if(toDiscard > 0){ // discarding
				g.setColor(Color.BLACK);
				g.fillRect(0,0,1524,1080);
                g.setColor(Color.WHITE);
				g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("You must discard " + toDiscard + " cards",25,1005);
                
                
            } else if(shop){ // shop menu
				g.setFont(new Font("Arial", Font.BOLD, 20));
				g.setColor(Color.BLACK);
				g.fillRect(0,0,1524,1080);
                g.setColor(Color.WHITE);

                if(bankS1 != -1){
                    g.drawString("For:",900, 800);

                    
                    g.drawImage(oreCardImage, 900, 875,null);
                    g.drawImage(brickCardImage, 1000, 875,null);
                    g.drawImage(wheatCardImage, 1100, 875,null);
                    g.drawImage(sheepCardImage, 1200, 875,null);
                    g.drawImage(woodCardImage, 1300, 875,null);
                }
                    


                
                if(bankS1 == -1){
                    g.drawString("Trade With Bank Using", 300, 800);
                } else {
                    switch(bankS1){
                        case 0:
                            g.drawString("Trade With Bank Using " + toTradeBank + " Ore", 300, 800);
                            break;
                        case 1:
                            g.drawString("Trade With Bank Using " + toTradeBank + " Brick", 300, 800);
                            break;
                        case 2:
                            g.drawString("Trade With Bank Using " + toTradeBank + " Wheat", 300, 800);
                            break;
                        case 3:
                            g.drawString("Trade With Bank Using " + toTradeBank + " Sheep", 300, 800);
                            break;
                        case 4:
                            g.drawString("Trade With Bank Using " + toTradeBank + " Wood", 300, 800);
                            break;
                    }
                    
                }
                

                g.fillRect(678,25,100,400);
				g.drawImage(shopImage,25,25,null);
                g.setColor(Color.RED);
                if(bankS1 != -1){
                    g.drawRect(bankS1*100+299,874,82,100+22*game.getPlayer(index).getResources()[bankS1]);
                }
                g.setColor(Color.BLACK);
                for(int i = 0; i < 4; i++){
                    g.drawRect(678,25+100*i,100,100);
                    g.drawString("Buy",718,85+100*i);
                }
                
                
                

            } else if (trading && !robbing && !robbing2){ // trading menu
                g.setColor(Color.BLACK);
				g.fillRect(0,0,1524,1080);

                g.setColor(Color.RED);
                g.fillRect(1034,909,380,35);
                g.setColor(Color.BLACK);
				g.setFont(new Font("Arial", Font.PLAIN, 15));
                g.drawString("Cancel Trade", 1184,933);

                if(recipient != -1){
                    g.setColor(Color.MAGENTA);
                    g.fillRect(1034,979,380,35);
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.PLAIN, 15));
                    g.drawString("Send Trade", 1184,1003);
                }
                

                g.setFont(new Font("Arial", Font.BOLD, 22));
                g.setColor(Color.WHITE);
                if(game.getSelected() == index){
                    g.drawString("Select who you want to trade with:", 20,40);
                    for(int i = 0; i < game.getSize(); i++){
                        if(i != index){
                            g.setColor(indexToColor[i]);
                            g.drawRect(50+100*i,50,50,50);
                            if(i == recipient){
                                g.fillRect(50+100*i,50,50,50);
                            }
                        }
                    }
                    if(recipient != -1){
                        for(int i = 0; i < 5; i++){
                            g.setColor(Color.WHITE);
                            g.drawRect(50+100*i,350,50,50);
                            g.drawRect(50+100*i,650,50,50);
                            g.drawString("Your Resources: ",50,330);
                            g.drawString("Their Resources: ",50,630);
                            if(tradeResources1[i] > 0){
                                if(tradeResources1[i] == game.getPlayer(index).getResources()[i]){
                                    g.setColor(Color.RED);
                                } else {
                                    g.setColor(Color.WHITE);
                                }
                                g.fillRect(50+100*i,350,50,50);
                                g.setColor(Color.BLACK);
                                g.drawString(""+tradeResources1[i],64+100*i,386);
                            }
                            if(tradeResources2[i] > 0){

                                g.setColor(Color.WHITE);
                                g.fillRect(50+100*i,650,50,50);
                                g.setColor(Color.BLACK);
                                g.drawString(""+tradeResources2[i],64+100*i,686);
                            }
                            
                        }

                        g.drawImage(oreCardImage, 35, 465,null);
                        g.drawImage(brickCardImage, 135, 465,null);
                        g.drawImage(wheatCardImage, 235, 465,null);
                        g.drawImage(sheepCardImage, 335, 465,null);
                        g.drawImage(woodCardImage, 435, 465,null);
                    }

                    
                } else {
                    g.drawString("You can only trade with this turn's player", 20, 40);

                    g.drawImage(oreCardImage, 25, 465,null);
                    g.drawImage(brickCardImage, 125, 465,null);
                    g.drawImage(wheatCardImage, 225, 465,null);
                    g.drawImage(sheepCardImage, 325, 465,null);
                    g.drawImage(woodCardImage, 425, 465,null);
                    for(int i = 0; i < 5; i++){
                        g.setColor(Color.WHITE);
                        g.drawRect(50+100*i,350,50,50);
                        g.drawRect(50+100*i,650,50,50);
                        g.drawString("Your Resources: ",50,330);
                        g.drawString("Their Resources: ",50,630);
                        if(tradeResources1[i] > 0){
                            g.setColor(Color.WHITE);
                            g.fillRect(50+100*i,650,50,50);
                            g.setColor(Color.BLACK);
                            g.drawString(""+tradeResources1[i],64+100*i,686);
                        }
                        if(tradeResources2[i] > 0){
                            if(tradeResources2[i] == game.getPlayer(index).getResources()[i]){
                                g.setColor(Color.RED);
                            } else {
                                g.setColor(Color.WHITE);
                            }
                            g.fillRect(50+100*i,350,50,50);
                            g.setColor(Color.BLACK);
                            g.drawString(""+tradeResources2[i],64+100*i,386);
                        }
                    }
                }
                

			} else { // game board
				g.drawImage(backgroundImage, 0, 0, null);
                g.setColor(new Color(75,127,219));
                g.fillRect(0,1024,1024,56);
				g.setColor(ownColor);
				g.drawString("You are Player " + index + " (" + username + ")", 25,970);
				g.setColor(Color.MAGENTA.darker());
				g.fillRect(1424,0,100,1080);
				g.setColor(Color.BLACK);
				g.fillRect(1024,0,400,1080);
				g.setColor(Color.WHITE);
				g.setFont(new Font("Arial", Font.BOLD, 30));
				g.drawString("Active Trades:", 1050,50);
				g.setFont(new Font("Arial", Font.BOLD, 12));
				g.drawString("Development", 1434,20);
				g.drawString("Cards:", 1434,33);

                int[] knights = game.getKnights();
                int[] roadLengths = game.getRoadLengths();
                for(int i = 0; i < knights.length; i++){
                    g.setColor(indexToColor[i]);
                    if(game.mostKnights() == i){
                        g.drawString("Player " + i + " has "+knights[i] + " knights (+2 VP)", 800, 800+i*15);
                    } else {
                        g.drawString("Player " + i + " has "+knights[i] + " knights", 800, 800+i*15);
                    }
                    if(game.mostRoads() == i){
                        g.drawString("Player " + i + " has "+roadLengths[i] + " roads (+2 VP)", 800, 900+i*15);
                    } else {
                        g.drawString("Player " + i + " has "+roadLengths[i] + " roads", 800, 900+i*15);
                    }
                }

                MyDLList<DevCard> devCards = game.getPlayer(index).getDevCards();
                for (int i = 0; i < devCards.size(); i++) {
                    DevCard card = devCards.get(i);
                    switch (card.getType()) {
                        case 0:
                            g.drawImage(knightCardImage, 1434, 50 + 130 * i, null);
                            
                            break;
                        case 1:
                            g.drawImage(twoRoadsCardImage, 1434, 50 + 130 * i, null);
                            break;
                        case 2:
                            g.drawImage(twoFreeCardImage, 1434, 50 + 130 * i, null);
                            break;
                        case 3:
                            g.drawImage(monopolyCardImage, 1434, 50 + 130 * i, null);
                            break;
                        case 4:
                            g.drawImage(victoryPointCardImage, 1434, 50 + 130 * i, null);
                            break;
                    }
                    if(!card.usable() && card.getType() != 4){
                        g.setColor(Color.RED);
                        g.drawString("Not Usable",1444, 70 + 130 * i);
                        g.drawString("Yet",1444, 83 + 130 * i);
                    }
                }

                
				TileType tempType;
				Tile[][] grid = game.getGrid();
				MyArrayList<Settlement> settlements = game.getSettlements();
				MyArrayList<Integer[]> roads = game.getRoads();
				int r,c,a,x,y;
                int[] ports = game.getPorts();
				g.setFont(new Font("Arial", Font.PLAIN, 6));
                g.setColor(Color.BLACK);
                for(int i = 0; i < 9; i++){
                    int tempPort = ports[i];
                    switch(tempPort){
                        case 5:
                            g.drawString("3:1", portsX[i], portsY[i]);
                            g.drawString("ANY", portsX[i], portsY[i]-7);
                            break;
                        case 0:
                            g.drawString("2:1", portsX[i], portsY[i]);
                            g.drawString("ORE", portsX[i], portsY[i]-7);
                            break;
                        case 1:
                            g.drawString("2:1", portsX[i], portsY[i]);
                            g.drawString("BRICK", portsX[i], portsY[i]-7);
                            break;
                        case 2:
                            g.drawString("2:1", portsX[i], portsY[i]);
                            g.drawString("WHEAT", portsX[i], portsY[i]-7);
                            break;
                        case 3:
                            g.drawString("2:1", portsX[i], portsY[i]);
                            g.drawString("SHEEP", portsX[i], portsY[i]-7);
                            break;
                        case 4:
                            g.drawString("2:1", portsX[i], portsY[i]);
                            g.drawString("WOOD", portsX[i], portsY[i]-7);
                            break;
                    }
                }
				g.setColor(Color.WHITE);
				g.setFont(new Font("Arial", Font.PLAIN, 15));
				for(int i = 0; i < 5; i++){
					if(i <= 2){
						a = 0;
					} else {
						a = i-2;
					}
					for(int k = a; k < a + 5-(Math.abs(2-i)); k++){
						Tile tempTile = grid[i][k];
						tempType = tempTile.getType();
						r = tempTile.getR();
						c = tempTile.getC();

						x = 301+(int)(trueHeight * Math.min(r,c)*cos60);
						y = 150+(int)(trueHeight * Math.min(r,c)*sin60);
						if(r > c){
							x -= (int)(trueHeight*(r-c)*cos60);
							y += (int)(trueHeight*(r-c)*sin60);
						} else if (r < c) {
							x += trueHeight*(c-r);
						}

						switch (tempType) {
							case ORE:
								g.drawImage(oreImage, x, y, null);
								break;
							case BRICK:
								g.drawImage(brickImage, x, y, null);
								break;
							case WHEAT:
								g.drawImage(wheatImage, x, y, null);
								break;
							case SHEEP:
								g.drawImage(sheepImage, x, y, null);
								break;
							case WOOD:
								g.drawImage(woodImage, x, y, null);
								break;
							case DESERT:
								g.drawImage(desertImage, x, y, null);
								break;
						}
						String ar = ""+tempTile.getAR();
						
						if(tempTile.getAR() != 0){
                            g.setColor(Color.WHITE);
                            g.fillOval(x+55,y+66,25,25);
							if(tempTile.getAR() == 6 || tempTile.getAR() == 8){
								g.setColor(Color.RED);
							} else {
								g.setColor(Color.BLACK);
							}
							g.drawString(ar, x+68-5*(ar.length()), y+85);
						}
                        if(tempTile.isRobber()){
                            g.drawImage(robberImage,x+65,y+60,null);
                        }
						
						
					}
				}
				int tempIndex;
                for(int i = 0; i < roads.size(); i++){
					Integer[] tempRoad = roads.get(i);
					int r1 = tempRoad[0];
					int c1 = tempRoad[1];
					int r2 = tempRoad[2];
					int c2 = tempRoad[3];
					tempIndex = tempRoad[4];
					int x1 = settleX + (int)(72.5*(c1));
					int y1 = settleY + 127*r1 - 43*((c1+r1)%2);
					int x2 = settleX + (int)(72.5*(c2));
					int y2 = settleY + 127*r2 - 43*((c2+r2)%2);
					
					g.setColor(indexToColor[tempIndex]);
					g.drawLine(x1,y1,x2,y2);

                    if(x1 == x2){
                        g.drawLine(x1+1,y1,x2+1,y2);
                        g.drawLine(x1-1,y1,x2-1,y2);
                        g.drawLine(x1-2,y1,x2-2,y2);
                    } else {
					    g.drawLine(x1,y1+1,x2,y2+1);
					    g.drawLine(x1,y1-1,x2,y2-1);
					    g.drawLine(x1,y1-2,x2,y2-2);

                    }
				}
				for(int i = 0; i < settlements.size(); i++){
					Settlement tempSettlement = settlements.get(i);
					r = tempSettlement.getR();
					c = tempSettlement.getC();
					tempIndex = tempSettlement.getIndex();
					x = settleX + (int)(72.5*(c));
					y = settleY + 127*r - 43*((c+r)%2);
					if(tempSettlement.isCity()){
                        switch(tempIndex){
                            case 0:
                                g.drawImage(redCityImage, x-31, y-26, null);
                                break;
                            case 1:
                                g.drawImage(yellowCityImage, x-31, y-26, null);
                                break;
                            case 2:
                                g.drawImage(greenCityImage, x-31, y-26, null);
                                break;
                            case 3:
                                g.drawImage(blueCityImage, x-31, y-26, null);
                                break;
                        }
                    } else {
                        switch(tempIndex){
                            case 0:
                                g.drawImage(redHouseImage, x-15, y-14, null);
                                break;
                            case 1:

                                g.drawImage(yellowHouseImage, x-15, y-14, null);
                                break;
                            case 2:
                                g.drawImage(greenHouseImage, x-15, y-14, null);

                                break;
                            case 3:
                                g.drawImage(blueHouseImage, x-15, y-14, null);

                                break;
                        }
                    }
				}
				g.setColor(Color.BLACK);
				g.drawString("Current Roll is " + game.getCurrentRoll(), 25, 850);
				if(game.getSelected() == index){
					g.setColor(Color.MAGENTA);
					g.setFont(new Font("Arial", Font.BOLD, 30));
					g.drawString("YOUR TURN!", 25,900);
					
					if(!(settling || roading || citing || robbing || robbing2)){
						g.setColor(Color.WHITE);
						g.fillRect(25,910,200,40);
						g.setFont(new Font("Arial", Font.BOLD, 15));
						g.setColor(Color.BLACK);
						g.drawString("End Turn", 100, 937);
						g.drawRect(25,910,200,40);
					}
					
					
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 24));
                    if(settling){
                        g.drawString("Place Settlement",25,74);
                        // g.setColor(Color.GREEN);
                        // for(x = 0; x < 1024; x++){
                        // 	for(y = 0; y < 1024; y++){
                        // 		double c2 = (x-settleX)/72.5;
                        // 		int realC2 = (int)Math.round(c2);
                        // 		double r2 = (y-settleY + 43*(((int)(realC2) + (int)Math.round((y-settleY)/127.0))%2))/127.0;
                        // 		int realR2 = (int)Math.round(r2);
                                
                        // 		if(Math.pow(c2-realC2,2) + Math.pow(r2-realR2,2)*3 <= 0.05 && game.validSettlement(realR2, realC2)){
                        // 			g.fillRect(x,y,1,1);
                        // 		}
                        // 	}
                        // }
                    } else if (roading){
                        if(firstClick){
                            g.drawString("Click Starting Point of Road",25,74);
                        } else {
                            g.drawString("Click Ending Point of Road",25,74);
                        }
                        
                    } else if (citing){
                        g.drawString("Upgrade a Settlement to a City",25,74);

                    } else if (robbing){
                        g.drawString("Click the center of a Tile to block resources from",25,74);

                    } else if (robbing2){
                        g.drawString("Click a Settlement adjacent to the robbed tile to steal from",25,74);

                    } else if (freeResources > 0){
                        g.drawString("Click one of your resource slots to get one of that card",25,74);
                        
                    } else if (monopoly){
                        g.drawString("Click one of your resource slots to get all copies of it",25,74);
                        
                    }
                    


				} else {
					g.setColor(Color.BLACK);
					g.setFont(new Font("Arial", Font.PLAIN, 15));
					g.drawString("Player " + game.getSelected() + "'s Turn" + " (" + game.getPlayer(game.getSelected()).getUsername() + ")", 25,900);

				}
                if(game.getPhase() == 1){
                    g.setColor(Color.MAGENTA);
                    g.fillRect(1034,979,380,35);
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.PLAIN, 15));
                    g.drawString("Make Trade", 1184,1003);
                }
                

			    if(game.getSelected() == index && game.getPhase() == 1){
                    for(int i = 0; i < game.getTrades().size(); i++){
                        drawTrade(g,game.getTrades().get(i), i);
                    }
                    tradesToDisplay = game.getTrades().size();
                } else if (game.getPhase() == 1){
                    int counter = 0;
                    for(int i = 0; i < game.getTrades().size(); i++){
                        if(game.getTrades().get(i).getIndex2() == index){
                            drawTrade(g,game.getTrades().get(i), counter);
                            counter++;
                        }
                    }
                    tradesToDisplay = counter;
                }

			}
            if(!trading){ // open shop, show trades
                if(game.getSelected() == index && game.getPhase() == 1){
                    g.setColor(Color.WHITE);
                    g.fillRect(800,24,200,50);
                    g.setColor(Color.BLACK);
                    g.drawRect(800,24,200,50);
                    g.setFont(new Font("Arial", Font.PLAIN, 10));
                    g.drawString("Open/Close Shop or Trade with Bank", 810, 53);
                    for(int i = 0; i < game.getTrades().size(); i++){
                        drawTrade(g,game.getTrades().get(i), i);
                    }
                    tradesToDisplay = game.getTrades().size();
                } else if (game.getPhase() == 1){
                    int counter = 0;
                    for(int i = 0; i < game.getTrades().size(); i++){
                        if(game.getTrades().get(i).getIndex2() == index){
                            drawTrade(g,game.getTrades().get(i), counter);
                            counter++;
                        }
                    }
                    tradesToDisplay = counter;
                }
            }
			
			
			Player self = game.getPlayer(index);
			int[] resources = self.getResources();
            if(trading || shop || toDiscard > 0){
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.BLACK);
            }
			
            //resources
			
            g.setFont(new Font("Arial", Font.BOLD, 15));
            g.drawString("ORE: " + resources[0], 300, 865);
            g.drawString("BRICK: " + resources[1], 400, 865);
            g.drawString("WHEAT: " + resources[2], 500, 865);
            g.drawString("SHEEP: " + resources[3], 600, 865);
            g.drawString("WOOD: " + resources[4], 700, 865);

            for(int i = 0; i < resources[0]; i++){
                g.drawImage(oreCardImage, 300, 875+22*i,null);
            }
            for(int i = 0; i < resources[1]; i++){
                g.drawImage(brickCardImage, 400, 875+22*i,null);
            }
            for(int i = 0; i < resources[2]; i++){
                g.drawImage(wheatCardImage, 500, 875+22*i,null);
            }
            for(int i = 0; i < resources[3]; i++){
                g.drawImage(sheepCardImage, 600, 875+22*i,null);
            }
            for(int i = 0; i < resources[4]; i++){
                g.drawImage(woodCardImage, 700, 875+22*i,null);
            }


		} else {
            g.drawString("Press R to restart after the game is over", 300, 300);
        }
		
		
	} 


	public Dimension getPreferredSize() {
		return new Dimension(1524,1080);
	}


	public void connect() throws IOException{

		repaint();

        try {
			hostName = "localhost"; 
			portNumber = 1024;
			socket = new Socket(hostName, portNumber);




			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

		} catch (UnknownHostException e) {
			System.err.println("Host unkown: " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}


		//listens for inputs
		while (true) {

			try{
				Object tlastInput = in.readObject();
				if(tlastInput instanceof Integer){
					index = (int)tlastInput;
					ownColor = indexToColor[index];
				} else if (tlastInput instanceof Game){
					game = (Game)tlastInput;
					// System.out.println("receieved");
					started = true;
					if(game.phaseChange()){
						settling = false;
						roading = false;
					}
                    if(game.shouldRob() == true){
                        if(game.getSelected() == index){
                            robbing = true;
                        }
                        if(game.getPlayer(index).cardCount() >= 8){
                            toDiscard = game.getPlayer(index).cardCount()/2;
                        }
                    }
				}
				repaint();

			} catch (UnknownHostException e) {
				System.err.println("Host unkown: ");
				System.exit(1);
			} catch (IOException e) {
				System.err.println(e);
				System.exit(1);
			} catch (ClassNotFoundException e){
				System.err.println("no class");
				System.exit(1);
			}

			
			

		}
	}
	public void actionPerformed(ActionEvent e) { 
        if(e.getSource() == startButton){
			startButton.setVisible(false);
			username = usernameField.getText();
			usernameField.setVisible(false);

			try{
				out.reset();
				out.writeObject(username);
			} catch (UnknownHostException ex) {
				System.err.println("Host unkown: " + hostName);
				System.exit(1);
			} catch (IOException ex) {
				System.err.println("Couldn't get I/O for the connection to " + hostName);
				System.exit(1);
			}

		}
		repaint();
    }
    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}
    
    public void mouseMoved(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        if(!started){
            return;
        }
		if(toDiscard > 0){ // discarding

			// g.drawImage(oreCardImage, 300, 875,null);
			// g.drawImage(brickCardImage, 400, 875,null);
			// g.drawImage(wheatCardImage, 500, 875,null);
			// g.drawImage(sheepCardImage, 600, 875,null);
			// g.drawImage(woodCardImage, 700, 875,null);

            if(e.getY() > 875 && (e.getX()-300)%100 < 80 && (e.getX()-300)/100 >= 0 && (e.getX()-300)/100 < 5 && e.getY() < 973+22*game.getPlayer(index).getResources()[(e.getX()-300)/100]){
                if(game.getPlayer(index).getResources()[(e.getX()-300)/100] > 0){
                    int[] tempRes = new int[]{0,0,0,0,0};
                    tempRes[(e.getX()-300)/100] = 1;
                    game.getPlayer(index).removeResources(tempRes);
                    toDiscard--;
                }
                
            }
            updateGame();
            
        
        } else if(robbing){ // robber
            int a;
            for(int i = 0; i < 5; i++){
                if(i <= 2){
                    a = 0;
                } else {
                    a = i-2;
                }
                for(int k = a; k < a + 5-(Math.abs(2-i)); k++){
                    Tile tempTile = game.getGrid()[i][k];
                    int r = tempTile.getR();
                    int c = tempTile.getC();

                    int x = 301+(int)(trueHeight * Math.min(r,c)*cos60);
                    int y = 189+(int)(trueHeight * Math.min(r,c)*sin60);
                    if(r > c){
                        x -= (int)(trueHeight*(r-c)*cos60);
                        y += (int)(trueHeight*(r-c)*sin60);
                    } else if (r < c) {
                        x += trueHeight*(c-r);
                    }


                    if(x < e.getX() && e.getX() < 135+x && y < e.getY() && e.getY() < y+79){

                        if(game.rob(r,c)){
                            robbing = false;
                            if(game.canStealFrom(r,c)){
                                robbing2 = true;
                            }
                            
                        }
                        
                    }
                }
            }
            updateGame();
            
        
        } else if (robbing2){ // robbing2
            double c = (e.getX()-settleX)/72.5;
			int realC = (int)Math.round(c);
			double r = (e.getY()-settleY + 43*(((int)(realC) + (int)Math.round((e.getY()-settleY)/127.0))%2))/127.0;
			int realR = (int)Math.round(r);
			
			if(Math.pow(c-realC,2) + Math.pow(r-realR,2)*3 <= 0.05 && game.validSettlement(realR,realC)){
                if(game.steal(realR,realC)){
                    robbing2 = false;
                }

            }
            updateGame();
            
        } else if (trading) {
            if(recipient != -1 && e.getX() > 1034 && e.getX() < 1414 && e.getY() > 979 && e.getY() < 1014){ // send trade
                if(game.getSelected() == index){ // send to specific
                    game.makeTrade(recipient, tradeResources1, tradeResources2, true, false);
                } else { //send to selected
                    game.makeTrade(index, tradeResources1, tradeResources2, false, true);
                }
                updateGame();
                trading = false;
            } else if(e.getX() > 1034 && e.getX() < 1414 && e.getY() > 909 && e.getY() < 944){ // exit trade
                trading = false;
            } else if (game.getSelected() == index && e.getY() > 50 && e.getY() < 100 && (e.getX()-50)%100 <= 50 && (e.getX()-50)/100 >= 0 && (e.getX()-50)/100 < game.getSize() && (e.getX()-50)/100 != index){ //select recipient
                recipient = (e.getX()-50)/100;
            } else if ((game.getSelected() != index || (recipient != -1) ) && e.getY() > 350 && e.getY() < 400 && (e.getX()-50)%100 <= 50 && e.getX() > 50 && (e.getX()-50)/100 >= 0 && (e.getX()-50)/100 < 5){ //select self resources
                System.out.println((e.getX()-50)/100);
                if(game.getSelected() == index && game.getPlayer(index).getResources()[(e.getX()-50)/100] > tradeResources1[(e.getX()-50)/100]){
                    tradeResources1[(e.getX()-50)/100] += 1;
                } else if (game.getSelected() != index && game.getPlayer(index).getResources()[(e.getX()-50)/100] > tradeResources2[(e.getX()-50)/100]){
                    tradeResources2[(e.getX()-50)/100] += 1;
                }
            } else if ((game.getSelected() != index || (recipient != -1) ) && e.getY() > 650 && e.getY() < 700 && (e.getX()-50)%100 <= 50 && e.getX() > 50 && (e.getX()-50)/100 >= 0 && (e.getX()-50)/100 < 5 ){ //select recipient resources
                System.out.println((e.getX()-50)/100);
                if(game.getSelected() == index){
                    tradeResources2[(e.getX()-50)/100] += 1;
                } else if (game.getSelected() != index){
                    tradeResources1[(e.getX()-50)/100] += 1;
                }
            }
        } else if(e.getX() > 800 && e.getX() < 1000 && e.getY() > 24 && e.getY() < 74 && game.getPhase() == 1){ // open shop
			if(shop){
				shop = false;
			} else {
                toTradeBank = 4;
                bankS1 = -1;
                bankS2 = -1;
				shop = true;
			}

		} else if (shop) {
            if(e.getX() > 678 && e.getX() < 778 && e.getY() > 25 && e.getY() < 425){
                switch((e.getY()-25)/100){
                    case 0:
                        if(game.getPlayer(index).canRoad()){
                            shop = false;
                            roading = true;
                        }
                        
                        break;
                    case 1:
                        if(game.getPlayer(index).canSettlement()){
                            shop = false;
                            settling = true;
                        }
                        
                        break;
                    case 2:
                        if(game.getPlayer(index).canCity()){
                            shop = false;
                            citing = true;
                        }
                        break;
                    case 3:
                        if(game.getPlayer(index).devCard()){
                            shop = false;
                            game.addDevCard();
                        }
                        break;

                }
            } else {
                if(e.getY() > 875 && (e.getX()-300)%100 < 80 && (e.getX()-300)/100 >= 0 && (e.getX()-300)/100 < 5 && e.getY() < 973+22*game.getPlayer(index).getResources()[(e.getX()-300)/100]){ // clicking yours
                    boolean[] unlockedTrades = game.getPlayer(index).getUnlockedTrades();
                    int cardIndex = (e.getX()-300)/100;
                    if(unlockedTrades[5] == true){
                        toTradeBank = 3;
                    }
                    if(unlockedTrades[cardIndex] == true){
                        toTradeBank = 2;
                    }
                    if(game.getPlayer(index).getResources()[(e.getX()-300)/100] >= toTradeBank){
                        bankS1 = cardIndex;
                    }
                    
                } else if(e.getY() > 875 && e.getY() < 995 && (e.getX()-900)%100 < 80 && (e.getX()-900)/100 >= 0 && (e.getX()-900)/100 < 5 && bankS1 != -1){ // clicking bank
                    bankS2 = (e.getX()-900)/100;

                    if(game.getPlayer(index).getResources()[bankS1] >= toTradeBank){
                        int[] toGiveBank = {0,0,0,0,0};
                        int[] toRecieveFromBank = {0,0,0,0,0};

                        toGiveBank[bankS1] = toTradeBank;
                        toRecieveFromBank[bankS2] = 1;

                        game.getPlayer(index).removeResources(toGiveBank);
                        game.getPlayer(index).addResources(toRecieveFromBank);

                        bankS1 = -1;
                        bankS2 = -1;
                        toTradeBank = 4;
                    }
                    

                    
                }
            }

        } else if (monopoly){
            if(e.getY() > 875 && (e.getX()-300)%100 < 80 && (e.getX()-300)/100 >= 0 && (e.getX()-300)/100 < 5 && e.getY() < 973+22*game.getPlayer(index).getResources()[(e.getX()-300)/100]){
                int[] tempResources = {0,0,0,0,0};
                for(int i = 0; i < game.getSize(); i++){
                    tempResources[(e.getX()-300)/100] += game.getPlayer(i).monopoly((e.getX()-300)/100);
                }
                game.getPlayer(index).addResources(tempResources);
                monopoly = false;
            }
            updateGame();
        } else if(game.getSelected() == index && e.getX() > 1434 && e.getX() < 1514 && (e.getY()-50)%130 <= 120 && (e.getY()-50)/130 >= 0 && (e.getY()-50)/130 < game.getPlayer(index).getDevCards().size()){ // click devCard
            DevCard devCardClicked = game.getPlayer(index).getDevCards().get((e.getY()-50)/130);
            if(!devCardClicked.usable()){
                return;
            }
            playSound2();
            int type = devCardClicked.getType();// get devcard index
            switch(type){
                case 0:
                    game.addKnight(index);
                    robbing = true;
                    break;
                case 1:
                    freeRoads = 2;
                    roading = true;
                    break;
                case 2:
                    freeResources = 2;
                    break;
                case 3:
                    monopoly = true;
                    break;
                case 4:
                    return;
            }
            game.getPlayer(index).removeCard((e.getY()-50)/130);
            updateGame();
            
        } else if(freeResources > 0 && e.getY() > 875 && (e.getX()-300)%100 < 80 && (e.getX()-300)/100 >= 0 && (e.getX()-300)/100 < 5 && e.getY() < 973+22*game.getPlayer(index).getResources()[(e.getX()-300)/100]){ // free Resources
            int[] tempRes = new int[]{0,0,0,0,0};
            tempRes[(e.getX()-300)/100] = 1;
            game.getPlayer(index).addResources(tempRes);
            freeResources--;
                
        } else if(e.getX() > 25 && e.getX() < 225 && e.getY() > 910 && e.getY() < 950 && game.getSelected() == index && !(settling || roading)){ // end turn
			// System.out.println("work");
			game.next();
			updateGame();
		} else if ((roading || settling || citing) && started && game.getSelected() == index){ //placing (settlement, etc)
			double c = (e.getX()-settleX)/72.5;
			int realC = (int)Math.round(c);
			double r = (e.getY()-settleY + 43*(((int)(realC) + (int)Math.round((e.getY()-settleY)/127.0))%2))/127.0;
			int realR = (int)Math.round(r);
			
			if(Math.pow(c-realC,2) + Math.pow(r-realR,2)*3 <= 0.05 && game.validSettlement(realR,realC)){
				System.out.println("clicking " + realR + ", " + realC);
                if(citing){
                    if(game.addCity(realR,realC)){
                        playSound();
                        citing = false;
                    }
                }if(settling){
					if (game.addSettlement(realR,realC)){
						playSound();
						if(game.getPhase() == 0){
							// settling = true; //temporary, should road next
							roading = true;
						} else {
							
						}
						settling = false;
					}
				} else if (roading && firstClick){
					firstClick = false;
					clickR = realR;
					clickC = realC;

				} else if (roading && !firstClick){
					if(freeRoads > 0 && game.addFreeRoad(clickR,clickC,realR,realC)){
                        freeRoads--;
                        firstClick = true;
                        if(freeRoads == 0){
                            roading = false;
                        }
                    }
					if (game.addRoad(clickR,clickC,realR,realC)){
						if(game.getPhase() == 0){
							settling = true;
							game.next();
							
						} else {
							
						}
						roading = false;
					} else {
						
					}
					firstClick = true;
				}
				updateGame();
			}

			// System.out.println(Math.pow(((e.getX()-155.0)/72.5),1) +"," + Math.pow((e.getY() + 78*(((e.getX()-155.0)/72.5)%2) - 211)/117.0, 1));
		} else if (e.getX() > 1364 && e.getX() < 1409 && (e.getY()-75)%120 < 45 && Math.floor((e.getY()-75.0)/120) >= 0 && Math.floor((e.getY()-75.0)/120) < tradesToDisplay){ // accept/deny trade
            Trade selectedTrade = game.getTrades().get((int)Math.floor((e.getY()-75.0)/120));
            if(game.getSelected() == index){
                boolean original = selectedTrade.getAgree1();
                if(original == true){
                    selectedTrade.setAgree1(false);
                } else if(selectedTrade.check1(game.getPlayer(index).getResources())){
                    selectedTrade.setAgree1(true);
                }
                
            } else {
                boolean original = selectedTrade.getAgree2();
                if(original == true){
                    selectedTrade.setAgree2(false);
                } else if(selectedTrade.check2(game.getPlayer(index).getResources())){
                    selectedTrade.setAgree2(true);
                }
                
            }
            game.recheckTrades();
			updateGame();
            // int startX = 1034;
            // int startY = 70+tradePriority*120;
            //g.fillRect(1364,120*mult+75,45,45);

        } else if (game.getPhase() == 1 && e.getX() > 1034 && e.getX() < 1414 && e.getY() > 979 && e.getY() < 1014){ // enter trade menu
            trading = true;
            if(game.getSelected() == index){
                recipient = -1;
            } else {
                recipient = game.getSelected();
            }
            tradeResources1 = new int[5];
            tradeResources2 = new int[5];
        }
		// System.out.println("FakeR:" + (int)Math.round((e.getY()-172)/127.0));
		// System.out.println((e.getY()-191 + 39*(((int)(Math.round((e.getX()-155)/72.5)) + (int)Math.round((e.getY()-172)/127.0))%2))/127.0 + "," + (e.getX()-155)/72.5);
		// repaint();
		System.out.println(e.getX() + "," + e.getY());
		repaint();


    }
	public void updateGame(){
		try{
            game.checkForWin();
			out.reset();
			out.writeObject(game);
			// System.out.println("SENDING");
		} catch (UnknownHostException ex) {
			System.err.println("Host unkown: " + hostName);
			System.exit(1);
		} catch (IOException ex) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}
	}



	public void playSound() {


        try {
            URL url = this.getClass().getClassLoader().getResource("cannon.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }
    public void playSound2() {


        try {
            URL url = this.getClass().getClassLoader().getResource("chime.wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(url));
            clip.start();
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }
	public void drawTrade(Graphics g, Trade trade, int tradePriority){
		g.setColor(Color.WHITE);
		int startX = 1034;
		int startY = 70+tradePriority*120;
		g.fillRect(startX,startY,380,110);
        int tempindex1;
        int tempindex2;
        int[] tempresources1;
        int[] tempresources2;
        boolean tempagree1;
        boolean tempagree2;

        if(game.getSelected() == index){
            tempindex1 = trade.getIndex1();
            tempindex2 = trade.getIndex2();
            tempresources1 = trade.getResources1();
            tempresources2 = trade.getResources2();
            tempagree1 = trade.getAgree1();
            tempagree2 = trade.getAgree2();
        } else {
            tempindex1 = trade.getIndex2();
            tempindex2 = trade.getIndex1();
            tempresources1 = trade.getResources2();
            tempresources2 = trade.getResources1();
            tempagree1 = trade.getAgree2();
            tempagree2 = trade.getAgree1();
        }
        g.setColor(indexToColor[tempindex1]);
        g.fillRect(startX+8,startY+5,63,17);
        g.setColor(indexToColor[tempindex2]);
        g.fillRect(startX+8,startY+60,63,17);
		
		g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.setColor(Color.BLACK);
        g.drawString("Player " + tempindex1 + ": ", startX + 10, startY + 20);
        if(tempagree1){
            g.setColor(indexToColor[tempindex1]);
            g.fillRect(startX+330,startY+5,45,45);
        }
        g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 7));
        int counter = 0;
        if(tempresources1[0] != 0){
            g.drawString("ORE: " + tempresources1[0], startX+10+counter*50, startY+30);
            counter++;
        }
        if(tempresources1[1] != 0){
            g.drawString("BRICK: " + tempresources1[1], startX+10+counter*50, startY+30);
            counter++;
        }
        if(tempresources1[2] != 0){
            g.drawString("WHEAT: " + tempresources1[2], startX+10+counter*50, startY+30);
            counter++;
        }
        if(tempresources1[3] != 0){
            g.drawString("SHEEP: " + tempresources1[3], startX+10+counter*50, startY+30);
            counter++;
        }
        if(tempresources1[4] != 0){
            g.drawString("WOOD: " + tempresources1[4], startX+10+counter*50, startY+30);
            counter++;
        }
        g.setColor(Color.BLACK);
        g.drawRect(startX+330,startY+5,45,45);
		g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawLine(startX,startY+55,startX+175,startY+55);
        g.drawLine(startX+205,startY+55,startX+380,startY+55);
        g.drawString("For",startX+179,startY+60);
        if(tempagree2){
            g.setColor(indexToColor[tempindex2]);
            g.fillRect(startX+330,startY+60,45,45);
        }
        g.setColor(Color.BLACK);
        g.drawRect(startX+330,startY+60,45,45);
        g.drawString("Player " + tempindex2 + ": ", startX + 10, startY + 75);
        g.setFont(new Font("Arial", Font.PLAIN, 7));
        counter = 0;
        if(tempresources2[0] != 0){
            g.drawString("ORE: " + tempresources2[0], startX+counter*50, startY+85);
            counter++;
        }
        if(tempresources2[1] != 0){
            g.drawString("BRICK: " + tempresources2[1], startX+counter*50, startY+85);
            counter++;
        }
        if(tempresources2[2] != 0){
            g.drawString("WHEAT: " + tempresources2[2], startX+counter*50, startY+85);
            counter++;
        }
        if(tempresources2[3] != 0){
            g.drawString("SHEEP: " + tempresources2[3], startX+counter*50, startY+85);
            counter++;
        }
        if(tempresources2[4] != 0){
            g.drawString("WOOD: " + tempresources2[4], startX+counter*50, startY+85);
            counter++;
        }

	}
    public void keyPressed(KeyEvent e){
		System.out.println(e.getKeyCode());
		if(e.getKeyCode() == 82 && game.getWinner() != -1){ // r (restart after game is done)
            try{
                out.reset();
                out.writeObject("restart");
                // System.out.println("SENDING");
            } catch (UnknownHostException ex) {
                System.err.println("Host unkown: " + hostName);
                System.exit(1);
            } catch (IOException ex) {
                System.err.println("Couldn't get I/O for the connection to " + hostName);
                System.exit(1);
            }
		} else if (e.getKeyCode() == 89){ // y (end game)
            game.getPlayer(index).addVP(10);
            updateGame();
		}
		repaint();
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}



}
// (e.getX()-155)/72.5 = c;
// (y-191 + 39*((e.getX()-155)/72.5 + Math.round((e.getY()-172)/127))%2)/127.0 = r
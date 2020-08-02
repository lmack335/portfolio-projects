//improvements --> none :-)

function shuffleDeck() {
	var turn = 0;
	var playerNum = prompt("Number of players?");
	var chasmApproaches = false;
	
	//starting arrays of all four decks by color
	var redCards = colorDecks("red");
	var yellowCards = colorDecks("yellow");
	var blueCards = colorDecks("blue");
	var greenCards = colorDecks("green");

	var masterDeck = resetMasterDeck();
	
	//7 sub decks of 4 cards, 1 random card from each starting deck, in random order
	var deck1 = deckDraw();
	var deck2 = deckDraw();
	var deck3 = deckDraw();
	var deck5 = deckDraw();

	//insert chasm card randomly into deck #4
	var deck4 = deckDraw();
	deck4.splice(Math.floor(Math.random()*(deck4.length + 1)), 0, "chasm");
	
	//starting deposits at beginning of game
	var deck6 = deckDraw();
	/*console.log("Starting deposits are:");
	for (var i = 0; i < deck6.length; i++) {
	  depositMessage(deck6[i]);
	}*/

	//chasm deck
	var deck7 = deckDraw();
	
	//combine decks 1-5 --> deposit pile
	var depositDeck = deck2.concat(deck3).concat(deck4).concat(deck5);
	if (playerNum == 4) {
		depositDeck = deck1.concat(depositDeck);
	}
	//console.log("depositDeck is " + depositDeck.join(", "));
	
}

function playGame() {
	//document.getElementById('entireDepositDeck').innerHTML = depositDeck.join(", ");
}

function colorDecks(colorName) {
	var colorDeckArray = [];
	for (var i = 1; i < 9; i++) {
		colorDeckArray.push(colorName + " " + i);
  }
  return colorDeckArray;
}

function resetMasterDeck() {
  var newMasterDeck = ["red", "yellow", "green", "blue"];
  return newMasterDeck;
}


//randomize the draw order from each deck
function drawOrder() {
  var drawingOrder = [];
  for (var i=0; i<4; i++) {
    var randomDeckColor = masterDeck[Math.floor(Math.random()*masterDeck.length)];
    //console.log("randomDeckColor is " + randomDeckColor);
    drawingOrder.push(randomDeckColor);
    //console.log("drawOrder is " + drawOrder);
    var removeElement = masterDeck.indexOf(randomDeckColor);
    masterDeck.splice(removeElement, 1);
    //console.log("masterDeck is now " + masterDeck);
    //deck1[i] = 0;
  }
  resetMasterDeck();
  return drawingOrder;
}

//select a random card from each color deck to create a single deck of 4
function deckDraw() {
  var deck = [];

  var drawCardOrder = drawOrder();
  //console.log("drawOrder is " + drawOrder);

  for (var i=0; i<4; i++) {
    
    switch(drawCardOrder[i]) {
      case "red":
        var randomCard = redCards[Math.floor(Math.random()*redCards.length)];
        //console.log("RED randomCard is " + randomCard);
        
        deck.push(randomCard);
        //console.log("deck1 is " + deck1);
        
        var removeElement = redCards.indexOf(randomCard);
        redCards.splice(removeElement, 1);

        //console.log("redCards is now " + redCards);
        break;

      case "yellow":
        var randomCard = yellowCards[Math.floor(Math.random()*yellowCards.length)];
        //console.log("YELLOW randomCard is " + randomCard);
        
        deck.push(randomCard);
        //console.log("deck1 is " + deck1);
        
        var removeElement = yellowCards.indexOf(randomCard);
        yellowCards.splice(removeElement, 1);

        //console.log("yellowCards is now " + yellowCards);
        break;

      case "green":
        var randomCard = greenCards[Math.floor(Math.random()*greenCards.length)];
        //console.log("GREEN randomCard is " + randomCard);
        
        deck.push(randomCard);
        //console.log("deck1 is " + deck1);
        
        var removeElement = greenCards.indexOf(randomCard);
        greenCards.splice(removeElement, 1);

        //console.log("greenCards is now " + greenCards);
        break;

      case "blue":
        var randomCard = blueCards[Math.floor(Math.random()*blueCards.length)];
        //console.log("BLUE randomCard is " + randomCard);
        
        deck.push(randomCard);
        //console.log("deck1 is " + deck1);
        
        var removeElement = blueCards.indexOf(randomCard);
        blueCards.splice(removeElement, 1);

        //console.log("blueCards is now " + blueCards);
        break;
    }
  }

return deck;
}


//reveal deposit deck cards in order on each turn (press button to continue)
//if chasm drawn, each player gets a final round of deposits before chasm tokens start



function nextDeposit() {

	if (depositDeck[turn] == "chasm") {
		console.log("CHASM!!!");
		chasmApproaches = true;
		turn++;
	}

	depositMessage(depositDeck[turn]);

	if (chasmApproaches) {
		playerNum--;
	}
	turn++;
}

function depositMessage(card) {
  var message = "Next Deposit is " + card + ", ";
  if (card.endsWith("6") || card.endsWith("7") || card.endsWith("8")) {
    message = message + "place 3 gemstones.";
  } else {
    message = message + "place 2 gemstones.";
  }
  console.log(message);
}


//chasm rounds
/*
//initialize chasm tokens
var chasmTokens = ["red chasm", "red chasm", "red chasm", "red chasm", "red chasm", "yellow chasm", "yellow chasm", "yellow chasm", "yellow chasm", "yellow chasm", "green chasm", "green chasm", "green chasm", "green chasm", "green chasm", "blue chasm", "blue chasm", "blue chasm", "blue chasm", "blue chasm"];

//randomly select a color chasm token
while (chasmTokens.length > 0) {
  var chasmTokenIndex = Math.floor(Math.random()*chasmTokens.length);
  //console.log("chasmTokenIndex is " + chasmTokenIndex);
  console.log("Chasm Token drawn was " + chasmTokens[chasmTokenIndex]);
  chasmTokens.splice(chasmTokenIndex, 1);
}

console.log("Game Over!");
*/

//hit button to start scoring app if game has ended?
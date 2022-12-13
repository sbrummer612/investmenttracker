//var stocks = new Stocks('SYTCQBUIU44BX2G4'); // Replace with your own
var stocks = new Stocks('U1QTDTR2CX2DKCMS');

// Let's get the stock data of Tesla Inc for the last 10 minutes
async function request () {
/** 
  var result = await stocks.timeSeries({
    symbol: 'TSLA',
    interval: '1min',
    amount: 10
   });
*/

var result = await stocks.timeSeries({
    symbol: 'SPY',
    interval: '1min',
    performance: 'real-time',
    amount: 10
   });

   document.body.innerHTML = JSON.stringify(result);
  
}

request();
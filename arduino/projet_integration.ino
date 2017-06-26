#include <LiquidCrystal.h>

LiquidCrystal lcd(8, 9, 4, 5, 6, 7);


/* Define state machine */
typedef enum
{
  C_IDLE,
  C_OUTPUT
} CCLOCK;

typedef enum
{
  WS_IDLE,
  WS_FORECAST
} WEATHER_STATE;

typedef enum
{
  BS_IDLE,
  BS_INCREASE,
  BS_DECREASE,
  BS_NORMALDAY
} BUTTON_STATE;


//Current state
CCLOCK cClock;
WEATHER_STATE weatherState;
BUTTON_STATE buttonState;


//Macros
int BUTTON_PIN = A0;
#define BUTTON_UP  0
#define BUTTON_DOWN  1
#define BUTTON_RESET  2


//Shared variables
int normalDay, j, weatherActual, weatherForecast, wf;
long second, hour;
unsigned long chrono;
boolean weatherGenerated, etatButton;
String data;

//Define clock function
void clockInit();
void clockUpdate();
void clockOutput();

//Define weather state function
void weatherInit();
void weatherUpdate();
void weatherOutput();

//Define button state function
void buttonInit();
void buttonUpdate();
void buttonOutput();



//Setup is for initialize variables
void setup() {
  Serial.begin(9600);
  lcd.begin(16, 2);
  clockInit();
  weatherInit();
  buttonInit();
}

//Main loop of the program
void loop() {
  clockUpdate();
  clockOutput();
  weatherUpdate();
  weatherOutput();
  buttonUpdate();
  buttonOutput();
  Serial.print("second    ");
  Serial.println(second);
//  lcd.print(analogRead(A0));
}



//Initialize the clock of the game
void clockInit()
{
  chrono = millis();
  hour = 0;
}

//Change the state of the clock when a day is past
void clockUpdate()
{
  CCLOCK nextState = cClock;
  switch (cClock)
  {
    case C_IDLE:
      if (millis() - chrono >= second)
      {
        nextState = C_OUTPUT;
      }
      break;

    case C_OUTPUT:
      nextState = C_IDLE;
      break;
  }

  cClock = nextState;
}

//Send hour to serial port
void clockOutput()
{
  switch (cClock)
  {
    case C_IDLE:
      break;

    case C_OUTPUT:
      hour++;
      chrono = millis();
      data = "";
      data += "[";
      data += hour;
      data += "|";
      data += weatherActual;
      data += "|";
      data += weatherForecast;
      data += "]";
      Serial.println(data);           //Format of data : [hour|weatherActual|weatherForecast]
      Serial.flush();

      /* Display hour et actual weather on the LCD */
      lcd.setCursor(0, 0);
      lcd.print("HOUR : ");
      lcd.print(hour);
      lcd .setCursor(0, 1);
      lcd.print("WEATHER : ");
      lcd.print(weatherActual);
      break;
  }
}


//Initialize weather at day 1
void weatherInit()
{
  weatherGenerated = false;
  randomSeed(analogRead(A5));
  weatherActual = random(0, 5);
  weatherForecast = random(0, 5);
}

//Update of the weather this day and day after
void weatherUpdate()
{
  WEATHER_STATE nextState = weatherState;
  switch (weatherState)
  {
    case WS_IDLE:
      if ((hour % 24 == 23) && !weatherGenerated)
      {
        nextState = WS_FORECAST;
        weatherGenerated = true;

      }
      if ((hour % 24 == 0) && weatherGenerated)
      {
        weatherGenerated = false;
      }
      break;

    case WS_FORECAST:
      weatherForecast = random(0, 5);
      weatherActual = W(weatherForecast);
      nextState = WS_IDLE;
      break;
  }

  weatherState = nextState;
}

//Nothing to do
void weatherOutput()
{
  switch (weatherState)
  {
    case WS_IDLE:
      //Nothing to do
      break;

    case WS_FORECAST:
      //Nothing to do
      break;
  }
}



void buttonInit()
{
  pinMode(BUTTON_PIN, INPUT);
  second = 10000;
  normalDay = 10000;
  etatButton = false;
}

void buttonUpdate()
{
  BUTTON_PIN = analogRead(A0);
  etatButton = digitalRead(BUTTON_PIN);
  BUTTON_STATE nextState = buttonState;

  switch (buttonState)
  {
    case BS_IDLE:
      if (BUTTON_PIN >= 50 && BUTTON_PIN < 250 && etatButton)
      {
        nextState = BS_INCREASE;
      }
      else if (BUTTON_PIN >= 250 && BUTTON_PIN < 450 && etatButton)
      {
        nextState = BS_DECREASE;
      }
      else if (BUTTON_PIN >= 650 && BUTTON_PIN < 850 && etatButton)
      {
        nextState = BS_NORMALDAY;
      }
      break;

    case BS_INCREASE:
      nextState = BS_IDLE;
      break;
    case BS_DECREASE:
      nextState = BS_IDLE;
      break;
    case BS_NORMALDAY:
      nextState = BS_IDLE;
      break;

  }

  buttonState = nextState;
}

void buttonOutput()
{
  switch (buttonState)
  {
    case BS_IDLE:
      second += 0;
      break;
    case BS_INCREASE:
      second -= 50;
      if (second < 3000)
      {
        second = 300;
      }
      break;
    case BS_DECREASE:   {
        second += 50;
        if (second > 20000)
        {
          second = 20000;
        }
        break;
      }
    case BS_NORMALDAY:
      second = normalDay;
      break;
  }
}


//This function allow to forecast for the day d+1
int W (int wf)
{
  j = random(0, 100);
  if (j >= 0 && j < 5)
  {
    return ((wf + 5 - 1) % 5);
  }
  if (j >= 5 && j < 10)
  {
    return ((wf + 1) % 5);
  }
  if (j == 10)
  {
    return ((wf + 5 - 2) % 5);
  }
  if (j == 11)
  {
    return ((wf + 2) % 5);
  }
  else
  {
    return wf;
  }
}






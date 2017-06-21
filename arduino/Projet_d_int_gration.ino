


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
  BS_PRESSED,
  BS_INCREASE,
  BS_DECREASE,
  BS_RESET
} BUTTON_STATE;


//Current state
CCLOCK cClock;
WEATHER_STATE weatherState;
BUTTON_STATE buttonState;


//Macros
#define  BUTTON_PIN  A0


//Shared variables
unsigned long chrono;
long second, hour;
int randWeather;
boolean weatherGenerated;


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



void setup() {
  Serial.begin(9600);
  clockInit();
  weatherInit();
}

void loop() {
  clockUpdate();
  clockOutput();
  weatherUpdate();
  weatherOutput();
}



void clockInit()
{
  chrono = millis();
  second = 1000;
  hour = 0;
}

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

  cClock  = nextState;
}

void clockOutput()
{
  switch (cClock)
  {
    case C_IDLE:
      break;

    case C_OUTPUT:
      hour ++;
      chrono = millis();
      Serial.print("Hour : ");
      Serial.print(hour);
      break;
  }
}




void weatherInit()
{
  weatherGenerated = false;
  randomSeed(analogRead(A5));
}

void weatherUpdate()
{
  WEATHER_STATE nextState = weatherState;
  switch (weatherState)
  {
    case WS_IDLE:
      if ((hour % 24 == 0) && !weatherGenerated)
      {
        nextState = WS_FORECAST;
        weatherGenerated = true;
      }
      if ((hour % 24 == 1) && weatherGenerated)
      {
        weatherGenerated = false;
      }
      break;

    case WS_FORECAST:
      nextState = WS_IDLE;
      break;
  }

  weatherState  = nextState;
}

void weatherOutput()
{
  switch (weatherState)
  {
    case WS_IDLE:
      //Nothing to do
      break;

    case WS_FORECAST:
      randWeather = random(0, 5);
      Serial.print ("Weather : ");
      Serial.print(randWeather);  //print -> read       //write -> scanf
      Serial.println();
      break;
  }
}





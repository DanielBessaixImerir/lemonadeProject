


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
#define BUTTON_PIN  A0
#define BUTTON_UP  250
#define BUTTON_DOWN  450
#define BUTTON_RESET  1000


//Shared variables
unsigned long chrono;
long second, hour;
int randWeather, normalDay;
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
  second = 500;
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
      Serial.print("[");
      Serial.print(hour);           //Hour
      Serial.print("|");
      Serial.print(randWeather);  //Weather
      Serial.println("]");
      Serial.flush();
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
      break;
  }
}





void buttonInit()
{
  pinMode(BUTTON_PIN, INPUT);
}

void buttonUpdate()
{
  BUTTON_STATE nextState = buttonState;

  switch (buttonState)
  {
    case BS_IDLE:
      if (BUTTON_PIN)
      {
        nextState = BS_PRESSED;
      }
      break;

    case BS_PRESSED:
      if (BUTTON_PIN < BUTTON_UP)
      {
        nextState = BS_INCREASE;
      }
      else if (BUTTON_PIN < BUTTON_DOWN)
      {
        nextState = BS_DECREASE;
      }
      else if (BUTTON_PIN > BUTTON_RESET)
      {
        nextState = BS_RESET;
      }
      break;

    case BS_INCREASE:
      if (!BUTTON_PIN)
      {
        nextState = BS_IDLE;
      }
      break;

    case BS_DECREASE:
      if (!BUTTON_PIN)
      {
        nextState = BS_IDLE;
      }
      break;

    case BS_RESET:
      if (!BUTTON_PIN)
      {
        nextState = BS_IDLE;
      }
      break;
  }

  buttonState = nextState;
}

void buttonOutput()
{
  switch (buttonState)
  {
    case BS_INCREASE:
      second += 1000;
      break;
    case BS_DECREASE:
      second -= 1000;
      break;

    case BS_RESET:
      second = normalDay;
      break;
  }
}

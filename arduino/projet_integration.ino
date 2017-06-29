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

} BUTTON_STATE;


//Current state
CCLOCK cClock;
WEATHER_STATE weatherState;
BUTTON_STATE buttonState;


//Macros


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

void loop() {
  clockUpdate();
  clockOutput();
  weatherUpdate();
  weatherOutput();
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

void clockOutput()
{
  switch (cClock)
  {
    case C_IDLE:
      break;

    case C_OUTPUT:

      break;
  }
}

void weatherInit()
{
  weatherGenerated = false;
  randomSeed(analogRead(A5));

void weatherUpdate()
{
  WEATHER_STATE nextState = weatherState;
  switch (weatherState)
  {
    case WS_IDLE:
      {
        weatherGenerated = false;
      }
      break;

    case WS_FORECAST:
      nextState = WS_IDLE;
      break;
  }


void weatherOutput()
{
  switch (weatherState)
  {
    case WS_IDLE:
      //Nothing to do
      break;

    case WS_FORECAST:
      break;
  }
}


}

void buttonUpdate()
{
  BUTTON_STATE nextState = buttonState;

  switch (buttonState)
  {
    case BS_IDLE:
      }
      break;

    case BS_INCREASE:
  }

  buttonState = nextState;
}

void buttonOutput()
{
  switch (buttonState)
  {
      second = normalDay;
      break;
  }
}
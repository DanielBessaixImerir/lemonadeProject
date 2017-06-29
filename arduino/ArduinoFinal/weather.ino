//***** Define state machine *****//
typedef enum
{
  WS_IDLE,
  WS_FORECAST
} WEATHER_STATE;

//***** Current *****//
WEATHER_STATE weatherState;

//***** Intermediate function *****//
int weather();
int forecast();

//***** Initialize weather at day 1 *****//
void weatherInit()
{
  weatherGenerated = false;
  randomSeed(analogRead(A5));
  weatherActual = random(0, 5);
  weatherForecast = random(0, 5);
}

//***** Update of the weather this day and day after *****//
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
      break;

    case WS_FORECAST:
      weatherActual = weather(weatherForecast);
      weatherForecast = forecast();
//      Serial.print("current : ");
//      Serial.print(j);
//      Serial.print("forecast: ");
//      Serial.print(i);
      weatherGenerated = false;
      nextState = WS_IDLE;
      break;
  }

  weatherState = nextState;
}

//***** Nothing to do *****//
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


//***** This function update the weather of today *****//
int weather (int wf)
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

//***** This function allow to forecast for the day d+1 *****//
int forecast()
{
  i = random(0, 20);
  if (i == 0)
  {
    return 4;
  }
  if (i >= 1 && i < 4)
  {
    return 0;
  }
  if (i >= 4 && i < 8)
  {
    return 1;
  }
  if (i >= 8 && i < 16)
  {
    return 2;
  }
  else
  {
    return 3;
  }
}

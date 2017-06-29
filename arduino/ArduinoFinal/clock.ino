//***** Define state machine *****//
typedef enum
{
  C_IDLE,
  C_OUTPUT
} CCLOCK;

//***** Current state *****//
CCLOCK cClock;


//***** Intermediate function *****//
void sendData();

//***** Initialize the clock of the game *****//
void clockInit()
{
  chrono = millis();
  second = 10000;
  hour = 0;
}

//***** Change the state of the clock when a day is past *****//
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

//***** Send hour to serial port *****//
void clockOutput()
{
  switch (cClock)
  {
    case C_IDLE:
      break;

    case C_OUTPUT:
      sendData();
      break;
  }
}

//***** Send data to serial port and LCD screen *****//
void sendData()
{
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
  if (weatherActual == 0)
  {
    lcd.setCursor(9, 1);
    lcd.print("      ");
    lcdWeather = "rainy";
  }
  else if (weatherActual == 1)
  {
    lcd.setCursor(10, 1);
    lcd.print("      ");
    lcdWeather = "cloudy";
  }
  else if (weatherActual == 2)
  {
    lcd.setCursor(9, 1);
    lcd.print("       ");
    lcdWeather = "sunny";
  }
  else if (weatherActual == 3)
  {
    lcdWeather = "heatwave";
  }
  else if (weatherActual == 4)
  {
    lcdWeather = "thunderstorm";
  }
  lcd.setCursor(0, 0);
  lcd.print("HOUR : ");
  lcd.print(hour);
  lcd.setCursor(0, 1);
  lcd.print("W : ");
  lcd.print(lcdWeather);
}

//***** Define state machine *****//
typedef enum
{
  BS_IDLE,
  BS_INCREASE,
  BS_DECREASE,
  BS_NORMALDAY
} BUTTON_STATE;

//***** Current state *****//
BUTTON_STATE buttonState;

//***** Initialize buttons from the Arduino and the period of one hour *****//
void buttonInit()
{
  pinMode(BUTTON_PIN, INPUT);
  normalDay = 10000;        //Normal speed of one hour
  etatButton = false;
}

//***** Update the flow of one hour *****//
void buttonUpdate()
{
  BUTTON_PIN = analogRead(A0);
  etatButton = digitalRead(BUTTON_PIN);
  BUTTON_STATE nextState = buttonState;

  switch (buttonState)
  {
    case BS_IDLE:
      if (BUTTON_PIN >= 50 && BUTTON_PIN < 250 && etatButton && (millis() - lastTime) >= 50)
      {
        lastTime = millis();
        nextState = BS_INCREASE;
      }
      else if (BUTTON_PIN >= 250 && BUTTON_PIN < 450 && etatButton && (millis() - lastTime) >= 50)
      {
        lastTime = millis();
        nextState = BS_DECREASE;
      }
      else if (BUTTON_PIN >= 650 && BUTTON_PIN < 850 && etatButton && (millis() - lastTime) >= 50)
      {
        lastTime = millis();
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

//***** Increase, decrease or reset of the time flow *****//
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
        second = 3000;
      }
      break;

    case BS_DECREASE:
      second += 50;
      if (second > 20000)
      {
        second = 20000;
      }
      break;

    case BS_NORMALDAY:
      second = normalDay;
      break;
  }
}

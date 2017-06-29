//***** Library to use LCD screen *****//
#include <LiquidCrystal.h>

//***** Object "lcd" to know which pin of the Arduino we use *****//
LiquidCrystal lcd(8, 9, 4, 5, 6, 7);

//***** Shared variables *****//
int i, j, wf, normalDay, weatherActual, weatherForecast, BUTTON_PIN;
boolean weatherGenerated, etatButton;
unsigned long chrono, lastTime;
long second, hour;
String data, lcdWeather;


//***** Define clock function *****//
void clockInit();
void clockUpdate();
void clockOutput();

//***** Define weather state function *****//
void weatherInit();
void weatherUpdate();
void weatherOutput();

//***** Define button state function *****//
void buttonInit();
void buttonUpdate();
void buttonOutput();


//***** Setup is for initialize variables *****//
void setup() {
  Serial.begin(9600);
  lcd.begin(16, 2);
  clockInit();
  weatherInit();
  buttonInit();
}

//***** Main loop of the program *****//
void loop() {
  clockUpdate();
  clockOutput();
  weatherUpdate();
  weatherOutput();
  buttonUpdate();
  buttonOutput();
}


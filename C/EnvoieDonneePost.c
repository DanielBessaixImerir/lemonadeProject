/* Created by imerir on 23/06/17.*/

/* Import useful library to send data to the server */
#include <stdio.h>
#include <stdlib.h>
#include <json-c/json.h>
#include <curl/curl.h>
#include <stdbool.h>
#include <string.h>
#include <fcntl.h>
#include <termios.h>
#include <unistd.h>

/* Shared variables */
char *tabWeather[] = {"rainy", "cloudy", "sunny", "heatwave", "thunderstorm"};
char buffer[20], tmpBuf[2];
int USB, nb, lastHour, i = 0, j = 0, hour, weather, forecast;

/* Create json object*/
json_object *json;
json_object *jsonForecast;
json_object *jsonWeather;
json_object *jsonWeatherArray;

/* This function concat data send to the server */
/* {timestamp : int , weather : [{dfn : int, weather : string}, {dfn : int, weather : string}]} */
json_object* createJson(int h,int w, int f)
{
    json = json_object_new_object();
    jsonForecast = json_object_new_object();
    jsonWeather = json_object_new_object();
    jsonWeatherArray  = json_object_new_array();

    /* Concat weather and forecast to json object */
    json_object_object_add(jsonForecast, "dfn", json_object_new_int(1));
    json_object_object_add(jsonForecast, "weather", json_object_new_string(tabWeather[f]));
    json_object_object_add(jsonWeather, "dfn", json_object_new_int(0));
    json_object_object_add(jsonWeather, "weather", json_object_new_string(tabWeather[w]));

    /* Add weather and forecast to an array */
    json_object_array_add(jsonWeatherArray, jsonWeather);
    json_object_array_add(jsonWeatherArray, jsonForecast);

    json_object_object_add(json, "timestamp", json_object_new_int(h));
    json_object_object_add(json, "weather", jsonWeatherArray);
    return json;
}

/* This function */
json_object* POST(char *url, json_object *json)
{
    CURL *ch; /* Creation of curl */
    struct curl_slist *headers = NULL; /* headers of http, send with the request */
    ch = curl_easy_init();

    /* Initialization of headers*/
    headers = curl_slist_append(headers, "Accept: application/json");
    headers = curl_slist_append(headers, "Content-Type: application/json");

    curl_easy_setopt(ch, CURLOPT_CUSTOMREQUEST, "POST");
    curl_easy_setopt(ch, CURLOPT_HTTPHEADER, headers);
    curl_easy_setopt(ch, CURLOPT_POSTFIELDS, json_object_to_json_string(json));

    /* Set url to fetch */
    curl_easy_setopt(ch, CURLOPT_URL, url);
    curl_easy_perform(ch);

    curl_easy_cleanup(ch);
    curl_slist_free_all(headers);
    return json;
}

/* This function read data send by Arduino to serial port */
void serialPort()
{
    lastHour = -1;
    printf ("usb: %d !", USB);

    /* USB open the port on which arduino send data */
    USB = open("/dev/ttyACM0", O_RDONLY);

    do {
        /* Reset of buffer */
        strcpy(buffer,"");

        /* nb  read data send by arduino*/
        nb = read(USB, buffer, sizeof(buffer) - 1);

        /* Reset of tmpBuf */
        strcpy(tmpBuf, "");
        j = 0;

        if (nb > 1)
        {
            buffer[nb] = '\0';
            if (nb == 1) {
                printf("buffer : %d\n", buffer[0]);
            }
            for (i = 0; i < strlen(buffer); i++)
            {
                if (buffer[i] == '[')
                {
                    j = 0;
                    tmpBuf[j] = buffer[i];
                    j++;
                }
                else if (buffer[i] == ']')
                {
                    tmpBuf[j] = buffer[i];
                    tmpBuf[j+1] = '\0';
                    break;
                }
                else
                {

                    tmpBuf[j] = buffer[i];
                    j++;
                }

                /* Reads formatted input from a string (tmpBuf) */
                sscanf(tmpBuf,"[%d|%d|%d]",&hour,&weather,&forecast);
            }
        }

        if (hour != lastHour)
        {
            json = createJson(hour, weather, forecast);
            printf(json_object_to_json_string(json));
            POST("https://tranquil-reef-75630.herokuapp.com/metrology", json);
            lastHour = hour;
            json_object_put(json);
        }
    } while(1);

    close(USB);
}

/* Main loop */
int main(int argc, char *argv[])
{
    serialPort();
}

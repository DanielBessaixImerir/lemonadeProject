
//
// Created by imerir on 23/06/17.
//

#include <stdio.h>
#include <stdlib.h>
#include <json-c/json.h>
#include <curl/curl.h>
#include <stdbool.h>
#include <string.h>
#include <fcntl.h>
#include <termios.h>
#include <unistd.h>

char *tabWeather[] = {"rainy", "cloudy", "sunny", "heatwave", "thunderstorm"};
int USB, nb, i = 0, j = 0, val1, val2, val3;
char buffer[20], tmpBuf[2];

json_object* createJson(int hour,int weather, int forecast ){
    /* creation du json object*/
    json_object *json;
    json_object *jsonForecast;
    json_object *jsonWeather;
    json_object *jsonWeatherArray;
    //printf("debug");
    json = json_object_new_object();
    jsonForecast = json_object_new_object();
    jsonWeatherArray = json_object_new_array();
    jsonWeather = json_object_new_object();
    json_object_object_add(json, "timestamp", json_object_new_int(hour));
    json_object_object_add(jsonForecast, "dfn", json_object_new_int(1));
    json_object_object_add(jsonForecast, "weather", json_object_new_string(tabWeather[forecast]));
    json_object_object_add(jsonWeather, "dfn", json_object_new_int(0));
    json_object_object_add(jsonWeather, "weather", json_object_new_string(tabWeather[weather]));
    json_object_object_add(jsonWeatherArray,"",jsonWeather);
    json_object_object_add(jsonWeatherArray,"weather",jsonForecast);
    json_object_object_add(json, "weather", jsonWeatherArray);
    return json;
}

json_object* POST(char *url, char *json){
    CURL *ch; //création du curl
    struct curl_slist *headers = NULL; //headers du http, a envoyer avec la requete
    ch = curl_easy_init();

    /* initialisation des headers*/
    headers = curl_slist_append(headers, "Accept: application/json");
    headers = curl_slist_append(headers, "Content-Type: application/json");

    curl_easy_setopt(ch, CURLOPT_CUSTOMREQUEST, "POST");
    curl_easy_setopt(ch, CURLOPT_HTTPHEADER, headers);
    curl_easy_setopt(ch, CURLOPT_POSTFIELDS, json);

    /* set url to fetch */
    curl_easy_setopt(ch, CURLOPT_URL, url);
    curl_easy_perform(ch);


    curl_easy_cleanup(ch);
    curl_slist_free_all(headers);
    //json_object_put(json);
    //printf("\n");
    //printf("Parsed JSON: %s\n", json_object_to_json_string(json));
    return json;
}


void serialPort()
{

    int lastHour = -1;
    printf ("usb: %d !",USB);
    do {
        USB = open("/dev/ttyACM0", O_RDONLY);
        nb = read(USB, buffer, sizeof(buffer) - 1);
        //printf("%d\n", nb);
                //Reinitialisation de tmpBuf
        strcpy(tmpBuf, "");
        j = 0;
        //printf ("nb: %d",nb);
        if (nb > 1)
        {
            //printf("coucou");
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
                printf("buffer : %s\n", tmpBuf);


                sscanf(tmpBuf,"[%d|%d|%d]",&val1,&val2,&val3);

                /*printf("heure : %d\n",val1);
                printf("meteo : %d\n",val2);
                printf("weather : %d\n",val3);*/


            }

        }
        if (val1!=lastHour){
            json_object *json;
            json=createJson(val1,val2,FALSE);
            if (val1%24==0){
                json_object *jsonForecast;
                jsonForecast=createJson(val1,val3,TRUE);
                POST("https://tranquil-reef-75630.herokuapp.com/metrology",jsonForecast);
                printf("jsonForecast ");
            }
            //POST("https://kabosu.herokuapp.com/test/c",json);
            POST("https://tranquil-reef-75630.herokuapp.com/metrology",json);
            lastHour=val1;
            printf("jsonWeather");
        }
        close(USB);
    } while(1);
}

int main(int argc, char *argv[]) {
    //serialPort();
    /*json_object *json;
    json=createJson(5,1,1);
    POST("https://kabosu.herokuapp.com/",json);*/
    json_object *json;
    json=createJson(5,1,1);
    printf(json_object_to_json_string(json));
}
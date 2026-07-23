#include <WiFi.h>
#include <PubSubClient.h>
#include <HardwareSerial.h>
#include <DFRobotDFPlayerMini.h>
#include <ArduinoJson.h>

#define ui unsigned int
#define ul unsigned long
#define LED_GREEN 18
#define LED_RED 19
#define LED_YELLOW 21
#define BUZZER 22
#define MP3_RX 16
#define MP3_TX 17

bool waitingMusic = false;
ul musicStart = 0;

//Config DFMiniPlayer
HardwareSerial dfSerial(2); //UART2 tren ESP32
DFRobotDFPlayerMini player;


//Config Wifi
const char *ssid = "son";
const char *password = "11111111";

//MQTT Broker
const char *mqtt_broker = "broker.hivemq.com";
const char *topic = "posture/alert";
const int mqtt_port = 1883;


WiFiClient espClient;
PubSubClient client(espClient);

//Kết nối đến Wifi;
void setUpWiFi(){
  delay(10);
  Serial.print("Kết nối tới ");
  Serial.println(ssid);

  WiFi.begin(ssid,password);

  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.println("Kết nối tới WiFi..");
  }
  Serial.println("");
  Serial.println("WiFi đã kết nối");
  Serial.print("Địa chỉ: ");
  Serial.println(WiFi.localIP());
}

void ok(int file){
  player.play(file);
}

void callback(char *topic, byte *payload, ui length){
  String json = "";
  for(int i = 0;i < length; ++i){
    json += (char)(payload[i]);
  }

  Serial.println(json);
  JsonDocument doc;
  DeserializationError error = deserializeJson(doc,json);

  if(error){
    Serial.println("Parse JSON failed");
    return;
  }

  String status = doc["status"];
  bool playVoice = doc["playVoice"];

  
  if(status == "GOOD_POSTURE"){

    digitalWrite(LED_GREEN,HIGH);
    digitalWrite(LED_YELLOW,LOW);
    digitalWrite(LED_RED,LOW);
    noTone(BUZZER); 
    if(playVoice){
      ok(1);
    }
  }

  else if(status == "WARNING_POSTURE"){
    digitalWrite(LED_YELLOW,HIGH);
    digitalWrite(LED_GREEN,LOW);
    digitalWrite(LED_RED,LOW);
    noTone(BUZZER);
    if(playVoice){
      ok(2);
    }
  }
  else if(status == "BAD_POSTURE"){
    digitalWrite(LED_RED,HIGH);

    digitalWrite(LED_GREEN,LOW);
    digitalWrite(LED_YELLOW,LOW);

    tone(BUZZER,7000);
    delay(2000);
    noTone(BUZZER);
    if(playVoice){
      ok(3);
    }
    delay(2000);
    noTone(BUZZER);
  }
  else if(status == "BREAK_TIME"){
    if(playVoice){
      ok(4);
      waitingMusic = true;
      musicStart = millis();
    }
  }
}

void reconnect(){
  while(!client.connected()){
    Serial.println("Kết nối tới MQTT...");
    Serial.println("");
    String clientId = "ESP32-";
    clientId += String(random(0xffff), HEX);
    
    if(client.connect(clientId.c_str())){
      Serial.println("Connected");
      client.subscribe(topic);
    }
    else{
      Serial.printf("Failed, reconnect: ");
      Serial.print(client.state());
      delay(2000);
    }
  }
}
void setup() {
  Serial.begin(115200);
  setUpWiFi();
  client.setServer(mqtt_broker,mqtt_port); //Connect to Server MqttBroker
  client.setCallback(callback);

  pinMode(LED_GREEN,OUTPUT);
  pinMode(LED_RED,OUTPUT);
  pinMode(LED_YELLOW,OUTPUT);
  pinMode(BUZZER,OUTPUT);

  dfSerial.begin(9600,SERIAL_8N1,MP3_RX,MP3_TX);
  if (!player.begin(dfSerial)) {
    Serial.println("Không kết nối được với DFMini!");
    while (true) {
      delay(1000);
    }
  }
  player.volume(20);
  Serial.println("Sẵn sàng làm việc");
}

void loop() {
  if(!client.connected()) reconnect();
  client.loop();

  if(waitingMusic && millis() - musicStart >= 10000){
    ok(5);
    waitingMusic = false;
  }
}

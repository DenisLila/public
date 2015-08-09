// SPI is needed by the RF24 library. It includes it, but for some reason the ide doesn't compile that,
// so we have to include it here.
#include <SPI.h>
#include <RF24.h>
#include <OneWire.h>
#include <DallasTemperature.h>
#include "setup_printf.h"
#include "common.h"

// Radio is using pins 9 and 10, as well as SPI pins.
RF24 radio(9, 10);
// We're assuming there is exactly one thermometer right now, at index 0. Using pin 2.
OneWire one_wire(2);
DallasTemperature thermos(&one_wire);
// The internal address of the thermometer. Shouldn't really need it if only using 1
// thermometer, but the dallas temperature library doesn't provide broadcast apis.
DeviceAddress addr0; // this is a pointer type.
temp_reading cur_reading;

// Function declarations.
void print_hex(uint8_t *array, size_t len);
void error_loop();
int init_thermos();
void init_radio();

void setup() {
  Serial.begin(57600);
  setup_printf();

  if (!init_thermos()) {
    error_loop();
  }
  memset(&cur_reading, 0, PAYLOAD_SIZE);
  init_radio();
}

// Right now this is a naive implementation where we just read temperature every INTERVAL millis
// TODO(dlila): Lots of room for improvement here:
//   2) Use the radio's low power operation mode.
//   4) Encryption
void loop() {
  cur_reading.loop_start = millis();
  cur_reading.idx++;
  // Just broadcast. Don't call the "byAddress" method. That will do a pointless scratchpad read.
  // Note that this uses the highest resolution in the thermometer chain. This is not a problem
  // in the single thermometer case.
  thermos.requestTemperatures();
  cur_reading.temp = thermos.getTempC(addr0);  
  P("temp reading: ");
  P(cur_reading.temp);

  // See notes in the RF24 library readme I wrote.
  radio.powerUp();
  delayMicroseconds(200);
  cur_reading.tx_millis = millis();
  if (!radio.write(&cur_reading, PAYLOAD_SIZE)) {
    P("Error sending payload");
  }
  uint32_t elapsed = millis() - cur_reading.loop_start;
  delay(max(0, INTERVAL - elapsed));
}

// Function definitions.

void print_hex(uint8_t *array, size_t len) {
  for (int i = 0; i < len; i++) {
    if (array[i] < 16) {
      // Serial.print(, HEX) prints just X for X in [0, 15]. We need 0X.
      Serial.print('0');
    }
    Serial.print(array[i], HEX);
  }
}

int init_thermos() {
  memset(addr0, 0, sizeof(DeviceAddress));
  thermos.begin();
  if (thermos.getAddress(addr0, 0)) {
    Serial.println("Thermometer address:");
    print_hex(addr0, sizeof(DeviceAddress));
    Serial.print('\n');
    return true;
  } else {
    printf("error reading address at idx 0\n");
    return false;
  }
}

void init_radio() {
  radio.begin();
  radio.setPayloadSize(PAYLOAD_SIZE);
  radio.openWritingPipe(PIPE);
#ifdef __DEBUG
  radio.printDetails();
#endif
}

void error_loop() {
  while (1) {
    Serial.println("error_loop");
    delay(1000);
  }
}


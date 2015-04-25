// SPI is needed by the RF24 library. It includes it, but for some reason the ide doesn't compile that,
// so we have to include it here.
#include <SPI.h>
#include <RF24.h>
#include <OneWire.h>
#include <DallasTemperature.h>
#include "printf.h"

// TODO(dlila): Some stuff in common with the thermo receiver here. Put it in libraries (along
// with print.h - that should be in a printf.c file).

// Local data.
// Pipe for RF24.
#define PIPE ((uint64_t) 0xE8E8F0F0E1LL)
// How long to sleep in each loop.
#define INTERVAL 1500
// Packet size. Each thermometer reading is a float. No reason to make this bigger for now.
#define PAYLOAD_SIZE ((uint8_t)sizeof(float))
// Radio is using pins 9 and 10, as well as SPI pins.
RF24 radio(9, 10);
// We're assuming there is exactly one thermometer right now, at index 0. Using pin 2.
OneWire one_wire(2);
DallasTemperature thermos(&one_wire);
// The internal address of the thermometer. Shouldn't really need it if only using 1
// thermometer, but the dallas temperature library doesn't provide broadcast apis.
DeviceAddress addr0;

// Function declarations.
void print_hex(uint8_t *array, size_t len);
int init_thermos();
int init_radio();

void setup() {
  Serial.begin(57600);
  setup_printf();

  init_thermos();
  init_radio();
}

// Right now this is a naive implementation where we just read temperature every INTERVAL millis
// TODO(dlila): Lots of room for improvement here:
//   1) Do both the temperature reading and the temperature sending async:
//      - Start both operations (send the previous iteration's temperature reading).
//      - Wait until they both complete.
//      - Wake up, read temperature, and save it to memory.
//      - Go back to sleep.
//      - Encryption
//   2) Use the radio's low power operation mode.
//   3) Right now we sleep for INTERVAL millis, so each iteration will last INTERVAL plus whatever
//      it takes to run. It would be nice to make each loop last INTERVAL millis.
void loop() {
  thermos.requestTemperatures();
  Serial.print("temp reading: ");
  float temp = thermos.getTempC(addr0);
  Serial.println(temp);

  // See notes in test programs. TODO(dlila): transfer those to a README or something here.
  radio.powerUp();
  delayMicroseconds(200);
  if (!radio.write(&temp, PAYLOAD_SIZE)) {
    Serial.println("Error sending payload");
  }
  delay(INTERVAL);
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
    // In this case we'll just be tring to read from a zeroed address in the loop.
    // Not ideal, but not a problem either.
    printf("error reading address at idx 0\n");
    return false;
  }
}

int init_radio() {
  radio.begin();
  radio.setPayloadSize(PAYLOAD_SIZE);
  radio.openWritingPipe(PIPE);
  radio.printDetails();
}


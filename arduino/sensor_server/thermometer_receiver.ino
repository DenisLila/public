// SPI is needed by the RF24 library. It includes it, but for some reason the ide doesn't compile that,
// so we have to include it here.
#include <SPI.h>
#include <RF24.h>
#include "setup_printf.h"

// Local data.
// Pipe for RF24.
#define PIPE ((uint64_t) 0xE8E8F0F0E1LL)
// How long to sleep in each loop.
#define INTERVAL 1500
// Packet size. Each thermometer reading is a float. No reason to make this bigger for now.
#define PAYLOAD_SIZE ((uint8_t)sizeof(float))
// Radio is using pins 9 and 10, as well as SPI pins.
RF24 radio(9, 10);

// Function declarations.
int init_radio();

void setup() {
  Serial.begin(57600);
  init_radio();
}

void loop() {
  // TODO(dlila): we know the sender INTERVAL. No need to poll here.
  if (radio.available()) {
    float temp = 0;
    radio.read(&temp, PAYLOAD_SIZE);
    // TODO(dlila): this is ridiculous. This is how we're communicating with the
    // server. This should be more parser friendly.
    Serial.print("Temperature from radio: ");
    Serial.println(temp);
  }
}


// Function definitions.
int init_radio() {
  radio.begin();
  radio.setPayloadSize(PAYLOAD_SIZE);
  radio.openReadingPipe(1, PIPE);
  radio.startListening();
//  radio.printDetails();
}


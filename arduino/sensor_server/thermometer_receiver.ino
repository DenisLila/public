// SPI is needed by the RF24 library. It includes it, but for some reason the ide doesn't compile that,
// so we have to include it here.
#include <SPI.h>
#include <RF24.h>
#include "setup_printf.h"
//#define __DEBUG
#include "common.h"

// Local data.
// Radio is using pins 9 and 10, as well as SPI pins.
RF24 radio(9, 10);
temp_reading last_reading;

// Function declarations.
int init_radio();

void setup() {
  Serial.begin(57600);
  init_radio();
  memset(&last_reading, 0, PAYLOAD_SIZE);
}

void loop() {
  // TODO(dlila): we know the sender INTERVAL. No need to poll here.
  if (radio.available()) {
    radio.read(&last_reading, PAYLOAD_SIZE);
    // TODO(dlila): this is ridiculous. This is how we're communicating with the
    // server. This should be more parser friendly.
    Serial.print(last_reading.idx);
    Serial.print(',');
    Serial.println(last_reading.temp);
  }
}

// Function definitions.
int init_radio() {
  radio.begin();
  radio.setPayloadSize(PAYLOAD_SIZE);
  radio.openReadingPipe(1, PIPE);
  radio.startListening();
#ifdef DEBUG
  radio.printDetails();
#endif
}


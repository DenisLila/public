// SPI is needed by the RF24 library. It includes it, but for some reason the ide doesn't compile that,
// so we have to include it here.
#include <SPI.h>
#include <RF24.h>
#include "setup_printf.h"
#include "common.h"

// Function declarations.
int init_radio();

// Local data.
// Radio is using pins 9 and 10, as well as SPI pins.
RF24 radio(9, 10);
temp_reading last_reading;

void setup() {
  Serial.begin(57600);
  init_radio();
  setup_printf();
  memset(&last_reading, 0, PAYLOAD_SIZE);
}

void loop() {
  while (!radio.available()) {
    // Wait for data to be available.
  }
  radio.read(&last_reading, PAYLOAD_SIZE);
  // for whatever reason, printf didn't work here.
  Serial.print(last_reading.idx);
  Serial.print(',');
  Serial.print(last_reading.loop_start);
  Serial.print(',');
  Serial.print(last_reading.tx_millis);
  Serial.print(',');
  Serial.println(last_reading.temp);
  // Wait for the next transmission (minus 100 so we don't miss it because of timing imprecisions)
  delay(INTERVAL - 100);
}

// Function definitions.
int init_radio() {
  radio.begin();
  radio.setPayloadSize(PAYLOAD_SIZE);
  radio.openReadingPipe(1, PIPE);
  radio.startListening();
#ifdef __DEBUG // from common.h
  radio.printDetails();
#endif
}


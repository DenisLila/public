#include <SPI.h>
#include <RF24.h>
#include "setup_printf.h"

#define PIPE ((uint64_t) 0xF0F0F0F0E1LL)
#define INTERVAL 1000
#define PAYLOAD_SIZE ((uint8_t)sizeof(uint32_t))
RF24 radio(9, 10);

int data;
// Function declarations.
int init_radio();

void setup() {
  Serial.begin(57600);

  setup_printf();
  data = 0;
  init_radio();
}

void loop() {
  printf("Sending data: %d\n", data);
  radio.powerUp();
  delayMicroseconds(200);
  if (!radio.write(&data, PAYLOAD_SIZE)) {
    Serial.println("Error sending payload");
  }
  data++;
  delay(INTERVAL);
}

// Function definitions.

int init_radio() {
  radio.begin();
  radio.setRetries(15, 15);
  radio.setPayloadSize(PAYLOAD_SIZE);
  radio.openWritingPipe(PIPE);
  printf("payload size: %d\n", radio.getPayloadSize());
  radio.printDetails();
}


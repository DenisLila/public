#include <SPI.h>
#include <RF24.h>
#include "printf.h"

#define PIPE ((uint64_t) 0xF0F0F0F0E1LL)
#define INTERVAL 4000
#define PAYLOAD_SIZE ((uint8_t)sizeof(uint32_t))
RF24 radio(9, 10);

// Function declarations.
int init_radio();

void setup() {
  Serial.begin(57600);
  
  setup_printf();
  init_radio();
}

void loop() {
  int data = 0;
  if (radio.available()) {
    if (radio.read(&data, PAYLOAD_SIZE)) {
      printf("read data: %d\n", data);
    } else {
      Serial.println("error reading\n");
    }
  }
  delay(INTERVAL);
}


// Function definitions.

int init_radio() {
  radio.begin();
  radio.setPayloadSize(PAYLOAD_SIZE);
  radio.openReadingPipe(1, PIPE);
  radio.startListening();
  printf("payload size: %d\n", radio.getPayloadSize());
  radio.printDetails();
}


#ifndef __THERMO_COMMON_
#define __THERMO_COMMON_

//#define __DEBUG
#ifdef __DEBUG
#define P(X) Serial.println(X)
#else
#define P(X)
#endif

typedef struct _temp_reading {
  // transmitter loop count
  uint32_t idx;
  // time in millis at the start of the transmitter loop
  uint32_t loop_start;
  // time just before transmission
  uint32_t tx_millis;
  float temp;
} temp_reading;

// Pipe for RF24.
#define PIPE ((uint64_t) 0xE8E8F0F0E1LL)
// How long to sleep in each loop.
#define INTERVAL 1500
// Packet size. Each thermometer reading is a float. No reason to make this bigger for now.
#define PAYLOAD_SIZE ((uint8_t)sizeof(temp_reading))

#endif

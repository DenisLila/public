#ifndef __THERMO_COMMON_
#define __THERMO_COMMON_

#ifdef __DEBUG
#define P(X) Serial.write(X)
#else
#define P(X)
#endif

typedef struct _temp_reading {
  uint32_t idx;
  uint32_t loop_start;
  uint32_t millis;
  float temp;
} temp_reading;

// Pipe for RF24.
#define PIPE ((uint64_t) 0xE8E8F0F0E1LL)
// How long to sleep in each loop.
#define INTERVAL 1500
// Packet size. Each thermometer reading is a float. No reason to make this bigger for now.
#define PAYLOAD_SIZE ((uint8_t)sizeof(temp_reading))

#endif

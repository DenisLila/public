#include <Arduino.h>
#include "setup_printf.h"

// Enables printf using Serial.write.
#ifdef ARDUINO

static int putc_func(char c, FILE * unused) {
  Serial.write(c);
  return c;
} 

void setup_printf() {
  fdevopen(putc_func, 0);
}

#else
#error ARDUINO not defined
#endif // ARDUINO

#ifndef __PRINTF_H__
#define __PRINTF_H__

// Enables printf using Serial.write.
#ifdef ARDUINO

int putc_func(char c, FILE * unused) {
  Serial.write(c);
  return c;
} 

void setup_printf() {
  fdevopen(putc_func, 0);
}

#else
#error ARDUINO not defined
#endif // ARDUINO

#endif // __PRINTF_H__
